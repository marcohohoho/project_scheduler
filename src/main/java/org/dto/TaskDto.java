package org.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TaskDto {

    private int id;
    private String name;
    private int durationInDays;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Integer> dependencies; //task ids only

    public TaskDto(int id, String name, int durationInDays) {
        this.id = id;
        this.name = name;
        this.durationInDays = durationInDays;
    }

    public TaskDto(int id, String name, int durationInDays, List<Integer> dependencies) {
        this.id = id;
        this.name = name;
        this.durationInDays = durationInDays;
        this.dependencies = dependencies;
    }
}
