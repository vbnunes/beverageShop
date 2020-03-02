package com.beershop.beershop.repositories;

import com.beershop.beershop.model.BeverageRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeverageRegistryRepository extends JpaRepository<BeverageRegistry, Long> {
}
