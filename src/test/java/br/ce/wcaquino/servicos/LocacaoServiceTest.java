package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class LocacaoServiceTest {

    private LocacaoService service;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        this.service = new LocacaoService();
    }

    @After
    public void tearDown() {
        System.out.println("After");
    }

    @BeforeClass
    public static void setupClass() {
        System.out.println("Before Class");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("After Class");
    }

    @Test
    public void testeLocacaoAssert() throws Exception {

        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificao
        assertEquals(5.0, locacao.getValor(), 0.01);
        assertTrue(isMesmaData(new Date(), locacao.getDataLocacao()));
        assertTrue(isMesmaData(obterDataComDiferencaDias(1), locacao.getDataRetorno()));

        //Usando Assert That
        assertThat(locacao.getValor(), is(equalTo(5.0)));
        assertThat(locacao.getValor(), is(not(6.0)));
        assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));


    }

    @Test
    public void deveAlugarFilme() throws Exception {

        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //Usando o error
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));


    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

        //acao
        service.alugarFilme(usuario, filmes);


    }

    @Test
    public void testeLocacao_filmeSemEstoque() {

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

        //acao
        try {
            service.alugarFilme(usuario, filmes);
            fail("Deveria lançar uma exceção");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Filme sem estoque"));
        }


    }

    @Test
    public void testeLocacao_filmeSemEstoque_Rule() throws Exception {

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

        expectedException.expect(FilmeSemEstoqueException.class);
        expectedException.expectMessage("Filme sem estoque");

        //acao
        service.alugarFilme(usuario, filmes);


    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {

        //cenario
        Usuario usuario = null;
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

        //acao
        try {
            service.alugarFilme(usuario, filmes);
            fail("Deveria lançar uma exceção");
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }

    }

    @Test
    public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = null;

        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Filme vazio");


        service.alugarFilme(usuario, filmes);
        fail("Deveria lançar uma exceção");


    }

    @Test
    public void deveDevolverFilmeNoDomingo() throws FilmeSemEstoqueException, LocadoraException {

        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0));

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
        assertTrue(ehSegunda);

    }

}
