package com.statbasket.statbasket_server.repository;

import com.statbasket.statbasket_server.model.PlayerStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Integer> { // ID теперь Integer
}
