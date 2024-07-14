package br.com.alura.adopet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TestandoMockito {

    @Test
    void whenCreateMock_thenCreated() {
        List<String> mockedList = mock(ArrayList.class);

        mockedList.add("one");
        //verifica que o metodo .add foi chamado
        verify(mockedList).add("one");
        //nada é adicionado na lista mockada, ja que apenas simula a chamada do metodo
        Assertions.assertThat(mockedList).hasSize(0);
    }

    @Test
    void whenCreateSpy_thenCreate() {
        List spyList = Mockito.spy(new ArrayList());

        spyList.add("one");

        //verifica que o metodo .add foi chamado
        Mockito.verify(spyList).add("one");

        //um spy realmente chama a implementacao do metodo, entao um elemento é adicionado de verdade na lista
        Assertions.assertThat(spyList).hasSize(1);
        Assertions.assertThat(spyList.get(0)).isEqualTo("one");
    }
    
}
