package com.taskflow.dto;

import lombok.Getter;
import lombok.Setter;

// Sent when a card is dragged to a new list/position
@Getter
@Setter
public class MoveCardRequest {
    private Long targetListId;
    private Integer newPosition;
}
