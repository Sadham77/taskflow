package com.taskflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private LocalDate dueDate;

    // Order of this card within its list, used for drag-and-drop positioning
    @Column(nullable = false)
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_list_id", nullable = false)
    @JsonIgnore
    private TaskList taskList;

    public Card(String title, String description, LocalDate dueDate, Integer position, TaskList taskList) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.position = position;
        this.taskList = taskList;
    }
}
