package com.ymm.ebatis.mapper;

import com.ymm.ebatis.annotation.Field;
import com.ymm.ebatis.domain.PageRequest;
import lombok.Data;

/**
 * @author 章多亮
 * @since 2020/5/25 17:16
 */
@Data
public class CargoCondition extends PageRequest {
    private String name;
    private Long id;
    private String[] labels;
    @Field("channel")
    private Integer[] channels;
}
