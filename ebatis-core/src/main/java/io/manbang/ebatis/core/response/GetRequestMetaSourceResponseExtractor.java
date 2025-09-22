package io.manbang.ebatis.core.response;

import io.manbang.ebatis.core.domain.MetaSource;
import lombok.val;
import org.elasticsearch.action.get.GetResponse;

class GetRequestMetaSourceResponseExtractor implements ConcreteResponseExtractor<MetaSource, GetResponse> {
    static final GetRequestMetaSourceResponseExtractor INSTANCE = new GetRequestMetaSourceResponseExtractor();

    @Override
    public MetaSource doExtractData(GetResponse response) {
        val meta = SimpleResponseMeta.builder()
                .id(response.getId())
                .index(response.getIndex())
                .primaryTerm(response.getPrimaryTerm())
                .seqNo(response.getSeqNo())
                .version(response.getVersion())
                .sourceAsMap(response.getSourceAsMap())
                .success(response.isExists())
                .build();
        return MetaSource.only(meta);
    }
}
