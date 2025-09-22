package io.manbang.ebatis.sample.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RequirementDoc {
    private Long id;
    private ModelDoc model;
    private String batchNo;
    private Long dealingQuantity;
    private Long dealtQuantity;
    private String unit;
    private ManufacturerDoc manufacturer;
    private LocalDate deadline;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
