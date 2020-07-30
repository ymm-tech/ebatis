package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.domain.Page;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MetaUtils;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.action.search.MultiSearchResponse;

import java.util.List;

/**
 * @author 章多亮
 * @since 2020/1/18 17:25
 */
@AutoService(ResponseExtractorProvider.class)
public class MultiSearchResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public MultiSearchResponseExtractorProvider() {
        super(RequestType.MULTI_SEARCH);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultType = genericType.resolve();

        if (List.class.isAssignableFrom(resultType)) {
            Class<?> entityClass = genericType.resolveGeneric(0);
            if (entityClass == Long.class) {
                return TotalHitsListMultiSearchResponseExtractor.INSTANCE;
            } else if (entityClass == Boolean.class) {
                return BooleanListMultiSearchResponseExtractor.INSTANCE;
            } else if (List.class.isAssignableFrom(entityClass)) {
                entityClass = genericType.resolveGeneric(0, 0);
                return new ListMultiSearchResponseExtractor<>(new DocumentExtractor<>(DocumentMapper.of(entityClass), Integer.MAX_VALUE));
            } else if (Page.class.isAssignableFrom(entityClass)) {
                entityClass = genericType.resolveGeneric(0, 0);
                return new PageMultiSearchResponseExtractor<>(new DocumentPageExtractor<>(DocumentMapper.of(entityClass)));
            } else if (entityClass.isArray()) {
                entityClass = entityClass.getComponentType();

                if (entityClass == Long.class || entityClass == long.class) {
                    return TotalHitsArrayMultiSearchResponseExtractor.INSTANCE;
                } else if (!MetaUtils.isBasic(entityClass)) {
                    return new ListArraySearchResponseExtractor<>(new ArrayDocumentExtractor<>(DocumentMapper.of(entityClass), Integer.MAX_VALUE));
                } else {
                    throw new UnsupportedOperationException("不支持");
                }
            } else {
                throw new UnsupportedOperationException("暂不支持的返回值类型");
            }
        } else if (resultType.isArray()) {
            Class<?> entityClass = genericType.resolveGeneric(0);
            if (Page.class.isAssignableFrom(entityClass)) {
                entityClass = genericType.resolveGeneric(0, 0);
                return new PageArrayMultiSearchResponseExtractor<>(new DocumentPageExtractor<>(DocumentMapper.of(entityClass)));
            } else if (entityClass.isArray()) {
                entityClass = genericType.resolveGeneric(0, 0);
                return new ArrayMultiSearchResponseExtractor<>(new ArrayDocumentExtractor<>(DocumentMapper.of(entityClass), Integer.MAX_VALUE),
                        entityClass);
            } else if (List.class.isAssignableFrom(entityClass)) {
                entityClass = genericType.resolveGeneric(0, 0);
                return new ArrayListSearchResponseExtractor<>(new DocumentExtractor<>(DocumentMapper.of(entityClass), Integer.MAX_VALUE));
            } else if (entityClass == Long.class) {
                return TotalHitsArrayMultiSearchResponseExtractor.INSTANCE;
            } else if (entityClass == long.class) {
                return TotalHitsBasicArrayMultiSearchResponseExtractor.INSTANCE;
            } else if (entityClass == Boolean.class) {
                return BooleanArrayMultiSearchResponseExtractor.INSTANCE;
            } else if (entityClass == boolean.class) {
                return BoolArrayMultiSearchResponseExtractor.INSTANCE;
            } else {
                throw new UnsupportedOperationException("暂不支持的返回值类型");
            }
        } else if (MultiSearchResponse.class == resultType) {
            return RawResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException("暂不支持的返回值类型");
        }
    }
}
