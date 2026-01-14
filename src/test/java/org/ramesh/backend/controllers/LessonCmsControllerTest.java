package org.ramesh.backend.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ramesh.backend.domain.dto.CreateLessonRequest;
import org.ramesh.backend.domain.dto.LessonResponse;
import org.ramesh.backend.domain.dto.ScheduleLessonRequest;
import org.ramesh.backend.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonCmsController.class)
class LessonCmsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LessonService lessonService;

    @Test
    @DisplayName("Create lesson returns 201 on success")
    void createLesson() throws Exception {
        UUID termId = UUID.randomUUID();
        LessonResponse resp = new LessonResponse();
        Mockito.when(lessonService.create(any(), any())).thenReturn(resp);
        mockMvc.perform(post("/cms/terms/" + termId + "/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("List lessons returns 200")
    void listLessons() throws Exception {
        UUID termId = UUID.randomUUID();
        Mockito.when(lessonService.list(termId)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/cms/terms/" + termId + "/lessons"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Schedule lesson returns 202")
    void scheduleLesson() throws Exception {
        UUID lessonId = UUID.randomUUID();
        ScheduleLessonRequest req = new ScheduleLessonRequest();
        Mockito.doNothing().when(lessonService).schedule(any(), any());
        mockMvc.perform(post("/cms/lessons/" + lessonId + "/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isAccepted());
    }
}

