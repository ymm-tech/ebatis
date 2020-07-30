package io.manbang.ebatis.core.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.manbang.ebatis.core.annotation.Field;
import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.annotation.QueryType;
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
