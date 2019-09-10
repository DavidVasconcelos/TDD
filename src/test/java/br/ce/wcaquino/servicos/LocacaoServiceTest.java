package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umaLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatcherProprios.*;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterData;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
//DataUtils quando estava sendo veirficado pelo construtor de Date
//@PrepareForTest({ LocacaoService.class, DataUtils.class })
@PrepareForTest(LocacaoService.class)
public class LocacaoServiceTest {

    @InjectMocks
    private LocacaoService service;

    @Mock
    private LocacaoDAO dao;

    @Mock
    private SPCService spcService;

    @Mock
    private EmailService emailService;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        //Para testar o metodo privado
        service = PowerMockito.spy(service);
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

        //Verificacao se é sabado
        //Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

        //PowerMockito.whenNew(Date.class).withAnyArguments().thenReturn(obterData(13, 9, 2019));
        //Quando se usa new Date() no Locacao Service
        //PowerMockito.whenNew(Date.class).withAnyArguments().thenReturn(obterData(14, 9, 2019));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 13);
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.YEAR, 2019);

        PowerMockito.mockStatic(Calendar.class);
        PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificao
        assertEquals(5.0, locacao.getValor(), 0.01);
        //assertTrue(isMesmaData(new Date(), locacao.getDataLocacao()));
        //assertTrue(isMesmaData(obterDataComDiferencaDias(1), locacao.getDataRetorno()));

        //Usando Assert That
        assertThat(locacao.getValor(), is(equalTo(5.0)));
        assertThat(locacao.getValor(), is(not(6.0)));
        //assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        //assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
        assertThat(isMesmaData(locacao.getDataLocacao(), obterData(13, 9, 2019)), is(true));
        assertThat(isMesmaData(locacao.getDataRetorno(), obterData(14, 9, 2019)), is(true));


    }

    @Test
    public void deveAlugarFilme() throws Exception {

        //Verificacao se é sabado
        //Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

        //PowerMockito.whenNew(Date.class).withAnyArguments().thenReturn(obterData(13, 9, 2019));
        //Quando se usa new Date() no Locacao Service
        //PowerMockito.whenNew(Date.class).withAnyArguments().thenReturn(obterData(14, 9, 2019));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 13);
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.YEAR, 2019);

        PowerMockito.mockStatic(Calendar.class);
        PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //Usando o error
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
//        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
//        error.checkThat(locacao.getDataLocacao(), ehHoje());
//        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
//        error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), obterData(13, 9, 2019)), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterData(14, 9, 2019)), is(true));


    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

        //acao
        service.alugarFilme(usuario, filmes);


    }

    @Test
    public void testeLocacao_filmeSemEstoque() {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

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
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

        expectedException.expect(FilmeSemEstoqueException.class);
        expectedException.expectMessage("Filme sem estoque");

        //acao
        service.alugarFilme(usuario, filmes);


    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {

        //cenario
        Usuario usuario = null;
        List<Filme> filmes = Arrays.asList(umFilme().agora());

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
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = null;

        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Filme vazio");


        service.alugarFilme(usuario, filmes);
        fail("Deveria lançar uma exceção");


    }

    @Test
    public void deveDevolverFilmeNaSegundaAoAlugarNoSabado() throws Exception {

        //Verificacao se é sabado
        //Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //Quando se usa new Date() no Locacao Service
        //PowerMockito.whenNew(Date.class).withAnyArguments().thenReturn(obterData(14, 9, 2019));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 14);
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.YEAR, 2019);

        PowerMockito.mockStatic(Calendar.class);
        PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(locacao.getDataRetorno(), caiNumaSegunda());

        //Quando se usa new Date() no LocacaoService e DataUtil
        //PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();

        PowerMockito.verifyStatic(Calendar.class, Mockito.times(2));
        Calendar.getInstance();

    }

    @Test
    public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

        //acao
        try {
            //verificacao
            service.alugarFilme(usuario, filmes);
            fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario negativado"));
        }

        verify(spcService).possuiNegativacao(usuario);

    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas() {

        //cenario
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
        Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();

        List<Locacao> locacoes = Arrays.asList(umaLocacao()
                        .comUsuario(usuario)
                        .atrasada()
                        .agora(),
                umaLocacao()
                        .comUsuario(usuario2)
                        .agora(),
                umaLocacao()
                        .comUsuario(usuario3)
                        .atrasada()
                        .agora(),
                umaLocacao()
                        .comUsuario(usuario3)
                        .atrasada()
                        .agora()
        );

        when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

        //acao
        service.notificarAtrasos();

        //verificacao
        verify(emailService, times(3)).notificarAtraso(Mockito.any(Usuario.class));
        verify(emailService).notificarAtraso(usuario);
        verify(emailService, never()).notificarAtraso(usuario2);
        verify(emailService, atLeastOnce()).notificarAtraso(usuario3);
        verifyNoMoreInteractions(emailService);


    }

    @Test
    public void deveTratarErroNoSPC() throws Exception {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrofica"));

        //verificao
        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Problemas com SPC, tente novamente");

        //acao
        service.alugarFilme(usuario, filmes);


    }

    @Test
    public void deveProrrogarLocacao() {

        //cenario
        Locacao locacao = umaLocacao().agora();
        int dias = 3;

        //acao
        service.prorrogarLocacao(locacao, dias);

        //verificao
        ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);
        verify(dao).salvar(argumentCaptor.capture());
        Locacao locacaoRetornada = argumentCaptor.getValue();

        error.checkThat(locacaoRetornada.getValor(), is(12.0));
        error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
        error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(dias));
    }

    @Test
    public void deveAlugarFilmeSemCalcularValor() throws Exception {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        Assert.assertThat(locacao.getValor(), is(1.0));
        PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);
    }


}
