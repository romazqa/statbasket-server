package com.statbasket.statbasket_server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "player_stats")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PlayerStats {

    @Id
    @Column(name = "id_playerstats")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stat_seq_gen")
    @SequenceGenerator(name = "stat_seq_gen", sequenceName = "ps_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "pointscored")
    private Integer pointsScored;

    @Column(name = "assists")
    private Integer assists;

    @Column(name = "steal")
    private Integer steals;

    @Column(name = "turnover")
    private Integer turnovers;

    @Column(name = "blocked_shot")
    private Integer blockedShots;

    @Column(name = "foul")
    private Integer fouls;

    @Column(name = "double")
    private Integer twoPointers; // "double" - зарезервированное слово, лучше переименовать

    @Column(name = "triple")
    private Integer threePointers; // "triple"

    @Column(name = "free_throw")
    private Integer freeThrows;

    @Column(name = "defensive_rebound")
    private Integer defensiveRebounds;

    @Column(name = "offensive_rebound")
    private Integer offensiveRebounds;

    // Связь с игроком
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_player", nullable = false)
    private Player player;

    // Связь с матчем
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_matches", nullable = false)
    @JsonBackReference
    private Match match;
}
