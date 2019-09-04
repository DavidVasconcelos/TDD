package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class Calculadora {

    public int soma(int a, int b) {
        return a + b;
    }

    public int subtrair(int a, int b) {
        return a- b;
    }

    public int dividir(int a, int b) throws NaoPodeDividirPorZeroException {

        if(b == 0) {
            throw new NaoPodeDividirPorZeroException();
        }

        return a / b;
    }

    public int dividir(String a, String b) {
        return Integer.valueOf(a) / Integer.valueOf(b);

    }
}
