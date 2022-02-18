package io.manbang.ebatis.core.cluster;

import io.manbang.ebatis.core.common.ObjectMapperHolder;
import io.manbang.ebatis.core.config.Env;
import io.manbang.ebatis.core.domain.ContextHolder;
import io.manbang.ebatis.core.domain.HttpConfig;
import io.manbang.ebatis.core.exception.ClusterCreationException;
import io.manbang.ebatis.core.request.CatRequest;
import io.manbang.ebatis.core.response.CatResponse;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.sniff.Sniffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author 章多亮
 * @since 2019/12/18 9:31
 */
@Slf4j
@ToString(of = "name")
public abstract class AbstractCluster implements Cluster {
    public static final int DEFAULT_SNIFF_INTERVAL = (int) TimeUnit.SECONDS.toMillis(10);
    public static final int DEFAULT_SNIFF_AFTER_FAILURE_DELAY = (int) TimeUnit.SECONDS.toMillis(10);
    private final AtomicBoolean highLevelClientInitialized = new AtomicBoolean(false);
    private final AtomicBoolean lowLevelClientInitialized = new AtomicBoolean(false);
    private final LazyInitializer<RestClient> lowLevelClientInitializer;
    private final LazyInitializer<RestHighLevelClient> highLevelClientInitializer;
    private final RestClientBuilder builder;
    private final HttpHost[] hosts;
    private final Credentials credentials;
    private Sniffer highLevelSniffer;
    private Sniffer lowLevelSniffer;
    private String name;

    public AbstractCluster(String hostname, int port) {
        this(new HttpHost[]{new HttpHost(hostname, port)}, null);
    }

    public AbstractCluster(String hostname, int port, Credentials credentials) {
        this(new HttpHost[]{new HttpHost(hostname, port)}, credentials);
    }

    public AbstractCluster(HttpHost[] hosts) {
        this(hosts, null);
    }

    public AbstractCluster(HttpHost[] hosts, Credentials credentials) {
        this.credentials = credentials;
        this.hosts = hosts;
        this.builder = custom(createBuilder(hosts));
        this.name = Arrays.toString(hosts);

        this.lowLevelClientInitializer = createLowLevelClientInitializer();
        this.highLevelClientInitializer = createHighLevelClientInitializer();
    }

    private LazyInitializer<RestHighLevelClient> createHighLevelClientInitializer() {
        return new LazyInitializer<RestHighLevelClient>() {
            @Override
            protected RestHighLevelClient initialize() {
                log.info("创建高级ES集群客户端：{}", name);
                highLevelClientInitialized.set(true);
                final RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
                highLevelSniffer = Sniffer.builder(restHighLevelClient.getLowLevelClient()).setSniffIntervalMillis(DEFAULT_SNIFF_INTERVAL)
                        .setSniffAfterFailureDelayMillis(DEFAULT_SNIFF_AFTER_FAILURE_DELAY).build();
                return restHighLevelClient;
            }
        };
    }

    private LazyInitializer<RestClient> createLowLevelClientInitializer() {
        return new LazyInitializer<RestClient>() {
            @Override
            protected RestClient initialize() {
                log.info("创建低级ES集群客户端：{}", name);
                lowLevelClientInitialized.set(true);
                final RestClient build = builder.build();
                lowLevelSniffer = Sniffer.builder(build).setSniffIntervalMillis(DEFAULT_SNIFF_INTERVAL)
                        .setSniffAfterFailureDelayMillis(DEFAULT_SNIFF_AFTER_FAILURE_DELAY).build();
                return build;
            }
        };
    }

    protected HttpHost[] getHosts() {
        return hosts;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * 子类可以手动设置集群的名称，可以更有意义些的名字
     *
     * @param name 集群名称
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * 获取ES集群构建器
     *
     * @return ES集群客户端构建器
     */
    public RestClientBuilder getBuilder() {
        return builder;
    }

    /**
     * 创建客户端构建器
     *
     * @param hosts ES主机列表
     * @return 客户端构建器
     */
    protected RestClientBuilder createBuilder(HttpHost[] hosts) {
        return RestClient.builder(hosts);
    }

    /**
     * 设置HttpClientConfig credentials 请求响应打印 请求超时时间
     *
     * @param builder 客户端构建器
     * @return 客户端构建器
     */
    private HttpAsyncClientBuilder setHttpClientConfig(HttpAsyncClientBuilder builder) {
        builder.setConnectionReuseStrategy(DefaultClientConnectionReuseStrategy.INSTANCE)
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                .setDefaultCredentialsProvider(credentials == null ? null : credentials.toCredentialsProvider());
        if (Env.isDebugEnabled()) {
            builder.addInterceptorLast(this::printRequest)
                    .addInterceptorLast(this::printResponse);
        }

        // 设置单次请求的超时时间
        builder.addInterceptorLast(this::setRequestTimeout);
        //定制HttpClientConfig
        customHttpClientConfig(builder);
        return builder;

    }

    /**
     * 定制HttpClientConfig
     *
     * @param builder 客户端构建器
     */
    protected void customHttpClientConfig(HttpAsyncClientBuilder builder) {
    }

    /**
     * 定制ES集群，比如可以设置Http IO线程池数量，请求超时时间等等
     *
     * @param builder ES集群客户端构建器
     * @return 返回构建器自己
     */
    private RestClientBuilder custom(RestClientBuilder builder) {
        builder.setHttpClientConfigCallback(this::setHttpClientConfig);

        // 设置默认超时时间
        builder.setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder);
        return builder;
    }

    private void setRequestTimeout(HttpRequest request, HttpContext context) {
        HttpConfig httpConfig = ContextHolder.getContext().getHttpConfig();
        if (httpConfig == null) {
            return;
        }

        RequestConfig config = (RequestConfig) context.getAttribute(HttpClientContext.REQUEST_CONFIG);
        config = RequestConfig.copy(config)
                .setSocketTimeout(httpConfig.socketTimeout())
                .setConnectTimeout(httpConfig.connectTimeout())
                .setConnectionRequestTimeout(httpConfig.connectionRequestTimeout())
                .build();

        context.setAttribute(HttpClientContext.REQUEST_CONFIG, config);
    }

    private void printRequest(HttpRequest request, HttpContext context) throws IOException {
        StringBuilder sb = new StringBuilder(System.lineSeparator());
        sb.append(request.getRequestLine()).append(System.lineSeparator());

        for (Header header : request.getAllHeaders()) {
            sb.append(header).append(System.lineSeparator());
        }

        sb.append(System.lineSeparator());

        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest enclosingRequest = (HttpEntityEnclosingRequest) request;
            HttpEntity entity = enclosingRequest.getEntity();
            if (entity != null) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        Map<?, ?> map = ObjectMapperHolder.objectMapper().readValue(line, Map.class);
                        String body = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
                        sb.append(body);
                    }
                }
            }
        }

        log.debug("{}", sb);
    }

    private void printResponse(HttpResponse response, HttpContext context) {
        StringBuilder sb = new StringBuilder(System.lineSeparator());
        sb.append(response.getStatusLine()).append(System.lineSeparator());

        for (Header header : response.getAllHeaders()) {
            sb.append(header).append(System.lineSeparator());
        }

        log.debug("{}", sb);
    }

    @Override
    public RestHighLevelClient highLevelClient() {
        try {
            return highLevelClientInitializer.get();
        } catch (ConcurrentException e) {
            throw new ClusterCreationException("High Level Rest Client 创建失败");
        }
    }

    @Override
    public RestClient lowLevelClient() {
        try {
            return lowLevelClientInitializer.get();
        } catch (ConcurrentException e) {
            throw new ClusterCreationException("Low Level Rest Client 创建失败");
        }
    }

    @Override
    public void catAsync(CatRequest catRequest, ActionListener<CatResponse> listener) {
        Request request = catRequest.toRequest();

        lowLevelClient().performRequestAsync(request, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                listener.onResponse(new CatResponse());
            }

            @Override
            public void onFailure(Exception exception) {
                listener.onFailure(exception);
            }
        });
    }

    @Override
    public void close() throws IOException {
        if (highLevelClientInitialized.get()) {
            log.info("关闭HighLevelClient：{}", name);
            highLevelSniffer.close();
            highLevelClient().close();
        }
        if (lowLevelClientInitialized.get()) {
            log.info("关闭LowLevelClient：{}", name);
            lowLevelSniffer.close();
            lowLevelClient().close();
        }
    }
}
