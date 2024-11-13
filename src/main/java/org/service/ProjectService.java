package org.service;

import org.dto.ProjectDto;
import org.dto.TaskDto;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.Main.SEQUENTIAL_DATE_VERSION;

public class ProjectService {

    public void addProject() {}

    public void initiateScheduler(ProjectDto projectDto, Integer versionId) {
        List<TaskDto> processedTasks;
        LocalDate projectStartDate = projectDto.getStartDate();

        if(versionId.equals(SEQUENTIAL_DATE_VERSION)) {
            processedTasks = updateTasksStartAndEndDatesV1(projectDto.getTasks(), projectStartDate);
        } else {
            processedTasks = updateTasksStartAndEndDatesV2(projectDto.getTasks(), projectStartDate);
        }

        processedTasks.sort(Comparator
                .comparing(TaskDto::getStartDate)
                .thenComparing(TaskDto::getEndDate));

        projectDto.setTasks(processedTasks);
    }

    public void printProjectSchedule(ProjectDto projectDto) {
        System.out.println("Project Id: " + projectDto.getId());
        System.out.println("Project Name: " + projectDto.getName());
        System.out.println("Project Start Date: " + projectDto.getStartDate());
        System.out.println("---");

        for (TaskDto task : projectDto.getTasks()) {
            System.out.println("Task Id: " + task.getId());
            System.out.println("    Name: " + task.getName());
            System.out.println("    Duration: " + task.getDurationInDays());
            System.out.println("    Start Date: " + task.getStartDate());
            System.out.println("    End Date: " + task.getEndDate());
            System.out.println("    Dependencies: " + task.getDependencies());
            System.out.println();
        }
    }

    /*
     * updates start and end dates sequentially
     */
    private List<TaskDto> updateTasksStartAndEndDatesV1(List<TaskDto> tasks, LocalDate startDate) {
        List<TaskDto> processedTasks = new ArrayList<>();
        List<TaskDto> unprocessedTasks = new ArrayList<>(tasks);

        unprocessedTasks.removeIf(task -> {
            if (task.getDependencies().isEmpty()) {
                processedTasks.add(task);
                return true;
            } return false;
        });

        while (!unprocessedTasks.isEmpty()) {
            Set<Integer> processedTaskIds = processedTasks.stream()
                    .map(TaskDto::getId)
                    .collect(Collectors.toSet());

            unprocessedTasks.removeIf(task -> {
                if(processedTaskIds.containsAll(task.getDependencies())) {
                    processedTasks.add(task);
                    return true;
                } return false;
            });
        }

        AtomicReference<LocalDate> runningStartDate = new AtomicReference<>(startDate);

        return processedTasks.stream()
                .filter(task ->
                        task.getStartDate() == null && task.getEndDate() == null)
                .map(task -> {
                    task.setStartDate(runningStartDate.get());
                    task.setEndDate(runningStartDate.get().plusDays(task.getDurationInDays()-1));
                    runningStartDate.set(task.getEndDate().plusDays(1));
                    return task;
                })
                .collect(Collectors.toList());
    }

    /*
     * enables tasks to start simultaneously if they can
     */
    private List<TaskDto> updateTasksStartAndEndDatesV2(List<TaskDto> tasks, LocalDate startDate) {
        List<TaskDto> processedTasks = new ArrayList<>();
        List<TaskDto> unprocessedTasks = new ArrayList<>(tasks);
        AtomicReference<LocalDate> runningStartDate = new AtomicReference<>(startDate);

        unprocessedTasks.removeIf(task -> {
            if (task.getDependencies().isEmpty()) {
                task.setStartDate(runningStartDate.get());
                task.setEndDate(task.getStartDate().plusDays(task.getDurationInDays()-1));
                processedTasks.add(task);
                return true;
            } return false;
        });

        while (!unprocessedTasks.isEmpty()) {
            Set<Integer> processedTaskIds = processedTasks.stream()
                    .map(TaskDto::getId)
                    .collect(Collectors.toSet());

            unprocessedTasks.removeIf(task -> {
                if(processedTaskIds.containsAll(task.getDependencies())) {
                    runningStartDate.set(getLatestEndDateFromDependencies(task, tasks).plusDays(1));
                    task.setStartDate(runningStartDate.get());
                    task.setEndDate(task.getStartDate().plusDays(task.getDurationInDays()-1));
                    processedTasks.add(task);
                    return true;
                } return false;
            });
        }

        return processedTasks;
    }

    private LocalDate getLatestEndDateFromDependencies(TaskDto task, List<TaskDto> taskDependencies) {
        return task.getDependencies().stream()
                .map(taskDependencyId -> taskDependencies.stream()
                        .filter(t -> t.getId() == taskDependencyId)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Task with ID " + taskDependencyId + " not found")))
                .map(TaskDto::getEndDate)
                .max(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("No dependencies found for task"));
    }


}
