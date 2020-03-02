package com.beershop.beershop.controllers.request;

import com.beershop.beershop.model.enums.BeverageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeverageRegistryRequest {

    private Long beverageId;
    private BigDecimal volume;
    private BeverageTypeEnum type;
    private Long userId;

}
