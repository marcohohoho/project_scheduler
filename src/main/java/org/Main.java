package org;

import org.dto.ProjectDto;
import org.dto.TaskDto;
import org.service.ProjectService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static final Integer SEQUENTIAL_DATE_VERSION = 0;
    public static final Integer SIMULTANEOUS_DATE_VERSION = 1;

    public static void main(String[] args) {

        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(1);
        projectDto.setName("New Project");
        projectDto.setStartDate(LocalDate.now());

        List<TaskDto> taskList = Arrays.asList(
                new TaskDto(1, "Task1", 2, Collections.emptyList()),
                new TaskDto(2, "Task2", 4, Collections.emptyList()),
                new TaskDto(3, "Task3", 5, Collections.emptyList()),
                new TaskDto(4, "Task4", 1, Arrays.asList(3)),
                new TaskDto(5, "Task5", 1, Arrays.asList(3)),
                new TaskDto(6, "Task6", 2, Arrays.asList(1))
        );

        projectDto.setTasks(taskList);

        ProjectService projectService = new ProjectService();
        projectService.initiateScheduler(projectDto, SIMULTANEOUS_DATE_VERSION);
        projectService.printProjectSchedule(projectDto);


    }

}