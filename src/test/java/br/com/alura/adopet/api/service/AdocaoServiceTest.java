package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.*;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoPetDisponivel;
import br.com.alura.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import br.com.alura.adopet.api.validacoes.ValidacaoTutorComLimiteDeAdocoes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;



@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    @InjectMocks
    private AdocaoService adocaoService;
    @Mock
    private AdocaoRepository adocaoRepository;
    @Mock
    private PetRepository petRepository;
    @Mock
    private TutorRepository tutorRepository;
    @Mock
    private EmailService emailService;
    @Spy
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();
    @Mock
    private ValidacaoTutorComLimiteDeAdocoes validador1;
    @Mock
    private ValidacaoPetDisponivel validador2;
    @Mock
    private Pet pet;
    @Mock
    private Tutor tutor;
    @Mock
    private Abrigo abrigo;
    private SolicitacaoAdocaoDto solicitacaoDto;
    @Captor
    private ArgumentCaptor<Adocao> adocaoCaptor;

    @Test
    void deveriaSalvarAdocaoAoSolicitar() {
        //ARRANGE
        this.solicitacaoDto = new SolicitacaoAdocaoDto(10l, 20l, "motivo qualquer");
        given(petRepository.getReferenceById(solicitacaoDto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(solicitacaoDto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);


        //ACT
        adocaoService.solicitar(solicitacaoDto);
        //ASSERT
        then(adocaoRepository).should().save(adocaoCaptor.capture());
        Adocao adocaoSalva = adocaoCaptor.getValue();
        Assertions.assertEquals(pet, adocaoSalva.getPet());
        Assertions.assertEquals(tutor, adocaoSalva.getTutor());
        Assertions.assertEquals(solicitacaoDto.motivo(), adocaoSalva.getMotivo());

    }

    @Test
    void deveriaChamarValidadoresDeAdocaoAoSolicitar() {
        //ARRANGE
        this.solicitacaoDto = new SolicitacaoAdocaoDto(10l, 20l, "motivo qualquer");
        given(petRepository.getReferenceById(solicitacaoDto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(solicitacaoDto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        validacoes.add(validador1);
        validacoes.add(validador2);

        //ACT
        adocaoService.solicitar(solicitacaoDto);
        //ASSERT
        BDDMockito.then(validador1).should().validar(solicitacaoDto);
        BDDMockito.then(validador2).should().validar(solicitacaoDto);
    }



}