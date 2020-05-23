package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.Bool;
import com.ymm.ebatis.core.meta.ConditionMeta;
import com.ymm.ebatis.core.meta.FieldConditionMeta;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;


/**
 * @author duoliang.zhang
 */
@Slf4j
public class BoolQueryBuilderFactory extends AbstractQueryBuilderFactory<BoolQueryBuilder, Bool> {
    public static final BoolQueryBuilderFactory INSTANCE = new BoolQueryBuilderFactory();

    private BoolQueryBuilderFactory() {
    }

    @Override
    protected BoolQueryBuilder doCreate(ConditionMeta<?> conditionMeta, Object condition) {
        BoolQueryBuilder builder = new BoolQueryBuilder();
        if (condition == null) {
            return builder;
        }

        Map<Class<? extends Annotation>, List<FieldConditionMeta>> elementGroup;

        if (conditionMeta instanceof FieldConditionMeta) {
            elementGroup = ((FieldConditionMeta) conditionMeta).getChildrenGroup();
        } else {
            BeanDescriptor beanDescriptor = BeanDescriptor.of(condition);
            elementGroup = beanDescriptor.getFieldElementGroup();
        }

        elementGroup.forEach((key, value) ->
                QueryClauseType.valueOf(key).addQueryBuilder(builder, value, condition));

        return builder;
    }
}

