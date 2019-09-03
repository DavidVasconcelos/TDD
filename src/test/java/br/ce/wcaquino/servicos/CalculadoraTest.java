package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculadoraTest {

    private Calculadora calc;

    @Before
    public void setup() {
        calc = new Calculadora();
    }


    @Test
    public void deveSomarDoisValores() {

        //cenario
        int a = 5;
        int b = 3;

        //acao
        int resulatado = calc.soma(a, b);

        //verificacao
        Assert.assertEquals(8, resulatado);
    }

    @Test
    public void deveSubtrairDoisValores() {

        //cenario
        int a = 8;
        int b = 5;

        //acao
        int resulatado = calc.subtrair(a, b);

        //verificacao
        Assert.assertEquals(3, resulatado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {

        //cenario
        int a = 6;
        int b = 3;

        //acao
        int resulatado = calc.dividir(a, b);

        //verificacao
        Assert.assertEquals(2, resulatado);
    }


    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {

        //cenario
        int a = 10;
        int b = 0;

        Calculadora calc = new Calculadora();


        //acao
        int resulatado = calc.dividir(a, b);
    }
}
