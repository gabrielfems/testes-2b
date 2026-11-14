package br.edu.exemplo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProdutoControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    // CT04 — Cadastro via API
    @Test
    void deveCadastrarProdutoPelaApi() throws Exception {
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Mouse\",\"preco\":80.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Mouse"))
                .andExpect(jsonPath("$.preco").value(80.0));
    }

    // CT05 — Consulta via API
    @Test
    void deveBuscarProdutoPelaApi() throws Exception {
        // 1) cadastra pelo POST e captura o corpo da resposta
        String respostaPost = mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Mouse\",\"preco\":80.0}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 2) extrai o ID gerado pelo banco
        JsonNode cadastrado = objectMapper.readTree(respostaPost);
        long id = cadastrado.get("id").asLong();

        // 3) consulta pelo GET e confere os dados
        String respostaGet = mockMvc.perform(get("/produtos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Mouse"))
                .andExpect(jsonPath("$.preco").value(80.0))
                .andReturn().getResponse().getContentAsString();

        // o JSON da consulta deve ser idêntico ao devolvido no cadastro
        assertEquals(cadastrado, objectMapper.readTree(respostaGet));
    }
}
