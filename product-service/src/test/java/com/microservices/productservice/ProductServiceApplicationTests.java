package com.microservices.productservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.productservice.dto.ProductRequest;
import com.microservices.productservice.dto.ProductResponse;
import com.microservices.productservice.repositories.ProductRepository;
import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static MongoDBContainer mongodbContainer = new MongoDBContainer("mongo:4.4.2");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongodbContainer::getReplicaSetUrl);
    }
    
    @Test
    void contextLoads() {
    }

    @Test
    public void shouldCreateProduct() throws Exception {
        createMockProduct("Samsung s23 Ultra", "Samsung S23 ULTRA: ROM - 128Go, RAM - 4GO", 1200L);
        Assertions.assertTrue(productRepository.findAll().size() == 1);
    }
    
    @Test
    public void shouldGetAllProducts() throws Exception {
        createMockProduct("Samsung s23 Ultra", "Samsung S23 ULTRA: ROM - 128Go, RAM - 4GO", 1200L);
        createMockProduct("Iphone 15 PRO MAX", " CAMERA FRONTALE - 32MP, ROM - 512Go, RAM - 16GO", 3000L);
    
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/product/products"))
          .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
        // Extract the response content as a String
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        // Convert the JSON response to a list of Product objects
        List<ProductResponse> productList = objectMapper.readValue(jsonResponse, new TypeReference<List<ProductResponse>>() {});
        
        org.junit.jupiter.api.Assertions.assertEquals(2, productList.size());
    }

    private ProductRequest getProductRequest(String name, String description, Long price) {
        return ProductRequest.builder()
          .name(name)
          .description(description)
          .price(BigDecimal.valueOf(price)).build();
    }
    
    private void createMockProduct(String name, String description, Long price) throws Exception {
        ProductRequest productRequest = getProductRequest(name, description, price);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product/createProduct")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest)))
          .andExpect(status().isCreated());
    }

}
