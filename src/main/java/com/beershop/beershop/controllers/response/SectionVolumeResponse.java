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
public class SectionVolumeResponse {
    private Long id;
    private BeverageTypeEnum type;
    private BigDecimal totalVolume;
    private Long sectionNumber;
}
