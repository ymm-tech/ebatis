package com.ymm.ebatis.core.cluster;

import com.ymm.ebatis.core.exception.ClusterCreationException;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author 章多亮
 * @since 2019/12/18 9:31
 */
@Slf4j
@ToString(of = "name")
public abstract class AbstractCluster implements Cluster {
    private final AtomicBoolean highLevelClientInitialized = new AtomicBoolean(false);
    private final AtomicBoolean lowLevelClientInitialized = new AtomicBoolean(false);
    private final LazyInitializer<RestClient> lowLevelClientInitializer;
    private final LazyInitializer<RestHighLevelClient> highLevelClientInitializer;

    private final RestClientBuilder builder;
    private final HttpHost[] hosts;
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
        this.hosts = hosts;
        this.builder = custom(createBuilder(hosts, credentials));
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
                return new RestHighLevelClient(builder);
            }
        };
    }

    private LazyInitializer<RestClient> createLowLevelClientInitializer() {
        return new LazyInitializer<RestClient>() {
            @Override
            protected RestClient initialize() {
                log.info("创建低级ES集群客户端：{}", name);
                lowLevelClientInitialized.set(true);
                return builder.build();
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
    protected RestClientBuilder createBuilder(HttpHost[] hosts, Credentials credentials) {
        return RestClient.builder(hosts)
                .setHttpClientConfigCallback(builder -> builder.setConnectionReuseStrategy(DefaultClientConnectionReuseStrategy.INSTANCE)
                        .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                        .setDefaultCredentialsProvider(Optional.of(credentials).map(Credentials::toCredentialsProvider).orElse(null)));
    }

    /**
     * 定制ES集群，比如可以设置Http IO线程池数量，请求超时时间等等
     *
     * @param builder ES集群客户端构建器
     * @return 返回构建器自己
     */
    protected RestClientBuilder custom(RestClientBuilder builder) {
        // do nothing
        return builder;
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
    public void close() throws IOException {
        if (highLevelClientInitialized.get()) {
            log.info("关闭HighLevelClient：{}", name);
            highLevelClient().close();
        }
        if (lowLevelClientInitialized.get()) {
            log.info("关闭LowLevelClient：{}", name);
            lowLevelClient().close();
        }
    }
}
