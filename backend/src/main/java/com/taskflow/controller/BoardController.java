package com.taskflow.controller;

import com.taskflow.model.Board;
import com.taskflow.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<List<Board>> getBoards(Authentication auth) {
        return ResponseEntity.ok(boardService.getBoardsForUser(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<Board> createBoard(@RequestBody Map<String, String> body, Authentication auth) {
        Board board = boardService.createBoard(auth.getName(), body.get("title"));
        return ResponseEntity.ok(board);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoard(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(boardService.getBoardOwnedBy(id, auth.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id, Authentication auth) {
        boardService.deleteBoard(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
