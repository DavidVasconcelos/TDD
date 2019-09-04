package br.ce.wcaquino.utils;

import br.ce.wcaquino.entidades.Locacao;

public class GeradorBuilders {

    public static void main(String[] args) {
        new BuilderMaster().gerarCodigoClasse(Locacao.class);
    }

}
