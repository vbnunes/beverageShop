package com.beershop.beershop.model;

import com.beershop.beershop.model.enums.BeverageTypeEnum;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SectionVolume extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private BeverageTypeEnum type;

    @Column(nullable = false)
    private BigDecimal totalVolume;

    @Column(nullable = false)
    private Long sectionNumber;
}
