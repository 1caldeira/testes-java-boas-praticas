package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AbrigoDto;
import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

    @InjectMocks
    private AbrigoService abrigoService;

    @Mock
    private AbrigoRepository abrigoRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private AbrigoDto dto;

    private CadastroAbrigoDto dtoCadastro;
    @Mock
    private Abrigo abrigo;

    @Captor
    private ArgumentCaptor<Abrigo> abrigoCaptor;

    @Test
    void deveriaConverterListaAbrigoEmListaAbrigoDTO(){
        List<Abrigo> abrigoList = new ArrayList<>();
        abrigoList.add(abrigo);
        List<AbrigoDto> dtos = new ArrayList<>();
        dtos.add(new AbrigoDto(abrigo));
        BDDMockito.given(abrigoRepository.findAll()).willReturn(abrigoList);
        Assertions.assertEquals(dtos, abrigoService.listar());
    }

    @Test
    void deveriaCadastrarAbrigo(){
        CadastroAbrigoDto dtoCadastro = new CadastroAbrigoDto("dunha","13981922929","dunha@teste.com");
        Abrigo abrigoQueDeveriaSerSalvo = new Abrigo(dtoCadastro);
        BDDMockito.given(abrigoRepository.existsByNomeOrTelefoneOrEmail(dtoCadastro.nome(), dtoCadastro.telefone(), dtoCadastro.email())).willReturn(false);
        abrigoService.cadastrar(dtoCadastro);
        then(abrigoRepository).should().save(abrigoCaptor.capture());
        Abrigo abrigoSalvo = abrigoCaptor.getValue();
        Assertions.assertEquals(abrigoQueDeveriaSerSalvo,abrigoSalvo);
    }

    @Test
    void deveriaLancarExceptionAbrigoJaExiste(){
        CadastroAbrigoDto dtoCadastro = new CadastroAbrigoDto("dunha","13981922929","dunha@teste.com");
        BDDMockito.given(abrigoRepository.existsByNomeOrTelefoneOrEmail(dtoCadastro.nome(), dtoCadastro.telefone(), dtoCadastro.email())).willReturn(true);
        Assertions.assertThrows(ValidacaoException.class,()-> abrigoService.cadastrar(dtoCadastro));

    }


}