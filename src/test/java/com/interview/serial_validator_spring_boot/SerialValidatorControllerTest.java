package com.interview.serial_validator_spring_boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.serial_validator_spring_boot.controller.SerialValidatorController;
import com.interview.serial_validator_spring_boot.dto.request.SerialDto;
import com.interview.serial_validator_spring_boot.enums.ResponseStatus;
import com.interview.serial_validator_spring_boot.validation.SerialValidationCache;
import com.interview.serial_validator_spring_boot.validation.ChecksumValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SerialValidatorController.class)
@Import({ChecksumValidator.class, SerialValidationCache.class})
public class SerialValidatorControllerTest {
	private static final String VALID_SERIAL_1 = "A1B2C3D4E5F6GAH3";
	private static final String VALID_SERIAL_2 = "A12CB34E56HFGDA3";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SerialValidationCache serialValidationCache;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldAcceptValidSerial() throws Exception {
		SerialDto dto = new SerialDto(VALID_SERIAL_1);

		mockMvc.perform(post("/serial")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value(ResponseStatus.VALID.name()))
				.andExpect(jsonPath("$.serial").value(VALID_SERIAL_1));
	}

	@Test
	void shouldRejectInvalidSerial() throws Exception {
		SerialDto dto = new SerialDto("invalid_serial");

		mockMvc.perform(post("/serial")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(ResponseStatus.INVALID.name()))
				.andExpect(jsonPath("$.reason").value("Checksum failed"));
	}

	@Test
	void shouldReturnListOfValidSerials() throws Exception {
		mockMvc.perform(post("/serial")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new SerialDto(VALID_SERIAL_1))));

		mockMvc.perform(post("/serial")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new SerialDto(VALID_SERIAL_2))));

		mockMvc.perform(get("/valid-serials"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.serials", containsInAnyOrder(VALID_SERIAL_1, VALID_SERIAL_2)));
	}
}
