package com.beershop.beershop.controllers;

import com.beershop.beershop.controllers.request.BeverageRegistryRequest;
import com.beershop.beershop.controllers.response.BeverageResponse;
import com.beershop.beershop.exptions.NoAvailableSectionException;
import com.beershop.beershop.model.BeverageRegistry;
import com.beershop.beershop.service.interfaces.IBeverageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/beverageshop/v1/beverage")
@Api(value = "Beverage Controller")
public class BeverageController {
    @Autowired
    private IBeverageService beverageService;

    @PostMapping("/save")
    @ApiOperation(value = "Save beverage information and update the related to the beverage type")
    public ResponseEntity<BeverageResponse> saveBeverageInformation (
            @RequestBody @Valid BeverageRegistryRequest beverageRegistryRequest) throws NoAvailableSectionException {

        BeverageRegistry beverageRegistry = beverageService.saveAndUpdateSection(beverageRegistryRequest);

        URI location = URI.create(String.format("/beverage?id=%s", beverageRegistry.getId()));

        BeverageResponse response = convertToResponse(beverageRegistry);

        return  ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping("/search")
    @ApiOperation(value = "Get beverage information")
    public ResponseEntity<BeverageResponse> saveBeverageInformation (@RequestParam String entryId) {
        BeverageRegistry beverageRegistry = beverageService.findById(entryId);

        return ResponseEntity.ok(convertToResponse(beverageRegistry));
    }

    private BeverageResponse convertToResponse(BeverageRegistry beverageRegistry) {
        return BeverageResponse.builder()
                .id(beverageRegistry.getId())
                .beerID(beverageRegistry.getBeverageId())
                .createdBy(beverageRegistry.getCreatedBy())
                .sectionId(beverageRegistry.getSection().getId())
                .type(beverageRegistry.getType())
                .volume(beverageRegistry.getVolume())
                .build();
    }
}
