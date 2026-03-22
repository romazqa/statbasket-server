package com.statbasket.statbasket_server.repository;

import com.statbasket.statbasket_server.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> { // ID теперь Integer
}
