package com.br.artur.producer.resources;

import com.br.artur.producer.creator.ProductCreator;
import com.br.artur.producer.dto.RequestDto;
import com.br.artur.producer.service.ProductService;
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

import java.math.RoundingMode;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String URL = "/products";

    @Test
    void getAllTest() throws Exception{
        ResultActions result = mockMvc
                .perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].category").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].category").value("FOOD"));
    }

    @Test
    void getByIdTest() throws Exception{
        ResultActions result = mockMvc
                .perform(MockMvcRequestBuilders.get(URL.concat("/{id}"),1)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.category").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.category").value("FOOD"));
    }

    @Test
    void postTest() throws Exception{
        RequestDto request = ProductCreator.createRequest();
        String jsonBody = objectMapper.writeValueAsString(request);
        ResultActions result = mockMvc
                .perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(request.getCode()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.category").value(request.getCategory()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.series").value(request.getSeries()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(request.getDescription()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.color").value(request.getColor()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.material").value(request.getMaterial()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(request.getName()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.grossAmount").value(request.getGrossAmount().setScale(1, RoundingMode.HALF_EVEN)));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.taxes").value(request.getTaxes().setScale(1, RoundingMode.HALF_EVEN)));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.price").value(ProductService.priceCalculator(request.getGrossAmount(),request.getTaxes())));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(0));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.barCode").value(request.getBarCode()+0));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.manufacturingDate").value(request.getManufacturingDate().toString()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.expirationDate").value(request.getExpirationDate().toString()));
    }

    @Test
    void postFakerTest() throws Exception{
        RequestDto request = ProductCreator.fakerRequest();
        String jsonBody = objectMapper.writeValueAsString(request);
        ResultActions result = mockMvc
                .perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(request.getCode()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.category").value(request.getCategory()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.series").value(request.getSeries()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(request.getDescription()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.color").value(request.getColor()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.material").value(request.getMaterial()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(request.getName()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.grossAmount").value(request.getGrossAmount()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.taxes").value(request.getTaxes().setScale(1, RoundingMode.HALF_EVEN)));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.price").value(ProductService.priceCalculator(request.getGrossAmount(),request.getTaxes())));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(request.getQuantity()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.barCode").value(request.getBarCode()+request.getQuantity()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.manufacturingDate").value(request.getManufacturingDate().toString()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.expirationDate").value(request.getExpirationDate().toString()));
    }

    @Test
    void updateTest() throws Exception{
        RequestDto request = ProductCreator.fakerRequest();
        String jsonBody = objectMapper.writeValueAsString(request);
        ResultActions result = mockMvc
                .perform(MockMvcRequestBuilders.put(URL.concat("/{id}"),2)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.category").value(request.getCategory()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.series").value(request.getSeries()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(request.getDescription()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.color").value(request.getColor()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.material").value(request.getMaterial()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(request.getName()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.grossAmount").value(request.getGrossAmount()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(request.getQuantity()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.manufacturingDate").value(request.getManufacturingDate().toString()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.expirationDate").value(request.getExpirationDate().toString()));
    }

    @Test
    void deleteTest() throws Exception{
        ResultActions result = mockMvc
                .perform(MockMvcRequestBuilders.delete(URL.concat("/{id}"),3)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void patchQuantityTest() throws Exception {
        ResultActions result = mockMvc
                .perform(MockMvcRequestBuilders.patch(URL.concat("/{code}"),"21h437s")
                .param("quantity","300"));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(300));
    }
}
