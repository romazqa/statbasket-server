package com.statbasket.statbasket_server.controller;

import com.statbasket.statbasket_server.model.Match;
import com.statbasket.statbasket_server.repository.EventRepository;
import com.statbasket.statbasket_server.repository.MatchRepository;
import com.statbasket.statbasket_server.repository.TeamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Map;
import java.util.HashMap;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final EventRepository eventRepository;

    public MatchController(MatchRepository matchRepository, TeamRepository teamRepository, EventRepository eventRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable Integer id) {
        return matchRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match match) {
        // Пере-привязываем сущности, чтобы избежать проблем с detached entities
        var event = eventRepository.findById(match.getEvent().getId()).orElse(null);
        var team1 = teamRepository.findById(match.getTeam1().getId()).orElse(null);
        var team2 = teamRepository.findById(match.getTeam2().getId()).orElse(null);

        if (event == null || team1 == null || team2 == null) {
            return ResponseEntity.badRequest().body(null); // Не найдены связанные сущности
        }

        match.setEvent(event);
        match.setTeam1(team1);
        match.setTeam2(team2);

        // Устанавливаем двустороннюю связь для статистики
        if (match.getPlayerStats() != null) {
            match.getPlayerStats().forEach(stat -> stat.setMatch(match));
        }

        Match savedMatch = matchRepository.save(match);
        return ResponseEntity.ok(savedMatch);
    }

    /**
     * НОВЫЙ МЕТОД: Обновление существующего матча.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Match> updateMatch(@PathVariable Integer id, @RequestBody Match matchDetails) {
        return matchRepository.findById(id)
                .map(existingMatch -> {
                    var event = eventRepository.findById(matchDetails.getEvent().getId()).orElse(null);
                    var team1 = teamRepository.findById(matchDetails.getTeam1().getId()).orElse(null);
                    var team2 = teamRepository.findById(matchDetails.getTeam2().getId()).orElse(null);

                    if (event == null || team1 == null || team2 == null) {
                        return ResponseEntity.badRequest().<Match>build();
                    }

                    // Обновляем базовые поля
                    existingMatch.setDate(matchDetails.getDate());
                    existingMatch.setTeam1Score(matchDetails.getTeam1Score());
                    existingMatch.setTeam2Score(matchDetails.getTeam2Score());
                    existingMatch.setPlayground(matchDetails.getPlayground());
                    existingMatch.setEvent(event);
                    existingMatch.setTeam1(team1);
                    existingMatch.setTeam2(team2);

                    // --- НОВЫЙ БЛОК: ОБНОВЛЕНИЕ СТАТИСТИКИ ИГРОКОВ ---
                    // 1. Очищаем старую статистику (JPA удалит её из базы благодаря orphanRemoval)
                    existingMatch.getPlayerStats().clear();

                    // 2. Если клиент прислал новую статистику - добавляем её
                    if (matchDetails.getPlayerStats() != null) {
                        // Каждой записи статистики указываем, какому матчу она принадлежит
                        matchDetails.getPlayerStats().forEach(stat -> stat.setMatch(existingMatch));
                        // Добавляем весь массив в наш матч
                        existingMatch.getPlayerStats().addAll(matchDetails.getPlayerStats());
                    }
                    // ---------------------------------------------------

                    Match updatedMatch = matchRepository.save(existingMatch);
                    return ResponseEntity.ok(updatedMatch);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * НОВЫЙ МЕТОД: Удаление матча по ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Integer id) {
        if (!matchRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        matchRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Стандартный ответ для успешного DELETE
    }

    @GetMapping("/paged")
    public ResponseEntity<Map<String, Object>> getPagedMatches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        // Сортировка: сначала новые даты. Если даты одинаковые - сортируем по ID (по убыванию)
        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date", "id"));
        Page<Match> pageMatches = matchRepository.findAll(paging);

        Map<String, Object> response = new HashMap<>();
        response.put("matches", pageMatches.getContent());      // Сами матчи (15 штук)
        response.put("currentPage", pageMatches.getNumber());   // Текущая страница
        response.put("totalPages", pageMatches.getTotalPages());// Всего страниц

        return ResponseEntity.ok(response);
    }
}
