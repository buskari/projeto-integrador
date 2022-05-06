package com.concat.projetointegrador.integration;

import com.concat.projetointegrador.dto.InboundOrderDTO;
import com.concat.projetointegrador.dto.ProductResponseDTO;
import com.concat.projetointegrador.model.Product;
import com.concat.projetointegrador.repository.ProductRepository;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/test-schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String payloadProduct() {
        return "{\"id\":2,\"name\":\"Asinha de frango\",\"volume\":10,\"price\":11.0,\"category\":\"FRESCOS\",\"seller\":{\"id\": 3}}";
    }

    @Test
    public void shouldCreateAProductAndReturn201() throws Exception {
        SimpleGrantedAuthority supervisor = new SimpleGrantedAuthority("Seller");
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(supervisor);
                mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadProduct())
                        .with(user("Seller").password("123").authorities(simpleGrantedAuthorities)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andReturn();
    }

    @Test
    public void shouldFindByCategoryAndReturn200() throws Exception {
        SimpleGrantedAuthority supervisor = new SimpleGrantedAuthority("Seller");
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(supervisor);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products/category/{category}", "CONGELADOS")
                .with(user("Seller").password("123").authorities(simpleGrantedAuthorities)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("[{\"name\":\"frango\",\"volume\":1,\"price\":20.00,\"category\":\"CONGELADOS\"}]", response);
    }

    @Test
    public void shouldFindAllAndReturn200() throws Exception {
        SimpleGrantedAuthority supervisor = new SimpleGrantedAuthority("Seller");
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(supervisor);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products")
                .with(user("Seller").password("123").authorities(simpleGrantedAuthorities)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("[{\"name\":\"frango\",\"volume\":1,\"price\":20.00,\"category\":\"CONGELADOS\"}]", response);
    }

    @Test
    public void shouldFindByBatchStockByProductsAndReturn200() throws Exception {
        SimpleGrantedAuthority supervisor = new SimpleGrantedAuthority("Seller");
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(supervisor);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products/list/{id}", "1" )
                .with(user("Seller").password("123").authorities(simpleGrantedAuthorities)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        String jsonReturned = result.getResponse().getContentAsString();
        ProductResponseDTO product = objectMapper.readValue(jsonReturned, ProductResponseDTO.class);
        assertEquals(1L, product.getProductId());
    }

    @Test
    public void shouldFindByIdAndReturn200() throws Exception {
        SimpleGrantedAuthority supervisor = new SimpleGrantedAuthority("Seller");
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(supervisor);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", "1" )
                .with(user("Seller").password("123").authorities(simpleGrantedAuthorities)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("{\"name\":\"frango\",\"volume\":1,\"price\":20.00,\"category\":\"CONGELADOS\"}", response);
    }

}
