package org.ramesh.backend.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ramesh.backend.domain.dto.CreateProgramRequest;
import org.ramesh.backend.domain.dto.ProgramResponse;
import org.ramesh.backend.service.ProgramPublishService;
import org.ramesh.backend.service.ProgramService;
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

@WebMvcTest(ProgramCmsController.class)
class ProgramCmsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProgramService programService;
    @MockBean
    private ProgramPublishService programPublishService;

    @Test
    @DisplayName("List returns 200 and list of programs")
    void listPrograms() throws Exception {
        Mockito.when(programService.list()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/cms/programs"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Create returns 201 on success")
    void createProgram() throws Exception {
        CreateProgramRequest req = new CreateProgramRequest();
        ProgramResponse resp = new ProgramResponse();
        Mockito.when(programService.create(any())).thenReturn(resp);
        mockMvc.perform(post("/cms/programs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Publish returns 200 on success")
    void publishSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(programPublishService).publish(id);
        mockMvc.perform(post("/cms/programs/" + id + "/publish"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Publish returns 400 on error")
    void publishError() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.doThrow(new RuntimeException("fail")).when(programPublishService).publish(id);
        mockMvc.perform(post("/cms/programs/" + id + "/publish"))
                .andExpect(status().isBadRequest());
    }
}

