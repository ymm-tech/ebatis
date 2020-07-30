package io.manbang.ebatis.core.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 章多亮
 * @since 2020/6/1 11:51
 */
@Data
@Accessors(fluent = true)
public class HttpConfig {
    public static final HttpConfig DEFAULT = new HttpConfig();

    static {
        DEFAULT.connectTimeout(1000)
                .socketTimeout(30000)
                .connectionRequestTimeout(-1);
    }

    private int connectTimeout;
    private int connectionRequestTimeout;
    private int socketTimeout;
}
