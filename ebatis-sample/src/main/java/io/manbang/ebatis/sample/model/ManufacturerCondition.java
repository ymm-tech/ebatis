package io.manbang.ebatis.sample.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManufacturerCondition {
    private Long id;
    private String name;
}
