package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class CalculadoraMockTest {

    @Mock
    private Calculadora calculadoraMock;

    @Spy
    private Calculadora calculadoraSpy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void devoMostrarDiferencaEntreMockSpy() {

        Mockito.when(calculadoraMock.soma(1,2)).thenCallRealMethod();
        Mockito.doReturn(5).when(calculadoraSpy).soma(1, 2);
        Mockito.doNothing().when(calculadoraSpy).imprime();

        System.out.println("Mock -> " + calculadoraMock.soma(1,2));
        System.out.println("Spy -> " + calculadoraSpy.soma(1,2));

        System.out.println("Mock");
        calculadoraMock.imprime();
        System.out.println("Spy");
        calculadoraSpy.imprime();
    }
    
    @Test
    public void teste() {

        Calculadora calculadora = Mockito.mock(Calculadora.class);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.when(calculadora.soma(argumentCaptor.capture(), argumentCaptor.capture())).thenReturn(5);


        Assert.assertEquals(5, calculadora.soma(1, 8));
    }
}
