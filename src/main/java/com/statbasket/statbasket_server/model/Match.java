package com.statbasket.statbasket_server.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Match {

    @Id
    @Column(name = "id_matches")
    // Используем генератор последовательности
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_seq_gen")
    // Указываем точное имя последовательности из вашей БД: "public.m_seq"
    // allocationSize = 1 обязателен, так как в вашей базе INCREMENT BY 1
    @SequenceGenerator(name = "match_seq_gen", sequenceName = "m_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "date_matches", nullable = false)
    private LocalDate date;

    @Column(name = "team1score", nullable = false)
    private Integer team1Score;

    @Column(name = "team2score", nullable = false)
    private Integer team2Score;

    @Column(name = "playground", nullable = false, length = 40)
    private String playground;

    // Связь с турниром
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_event", nullable = false)
    private Event event;

    // Связь с первой командой
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_team1", nullable = false)
    private Team team1;

    // Связь со второй командой
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_team2", nullable = false)
    private Team team2;

    // Обратная связь: один матч - много записей статистики
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayerStats> playerStats = new ArrayList<>();
}