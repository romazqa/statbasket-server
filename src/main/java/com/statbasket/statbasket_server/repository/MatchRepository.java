package com.statbasket.statbasket_server.repository;

import com.statbasket.statbasket_server.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> { // ID теперь Integer
}
