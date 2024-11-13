import org.dto.ProjectDto;
import org.dto.TaskDto;
import org.junit.Test;
import org.service.ProjectService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProjectServiceTest {

    @Test
    public void testSchedulerSequential() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(1);
        projectDto.setName("Test Project");
        projectDto.setStartDate(LocalDate.of(2024, 1, 1));

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
        projectService.initiateScheduler(projectDto, 0); // Using SEQUENTIAL_DATE_VERSION

        assertEquals(LocalDate.of(2024, 1, 1), projectDto.getTasks().get(0).getStartDate());
        assertEquals(LocalDate.of(2024, 1, 2), projectDto.getTasks().get(0).getEndDate());

        assertEquals(LocalDate.of(2024, 1, 7), projectDto.getTasks().get(2).getStartDate());
        assertEquals(LocalDate.of(2024, 1, 11), projectDto.getTasks().get(2).getEndDate());
    }

    @Test
    public void testSchedulerSimultaneous() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(1);
        projectDto.setName("Test Project");
        projectDto.setStartDate(LocalDate.of(2024, 1, 1));

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
        projectService.initiateScheduler(projectDto, 1);

        assertEquals(LocalDate.of(2024, 1, 1), projectDto.getTasks().get(0).getStartDate());
        assertEquals(LocalDate.of(2024, 1, 2), projectDto.getTasks().get(0).getEndDate());

        assertEquals(LocalDate.of(2024, 1, 3), projectDto.getTasks().get(3).getStartDate());
        assertEquals(LocalDate.of(2024, 1, 4), projectDto.getTasks().get(3).getEndDate());
    }
}