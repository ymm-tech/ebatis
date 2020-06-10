package com.ymm.ebatis.core.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ymm.ebatis.core.annotation.Field;
import com.ymm.ebatis.core.annotation.Ignore;
import com.ymm.ebatis.core.annotation.Must;
import com.ymm.ebatis.core.annotation.QueryType;
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

    @JsonProperty("customer_id")
    private Long customerId;
    @JsonProperty("customer_first_name")
    private String customerFirstName;
    @Field("customer_last_name")
    private String customerLastName;
    @Must(queryType = QueryType.MATCH)
    @JsonProperty("customer_full_name")
    private String customerFullName;
    @JsonProperty("customer_phone")
    private String customerPhone;
    @Ignore
    private String ignoreField;
}
