package io.manbang.ebatis.spring.boot.autoconfigure;

import io.manbang.ebatis.core.cluster.AbstractCluster;
import io.manbang.ebatis.core.cluster.Credentials;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;

import javax.net.ssl.SSLContext;
import java.io.File;

final class ConfiguredCluster extends AbstractCluster {
    private final EbatisProperties.ClusterProperties properties;

    ConfiguredCluster(HttpHost[] hosts, EbatisProperties.ClusterProperties properties) {
        super(hosts, credentials(properties));
        this.properties = properties;
        getBuilder().setRequestConfigCallback(builder -> builder
                .setConnectTimeout(properties.getConnectTimeout())
                .setSocketTimeout(properties.getSocketTimeout())
                .setConnectionRequestTimeout(properties.getConnectionRequestTimeout()));
        Header[] headers = properties.getDefaultHeaders().entrySet().stream()
                .map(entry -> new BasicHeader(entry.getKey(), entry.getValue()))
                .toArray(Header[]::new);
        getBuilder().setDefaultHeaders(headers);
        sniff(properties.isSniff(), properties.getSniffInterval(), properties.getSniffAfterFailureDelay());
    }

    private static Credentials credentials(EbatisProperties.ClusterProperties properties) {
        return properties.getUsername() == null || properties.getUsername().trim().isEmpty()
                ? null : Credentials.basic(properties.getUsername(), properties.getPassword());
    }

    @Override
    protected void customHttpClientConfig(HttpAsyncClientBuilder builder) {
        builder.setMaxConnTotal(properties.getMaxConnTotal())
                .setMaxConnPerRoute(properties.getMaxConnPerRoute());
        EbatisProperties.SslProperties ssl = properties.getSsl();
        if (ssl == null || !ssl.isEnabled()) {
            return;
        }
        try {
            org.apache.http.ssl.SSLContextBuilder contextBuilder = SSLContexts.custom();
            if (ssl.getTrustStore() != null && !ssl.getTrustStore().trim().isEmpty()) {
                contextBuilder.loadTrustMaterial(new File(ssl.getTrustStore()),
                        ssl.getTrustStorePassword() == null ? null : ssl.getTrustStorePassword().toCharArray());
            }
            SSLContext context = contextBuilder.build();
            builder.setSSLContext(context);
            if (!ssl.isVerifyHostname()) {
                builder.setSSLStrategy(new SSLIOSessionStrategy(context, NoopHostnameVerifier.INSTANCE));
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to initialize Ebatis SSL configuration", exception);
        }
    }
}
