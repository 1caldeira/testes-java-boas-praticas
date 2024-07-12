package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidacaoPetDisponivelTest {

    @Test
    void deveriaPermitirSolicitacaoDeAdocaoPet(){
        ValidacaoPetDisponivel validacao = new ValidacaoPetDisponivel();

        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(7l, 2l, "motivo qualquer");



      //  Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

}