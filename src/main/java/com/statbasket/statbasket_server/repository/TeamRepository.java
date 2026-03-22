package com.statbasket.statbasket_server.repository;

import com.statbasket.statbasket_server.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> { // ID теперь Integer
}
