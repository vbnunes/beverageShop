package com.beershop.beershop.service.interfaces;

import com.beershop.beershop.controllers.request.BeverageRegistryRequest;
import com.beershop.beershop.exptions.NoAvailableSectionException;
import com.beershop.beershop.model.BeverageRegistry;

public interface IBeverageService {
    BeverageRegistry saveAndUpdateSection (BeverageRegistryRequest request) throws NoAvailableSectionException;
    BeverageRegistry findById (String id);

}
