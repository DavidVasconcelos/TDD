package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatcherProprios.caiNumaSegunda;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterData;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LocacaoService.class, DataUtils.class })
public class LocacaoServiceTest_PowerMock {

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

        //Quando se usa new Date() no Locacao Service
        PowerMockito.whenNew(Date.class).withAnyArguments().thenReturn(obterData(13, 9, 2019));

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

        PowerMockito.whenNew(Date.class).withAnyArguments().thenReturn(obterData(13, 9, 2019));

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


    @Test
    public void deveDevolverFilmeNaSegundaAoAlugarNoSabado() throws Exception {

        //Verificacao se é sabado
        //Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //Quando se usa new Date() no Locacao Service
        PowerMockito.whenNew(Date.class).withAnyArguments().thenReturn(obterData(14, 9, 2019));

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(locacao.getDataRetorno(), caiNumaSegunda());

        //Quando se usa new Date() no LocacaoService e DataUtil
        PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();



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

    @Test
    public void deveCalcularValorLocacao() throws Exception {

        //cenario
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //acao
        Double valorLocacao = (Double) Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);

        //verificacao
        assertThat(valorLocacao, is(4.0));
    }


}
