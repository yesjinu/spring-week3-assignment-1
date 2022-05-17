package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    private TaskService service;
    private TaskController controller;
    private final String TASK_TITLE = "Test Task";
    private final String INPUT_TITLE = "Input Task Title";
    private final Long TASK_ID = 1L;
    private final int FIRST = 0;
    private final int TASKS_SIZE = 1;

    @BeforeEach
    void setUp() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        service = new TaskService();
        service.createTask(task);
        controller = new TaskController(service);
    }

    @Test
    @DisplayName("기본 생성된 Task가 하나만 존재하면, 목록조회 요청 시 반환값 내 Task의 개수는 1개여야 한다")
    void whenList_returnEqualSize() {
        final List<Task> expected = service.getTasks();
        final List<Task> actual = controller.list();

        assertThat(actual).hasSameSizeAs(expected);
    }

    @Test
    @DisplayName("기본 생성된 Task가 하나만 존재하면, 목록조회 요청 시 반환된 Task id는 기본 생성된 Task의 id와 같아야 한다")
    void whenList_returnEqualId() {
        final Long actual = controller.list().get(FIRST).getId();

        assertThat(actual).isEqualTo(TASK_ID);
    }

    @Test
    @DisplayName("기본 생성된 Task가 하나만 존재하면, 목록조회 요청 시 반환된 Task title은 기본 생성된 Task의 title과 같아야 한다")
    void whenList_returnEqualTitle() {
        final String actual = controller.list().get(FIRST).getTitle();

        assertThat(actual).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("기본 생성된 Task가 하나만 존재하면, 해당 Task 상세조회 요청 시 반환된 Task의 id는 기본 생성된 Task의 id와 같아야 한다")
    void whenDetail_returnEqualId() {
        final Long actual = controller.detail(TASK_ID).getId();

        assertThat(actual).isEqualTo(TASK_ID);
    }

    @Test
    @DisplayName("기본 생성된 Task가 하나만 존재하면, 해당 Task 상세조회 요청 시 반환된 Task의 title은 기본 생성된 Task의 title과 같아야 한다")
    void whenDetail_returnEqualTitle() {
        final String actual = controller.detail(TASK_ID).getTitle();

        assertThat(actual).isEqualTo(TASK_TITLE);
    }
    @Test
    @DisplayName("기본 생성된 Task가 하나만 존재하면, 해당 Task 생성 요청 시 Task 목록 요청에 따른 반환값의 사이즈는 1이 증가해야 한다")
    void whenCreate_shouldSizeUp() {
        final int expected = TASKS_SIZE + 1;
        final Task task = new Task();
        task.setTitle(INPUT_TITLE);

        controller.create(task);
        final int actual = controller.list().size();

        assertThat(actual).isEqualTo(expected);
    }


    @Test
    @DisplayName("Task 생성 요청 시 반환된 Task의 title은 요청 시 전달한 title과 같아야 한다")
    void whenCreate_returnTaskWithFields() {
        final Task task = new Task();
        task.setTitle(INPUT_TITLE);

        final String actual = controller.create(task).getTitle();

        assertThat(actual).isEqualTo(INPUT_TITLE);
    }

    @Test
    @DisplayName("Task 생성 요청 시 반환된 Task의 id는 null일 수 없다")
    void whenCreate_returnTaskWithNotNullId() {
        final Task task = new Task();
        task.setTitle(INPUT_TITLE);

        final Long actual = controller.create(task).getId();
        assertThat(actual).isNotNull();
    }
}
