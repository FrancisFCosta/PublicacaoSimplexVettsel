package ObjetosDeProcessamento;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Francis
 */
public class RequisicaoVettsel {

    public String ExpressaoFuncaoObjetivo;
    public boolean EhMaximizacao;
    public List<Restricao> ListaRestricoes;

    public RequisicaoVettsel() {
        ListaRestricoes = new ArrayList();
    }
}
