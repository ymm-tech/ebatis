package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.meta.MethodMeta;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;

/**
 * # 请求工厂接口，根据请求的方法定义和实参，创建ES请求
 *
 * @author 章多亮
 * @since 2020/5/14 11:16
 */
public interface RequestFactory<R extends ActionRequest> {
    static RequestFactory<SearchRequest> search() {
        return SearchRequestFactory.INSTANCE;
    }

    static RequestFactory<IndexRequest> index() {
        return IndexRequestFactory.INSTANCE;
    }

    static RequestFactory<MultiSearchRequest> multiSearch() {
        return MultiSearchRequestFactory.INSTANCE;
    }

    static RequestFactory<BulkRequest> bulk() {
        return BulkRequestFactory.INSTANCE;
    }

    static RequestFactory<UpdateRequest> update() {
        return UpdateRequestFactory.INSTANCE;
    }

    static RequestFactory<DeleteRequest> delete() {
        return DeleteRequestFactory.INSTANCE;
    }

    static RequestFactory<UpdateByQueryRequest> updateByQuery() {
        return UpdateByQueryRequestFactory.INSTANCE;
    }

    static RequestFactory<DeleteByQueryRequest> deleteByQuery() {
        return DeleteByQueryRequestFactory.INSTANCE;
    }

    static RequestFactory<SearchRequest> agg() {
        return AggRequestFactory.INSTANCE;
    }

    static RequestFactory<CatRequest> cat() {
        return CatRequestFactory.INSTANCE;
    }

    static RequestFactory<CatAliasesRequest> catAliases() {
        return CatAliasesRequestFactory.INSTANCE;
    }

    static RequestFactory<CatCountRequest> catCount() {
        return CatCountRequestFactory.INSTANCE;
    }

    static RequestFactory<GetRequest> get() {
        return GetRequestFactory.INSTANCE;
    }

    static RequestFactory<ActionRequest> searchScroll() {
        return SearchScrollRequestFactory.INSTANCE;
    }

    static RequestFactory<MultiGetRequest> multiGet() {
        return MultiGetRequestFactory.INSTANCE;
    }

    /**
     * 创建ES请求，根据注解对应创建不同的ES请求
     *
     * @param method 接口方法
     * @param args   实参
     * @return ES请求
     * @see org.elasticsearch.action.search.SearchRequest 搜索请求
     * @see org.elasticsearch.action.index.IndexRequest 索引请求
     * @see org.elasticsearch.action.update.UpdateRequest 更新请求
     * @see org.elasticsearch.action.delete.DeleteRequest 删除请求
     */
    R create(MethodMeta method, Object... args);
}
