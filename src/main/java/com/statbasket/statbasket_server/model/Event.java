package com.statbasket.statbasket_server.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "eventt") // Две 't' в названии таблицы, как в вашем SQL
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Event {

    @Id
    @Column(name = "id_event")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq_gen")
    @SequenceGenerator(name = "event_seq_gen", sequenceName = "ev_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "name_event", length = 40)
    private String name;

    @Column(name = "locationn", length = 40) // Две 'n' в названии колонки
    private String location;

    @Column(name = "year_event")
    private Integer year;

    // Связь "Много-к-Одному": Много событий могут иметь один уровень
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_competition_level", nullable = false)
    private CompetitionLevel competitionLevel;
}

