/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjetosDeProcessamento;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Francis
 */
public class AlgoritmoVettsel {

    public boolean Vitoria;
    public String MensagemErro;
    public Tabela TabelaExecucao;
    public boolean ExecucaoFinalizada;

    public void ExecutarProcessamento(RequisicaoVettsel requisicao) {

        String funcaoTratada = ObterFuncaoObjetivoTratada(requisicao.ExpressaoFuncaoObjetivo, requisicao.EhMaximizacao);
        TabelaExecucao = new Tabela(funcaoTratada, requisicao.ListaRestricoes, requisicao.EhMaximizacao);

        PrimeiraFase();
    }

    private String ObterFuncaoObjetivoTratada(String funcaoObjetivo, boolean ehMaximizacao) {
        if (ehMaximizacao) {
            return "0-(" + funcaoObjetivo.replaceAll("-", "+") + ")";
        }
        return funcaoObjetivo;
    }

    private void PrimeiraFase() {

        //Verifica se existe Membros Livres Negativos
        if (VerificaExistenciaMembroLivreNegativo()) {
            Integer indiceColunaPermissiva = ObterPosicaoColunaPermissivaPrimeiraFase();

            if (indiceColunaPermissiva == null) {
                Vitoria = false;
                MensagemErro = "Não existe solução permissível.";
                ExecucaoFinalizada = true;
                return;
            }

            Integer indiceLinhaPermissiva = ObterPosicaoLinhaPermissiva(indiceColunaPermissiva);

            if (indiceLinhaPermissiva == null) {
                Vitoria = false;
                MensagemErro = "Não foi possível encontrar o índice da linha permissiva. Obs.: Não foi encontrado um quocioente válido.";
                ExecucaoFinalizada = true;
                return;
            }

            ExecutarAlgoritmoTroca(indiceLinhaPermissiva, indiceColunaPermissiva);
        } else {
            SegundaFase();
        }
    }

    private boolean VerificaExistenciaMembroLivreNegativo() {

        for (int i = 1; i < TabelaExecucao.Matriz[0].length; i++) {
            if (TabelaExecucao.Matriz[i][0].SubCelulaSuperior < 0) {
                return true;
            }
        }
        return false;
    }

    private Integer ObterPosicaoColunaPermissivaPrimeiraFase() {

        for (int indiceLinha = 1; indiceLinha < TabelaExecucao.Matriz.length; indiceLinha++) {
            if (TabelaExecucao.Matriz[indiceLinha][0].SubCelulaSuperior <= 0) {
                for (int indiceColuna = 1; indiceColuna < TabelaExecucao.Matriz[0].length; indiceColuna++) {
                    if (TabelaExecucao.Matriz[indiceLinha][indiceColuna].SubCelulaSuperior < 0) {
                        return indiceColuna;
                    }
                }
            }
        }
        return null;
    }

    private void SegundaFase() {

        if (VerificarMembroLivrePositivoNaFO()) {

            Integer indiceColunaPermissiva = ObterPosicaoColunaPermissivaSegundaFase();

            if (VerificarSolucaoIlimitada(indiceColunaPermissiva)) {
                Vitoria = false;
                MensagemErro = "Solução é ilimitada.";
                ExecucaoFinalizada = true;
                return;
            }

            Integer indiceLinhaPermissiva = ObterPosicaoLinhaPermissiva(indiceColunaPermissiva);

            if (indiceLinhaPermissiva == null) {
                Vitoria = false;
                MensagemErro = "Não foi possível encontrar o índice da linha permissiva. Obs.: Não foi encontrado um quocioente válido.";
                ExecucaoFinalizada = true;
                return;
            }

            ExecutarAlgoritmoTroca(indiceLinhaPermissiva, indiceColunaPermissiva);
        }

        Vitoria = true;
        MensagemErro = "Solução ótima encontrada.";
        ExecucaoFinalizada = true;
    }

    private Integer ObterPosicaoColunaPermissivaSegundaFase() {

        for (int i = 0; i < TabelaExecucao.Matriz[0].length; i++) {
            if (TabelaExecucao.Matriz[0][i].SubCelulaSuperior > 0) {
                return i;
            }
        }
        return null;
    }

    private boolean VerificarMembroLivrePositivoNaFO() {
        for (Celula item : TabelaExecucao.Matriz[0]) {
            if (item.SubCelulaSuperior > 0) {
                return true;
            }
        }
        return false;
    }

    private boolean VerificarSolucaoIlimitada(int indiceColunaPermissiva) {
        boolean existePositivo = false;
        for (Celula[] Matriz : TabelaExecucao.Matriz) {
            if (Matriz[indiceColunaPermissiva].SubCelulaSuperior > 0) {
                existePositivo = true;
            }
        }
        return !existePositivo;
    }

    private Integer ObterPosicaoLinhaPermissiva(int colunaPermissiva) {
        Integer indiceLinhaPermissiva = null;
        double menorQuociente = Double.MAX_VALUE;

        for (int indiceLinha = 1; indiceLinha < TabelaExecucao.Matriz.length; indiceLinha++) {
            double numerador, denominador, quocienteLinha;
            numerador = TabelaExecucao.Matriz[indiceLinha][0].SubCelulaSuperior;
            denominador = TabelaExecucao.Matriz[indiceLinha][colunaPermissiva].SubCelulaSuperior;

            if ((numerador >= 0 && denominador >= 0 || numerador < 0 && denominador < 0) && denominador != 0.0) {

                quocienteLinha = numerador / denominador;

                if (menorQuociente > quocienteLinha) {
                    indiceLinhaPermissiva = indiceLinha;
                    menorQuociente = quocienteLinha;
                }
            }
        }
        return indiceLinhaPermissiva;
    }

    private void ExecutarAlgoritmoTroca(int indiceLinhaPermissiva, int indiceColunaPermissiva) {
        double elementoPermitidoInverso;
        //EXECUTANDO PASSOS DO ALGORITMO DA TROCA:
        //1 - Calcula o valor do elemento permitido inverso e Atualiza valor do elementoPermitido Inverso na tabela
        elementoPermitidoInverso = 1 / TabelaExecucao.Matriz[indiceLinhaPermissiva][indiceColunaPermissiva].SubCelulaSuperior;
        TabelaExecucao.Matriz[indiceLinhaPermissiva][indiceColunaPermissiva].SubCelulaInferior = elementoPermitidoInverso;

        //2 - Multiplica a linha permissiva pelo valor do elementoPermitido inverso
        for (int indiceColuna = 0; indiceColuna < TabelaExecucao.Matriz[indiceLinhaPermissiva].length; indiceColuna++) {
            if (indiceColuna != indiceColunaPermissiva) {
                TabelaExecucao.Matriz[indiceLinhaPermissiva][indiceColuna].SubCelulaInferior
                        = elementoPermitidoInverso * TabelaExecucao.Matriz[indiceLinhaPermissiva][indiceColuna].SubCelulaSuperior;
            }
        }

        //3 - Multiplica a coluna permissiva pelo valor do - elementoPermitivo invers, ou seja multiplicar por (EPI * -1)
        for (int indiceLinha = 0; indiceLinha < TabelaExecucao.Matriz.length; indiceLinha++) {
            if (indiceLinha != indiceLinhaPermissiva) {
                TabelaExecucao.Matriz[indiceLinha][indiceColunaPermissiva].SubCelulaInferior
                        = elementoPermitidoInverso * (-1) * TabelaExecucao.Matriz[indiceLinha][indiceColunaPermissiva].SubCelulaSuperior;
            }
        }

        //5 - Nas (SCI) vazias, multiplica-se a (SCS) marcada em sua respectiva coluna com a (SCI) marcada de sua respectiva linha
        for (int indiceColuna = 0; indiceColuna < TabelaExecucao.Matriz[0].length; indiceColuna++) {
            for (int indiceLinha = 0; indiceLinha < TabelaExecucao.Matriz.length; indiceLinha++) {
                if (indiceLinha != indiceLinhaPermissiva && indiceColuna != indiceColunaPermissiva) {
                    TabelaExecucao.Matriz[indiceLinha][indiceColuna].SubCelulaInferior
                            = TabelaExecucao.Matriz[indiceLinha][indiceColunaPermissiva].SubCelulaInferior
                            * TabelaExecucao.Matriz[indiceLinhaPermissiva][indiceColuna].SubCelulaSuperior;
                }
            }
        }

        // Criando Copia da Matriz 
        Celula[][] NovaMatriz = new Celula[TabelaExecucao.Matriz.length][TabelaExecucao.Matriz[0].length];

        // 7 , 8 e 9 - A tabela está sendo copiada trocando a posição da linha permissiva pela coluna permissiva
        String variavelColunaPermissiva = TabelaExecucao.Matriz[0][indiceColunaPermissiva].CodigoVariavel;
        String variavelLinhaPermissiva = TabelaExecucao.Matriz[indiceLinhaPermissiva][0].CodigoRestricao;

        for (int indiceColuna = 0; indiceColuna < NovaMatriz[0].length; indiceColuna++) {
            for (int indiceLinha = 0; indiceLinha < NovaMatriz.length; indiceLinha++) {
                double valorNovaSubCelulaSuperior;
                String restricao, variavel;
                //Caso for uma célula correspondente a linha ou coluna permissiva troca-se a posicao da informação
                if (indiceLinha == indiceLinhaPermissiva && indiceColuna == indiceColunaPermissiva) {

                    restricao = TabelaExecucao.Matriz[indiceLinha][indiceColuna].CodigoVariavel;
                    variavel = TabelaExecucao.Matriz[indiceLinha][indiceColuna].CodigoRestricao;
                    valorNovaSubCelulaSuperior = TabelaExecucao.Matriz[indiceLinha][indiceColuna].SubCelulaInferior;
                    NovaMatriz[indiceLinha][indiceColuna] = new Celula(restricao, variavel, valorNovaSubCelulaSuperior);
                } else if (indiceLinha == indiceLinhaPermissiva) {

                    restricao = TabelaExecucao.Matriz[indiceLinha][indiceColuna].CodigoVariavel;
                    variavel = variavelColunaPermissiva;
                    valorNovaSubCelulaSuperior = TabelaExecucao.Matriz[indiceLinha][indiceColuna].SubCelulaInferior;
                    NovaMatriz[indiceLinha][indiceColuna] = new Celula(restricao, variavel, valorNovaSubCelulaSuperior);
                } else if (indiceColuna == indiceColunaPermissiva) {

                    restricao = TabelaExecucao.Matriz[indiceLinha][indiceColuna].CodigoRestricao;
                    variavel = variavelLinhaPermissiva;
                    valorNovaSubCelulaSuperior = TabelaExecucao.Matriz[indiceLinha][indiceColuna].SubCelulaInferior;
                    NovaMatriz[indiceLinha][indiceColuna] = new Celula(restricao, variavel, valorNovaSubCelulaSuperior);
                } // caso não for correspondente a linha e coluna, mantém a célula e soma o valor das SubCelulas superior e inferior
                else {
                    TabelaExecucao.Matriz[indiceLinha][indiceColuna].SomarSubCelulas();
                    restricao = TabelaExecucao.Matriz[indiceLinha][indiceColuna].CodigoRestricao;
                    variavel = TabelaExecucao.Matriz[indiceLinha][indiceColuna].CodigoVariavel;
                    valorNovaSubCelulaSuperior = TabelaExecucao.Matriz[indiceLinha][indiceColuna].SubCelulaSuperior;
                    NovaMatriz[indiceLinha][indiceColuna] = new Celula(restricao, variavel,valorNovaSubCelulaSuperior);
                }
            }
        }

        TabelaExecucao.Matriz = NovaMatriz;

        //10 - Deverá ser avaliado a necessidade de realizar mais uma vez o algoritmo de troca,
        //tais validações são feitas no início da primeira etapa. Logo, iniciaremos uma recursividade. 
        PrimeiraFase();
    }

}
