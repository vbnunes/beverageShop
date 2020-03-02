package com.beershop.beershop.service;

import com.beershop.beershop.controllers.request.BeverageRegistryRequest;
import com.beershop.beershop.exptions.NoAvailableSectionException;
import com.beershop.beershop.model.SectionVolume;
import com.beershop.beershop.model.enums.BeverageTypeEnum;
import com.beershop.beershop.repositories.SectionVolumeRepository;
import com.beershop.beershop.service.interfaces.ISectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SectionServiceImpl implements ISectionService {

    public static final long FIRST_SECTION_NUMBER = 1L;
    private static final Long MAX_NUMBER_OF_SECTIONS = 5L;
    private static final BigDecimal MAX_ALCOHOLIC_VOLUME = new BigDecimal("500.00");
    private static final BigDecimal MAX_NON_ALCOHOLIC_VOLUME = new BigDecimal("400.00");;

    @Autowired
    private SectionVolumeRepository sectionVolumeRepository;

    /*
        With this method wouldn't be pass which section the beer will be sent, the application can know
        by itself where the beer needs to go. This will just return the correct number of the section that beer
        has been sent
     */
    @Override
    public Long findAvailableSectionForBeverage(BeverageRegistryRequest registry) throws NoAvailableSectionException {
        Optional<List<SectionVolume>> sectionsVolume;
        List<SectionVolume> availableSections;

        switch (registry.getType()) {
            case ALCOHOLIC:
                log.info("[SectionService] >> Starting ALCOHOLIC section validation");

                sectionsVolume = sectionVolumeRepository.findAvailableSectionForAlcoholic(MAX_NUMBER_OF_SECTIONS);

                if (!sectionsVolume.isPresent()) {
                    return createSectionIfPossible(registry);
                }

                availableSections = sectionsVolume.get().stream().filter(sectionVolume -> {
                    BigDecimal totalVolume = sectionVolume.getTotalVolume().add(registry.getVolume());

                    return (totalVolume.compareTo(MAX_ALCOHOLIC_VOLUME) <= 0);
                }).collect(Collectors.toList());

                if (!availableSections.isEmpty()) {
                    return availableSections.get(0).getId();
                }

                return createSectionIfPossible(registry);
            case NON_ALCOHOLIC:
                log.info("[SectionService] >> Starting NON_ALCOHOLIC section validation");

                sectionsVolume = sectionVolumeRepository.findAvailableSectionForNonAlcoholic(MAX_NUMBER_OF_SECTIONS);

                if (!sectionsVolume.isPresent()) {
                    return createSectionIfPossible(registry);
                }

                availableSections = sectionsVolume.get().stream().filter(sectionVolume -> {
                    BigDecimal totalVolume = sectionVolume.getTotalVolume().add(registry.getVolume());

                    return (totalVolume.compareTo(MAX_NON_ALCOHOLIC_VOLUME) <= 0);
                }).collect(Collectors.toList());

                if (!availableSections.isEmpty()) {
                    return availableSections.get(0).getId();
                }

                return createSectionIfPossible(registry);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public Optional<SectionVolume> findById(String id) {
        return sectionVolumeRepository.findById(Long.parseLong(id));
    }

    @Override
    public Optional<List<SectionVolume>> findByType(BeverageTypeEnum type) {
        return sectionVolumeRepository.findByType(type);
    }

    private Long createSectionIfPossible(BeverageRegistryRequest registry) throws NoAvailableSectionException {
        Optional<List<SectionVolume>> nextAvailableSection = sectionVolumeRepository.findNextAvailableSection();

        if (!nextAvailableSection.isPresent()) {
            log.info("[SectionService] >> Creating the first section");

            SectionVolume savedItem = sectionVolumeRepository.save(SectionVolume.builder()
                    .sectionNumber(FIRST_SECTION_NUMBER)
                    .totalVolume(new BigDecimal(0))
                    .type(registry.getType())
                    .build());

            return savedItem.getId();
        }

        SectionVolume foundSection = nextAvailableSection.get().get(0);

        Long currentSectionNumber = foundSection.getSectionNumber();
        if (currentSectionNumber.equals(MAX_NUMBER_OF_SECTIONS)) {
            log.error("Could not create more sections");

            throw new NoAvailableSectionException("The max number of sections has been reached there is no more space for this size of volume: " + registry.getVolume());
        }

        log.info("[SectionService] >> Creating section with number: {} for beer type: {}",
                currentSectionNumber + 1, registry.getType());

        SectionVolume newSection = sectionVolumeRepository.save(SectionVolume.builder()
                .sectionNumber(currentSectionNumber + 1)
                .totalVolume(new BigDecimal(0))
                .type(registry.getType())
                .build());

        return newSection.getId();
    }
}
