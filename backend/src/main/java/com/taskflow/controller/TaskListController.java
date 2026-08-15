package com.taskflow.controller;

import com.taskflow.model.TaskList;
import com.taskflow.service.TaskListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskListController {

    private final TaskListService taskListService;

    @GetMapping("/boards/{boardId}/lists")
    public ResponseEntity<List<TaskList>> getLists(@PathVariable Long boardId, Authentication auth) {
        return ResponseEntity.ok(taskListService.getListsForBoard(boardId, auth.getName()));
    }

    @PostMapping("/boards/{boardId}/lists")
    public ResponseEntity<TaskList> createList(
            @PathVariable Long boardId,
            @RequestBody Map<String, String> body,
            Authentication auth
    ) {
        TaskList list = taskListService.createList(boardId, body.get("title"), auth.getName());
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/lists/{listId}")
    public ResponseEntity<Void> deleteList(@PathVariable Long listId, Authentication auth) {
        taskListService.deleteList(listId, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
