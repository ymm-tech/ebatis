package com.ymm.ebatis.core.executor;

import lombok.Data;

@Data
public class Retries {
    private long bulk;
    private long search;
}
