package io.manbang.ebatis.spring.cluster;

import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;

import static org.junit.Assert.assertNull;

public class SpringClusterRouterProviderTest {
    @Test
    public void missingNamedRouterMapKeepsLegacyProviderFallback() {
        SpringClusterRouterProvider provider = new SpringClusterRouterProvider();
        provider.setApplicationContext(new StaticApplicationContext());

        assertNull(provider.getClusterRouter("missing"));
    }
}
