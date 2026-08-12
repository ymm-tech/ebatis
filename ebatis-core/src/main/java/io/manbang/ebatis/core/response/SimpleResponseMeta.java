package io.manbang.ebatis.core.response;

import io.manbang.ebatis.core.domain.ResponseMeta;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
@ToString
class SimpleResponseMeta implements ResponseMeta {
    private final long total;
    private final int from;
    private final int size;
    private final String index;
    private final String type;
    private final int took;
    private final String id;
    private final boolean success;
    private final boolean timeout;
    private final String cause;
    private final float score;
    private final long version;
    private final long seqNo;
    private final long primaryTerm;
    private final String sourceAsString;
    private final Map<String, Object> sourceAsMap;
    private final Object[] sortValues;
    private final Object[] rawSortValues;
    private final String clusterAlias;
    private final String[] matchedQueries;

}
