package com.concat.projetointegrador.integration;

import com.concat.projetointegrador.model.InboundOrder;
import com.concat.projetointegrador.model.PurchasedOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql({"/test-schema.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PurchaseOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String payload() {
        return "{\"buyer\":{\"id\":4},\"status\":\"aberto\",\"cart\":[{\"product\":{\"id\": 1},\"quantity\": 5}]}";
    }

    @Test
    public void shouldCreateAPurchaseOrderAndReturn201() throws Exception {
        SimpleGrantedAuthority supervisor = new SimpleGrantedAuthority("Buyer");
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(supervisor);
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("Buyer").password("123").authorities(simpleGrantedAuthorities))
                .content(payload()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andReturn();
    }

    @Test
    public void shouldFindByIdAndReturn200() throws Exception {
        SimpleGrantedAuthority supervisor = new SimpleGrantedAuthority("Buyer");
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(supervisor);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/orders")
                .with(user("Buyer").password("123").authorities(simpleGrantedAuthorities))
                .param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        String jsonReturned = result.getResponse().getContentAsString();
        PurchasedOrder purchasedOrder = objectMapper.readValue(jsonReturned, PurchasedOrder.class);
        assertEquals("aberto", purchasedOrder.getStatus());
    }

    @Test
    public void shouldUpdateAndReturn200() throws Exception {
        SimpleGrantedAuthority supervisor = new SimpleGrantedAuthority("Buyer");
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(supervisor);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/orders")
                .with(user("Buyer").password("123").authorities(simpleGrantedAuthorities))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload())
                .param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        String jsonReturned = result.getResponse().getContentAsString();
        PurchasedOrder purchasedOrder = objectMapper.readValue(jsonReturned, PurchasedOrder.class);
        assertEquals("finalizado", purchasedOrder.getStatus());
    }
}
