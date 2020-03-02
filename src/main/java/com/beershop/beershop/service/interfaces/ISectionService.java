package com.beershop.beershop.service.interfaces;

import com.beershop.beershop.controllers.request.BeverageRegistryRequest;
import com.beershop.beershop.exptions.NoAvailableSectionException;
import com.beershop.beershop.model.SectionVolume;
import com.beershop.beershop.model.enums.BeverageTypeEnum;

import java.util.List;
import java.util.Optional;

public interface ISectionService {
    Long findAvailableSectionForBeverage(BeverageRegistryRequest registry) throws NoAvailableSectionException;
    Optional<SectionVolume> findById(String id);
    Optional<List<SectionVolume>> findByType(BeverageTypeEnum typeEnum);

}
