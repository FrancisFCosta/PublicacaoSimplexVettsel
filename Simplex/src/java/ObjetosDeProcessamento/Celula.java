package ObjetosDeProcessamento;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Francis
 */
public class Celula {

    public String CodigoRestricao;
    public String CodigoVariavel;
    public double SubCelulaSuperior;
    public double SubCelulaInferior;

    public Celula(String codigoRestricao, String codigoVariavel, double valorSubCelulaSuperior) {
        this.CodigoRestricao = codigoRestricao;
        this.CodigoVariavel = codigoVariavel;
        this.SubCelulaSuperior = valorSubCelulaSuperior;
    }

    public Celula(String elemento) {
        boolean ehNegativa = false;
        String codigoVariavel = "";
        String valorVariavel = "";

        for (int i = 0; i < elemento.trim().length(); i++) {
            char caracterCorrente = elemento.charAt(i);
            if (caracterCorrente == '-') {
                ehNegativa = true;
            } else if (Character.isDigit(caracterCorrente)) {
                if (!"".equals(codigoVariavel)) {
                    codigoVariavel += caracterCorrente;
                } else {
                    valorVariavel += caracterCorrente;
                }
            } else {
                codigoVariavel += caracterCorrente;
                if ("".equals(valorVariavel)) {
                    valorVariavel = "1";
                }
            }
        }

        CodigoVariavel = !"".equals(codigoVariavel) ? codigoVariavel : "ML";
        SubCelulaSuperior = ehNegativa? (-1)* Double.parseDouble(valorVariavel) : Double.parseDouble(valorVariavel);
    }

    public void SomarSubCelulas() {
        
        double elementoSuperiorTemporario = SubCelulaSuperior + SubCelulaInferior;
        SubCelulaSuperior = elementoSuperiorTemporario;
        SubCelulaInferior = 0;
    }
}
