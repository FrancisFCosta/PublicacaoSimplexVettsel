package Servlets;

import ObjetosDeProcessamento.AlgoritmoVettsel;
import ObjetosDeProcessamento.Celula;
import ObjetosDeProcessamento.RequisicaoVettsel;
import ObjetosDeProcessamento.Tabela;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;

public class Vettsel extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String mensagemErroValidacao;
        JSONArray jsonArray = new JSONArray();

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            String jsonString = request.getParameter("objarray");
            Gson gson = new Gson();
            AlgoritmoVettsel executorSimplex = new AlgoritmoVettsel();
            RequisicaoVettsel requisicao = gson.fromJson(jsonString, RequisicaoVettsel.class);

            mensagemErroValidacao = ValidarRequisicao(requisicao);

            if (!"".equals(mensagemErroValidacao)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("sucesso", false);
                jsonObject.put("mensagemErro", mensagemErroValidacao);
                jsonArray.put(jsonObject);
                out.print(jsonArray);
                out.flush();
            }

            executorSimplex.ExecutarProcessamento(requisicao);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sucesso", executorSimplex.Vitoria);
            jsonObject.put("mensagemErro", executorSimplex.MensagemErro);

            String htmlModalItensRetornados = "";
            
            for (Celula[] linhaCelula : executorSimplex.TabelaExecucao.Matriz) {

                htmlModalItensRetornados +="<tr class='linhaRetorno'>" +
                        "<td class='DescricaoVariavel'>" + (linhaCelula[0].CodigoVariavel != "ML" ? linhaCelula[0].CodigoVariavel : "FO(x)")  + "</td>" +
                        "<td class='ValorVariavel' style='text-align:center'>" + linhaCelula[0].SubCelulaSuperior + "</td>" +
                        "</tr>";
            }
            
            jsonObject.put("ListaRetorno", htmlModalItensRetornados);
            jsonArray.put(jsonObject);
            out.print(jsonArray);
            out.flush();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String ValidarRequisicao(RequisicaoVettsel requisicao) {
        String mensagemErroValidacao = "";
        //TODO: Validacoes
        return mensagemErroValidacao;
    }
}
