package com.ymm.ebatis.core.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OrderCondition {
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("order_date")
    private Date orderDate;
}
