package org.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectDto {

    private int id;
    private String name;
    private List<TaskDto> tasks;
    private LocalDate startDate;

}
