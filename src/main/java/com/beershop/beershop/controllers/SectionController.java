package com.beershop.beershop.controllers;

import com.beershop.beershop.controllers.converter.SectionConverter;
import com.beershop.beershop.controllers.response.SectionVolumeResponse;
import com.beershop.beershop.model.SectionVolume;
import com.beershop.beershop.model.enums.BeverageTypeEnum;
import com.beershop.beershop.service.interfaces.ISectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.beershop.beershop.controllers.converter.SectionConverter.convertToResponse;

@RestController
@RequestMapping("/beershop/v1/section")
@Api(value = "Section Controller")
public class SectionController {
    @Autowired
    private ISectionService sectionService;

    @GetMapping("/search")
    @ApiOperation(value = "Get section information")
    public ResponseEntity<SectionVolumeResponse> saveBeverageInformation (@RequestParam String sectionId) {
        SectionVolume sectionVolume = sectionService.findById(sectionId).get();

        return ResponseEntity.ok(convertToResponse(sectionVolume));
    }

    @GetMapping("/search/withType")
    @ApiOperation(value = "Get section information using beverage type")
    public ResponseEntity<List<SectionVolumeResponse>> getAllSectionInformationByType(
            @RequestParam BeverageTypeEnum beverageType) {

        List<SectionVolume> sectionVolume = sectionService.findByType(beverageType).get();

        return ResponseEntity.ok(sectionVolume.stream()
                .map(SectionConverter::convertToResponse)
                .collect(Collectors.toList()));
    }

}
