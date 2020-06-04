package com.ymm.ebatis.core.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ymm.ebatis.core.annotation.Field;
import com.ymm.ebatis.core.annotation.Filter;
import com.ymm.ebatis.core.annotation.MustNot;
import com.ymm.ebatis.core.annotation.QueryType;
import lombok.Data;

import java.util.Date;

@Data
public class OrderCondition {
    @MustNot
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("order_date")
    private Date orderDate;
    @Filter(queryType = QueryType.MATCH)
    private String category;

    @Field("manufacturer")
    private String[] manufacturers;

    private ProductCondition[] products;

    private CustomerCondition customerCondition;
}
