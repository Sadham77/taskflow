package com.taskflow.service;

import com.taskflow.model.Board;
import com.taskflow.model.TaskList;
import com.taskflow.repository.TaskListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskListService {

    private final TaskListRepository taskListRepository;
    private final BoardService boardService;

    public List<TaskList> getListsForBoard(Long boardId, String email) {
        boardService.getBoardOwnedBy(boardId, email); // ownership check
        return taskListRepository.findByBoardIdOrderByPositionAsc(boardId);
    }

    public TaskList createList(Long boardId, String title, String email) {
        Board board = boardService.getBoardOwnedBy(boardId, email);
        int nextPosition = taskListRepository.findByBoardIdOrderByPositionAsc(boardId).size();
        TaskList list = new TaskList(title, nextPosition, board);
        return taskListRepository.save(list);
    }

    public void deleteList(Long listId, String email) {
        TaskList list = taskListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("List not found"));
        boardService.getBoardOwnedBy(list.getBoard().getId(), email); // ownership check
        taskListRepository.delete(list);
    }
}
