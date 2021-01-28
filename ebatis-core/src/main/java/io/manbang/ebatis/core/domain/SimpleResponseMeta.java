package io.manbang.ebatis.core.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
class SimpleResponseMeta implements ResponseMeta {
    private long total;
    private int from;
    private int size;
    private String index;
    private String type;
    private int took;
    private String id;
    private boolean success;
    private boolean timeout;
    private String cause;
    private float score;
    private long version;
    private String sourceAsString;
    private Map<String, Object> sourceAsMap;
    private Object[] sortValues;
    private String clusterAlias;
    private String[] matchedQueries;

}
