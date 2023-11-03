package io.manbang.ebatis.sample.model;

public interface Wildcard {
    static String leftLike(String value) {
        return String.format("*%s", value);
    }

    static String rightLike(String value) {
        return String.format("%s*", value);
    }

    static String fullLike(String value) {
        return String.format("*%s*", value);
    }
}
