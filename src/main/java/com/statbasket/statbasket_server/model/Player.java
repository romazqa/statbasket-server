package com.statbasket.statbasket_server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "player")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Player {

    @Id
    @Column(name = "id_player")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_seq_gen")
    @SequenceGenerator(name = "player_seq_gen", sequenceName = "pl_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "player_name", nullable = false, length = 40)
    private String name;

    @Column(name = "height", nullable = false)
    private BigDecimal height;

    @Column(name = "weight", nullable = false)
    private BigDecimal weight;

    @Column(name = "role", nullable = false, length = 30)
    private String role;

    @Column(name = "game_number", nullable = false)
    private BigDecimal gameNumber;

    @Column(name = "gender", nullable = false, length = 5)
    private String gender;

    // Связь "Много-к-Одному": Много игроков в одной команде
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_team", nullable = false)
    @JsonIgnoreProperties("players")
    private Team team;
}
