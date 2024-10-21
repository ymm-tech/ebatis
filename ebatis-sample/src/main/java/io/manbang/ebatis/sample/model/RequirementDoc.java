package io.manbang.ebatis.sample.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RequirementDoc {
    private Long id;
    private ModelDoc model;
    private String batchNo;
    private Long expectedPrice;
    private Long dealingQuantity;
    private Long dealtQuantity;
    private String unit;
    private ManufacturerDoc manufacturer;
    private LocalDate deadline;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
