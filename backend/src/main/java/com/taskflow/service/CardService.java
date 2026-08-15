package com.taskflow.service;

import com.taskflow.dto.CardRequest;
import com.taskflow.dto.MoveCardRequest;
import com.taskflow.model.Card;
import com.taskflow.model.TaskList;
import com.taskflow.repository.CardRepository;
import com.taskflow.repository.TaskListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final TaskListRepository taskListRepository;
    private final BoardService boardService;

    public Card createCard(Long listId, CardRequest request, String email) {
        TaskList list = getListChecked(listId, email);
        int nextPosition = cardRepository.findByTaskListIdOrderByPositionAsc(listId).size();

        Card card = new Card(request.getTitle(), request.getDescription(),
                request.getDueDate(), nextPosition, list);
        return cardRepository.save(card);
    }

    public Card updateCard(Long cardId, CardRequest request, String email) {
        Card card = getCardChecked(cardId, email);
        card.setTitle(request.getTitle());
        card.setDescription(request.getDescription());
        card.setDueDate(request.getDueDate());
        return cardRepository.save(card);
    }

    // Moves a card to a (possibly different) list and re-indexes positions
    // so drag-and-drop ordering stays consistent, mirroring what the
    // React DragDropContext onDragEnd handler will send.
    public void moveCard(Long cardId, MoveCardRequest request, String email) {
        Card card = getCardChecked(cardId, email);
        TaskList targetList = getListChecked(request.getTargetListId(), email);

        List<Card> sourceCards = cardRepository.findByTaskListIdOrderByPositionAsc(card.getTaskList().getId());
        sourceCards.remove(card);
        reindex(sourceCards);

        card.setTaskList(targetList);

        List<Card> targetCards = cardRepository.findByTaskListIdOrderByPositionAsc(targetList.getId());
        int insertAt = Math.min(request.getNewPosition(), targetCards.size());
        targetCards.add(insertAt, card);
        reindex(targetCards);

        cardRepository.saveAll(targetCards);
        if (!sourceCards.isEmpty()) {
            cardRepository.saveAll(sourceCards);
        }
    }

    public void deleteCard(Long cardId, String email) {
        Card card = getCardChecked(cardId, email);
        cardRepository.delete(card);
    }

    private void reindex(List<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setPosition(i);
        }
    }

    private TaskList getListChecked(Long listId, String email) {
        TaskList list = taskListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("List not found"));
        boardService.getBoardOwnedBy(list.getBoard().getId(), email); // ownership check
        return list;
    }

    private Card getCardChecked(Long cardId, String email) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        boardService.getBoardOwnedBy(card.getTaskList().getBoard().getId(), email); // ownership check
        return card;
    }
}
