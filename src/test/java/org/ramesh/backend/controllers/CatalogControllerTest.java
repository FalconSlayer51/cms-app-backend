package org.ramesh.backend.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ramesh.backend.domain.dto.LessonCatalogResponse;
import org.ramesh.backend.domain.dto.ProgramCatalogResponse;
import org.ramesh.backend.domain.dto.TermCatalogResponse;
import org.ramesh.backend.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CatalogController.class)
class CatalogControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CatalogService catalogService;

    @Test
    @DisplayName("Programs returns 200")
    void programs() throws Exception {
        Mockito.when(catalogService.listPrograms()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/catalog/programs"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Program returns 200")
    void program() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(catalogService.getProgram(id)).thenReturn(new ProgramCatalogResponse());
        mockMvc.perform(get("/catalog/programs/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Terms returns 200")
    void terms() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(catalogService.listTerms(id)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/catalog/programs/" + id + "/terms"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Lessons returns 200")
    void lessons() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(catalogService.listLessons(id)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/catalog/terms/" + id + "/lessons"))
                .andExpect(status().isOk());
    }
}
