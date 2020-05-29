package com.ymm.ebatis.mapper;

import lombok.Data;

/**
 * @author 章多亮
 * @since 2020/5/25 17:17
 */
@Data
public class Cargo {
    private Long id;
    private String name;
    private String[] labels;
    private Integer channel;
}
