package com.ymm.ebatis.core.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ymm.ebatis.core.annotation.Field;
import com.ymm.ebatis.core.annotation.Filter;
import com.ymm.ebatis.core.annotation.MustNot;
import com.ymm.ebatis.core.annotation.QueryType;
import com.ymm.ebatis.core.provider.DefaultScrollProvider;
import com.ymm.ebatis.core.provider.SourceProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderCondition extends DefaultScrollProvider implements SourceProvider {
    private static final String[] INCLUDE_FIELDS = {"order_id", "order_date", "products"};
    @MustNot
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("order_date")
    private Date orderDate;
    @Filter(queryType = QueryType.MATCH)
    private String category;

    @Field("manufacturer")
    private String[] manufacturers;

    private ProductCondition productCondition;

    private CustomerCondition customerCondition;

    @Override
    public String[] getIncludeFields() {
        return INCLUDE_FIELDS;
    }
}
