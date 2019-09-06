package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Locacao;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class CalculadoraMockTest {

    @Test
    public void teste() {

        Calculadora calculadora = Mockito.mock(Calculadora.class);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.when(calculadora.soma(argumentCaptor.capture(), argumentCaptor.capture())).thenReturn(5);


        Assert.assertEquals(5, calculadora.soma(1, 8));
    }
}
