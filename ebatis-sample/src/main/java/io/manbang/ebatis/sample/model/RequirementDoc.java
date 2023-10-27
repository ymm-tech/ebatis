package io.manbang.ebatis.sample.model;

import lombok.Data;

import java.util.Date;

/*
 "id" : 8,
          "status" : "CREATED",
          "model" : {
            "id" : 1,
            "name" : "abc#001"
          },
          "batchNo" : "2023#abc",
          "expectedPrice" : 100,
          "dealingQuantity" : 0,
          "dealtQuantity" : 0,
          "unit" : "箱",
          "deadline" : "2023-11-11",
          "manufacturer" : {
            "id" : 2,
            "name" : "伟创力国际有限公司"
          }
 */
@Data
public class RequirementDoc {
    private Long id;
    private ModelDoc model;
    private String batchNo;
    private Long expectedPrice;
    private Long dealingQuantity;
    private Long dealtQuantity;
    private Date deadline;
    private String unit;
    private ManufacturerDoc manufacturer;
}
