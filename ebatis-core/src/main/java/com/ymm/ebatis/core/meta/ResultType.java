package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.cluster.Cluster;
import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.executor.EsClient;
import com.ymm.ebatis.core.response.ResponseExtractor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author duoliang.zhang
 */

@Slf4j
public enum ResultType {
    /**
     * {@link CompletableFuture}
     */
    COMPLETABLE_FUTURE(CompletableFuture.class) {
        @Override
        public Object search(Cluster cluster, SearchRequest request, ResponseExtractor<?> extractor) {
            return EsClient.searchAsync(cluster, request, extractor);
        }

        @Override
        public Object multiSearch(Cluster cluster, MultiSearchRequest request, ResponseExtractor<?> extractor) {
            return EsClient.multiSearchAsync(cluster, request, extractor);
        }

        @Override
        public Object index(Cluster cluster, IndexRequest request, ResponseExtractor<?> extractor) {
            return EsClient.indexAsync(cluster, request, extractor);
        }

        @Override
        public Object bulk(Cluster cluster, BulkRequest request, ResponseExtractor<?> extractor) {
            return EsClient.bulkAsync(cluster, request, extractor);
        }

        @Override
        public Object delete(Cluster cluster, DeleteRequest request, ResponseExtractor<?> extractor) {
            return EsClient.deleteAsync(cluster, request, extractor);
        }

        @Override
        public Object deleteByQuery(Cluster cluster, DeleteByQueryRequest request, ResponseExtractor<?> extractor) {
            return EsClient.deleteByQueryAsync(cluster, request, extractor);
        }

        @Override
        public Object update(Cluster cluster, UpdateRequest request, ResponseExtractor<?> extractor) {
            return EsClient.updateAsync(cluster, request, extractor);
        }

        @Override
        public Object updateByQuery(Cluster cluster, UpdateByQueryRequest request, ResponseExtractor<?> extractor) {
            return EsClient.updateByQueryAsync(cluster, request, extractor);
        }
    },
    /**
     * {@link Optional}
     */
    OPTIONAL(Optional.class) {
        @Override
        public Object search(Cluster cluster, SearchRequest request, ResponseExtractor<?> extractor) {
            return Optional.ofNullable(EsClient.searchSync(cluster, request, extractor));
        }
    },
    /**
     * {@link Page}
     */
    PAGE(Page.class) {
        @Override
        public Object search(Cluster cluster, SearchRequest request, ResponseExtractor<?> extractor) {
            return EsClient.searchSync(cluster, request, extractor);
        }
    },
    /**
     * {@link List}
     */
    LIST(List.class) {
        @Override
        public Object search(Cluster cluster, SearchRequest request, ResponseExtractor<?> extractor) {
            return EsClient.searchSync(cluster, request, extractor);
        }

        @Override
        public Object index(Cluster cluster, IndexRequest request, ResponseExtractor<?> extractor) {
            return Collections.emptyList();
        }
    },
    /**
     * 其他实体类型
     */
    OTHER(Object.class) {
        @Override
        public Object search(Cluster cluster, SearchRequest request, ResponseExtractor<?> extractor) {
            return EsClient.searchSync(cluster, request, extractor);
        }

        @Override
        public Object index(Cluster cluster, IndexRequest request, ResponseExtractor<?> extractor) {
            return EsClient.indexSync(cluster, request, extractor);
        }

        @Override
        public Object bulk(Cluster cluster, BulkRequest request, ResponseExtractor<?> extractor) {
            return EsClient.bulkSync(cluster, request, extractor);
        }

        @Override
        public Object delete(Cluster cluster, DeleteRequest request, ResponseExtractor<?> extractor) {
            return EsClient.deleteSync(cluster, request, extractor);
        }

        @Override
        public Object deleteByQuery(Cluster cluster, DeleteByQueryRequest request, ResponseExtractor<?> extractor) {
            return EsClient.deleteByQuerySync(cluster, request, extractor);
        }

        @Override
        public Object update(Cluster cluster, UpdateRequest request, ResponseExtractor<?> extractor) {
            return EsClient.updateSync(cluster, request, extractor);
        }

        @Override
        public Object updateByQuery(Cluster cluster, UpdateByQueryRequest request, ResponseExtractor<?> extractor) {
            return EsClient.updateByQuerySync(cluster, request, extractor);
        }
    };

    private static final Map<Class<?>, ResultType> METHOD_RETURN_TYPES = new HashMap<>();

    static {
        for (ResultType type : values()) {
            METHOD_RETURN_TYPES.put(type.returnType, type);
        }
    }

    private final Class<?> returnType;

    ResultType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public static ResultType valueOf(Class<?> returnType) {
        return METHOD_RETURN_TYPES.getOrDefault(returnType, OTHER);
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public Object search(Cluster cluster, SearchRequest request, ResponseExtractor<?> extractor) {
        throw new UnsupportedOperationException(request.toString());
    }

    public Object multiSearch(Cluster cluster, MultiSearchRequest request, ResponseExtractor<?> extractor) {
        throw new UnsupportedOperationException(request.toString());
    }

    public Object index(Cluster cluster, IndexRequest request, ResponseExtractor<?> extractor) {
        throw new UnsupportedOperationException(request.toString());
    }

    public Object bulk(Cluster cluster, BulkRequest request, ResponseExtractor<?> extractor) {
        throw new UnsupportedOperationException(request.toString());
    }

    public Object delete(Cluster cluster, DeleteRequest request, ResponseExtractor<?> extractor) {
        throw new UnsupportedOperationException(request.toString());
    }

    public Object deleteByQuery(Cluster cluster, DeleteByQueryRequest request, ResponseExtractor<?> extractor) {
        throw new UnsupportedOperationException(request.toString());
    }

    public Object update(Cluster cluster, UpdateRequest request, ResponseExtractor<?> extractor) {
        throw new UnsupportedOperationException(request.toString());
    }

    public Object updateByQuery(Cluster cluster, UpdateByQueryRequest request, ResponseExtractor<?> extractor) {
        throw new UnsupportedOperationException(request.toString());
    }
}
