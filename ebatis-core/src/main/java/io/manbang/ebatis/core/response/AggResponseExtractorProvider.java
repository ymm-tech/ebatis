package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;

import java.util.List;
import java.util.Map;

/**
 * @author 章多亮
 * @since 2020/1/17 16:34
 */
@AutoService(ResponseExtractorProvider.class)
public class AggResponseExtractorProvider extends AbstractResponseExtractorProvider {

    public AggResponseExtractorProvider() {
        super(RequestType.AGG);
    }


    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultClass = genericType.resolve();

        if (SearchResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (Aggregations.class == resultClass) {
            return AggregationsResponseExtractor.INSTANCE;
        } else if (List.class.isAssignableFrom(resultClass)) {
            if (Aggregation.class == genericType.resolveGeneric(0)) {
                return AggregationListResponseExtractor.INSTANCE;
            } else {
                throw new UnsupportedOperationException("暂不支持的返回值类型");
            }
        } else if (Map.class.isAssignableFrom(resultClass)) {
            Class<?> keyClass = genericType.resolveGeneric(0);
            Class<?> valueClass = genericType.resolveGeneric(1);

            if (String.class == keyClass && Aggregation.class == valueClass) {
                return AggregationMapResponseExtractor.INSTANCE;
            } else {
                throw new UnsupportedOperationException("暂不支持的返回值类型");
            }
        }

        throw new UnsupportedOperationException("暂不支持的返回值类型");
    }

}
