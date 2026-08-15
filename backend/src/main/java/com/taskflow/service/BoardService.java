package com.taskflow.service;

import com.taskflow.model.Board;
import com.taskflow.model.User;
import com.taskflow.repository.BoardRepository;
import com.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<Board> getBoardsForUser(String email) {
        User user = getUser(email);
        return boardRepository.findByOwnerId(user.getId());
    }

    public Board createBoard(String email, String title) {
        User user = getUser(email);
        Board board = new Board(title, user);
        return boardRepository.save(board);
    }

    public Board getBoardOwnedBy(Long boardId, String email) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        if (!board.getOwner().getEmail().equals(email)) {
            throw new SecurityException("You do not have access to this board");
        }
        return board;
    }

    public void deleteBoard(Long boardId, String email) {
        Board board = getBoardOwnedBy(boardId, email);
        boardRepository.delete(board);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
