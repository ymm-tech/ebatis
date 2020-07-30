package io.manbang.ebatis.sample.model;

import io.manbang.ebatis.core.annotation.Field;
import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.annotation.QueryType;
import lombok.Data;

/**
 * @author 章多亮
 * @since 2020/6/1 18:20
 */
@Data
public class OrderCondition {
    @Field("customer_first_name")
    private String firstName;
    @Field("customer_last_name.keyword")
    private String lastName;
    @Must(queryType = QueryType.MATCH)
    @Field("customer_full_name")
    private String fullName;
    @Field("customer_phone")
    private String phone;
}
