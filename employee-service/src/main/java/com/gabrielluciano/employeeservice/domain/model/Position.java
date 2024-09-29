package com.gabrielluciano.employeeservice.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "positions")
@SequenceGenerator(name = Position.SEQUENCE_NAME, sequenceName = Position.SEQUENCE_NAME)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Position {

    public static final String SEQUENCE_NAME = "positions_sequence";

    public Position(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String name;
}
