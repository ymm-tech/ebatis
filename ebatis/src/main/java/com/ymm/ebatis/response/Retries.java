package com.ymm.ebatis.response;

import lombok.Data;

@Data
public class Retries {
    private long bulk;
    private long search;
}
