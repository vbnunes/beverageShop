package com.beershop.beershop.repositories;

import com.beershop.beershop.model.SectionVolume;
import com.beershop.beershop.model.enums.BeverageTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionVolumeRepository extends JpaRepository<SectionVolume, Long> {
    @Query("SELECT s FROM SectionVolume s WHERE s.type = 0 AND s.totalVolume < 500 AND s.sectionNumber <= :maxSectionNumber ORDER BY s.totalVolume")
    Optional<List<SectionVolume>> findAvailableSectionForAlcoholic(@Param("maxSectionNumber") Long maxSectionNumber);

    @Query("SELECT s FROM SectionVolume s WHERE s.type = 1 AND s.totalVolume < 400 AND s.sectionNumber <= :maxSectionNumber ORDER BY s.totalVolume")
    Optional<List<SectionVolume>> findAvailableSectionForNonAlcoholic(@Param("maxSectionNumber") Long maxSectionNumber);

    @Query("SELECT s FROM SectionVolume s ORDER BY s.sectionNumber DESC")
    Optional<List<SectionVolume>> findNextAvailableSection();

    Optional<List<SectionVolume>> findByType(BeverageTypeEnum type);
}
