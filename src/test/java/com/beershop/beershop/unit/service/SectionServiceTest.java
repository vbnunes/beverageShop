package com.beershop.beershop.unit.service;

import com.beershop.beershop.controllers.request.BeverageRegistryRequest;
import com.beershop.beershop.exptions.NoAvailableSectionException;
import com.beershop.beershop.model.SectionVolume;
import com.beershop.beershop.model.enums.BeverageTypeEnum;
import com.beershop.beershop.repositories.SectionVolumeRepository;
import com.beershop.beershop.service.SectionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@SpringBootConfiguration
@TestPropertySource(properties = {
        "application.max.number.of.sections=5"
})
public class SectionServiceTest {

    private static final long BEVERAGE_ID = 10L;
    private static final long USER_ID = 11L;
    public static final long MAX_SECTION_NUMBER = 5L;

    @InjectMocks
    private SectionServiceImpl sectionService;

    private Optional<List<SectionVolume>> sectionsVolumeAlcoholics;

    private Optional<List<SectionVolume>> sectionsVolumeNonAlcoholics;

    @Mock
    private SectionVolumeRepository sectionVolumeRepository;

    @Before
    public void setUp() {
        SectionVolume sv1 = SectionVolume.builder()
                .totalVolume(new BigDecimal(450.00))
                .id(1L)
                .type(BeverageTypeEnum.ALCOHOLIC)
                .sectionNumber(1L)
                .build();

        SectionVolume sv2 = SectionVolume.builder()
                .totalVolume(new BigDecimal(150.00))
                .id(2L)
                .type(BeverageTypeEnum.ALCOHOLIC)
                .sectionNumber(2L)
                .build();

        SectionVolume sv3 = SectionVolume.builder()
                .totalVolume(new BigDecimal(350.00))
                .id(3L)
                .type(BeverageTypeEnum.NON_ALCOHOLIC)
                .sectionNumber(3L)
                .build();

        SectionVolume sv4 = SectionVolume.builder()
                .totalVolume(new BigDecimal(200.00))
                .id(4L)
                .type(BeverageTypeEnum.NON_ALCOHOLIC)
                .sectionNumber(4L)
                .build();

        sectionsVolumeAlcoholics = Optional.of(Arrays.asList(sv1, sv2));
        sectionsVolumeNonAlcoholics = Optional.of(Arrays.asList(sv3, sv4));
    }

    @Test
    public void shouldReturnCorrectSectionIdForAlcoholicBeverage() throws NoAvailableSectionException {
        doReturn(sectionsVolumeAlcoholics)
                .when(sectionVolumeRepository)
                .findAvailableSectionForAlcoholic(MAX_SECTION_NUMBER);

        BeverageRegistryRequest request = BeverageRegistryRequest.builder()
                .volume(new BigDecimal("100.00"))
                .type(BeverageTypeEnum.ALCOHOLIC)
                .beverageId(BEVERAGE_ID)
                .userId(USER_ID)
                .build();

        Long correctSection = sectionService.findAvailableSectionForBeverage(request);

        assertEquals(correctSection, sectionsVolumeAlcoholics.get().get(1).getSectionNumber());
    }

    @Test
    public void shouldReturnCorrectSectionIdForNonAlcoholicBeverage() throws NoAvailableSectionException {
        doReturn(sectionsVolumeNonAlcoholics)
                .when(sectionVolumeRepository)
                .findAvailableSectionForNonAlcoholic(MAX_SECTION_NUMBER);

        BeverageRegistryRequest request = BeverageRegistryRequest.builder()
                .volume(new BigDecimal("60.00"))
                .type(BeverageTypeEnum.NON_ALCOHOLIC)
                .beverageId(BEVERAGE_ID)
                .userId(USER_ID)
                .build();

        Long correctSection = sectionService.findAvailableSectionForBeverage(request);

        assertEquals(correctSection, sectionsVolumeNonAlcoholics.get().get(1).getSectionNumber());
    }

    @Test
    public void shouldCreateFirstSection() throws NoAvailableSectionException {
        BeverageRegistryRequest request = BeverageRegistryRequest.builder()
                .volume(new BigDecimal("350.00"))
                .type(BeverageTypeEnum.NON_ALCOHOLIC)
                .beverageId(BEVERAGE_ID)
                .userId(USER_ID)
                .build();

        SectionVolume sectionVolume = SectionVolume.builder()
                .sectionNumber(1L)
                .totalVolume(new BigDecimal(0))
                .type(request.getType())
                .build();

        doReturn(sectionVolume)
                .when(sectionVolumeRepository)
                .save(sectionVolume);

        Long correctSection = sectionService.findAvailableSectionForBeverage(request);

        assertEquals(correctSection, new Long(0L));
    }

    @Test
    public void shouldReturnNextAvailableSection() throws NoAvailableSectionException{
        BeverageRegistryRequest request = BeverageRegistryRequest.builder()
                .volume(new BigDecimal("350.00"))
                .type(BeverageTypeEnum.NON_ALCOHOLIC)
                .beverageId(BEVERAGE_ID)
                .userId(USER_ID)
                .build();

        SectionVolume sectionVolume = SectionVolume.builder()
                .sectionNumber(4L)
                .totalVolume(new BigDecimal(350.00))
                .type(request.getType())
                .build();

        doReturn(Optional.of(Arrays.asList(sectionVolume)))
                .when(sectionVolumeRepository)
                .findNextAvailableSection();

        doReturn(sectionVolume)
                .when(sectionVolumeRepository)
                .save(any());

        sectionService.findAvailableSectionForBeverage(request);

        verify(sectionVolumeRepository, times(1)).save(SectionVolume.builder()
                .sectionNumber(5L)
                .totalVolume(new BigDecimal(0))
                .type(request.getType())
                .build());
    }

    @Test(expected = NoAvailableSectionException.class)
    public void shouldThrowNoAvailableSectionException() throws NoAvailableSectionException{
        BeverageRegistryRequest request = BeverageRegistryRequest.builder()
                .volume(new BigDecimal("350.00"))
                .type(BeverageTypeEnum.NON_ALCOHOLIC)
                .beverageId(BEVERAGE_ID)
                .userId(USER_ID)
                .build();

        SectionVolume sectionVolume = SectionVolume.builder()
                .sectionNumber(5L)
                .totalVolume(new BigDecimal(350.00))
                .type(request.getType())
                .build();

        doReturn(Optional.of(Arrays.asList(sectionVolume)))
                .when(sectionVolumeRepository)
                .findNextAvailableSection();

        sectionService.findAvailableSectionForBeverage(request);
    }
}
