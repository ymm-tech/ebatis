package io.manbang.ebatis.sample.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ProductDoc {
    private Long id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Long highestPrice;
    private Long lowestPrice;
    private Long preferredPrice;
    private Long quantity;
    private Long miniPackQuantity;
    private ModelDoc model;
    private CompanyDoc supplier;
    private Boolean spot;
    private String batchNo;

}
