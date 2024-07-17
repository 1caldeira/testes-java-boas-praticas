package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComLimiteDeAdocoesTest {

    @InjectMocks
    private ValidacaoTutorComLimiteDeAdocoes validacao;
    @Mock
    private AdocaoRepository adocaoRepository;
    @Mock
    private TutorRepository tutorRepository;
    @Mock
    private Adocao adocao;
    @Mock
    private Adocao adocao2;
    @Mock
    private Adocao adocao3;
    @Mock
    private Adocao adocao4;
    @Mock
    private Adocao adocao5;
    @Mock
    private Tutor tutor;
    @Mock
    private SolicitacaoAdocaoDto dto;

    @Test
    void naoDeveriaLancarExceptionParaTutorAbaixoDoLimiteDeAdocoes() {
        List<Adocao> adocaoList = new ArrayList<>();
        adocaoList.add(adocao);
        BDDMockito.given(adocaoRepository.findAll()).willReturn(adocaoList);
        BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        BDDMockito.given(adocao.getTutor()).willReturn(tutor);
        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

    @Test
    void deveriaLancarExceptionParaTutorNoLimiteDeAdocoes() {
        List<Adocao> adocaoList = new ArrayList<>();
        adocaoList.add(adocao);
        adocaoList.add(adocao2);
        adocaoList.add(adocao3);
        adocaoList.add(adocao4);
        adocaoList.add(adocao5);
        BDDMockito.given(adocaoRepository.findAll()).willReturn(adocaoList);
        BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        BDDMockito.given(adocao.getTutor()).willReturn(tutor);
        BDDMockito.given(adocao.getStatus()).willReturn(StatusAdocao.APROVADO);
        BDDMockito.given(adocao2.getTutor()).willReturn(tutor);
        BDDMockito.given(adocao2.getStatus()).willReturn(StatusAdocao.APROVADO);
        BDDMockito.given(adocao3.getTutor()).willReturn(tutor);
        BDDMockito.given(adocao3.getStatus()).willReturn(StatusAdocao.APROVADO);
        BDDMockito.given(adocao4.getTutor()).willReturn(tutor);
        BDDMockito.given(adocao4.getStatus()).willReturn(StatusAdocao.APROVADO);
        BDDMockito.given(adocao5.getTutor()).willReturn(tutor);
        BDDMockito.given(adocao5.getStatus()).willReturn(StatusAdocao.APROVADO);
        Assertions.assertThrows(ValidacaoException.class,() -> validacao.validar(dto));
    }
}