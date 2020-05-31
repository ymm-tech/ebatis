package com.ymm.ebatis.core.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {
    @JsonProperty("order_id")
    private Long id;
    @JsonProperty("order_date")
    private Date orderDate;

    @JsonProperty("taxful_total_price")
    private BigDecimal taxfulTotalPrice;
}
