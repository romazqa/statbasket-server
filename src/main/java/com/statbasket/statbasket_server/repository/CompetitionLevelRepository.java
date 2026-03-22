package com.statbasket.statbasket_server.repository;

import com.statbasket.statbasket_server.model.CompetitionLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionLevelRepository extends JpaRepository<CompetitionLevel, Integer> {
}
