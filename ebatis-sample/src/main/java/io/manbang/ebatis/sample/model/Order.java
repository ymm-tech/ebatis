package io.manbang.ebatis.sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author 章多亮
 * @since 2020/6/1 18:19
 */
@Data
public class Order {
    @JsonProperty("order_id")
    private Long id;
}
