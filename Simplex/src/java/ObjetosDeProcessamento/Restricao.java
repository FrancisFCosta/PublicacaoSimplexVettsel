package ObjetosDeProcessamento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Restricao {

    public String Codigo;
    public String Expressao;
    //public String OperadorComparativo;
    //public List<String> ListaElementosLivresOrdenados;

    public Restricao() {
    }

    public List<String> ObterListaElementosLivresRestricao() {

        List<String> listRetorno = new ArrayList();
        String operadorComparativo = ObterOperadorComparativo(Expressao);
        String[] arrayOperacao = Expressao.split(operadorComparativo);
        List<String> listOperacao = new ArrayList();
        for (String elemento : arrayOperacao) {
            if (operadorComparativo.contains(">")) {
                listOperacao.add("-" + elemento.replace("+", "-"));
            } else {
                listOperacao.add(elemento);
            }
        }

        if (listOperacao.size() == 2) {
            listRetorno.add(listOperacao.get(1));

            String elementosLivres = listOperacao.get(0).replace("0-(", "").replace(")", "");
            String[] arraySplitAdicao = elementosLivres.split(Pattern.quote("+"));
            List<String> listaElementosLivres = new ArrayList();

            for (String splitAdicao : arraySplitAdicao) {
                if (splitAdicao.contains("-")) {
                    String[] arraySplitSubtracao = splitAdicao.split("-");
                    List<String> listaElementosSubtracao = new ArrayList();

                    for (String elemento : arraySplitSubtracao) {
                        if (!elemento.equals("")) {
                            listaElementosSubtracao.add("-" + elemento);
                        }
                    }
                    listaElementosLivres.addAll(listaElementosSubtracao);
                } else {
                    listaElementosLivres.add(splitAdicao);
                }
            }
            listRetorno.addAll(listaElementosLivres);
        }
        return listRetorno;
    }

    private String ObterOperadorComparativo(String expressao) {
        String operadorRetorno = "";

        if (expressao.contains(">")) {
            operadorRetorno += ">";
        }
        if (expressao.contains("<")) {
            operadorRetorno += "<";
        }
        if (expressao.contains("=")) {
            operadorRetorno += "=";
        }
        return operadorRetorno;
    }
}
