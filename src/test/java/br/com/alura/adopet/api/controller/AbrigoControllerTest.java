package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AbrigoDto;
import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.AdocaoService;
import br.com.alura.adopet.api.service.PetService;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AbrigoControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private PetService petService;
    @MockBean
    private AbrigoService abrigoService;

    @Test
    void deveriaDevolverCodigo400ParaSolicitacaoDeCadastroDeAbrigoComErros() throws Exception {
        //ARRANGE
        String json = "{}";

        //ACT
        MockHttpServletResponse response = mvc.perform(
                post("/abrigos")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaSolicitacaoDeCadastroSemErros() throws Exception {
        //ARRANGE
        String json = """
                {
                    "nome" : "teste",
                    "telefone" : "13981199202",
                    "motivo" : "email@teste.com"
                }
                """;

        //ACT
        MockHttpServletResponse response = mvc.perform(
                post("/adocoes")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaDevolverAbrigosCadastradosEStatusCode200() throws Exception {
        //ARRANGE
        List<AbrigoDto> abrigos = Arrays.asList(
                new AbrigoDto(1l, "Abrigo1"),
                new AbrigoDto(2l, "Abrigo2"));

        BDDMockito.given(abrigoService.listar()).willReturn(abrigos);

        //ACT
        ResultActions response = mvc.perform(
                get("/abrigos"));

        //ASSERT
        response.andExpect(status().isOk()).andExpect(content().json(
                "[{\"id\":1,\"nome\":\"Abrigo1\"},{\"id\":2,\"nome\":\"Abrigo2\"}]"));
    }

    @Test
    void deveriaDevolverCodigo200ParaCadastroValidoDePet() throws Exception {
        CadastroPetDto cadastroPetDto = new CadastroPetDto(TipoPet.CACHORRO,"nome","raca",10,"cor",10f);
        Abrigo abrigo = new Abrigo(new CadastroAbrigoDto("teste","13982292929","teste@teste.com"));
        BDDMockito.when(abrigoService.carregarAbrigo("1")).thenReturn(abrigo);
        String json = """
                {
                    "tipo" : "CACHORRO",
                    "nome" : "nometeste",
                    "raca" : "poodle",
                    "idade" : 10,
                    "cor" : "corteste",
                    "peso" : 10
                }
                """;

        MockHttpServletResponse response = mvc.perform(
                post("/abrigos/1/pets")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        Assertions.assertEquals(200,response.getStatus());
    }

}