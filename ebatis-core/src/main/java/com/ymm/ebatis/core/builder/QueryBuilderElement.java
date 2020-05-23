package com.ymm.ebatis.core.builder;


import com.ymm.ebatis.core.annotation.QueryType;
import com.ymm.ebatis.core.common.AnnotationUtils;
import com.ymm.ebatis.core.meta.FieldConditionMeta;
import lombok.ToString;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author duoliang.zhang
 */
@ToString
public class QueryBuilderElement {
    private final Object value;
    private final FieldConditionMeta conditionMeta;
    private final QueryType queryType;

    public QueryBuilderElement(FieldConditionMeta conditionMeta, Object value) {
        this.value = value;
        this.conditionMeta = conditionMeta;
        this.queryType = conditionMeta.getQueryClauseAnnotation().flatMap(annotation -> AnnotationUtils.findAttribute(annotation, QueryType.class)).orElse(QueryType.AUTO);
    }

    public QueryBuilder[] toQueryBuilder() {
        if (conditionMeta.isArrayOrCollection() && !conditionMeta.isArrayOrCollectionBasicCondition()) {
            if (conditionMeta.getElementType().isArray()) {
                return Arrays.stream((Object[]) value).map(v -> queryType.createBuilder(conditionMeta, v)).toArray(QueryBuilder[]::new);
            } else {
                return ((Collection<?>) value).stream().map(v -> queryType.createBuilder(conditionMeta, v)).toArray(QueryBuilder[]::new);
            }
        }
        return new QueryBuilder[]{queryType.createBuilder(conditionMeta, value)};
    }
}
