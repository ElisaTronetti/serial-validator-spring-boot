package com.interview.serial_validator_spring_boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.serial_validator_spring_boot.dto.request.SerialDto;
import com.interview.serial_validator_spring_boot.enums.ResponseStatus;
import com.interview.serial_validator_spring_boot.service.SerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;


@WebMvcTest(SerialValidatorController.class)
public class SerialValidatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SerialService serialService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAcceptValidSerial() throws Exception {
        SerialDto dto = new SerialDto();
        dto.setSerial("A123456789012345");

        mockMvc.perform(post("/serial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ResponseStatus.VALID.name()))
                .andExpect(jsonPath("$.serial").value("A123456789012345"));
    }

    @Test
    void shouldRejectInvalidSerial() throws Exception {
        SerialDto dto = new SerialDto();
        dto.setSerial("invalid_serial");

        mockMvc.perform(post("/serial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnListOfValidSerials() throws Exception {
        List<String> mockSerials = List.of("A123456789012345", "B987654321098765");
        Mockito.when(serialService.getSerials()).thenReturn(mockSerials);

        mockMvc.perform(get("/valid-serials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serials", containsInAnyOrder("A123456789012345", "B987654321098765")));
    }
}