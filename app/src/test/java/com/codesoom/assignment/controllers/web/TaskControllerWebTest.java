package com.codesoom.assignment.controllers.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private enum TaskList {
        FIRST(1L, "FIRST TASK"),
        SECOND(2L, "SECOND TASK");

        private final long id;
        private final String title;

        TaskList(long id, String title) {
            this.id = id;
            this.title = title;
        }

        Task toTask() {
            return new Task(this.id, this.title);
        }

        public Long getId() {
            return this.id;
        }
    }

    @Nested
    @DisplayName("GET /tasks 요청은")
    class Describe_getTasks {

        @BeforeEach
        void setUp() {
            Task task = TaskList.FIRST.toTask();

            given(taskService.getTasks())
                .willReturn(Collections.singletonList(task));
        }

        @Test
        @DisplayName("모든 할 일 목록을 응답한다")
        void it_response_200() throws Exception {
            mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("GET /tasks/{id} 요청은")
    class Describe_getTask {

        @Nested
        @DisplayName("존재하지 않는 할 일 일경우")
        class Context_notExistTask {

            @BeforeEach
            void setUp() {
                Long id = TaskList.SECOND.getId();

                given(taskService.getTask(id))
                    .willThrow(new TaskNotFoundException(id));
            }

            @Test
            @DisplayName("404를 응답한다")
            void it_response_404() throws Exception {
                mockMvc.perform(get("/tasks/2"))
                    .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("존재하는 할 일 일경우")
        class Context_existTask {

            @BeforeEach
            void setUp() {
                Long id = TaskList.FIRST.getId();
                Task task = TaskList.FIRST.toTask();

                given(taskService.getTask(id))
                    .willReturn(task);
            }

            @Test
            @DisplayName("200을 응답한다")
            void it_response_200() throws Exception {
                mockMvc.perform(get("/tasks/1"))
                    .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("POST /tasks 요청은")
    class Describe_createTask {

        private Task task;

        @BeforeEach
        void setUp() {
            task = TaskList.FIRST.toTask();

            given(taskService.createTask(task))
                .willReturn(task);
        }

        @Test
        @DisplayName("201을 응답한다")
        void it_response_201() throws Exception {
            mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(task.stringify()))
                .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("PUT /tasks/{id} 요청은")
    class Describe_updateTaskPut {

        private Task task;

        @Nested
        @DisplayName("존재하지 않는 할 일 일경우")
        class Context_notExistTask {

            @BeforeEach
            void setUp() {
                Long id = TaskList.SECOND.getId();
                task = TaskList.SECOND.toTask();

                given(taskService.updateTask(id, task))
                    .willThrow(new TaskNotFoundException(id));
            }

            @Test
            @DisplayName("404을 응답한다")
            void it_response_404() throws Exception {
                mockMvc.perform(put("/tasks/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(task.stringify()))
                    .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("존재하는 할 일 일경우")
        class Context_existTask {

            @BeforeEach
            void setUp() {
                Long id = TaskList.FIRST.getId();
                task = TaskList.SECOND.toTask();

                given(taskService.updateTask(id, task))
                    .willReturn(new Task(id, task.getTitle()));
            }

            @Test
            @DisplayName("200을 응답한다")
            void it_response_200() throws Exception {
                mockMvc.perform(put("/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(task.stringify()))
                    .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("PATCH /tasks/{id} 요청은")
    class Describe_updateTaskPatch {

        private Task task;

        @Nested
        @DisplayName("존재하지 않는 할 일 일경우")
        class Context_notExistTask {

            @BeforeEach
            void setUp() {
                Long id = TaskList.SECOND.getId();
                task = TaskList.SECOND.toTask();

                given(taskService.updateTask(id, task))
                    .willThrow(new TaskNotFoundException(id));
            }

            @Test
            @DisplayName("404을 응답한다")
            void it_response_404() throws Exception {
                mockMvc.perform(patch("/tasks/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(task.stringify()))
                    .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("존재하는 할 일 일경우")
        class Context_existTask {

            @BeforeEach
            void setUp() {
                Long id = TaskList.FIRST.getId();
                task = TaskList.SECOND.toTask();

                given(taskService.updateTask(id, task))
                    .willReturn(new Task(id, task.getTitle()));
            }

            @Test
            @DisplayName("200을 응답한다")
            void it_response_200() throws Exception {
                mockMvc.perform(patch("/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(task.stringify()))
                    .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("DELETE /tasks/{id} 요청은")
    class Describe_deleteTask {

        @Nested
        @DisplayName("존재하지 않는 할 일 일경우")
        class Context_notExistTask {

            @BeforeEach
            void setUp() {
                Long id = TaskList.SECOND.getId();

                given(taskService.deleteTask(id))
                    .willThrow(new TaskNotFoundException(id));
            }

            @Test
            @DisplayName("404을 응답한다")
            void it_response_404() throws Exception {
                mockMvc.perform(delete("/tasks/2"))
                    .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("존재하는 할 일 일경우")
        class Context_existTask {

            @BeforeEach
            void setUp() {
                Long id = TaskList.FIRST.getId();
                Task task = TaskList.FIRST.toTask();

                given(taskService.deleteTask(id))
                    .willReturn(task);
            }

            @Test
            @DisplayName("204를 응답한다")
            void it_response_204() throws Exception {
                mockMvc.perform(delete("/tasks/1"))
                    .andExpect(status().isNoContent());
            }
        }
    }
}
