package com.beershop.beershop.service;

import com.beershop.beershop.controllers.request.BeverageRegistryRequest;
import com.beershop.beershop.exptions.NoAvailableSectionException;
import com.beershop.beershop.model.BeverageRegistry;
import com.beershop.beershop.model.SectionVolume;
import com.beershop.beershop.repositories.BeverageRegistryRepository;
import com.beershop.beershop.repositories.SectionVolumeRepository;
import com.beershop.beershop.service.interfaces.IBeverageService;
import com.beershop.beershop.service.interfaces.ISectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BeverageServiceImpl implements IBeverageService {

    @Autowired
    private BeverageRegistryRepository beverageRepository;

    @Autowired
    private SectionVolumeRepository sectionRepository;

    @Autowired
    private ISectionService sectionService;

    public BeverageRegistry saveAndUpdateSection (BeverageRegistryRequest request) throws NoAvailableSectionException {
        log.info("[BeverageServiceImpl] >> Starting new entry process...");

        Long availableSection = sectionService.findAvailableSectionForBeverage(request);

        SectionVolume sectionVolume = sectionRepository.findById(availableSection).get();
        sectionVolume.setTotalVolume(sectionVolume.getTotalVolume().add(request.getVolume()));
        sectionRepository.save(sectionVolume);

        BeverageRegistry beverageEntryCreated = beverageRepository.save(BeverageRegistry.builder()
                .volume(request.getVolume())
                .section(sectionVolume)
                .beverageId(request.getBeverageId())
                .type(request.getType())
                .createdBy(request.getUserId())
                .build());

        return beverageEntryCreated;
    }

    public BeverageRegistry findById (String id) {
        return beverageRepository.findById(Long.parseLong(id)).get();
    }
}
