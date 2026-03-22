package com.statbasket.statbasket_server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "level_event")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CompetitionLevel {

    @Id
    @Column(name = "id_competition_level")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "level_seq_gen")
    @SequenceGenerator(name = "level_seq_gen", sequenceName = "lvl_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "competition_level", length = 30)
    private String name;
}