package com.statbasket.statbasket_server.controller;

import com.statbasket.statbasket_server.model.CompetitionLevel;
import com.statbasket.statbasket_server.repository.CompetitionLevelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competition-levels")
public class CompetitionLevelController {

    private final CompetitionLevelRepository repository;

    public CompetitionLevelController(CompetitionLevelRepository repository) {
        this.repository = repository;
    }

    // Получить все
    @GetMapping
    public List<CompetitionLevel> getAllLevels() {
        return repository.findAll();
    }

    // Создать новый (POST)
    @PostMapping
    public CompetitionLevel createLevel(@RequestBody CompetitionLevel level) {
        return repository.save(level);
    }

    // Обновить существующий (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<CompetitionLevel> updateLevel(@PathVariable Integer id, @RequestBody CompetitionLevel levelDetails) {
        return repository.findById(id)
                .map(level -> {
                    level.setName(levelDetails.getName());
                    return ResponseEntity.ok(repository.save(level));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLevel(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}