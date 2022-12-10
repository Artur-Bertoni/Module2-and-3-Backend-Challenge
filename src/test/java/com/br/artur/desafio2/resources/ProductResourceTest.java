package com.br.artur.desafio2.resources;

import com.br.artur.desafio2.creator.ProductCreator;
import com.br.artur.desafio2.dto.RequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String URL = "/products";

    @Test
    void findAllShouldReturnResponseEntityWhenSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].category").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].category").value("FOOD"));
    }

    @Test
    void findAllShouldReturnResponseEntityWhenSuccessV2() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].category").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].category").value("FOOD"));
    }

    @Test
    void findByIdShouldReturnResponseEntityWhenSuccess() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/{id}"),1)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.category").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.category").value("FOOD"));
    }

    @Test
    void insertShouldReturnResponseEntityWhenSuccess() throws Exception{
        RequestDTO request = ProductCreator.createRequest();
        String jsonBody = objectMapper.writeValueAsString(request);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(request.getCode()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.category").value(request.getCategory()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.series").value(request.getSeries()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(request.getDescription()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.color").value(request.getColor()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.material").value(request.getMaterial()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(request.getName()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.grossAmount").value(request.getGrossAmount()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.taxes").value(request.getTaxes()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.price").value(request.getPrice()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(request.getQuantity()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.barCode").value(request.getBarCode()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.manufacturingDate").value(request.getManufacturingDate()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.expirationDate").value(request.getExpirationDate()));
    }

    @Test
    void insertShouldReturnResponseEntityWhenSuccessFaker() throws Exception{
        var request = ProductCreator.createRequest();
        var jsonBody = objectMapper.writeValueAsString(request);
        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.category").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.category").value(request.get...().toString())); //TODO para tdos (menos 'toString')
    }

    @Test
    void updateShouldReturnResponseEntityWhenSuccess() throws Exception{
        var request = ProductCreator.updateRequest();
        var jsonBody = objectMapper.writeValueAsString(request);
        var result = mockMvc.perform(MockMvcRequestBuilders.put(URL.concat("/{id}"),1)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.category").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.category").value(request.get...().toString())); //TODO para tdos os updates (menos 'toString')
    }

}
