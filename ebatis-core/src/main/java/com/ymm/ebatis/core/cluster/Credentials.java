package com.ymm.ebatis.core.cluster;

import org.apache.http.client.CredentialsProvider;

/**
 * @author weilong.hu
 * @date 2020-04-08
 */
public interface Credentials {
    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @return credentials 认证
     */
    static Credentials basic(String username, String password) {
        return new BasicCredentials(username, password);
    }

    /**
     * 转换成CredentialsProvider
     *
     * @return CredentialsProvider
     */
    CredentialsProvider toCredentialsProvider();
}
