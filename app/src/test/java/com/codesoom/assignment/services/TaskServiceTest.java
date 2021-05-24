package com.codesoom.assignment.services;

import com.codesoom.assignment.common.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("TaskService의 단위 테스트")
class TaskServiceTest {

    private TaskService taskService;

    private final Long NOT_FOUND_TASK_ID = 100L; // 목록에 없는 할 일 ID
    private final Long NEW_TASK_ID = 1L; // 새로 생성할 할 일 ID

    private final String NEW_TASK_TITLE = "Test Title"; // 새로 생성할 할 일 제목
    private final String UPDATE_TASK_TITLE = "Test Title Updated"; // 수정된 할 일 제목

    @BeforeEach
    void setUp(){

        taskService = new TaskService();

    }

    @Nested
    @DisplayName("getTaskList 메소드는")
    class Describe_getTaskList {

        @Nested
        @DisplayName("만약 목록이 비어있다면")
        class Context_empty {

            @Test
            @DisplayName("빈 할 일 목록을 반환합니다.")
            void it_return_emptyList() {
                List<Task> taskList = taskService.getTaskList();

                Assertions.assertThat(taskList).isEmpty();
                Assertions.assertThat(taskList).hasSize(0);
            }

        }

        @Nested
        @DisplayName("만약 목록이 비어있지 않다면")
        class Context_not_empty {

            @BeforeEach
            void setUp() {
                String taskTitle1 = NEW_TASK_TITLE + "1";
                String taskTitle2 = NEW_TASK_TITLE + "2";
                taskService.saveNewTask(taskTitle1);
                taskService.saveNewTask(taskTitle2);
            }

            @Test
            @DisplayName("비어있지 않은 할 일 목록을 반환합니다.")
            void it_return_list() {
                List<Task> taskList = taskService.getTaskList();
                Assertions.assertThat(taskList).isNotEmpty().hasSize(2);
            }

        }

    }

    @Nested
    @DisplayName("findTaskOne 메소드는")
    class Describe_findTaskOne {

        @Nested
        @DisplayName("만약 할 일 목록에 없는 할 일을 조회한다면")
        class Context_invalid_task_id {

            @BeforeEach
            void setUp() {
                taskService.saveNewTask(NEW_TASK_TITLE);
            }

            @Test
            @DisplayName("TaskNotFound 예외를 던집니다.")
            void it_throw_task_not_found_exception() {
                Assertions.assertThatThrownBy( () -> taskService.findTaskOne(NOT_FOUND_TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }

        @Nested
        @DisplayName("만약 할 일 목록에 있는 할 일을 조회한다면")
        class Context_valid_task_id {

            private Long foundTaskId = NEW_TASK_ID; // 조회할 할 일 ID

            @BeforeEach
            void setUp() {
                taskService.saveNewTask(NEW_TASK_TITLE);
            }

            @Test
            @DisplayName("조회된 할 일을 반환합니다.")
            void it_return_found_task() {
                Task foundTask = taskService.findTaskOne(foundTaskId);

                Assertions.assertThat(foundTask).isNotNull();
                Assertions.assertThat(foundTask.getId()).isEqualTo(foundTaskId);
                Assertions.assertThat(foundTask.getTitle()).isEqualTo(NEW_TASK_TITLE);
            }

        }

    }

    @Nested
    @DisplayName("saveNewTask 메소드는")
    class Describe_saveTask {

        @Nested
        @DisplayName("만약 할 일 생성 요청을 보낸다면")
        class Context_valid_task_id {

            private Long newTaskId = NEW_TASK_ID; // 생성할 할 일 ID

            @Test
            @DisplayName("생성된 할 일을 반환합니다.")
            void it_return_created_task() {
                Task createdTask = taskService.saveNewTask(NEW_TASK_TITLE);

                Assertions.assertThat(createdTask.getId()).isEqualTo(newTaskId);
                Assertions.assertThat(createdTask.getTitle()).isEqualTo(NEW_TASK_TITLE);

                // 새로운 할 일이 생성되었음을 확인
                Task foundTask = taskService.findTaskOne(createdTask.getId());
                Assertions.assertThat(foundTask.getId()).isEqualTo(createdTask.getId());
                Assertions.assertThat(foundTask.getTitle()).isEqualTo(createdTask.getTitle());
            }

        }

    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {

        @Nested
        @DisplayName("만약 할 일 목록에 없는 할 일을 수정한다면")
        class Context_invalid_task_id {

            private Long updateTaskId = NEW_TASK_ID; // 수정할 할 일 ID
            private Task paramTask = new Task(); // 파라미터로 사용될 할 일 객체

            @BeforeEach
            void setUp() {
                paramTask.setTitle(UPDATE_TASK_TITLE);
            }

            @Test
            @DisplayName("TaskNotFound 예외를 던집니다.")
            void it_throw_task_not_found_exception() {
                Assertions.assertThatThrownBy( () -> taskService.updateTask(updateTaskId, paramTask.getTitle()))
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }

        @Nested
        @DisplayName("만약 할 일 목록에 있는 할 일을 수정한다면")
        class Context_valid_task_id {

            private Long updateTaskId; // 수정할 할 일 ID
            private Task paramTask = new Task(); // 파라미터로 사용될 할 일 객체

            @BeforeEach
            void setUp() {
                Task targetTask = taskService.saveNewTask(NEW_TASK_TITLE);
                updateTaskId = targetTask.getId();

                paramTask.setTitle(UPDATE_TASK_TITLE);
            }

            @Test
            @DisplayName("수정된 할 일을 반환합니다.")
            void it_return_updated_task() {
                Task updatedTask = taskService.updateTask(updateTaskId, paramTask.getTitle());

                Assertions.assertThat(updatedTask).isNotNull();
                Assertions.assertThat(updatedTask.getId()).isEqualTo(updateTaskId);
                Assertions.assertThat(updatedTask.getTitle()).isEqualTo(UPDATE_TASK_TITLE);

                // 해당 할 일이 수정되었음을 확인
                Task foundTask = taskService.findTaskOne(updatedTask.getId());
                Assertions.assertThat(foundTask.getId()).isEqualTo(updatedTask.getId());
                Assertions.assertThat(foundTask.getTitle()).isEqualTo(updatedTask.getTitle());
            }

        }

    }

    @Nested
    @DisplayName("removeTask 메소드는")
    class Describe_removeTask {

        @Nested
        @DisplayName("만약 할 일 목록에 없는 할 일을 삭제한다면")
        class Context_invalid_task_id {

            @Test
            @DisplayName("TaskNotFound 예외를 던집니다.")
            void it_throw_task_not_found_exception() {
                Assertions.assertThatThrownBy( () -> taskService.removeTask(NOT_FOUND_TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }

        @Nested
        @DisplayName("만약 할 일 목록에 있는 할 일을 삭제한다면")
        class Context_valid_task_id {

            Long removedTaskId;

            @BeforeEach
            void setUp() {
                Task targetTask = taskService.saveNewTask(NEW_TASK_TITLE);
                removedTaskId = targetTask.getId(); // 삭제할 할 일 ID
            }

            @Test
            @DisplayName("할 일을 삭제하고, 삭제한 할 일을 반환합니다.")
            void it_return_and_deleted_task() {
                Task removedTask = taskService.removeTask(removedTaskId);
                Assertions.assertThat(removedTask.getId()).isEqualTo(removedTaskId);

                // 현재 할 일 목록에서 삭제됨을 확인
                Assertions.assertThatThrownBy( () -> taskService.findTaskOne(removedTask.getId()))
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }

    }

}