package com.ymm.ebatis.cluster;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.util.Args;

/**
 * @author weilong.hu
 * @date 2020-04-08
 */
class BasicCredentials implements Credentials {
    private final String username;
    private final String password;

    public BasicCredentials(String username, String password) {
        Args.notNull(username, "username");
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public CredentialsProvider toCredentialsProvider() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        return credentialsProvider;
    }
}
