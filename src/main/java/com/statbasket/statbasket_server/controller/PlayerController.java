package com.statbasket.statbasket_server.controller;

import com.statbasket.statbasket_server.model.Player;
import com.statbasket.statbasket_server.repository.PlayerRepository;
import com.statbasket.statbasket_server.repository.TeamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerController(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Integer id) {
        return playerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создаем игрока и сразу привязываем к команде по ее ID
    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        // Проверяем, что клиент прислал команду вместе с игроком
        if (player.getTeam() == null || player.getTeam().getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Достаем ID команды прямо из присланного JSON-объекта игрока
        Integer teamId = player.getTeam().getId();

        return teamRepository.findById(teamId)
                .map(team -> {
                    player.setTeam(team);
                    return ResponseEntity.ok(playerRepository.save(player));
                })
                .orElse(ResponseEntity.badRequest().build()); // Если команда не найдена
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Integer id, @RequestBody Player playerDetails) {
        return playerRepository.findById(id)
                .map(player -> {
                    player.setName(playerDetails.getName());
                    player.setBirthday(playerDetails.getBirthday());
                    player.setHeight(playerDetails.getHeight());
                    player.setWeight(playerDetails.getWeight());
                    player.setRole(playerDetails.getRole());
                    player.setGameNumber(playerDetails.getGameNumber());
                    player.setGender(playerDetails.getGender());
                    // Логика смены команды может быть добавлена здесь при необходимости
                    return ResponseEntity.ok(playerRepository.save(player));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Integer id) {
        if (!playerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        playerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
