package org.ramesh.backend.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ramesh.backend.domain.dto.PresignAssetRequest;
import org.ramesh.backend.domain.dto.PresignAssetResponse;
import org.ramesh.backend.domain.dto.PresignResponse;
import org.ramesh.backend.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssetCmsController.class)
class AssetCmsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AssetService assetService;

    @Test
    @DisplayName("Presign program returns 200 on success")
    void presignProgram() throws Exception {
        UUID programId = UUID.randomUUID();
        PresignAssetResponse resp = new PresignAssetResponse();
        Mockito.when(assetService.presignAssetResponse(any(), any())).thenReturn(resp);
        mockMvc.perform(post("/cms/assets/presign/program/" + programId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get all program urls returns 200")
    void getAllProgramUrls() throws Exception {
        UUID programId = UUID.randomUUID();
        PresignResponse resp = new PresignResponse();
        Mockito.when(assetService.getAllProgramUrls(programId)).thenReturn(resp);
        mockMvc.perform(get("/cms/assets/program/" + programId))
                .andExpect(status().isOk());
    }
}

