package com.ymm.ebatis.core.common;

import lombok.SneakyThrows;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;

import java.io.ByteArrayOutputStream;

/**
 * @author 章多亮
 * @since 2020/1/9 9:56
 */
public class LazyPrettyToString {
    private final Object value;

    public LazyPrettyToString(Object value) {
        this.value = value;
    }

    @SneakyThrows
    @Override
    public String toString() {
        if (value instanceof SearchRequest) {
            SearchRequest searchRequest = (SearchRequest) this.value;
            return printPretty(searchRequest.source());
        } else if (value instanceof UpdateRequest) {
            UpdateRequest updateRequest = (UpdateRequest) this.value;
            return printPretty(updateRequest);
        } else if (value instanceof MultiSearchRequest) {
            StringBuilder builder = new StringBuilder();

            ((MultiSearchRequest) value).requests().stream()
                    .map(SearchRequest::source)
                    .map(this::printPretty)
                    .forEach(builder::append);
            return builder.toString();
        } else if (value instanceof UpdateByQueryRequest) {
            SearchRequest searchRequest = ((UpdateByQueryRequest) value).getSearchRequest();
            return printPretty(searchRequest.source());
        } else if (value instanceof DeleteByQueryRequest) {
            SearchRequest searchRequest = ((DeleteByQueryRequest) value).getSearchRequest();
            return printPretty(searchRequest.source());
        }

        return "";
    }

    @SneakyThrows
    private String printPretty(ToXContent content) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XContentBuilder builder = new XContentBuilder(JsonXContent.jsonXContent, out);
        builder.prettyPrint();

        content.toXContent(builder, null);
        builder.flush();

        return out.toString();
    }
}
