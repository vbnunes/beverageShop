package com.beershop.beershop.controllers.response;

import com.beershop.beershop.model.enums.BeverageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeverageResponse {
    private long id;
    private Long beerID;
    private BigDecimal volume;
    private BeverageTypeEnum type;
    private Long createdBy;
    private Long sectionId;
}
