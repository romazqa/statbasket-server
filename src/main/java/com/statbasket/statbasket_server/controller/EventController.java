package com.statbasket.statbasket_server.controller;

import com.statbasket.statbasket_server.model.Event;
import com.statbasket.statbasket_server.repository.CompetitionLevelRepository;
import com.statbasket.statbasket_server.repository.EventRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;
    private final CompetitionLevelRepository levelRepository;

    public EventController(EventRepository eventRepository, CompetitionLevelRepository levelRepository) {
        this.eventRepository = eventRepository;
        this.levelRepository = levelRepository;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * НОВЫЙ МЕТОД: Получение турнира по ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Integer id) {
        return eventRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        // Проверяем, что клиент прислал уровень вместе с соревнованием
        if (event.getCompetitionLevel() == null || event.getCompetitionLevel().getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Достаем ID уровня из присланного JSON
        Integer levelId = event.getCompetitionLevel().getId();

        return levelRepository.findById(levelId)
                .map(level -> {
                    event.setCompetitionLevel(level);
                    return ResponseEntity.ok(eventRepository.save(event));
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    /**
     * НОВЫЙ МЕТОД: Обновление турнира.
     * Ожидает, что в теле запроса будет ID уровня соревнования.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Integer id, @RequestBody Event eventDetails) {
        // Сначала находим сам турнир
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    // Затем находим уровень соревнования, который хотят присвоить
                    return levelRepository.findById(eventDetails.getCompetitionLevel().getId())
                            .map(level -> {
                                // Если все найдено, обновляем поля
                                existingEvent.setName(eventDetails.getName());
                                existingEvent.setLocation(eventDetails.getLocation());
                                existingEvent.setYear(eventDetails.getYear());
                                existingEvent.setCompetitionLevel(level);
                                return ResponseEntity.ok(eventRepository.save(existingEvent));
                            })
                            // Если уровень не найден, возвращаем badRequest
                            .orElse(ResponseEntity.badRequest().build());
                })
                // Если турнир не найден, возвращаем notFound
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * НОВЫЙ МЕТОД: Удаление турнира.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
