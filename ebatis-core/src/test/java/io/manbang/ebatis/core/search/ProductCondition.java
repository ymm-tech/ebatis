package io.manbang.ebatis.core.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.manbang.ebatis.core.annotation.Field;
import io.manbang.ebatis.core.annotation.Prefix;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.annotation.Should;
import io.manbang.ebatis.core.domain.Range;
import lombok.Data;

/**
 * "base_price" : 11.99,
 * "discount_percentage" : 0,
 * "quantity" : 1,
 * "manufacturer" : "Elitelligence",
 * "tax_amount" : 0,
 * "product_id" : 6283,
 * "category" : "Men's Clothing",
 * "sku" : "ZO0549605496",
 * "taxless_price" : 11.99,
 * "unit_discount_amount" : 0,
 * "min_price" : 6.35,
 * "_id" : "sold_product_584677_6283",
 * "discount_amount" : 0,
 * "created_on" : "2016-12-26T09:28:48+00:00",
 * "product_name" : "Basic T-shirt - dark blue/white",
 * "price" : 11.99,
 * "taxful_price" : 11.99,
 * "base_unit_price" : 11.99
 */

@Data
@Prefix("products")
public class ProductCondition {
    @JsonProperty("base_price")
    private Range<Double> basePrice;
    @Field("product_name")
    @Should(queryType = QueryType.MATCH_PHRASE_PREFIX)
    private String name;

}
