package com.taskflow.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CardRequest {
    private String title;
    private String description;
    private LocalDate dueDate;
}
