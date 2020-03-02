package com.beershop.beershop.controllers;

import com.beershop.beershop.controllers.response.SectionVolumeResponse;
import com.beershop.beershop.model.SectionVolume;

public class SectionConverter {
    public static SectionVolumeResponse convertToResponse(SectionVolume sectionVolume) {
        return SectionVolumeResponse.builder()
                .id(sectionVolume.getId())
                .type(sectionVolume.getType())
                .totalVolume(sectionVolume.getTotalVolume())
                .sectionNumber(sectionVolume.getSectionNumber())
                .build();
    }
}
