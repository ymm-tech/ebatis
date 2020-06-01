package com.ymm.ebatis.core.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ymm.ebatis.core.annotation.Field;
import com.ymm.ebatis.core.annotation.Must;
import com.ymm.ebatis.core.annotation.QueryType;
import lombok.Data;

/**
 * @author 章多亮
 * @since 2020/6/1 16:45
 */
@Data
public class CustomerCondition {
    @JsonProperty("customer_id")
    private Long id;
    @JsonProperty("customer_first_name")
    private String firstName;
    @Field("customer_last_name")
    private String lastName;
    @Must(queryType = QueryType.MATCH)
    @JsonProperty("customer_full_name")
    private String fullName;
    @JsonProperty("customer_phone")
    private String phone;
}
