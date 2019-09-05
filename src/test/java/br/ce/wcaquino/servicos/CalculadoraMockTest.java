package br.ce.wcaquino.servicos;

import org.junit.Test;
import org.mockito.Mockito;

public class CalculadoraMockTest {

    @Test
    public void teste() {

        Calculadora calculadora = Mockito.mock(Calculadora.class);
        Mockito.when(calculadora.soma(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);

        System.out.println(calculadora.soma(1, 8));
    }
}
