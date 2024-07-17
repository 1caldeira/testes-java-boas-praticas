package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AbrigoDto;
import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.PetDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

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
    @Mock
    private Pet pet;
    @Mock
    private Optional<Abrigo> abrigoOptional;

    @Mock
    private ArgumentCaptor<Pet> petCaptor;

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
    @Test
    void deveriaRetornarPetsDoAbrigoPorId() {
        String idOuNome = "1";
        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

        Mockito.when(abrigoRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(abrigo));
        Mockito.when(abrigoRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(abrigo));
        when(petRepository.findByAbrigo(abrigo)).thenReturn(pets);

        List<PetDto> petDtos = abrigoService.listarPetsDoAbrigo(idOuNome);

        Assertions.assertEquals(pets.size(), petDtos.size());
    }

    @Test
    void deveriaRetornarPetsDoAbrigoPorNome() {
        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

        Mockito.when(abrigoRepository.findByNome(Mockito.anyString())).thenReturn(Optional.of(abrigo));
        Mockito.when(abrigoRepository.findByNome(Mockito.anyString())).thenReturn(Optional.ofNullable(abrigo));
        when(petRepository.findByAbrigo(abrigo)).thenReturn(pets);

        List<PetDto> petDtos = abrigoService.listarPetsDoAbrigo("anyString");

        Assertions.assertEquals(pets.size(), petDtos.size());
    }

}