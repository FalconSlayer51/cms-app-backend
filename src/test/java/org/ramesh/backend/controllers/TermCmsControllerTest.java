package org.ramesh.backend.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ramesh.backend.domain.dto.TermResponse;
import org.ramesh.backend.service.TermService;
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

@WebMvcTest(TermCmsController.class)
class TermCmsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TermService termService;

    @Test
    @DisplayName("Create term returns 201 on success")
    void createTerm() throws Exception {
        UUID programId = UUID.randomUUID();
        TermResponse resp = new TermResponse(UUID.randomUUID(), 1, "Test Term");
        Mockito.when(termService.create(any(), any())).thenReturn(resp);
        mockMvc.perform(post("/cms/programs/" + programId + "/terms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("List terms returns 200")
    void listTerms() throws Exception {
        UUID programId = UUID.randomUUID();
        Mockito.when(termService.list(programId)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/cms/programs/" + programId + "/terms"))
                .andExpect(status().isOk());
    }
}
