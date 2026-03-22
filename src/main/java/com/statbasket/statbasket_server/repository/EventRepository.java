package com.statbasket.statbasket_server.repository;

import com.statbasket.statbasket_server.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
}
