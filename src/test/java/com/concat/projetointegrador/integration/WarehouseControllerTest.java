package com.concat.projetointegrador.integration;

import com.concat.projetointegrador.dto.WarehouseDTO;
import com.concat.projetointegrador.dto.WarehouseResponseForQuantityProductsDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.concat.projetointegrador.repository.WarehouseRepository;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/test-schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WarehouseRepository warehouseRepository;

    private String getObject() {
        return "{\"name\":\"armazem frios\", \"region\":\"melhor regiao\"}";
    }

    private String warehouseExist() {
        return "{\"name\":\"Tools\", \"region\":\"Doujia\"}";
    }
    @Test
    public void shouldCreateAWarehouseAndReturn201() throws Exception {
        SimpleGrantedAuthority supervisor = new SimpleGrantedAuthority("ADMIN");
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(supervisor);
        MvcResult mvcResult = mockMvc.perform(post("/warehouse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObject())
                        .with(user("Supervisor").password("123").authorities(simpleGrantedAuthorities)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        WarehouseDTO warehouseDTO = objectMapper.readValue(response, WarehouseDTO.class);
        warehouseRepository.deleteById(warehouseDTO.getId());
    }

    @Test
    public void shouldReturnWarehouseByIdReturnStatus200() throws Exception {
        MvcResult result = mockMvc.perform(get("/warehouse/{id}", 1)
                        .with(user("Supervisor").password("123")))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        String jsonReturned = result.getResponse().getContentAsString();

        WarehouseDTO warehouseDTO = objectMapper.readValue(jsonReturned, WarehouseDTO.class);

        assertEquals("Tools", warehouseDTO.getName());
    }

    @Test
    public void shouldReturnQuantityProductByIdForWarehouseReturnStatus200() throws Exception {
        MvcResult result = mockMvc.perform(get("/warehouse/products")
                        .param("productId", "1")
                        .with(user("Supervisor").password("123")))
                .andExpect(status().isOk())
                .andReturn();

        String jsonReturned = result.getResponse().getContentAsString();

        assertEquals("{\"productId\":1,\"warehouses\":[{\"warehouseCode\":1,\"totalQuantity\":11}]}", jsonReturned);
    }
}
