package io.manbang.ebatis.core.response;

import io.manbang.ebatis.core.domain.ResponseMeta;
import org.elasticsearch.search.SearchHit;

/**
 * @author duoliang.zhang
 */
public interface DocumentMapper<T> {

    /**
     * 通过实体类型创建文档映射器
     *
     * @param entityClass 实体对象
     * @param <T>         实体对象类型
     * @return 文档映射器
     */
    static <T> DocumentMapper<T> of(Class<T> entityClass) {
        return JacksonDocumentMapper.of(entityClass);
    }

    /**
     * 创建元信息
     *
     * @param hit 命中文档
     * @return 元信息
     */
    @SuppressWarnings("deprecation")
    static ResponseMeta of(SearchHit hit) {
        return SimpleResponseMeta.builder()
                .id(hit.getId())
                .index(hit.getIndex())
                .type(hit.getType())
                .score(hit.getScore())
                .version(hit.getVersion())
                .seqNo(hit.getSeqNo())
                .primaryTerm(hit.getPrimaryTerm())
                .sourceAsString(hit.getSourceAsString())
                .sourceAsMap(hit.getSourceAsMap())
                .sortValues(hit.getSortValues())
                .rawSortValues(hit.getRawSortValues())
                .clusterAlias(hit.getClusterAlias())
                .matchedQueries(hit.getMatchedQueries())
                .build();
    }

    /**
     * 将ES文档映射成实体对象
     *
     * @param hit   命中的文档
     * @param index 文档下标
     * @return 映射后的实体对象
     */
    T mapRow(SearchHit hit, int index);

    Class<T> getEntityClass();
}
