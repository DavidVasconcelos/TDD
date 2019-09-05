package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

public class LocacaoService {

    private LocacaoDAO dao;
    private SPCService spcService;
    private EmailService emailService;

    public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {

        Integer contador = 0;
        Double valorTotal = 0d;

        if (usuario == null) {
            throw new LocadoraException("Usuario vazio");
        }

        if (filmes == null || filmes.isEmpty()) {
            throw new LocadoraException("Filme vazio");
        }

        if (filmes.stream().anyMatch(filme -> filme.getEstoque() == 0)) {
            throw new FilmeSemEstoqueException("Filme sem estoque");
        }

        for (Filme filme : filmes) {

            contador++;

            Double valorFilme = filme.getPrecoLocacao();

            switch (contador) {


                case 3:
                    valorFilme = valorFilme * 0.75;
                    break;
                case 4:
                    valorFilme = valorFilme * 0.5;
                    break;
                case 5:
                    valorFilme = valorFilme * 0.25;
                    break;
                case 6:
                    valorFilme = 0d;
                    break;

            }

            valorTotal += valorFilme;

        }

        if (spcService.possuiNegativacao(usuario)) {
            throw new LocadoraException("Usuario negativado");
        }

        Locacao locacao = new Locacao();
        locacao.setFilmes(filmes);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(new Date());
        locacao.setValor(valorTotal);


        //Entrega no dia seguinte
        Date dataEntrega = new Date();
        dataEntrega = adicionarDias(dataEntrega, 1);

        if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
            dataEntrega = adicionarDias(dataEntrega, 1);
        }

        locacao.setDataRetorno(dataEntrega);

        //Salvando a locacao...
        dao.salvar(locacao);

        return locacao;
    }

    public void notificarAtrasos() {
        List<Locacao> locacoes = dao.obterLocacoesPendentes();
        locacoes.forEach(locacao -> {

            if (locacao.getDataRetorno().before(new Date()))
                emailService.notificarAtraso(locacao.getUsuario());
        });
    }

    public void setLocacaoDAO(LocacaoDAO dao) {
        this.dao = dao;
    }

    public void setSpcService(SPCService spcService) {
        this.spcService = spcService;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}