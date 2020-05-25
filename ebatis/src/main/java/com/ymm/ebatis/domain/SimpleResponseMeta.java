package com.ymm.ebatis.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
}
