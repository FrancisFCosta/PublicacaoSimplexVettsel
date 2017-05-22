package ObjetosDeProcessamento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Tabela {

    List<Restricao> ListaRestricoes;
    Boolean EhMaximizacao;

    public Celula[][] Matriz;

    public Tabela(String funcaoObjetivoTratada, List<Restricao> listaRestricoes, boolean ehMaximizacao) {
        EhMaximizacao = ehMaximizacao;
        ListaRestricoes = listaRestricoes;

        InstanciarMatriz(funcaoObjetivoTratada);
        InserirLinhaFuncaoObjetivo(funcaoObjetivoTratada);
        InserirLinhasRestricoes(listaRestricoes);
    }
       
    private void InstanciarMatriz(String funcaoObjetivoTratada) {
        int numeroVariaveisNaoBasicas = ObterNumeroDeVariaveisNaoBasicas(funcaoObjetivoTratada);
        int numeroRestricoes = ListaRestricoes.size();
        Matriz = new Celula[numeroRestricoes + 1][numeroVariaveisNaoBasicas + 1];
    }

    private int ObterNumeroDeVariaveisNaoBasicas(String funcaoObjetivo) {
        return ObterListaElementosLivresFuncaoObjetivo(funcaoObjetivo).size();
    }

    private void InserirLinhaFuncaoObjetivo(String funcaoObjetivo) {
        List<String> listaElementosLivres;
        Matriz[0][0] = new Celula("FO","ML", 0);

        listaElementosLivres = ObterListaElementosLivresFuncaoObjetivo(funcaoObjetivo);

        int controleColunas = 1;
        for (String elemento : listaElementosLivres) {
            Celula celulaCorrente = new Celula(elemento);
            celulaCorrente.CodigoRestricao = "FO";
            Matriz[0][controleColunas] = celulaCorrente;
            controleColunas++;
        }
    }

    private List<String> ObterListaElementosLivresFuncaoObjetivo(String funcaoObjetivo) {
        List<String> listRetorno = new ArrayList();

        String elementosLivres = funcaoObjetivo.replace("0-(", "").replace(")", "");
        String[] arraySplitAdicao = elementosLivres.split(Pattern.quote("+"));

        for (String arraySplitAdicao1 : arraySplitAdicao) {
            listRetorno.addAll(Arrays.asList(arraySplitAdicao1.split("-")));
        }
        return listRetorno;
    }

    private void InserirLinhasRestricoes(List<Restricao> listaRestricao) {
        
        int controleLinhas = 1;
        for (Restricao restricao : listaRestricao) {
            int controleColunas = 0;
            for (String elemento : restricao.ObterListaElementosLivresRestricao()) {
                Celula celulaCorrente = new Celula(elemento);
                celulaCorrente.CodigoRestricao = "x"+(controleLinhas+ (Matriz[0].length-1));
                Matriz[controleLinhas][controleColunas] = celulaCorrente;
                controleColunas++;
            }
            controleLinhas++;
        }
    }
    
}
