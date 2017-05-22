var ContadorElementosGrid;

$('[name="adicionarRestricao"').on('click',
        function () {
            var restricao = $('#campoAdicionaRetricao').val();
            if (!restricao) {
                $('#campoAdicionaRetricao').addClass('error');
                $.notify("Informe a restrição.", "error");
            } else {
                $('#campoAdicionaRetricao').removeClass('error').val(null);
                ContadorElementosGrid++;
                $('#gridRestricoes').append("<tr class='linhaRestricao'>" +
                        "<td class='numeroRestricao'>" + ContadorElementosGrid + "</td>" +
                        "<td class='expressaoRestricao' style='text-align:center'>" + restricao + "</td>" +
                        "<td class='actions'>" +
                        "<button type='submit' class='btn btn-danger' name='excluirLinha'> x </button>" +
                        "</td>" +
                        "</tr>");
                adicionarEventosGrid();
            }
        });
$('#btnExecutar').on('click',
        function () {
            if (!$('#funcaoObjetivo').val()) {
                $.notify("Favor Informar função objetivo.", "error");
            } else if ($('.linhaRestricao').length && $('.linhaRestricao').length <= 0) {
                $.notify("Favor Informar ao menos uma restrição.", "error");
            } else {
                ExecutarAlgoritmoSimplex();
            }

        });

function adicionarEventosGrid() {
    $('[name="excluirLinha"').unbind('click').on('click',
            function () {
                $(this).parent().parent().remove();
            });
}

function ObterDadosExecucaoAlgoritmo() {

    var requisicaoVettsel = {};
    requisicaoVettsel.ExpressaoFuncaoObjetivo = $('#funcaoObjetivo').val();
    requisicaoVettsel.EhMaximizacao = $('#tipoFuncaoObjetivo').val() === "MAX";
    requisicaoVettsel.ListaRestricoes = new Array();
    $('.linhaRestricao').each(function () {
        var restricao = {};
        restricao.Codigo = $(this).find('.numeroRestricao').text();
        restricao.Expressao = $(this).find('.expressaoRestricao').text();
        requisicaoVettsel.ListaRestricoes.push(restricao);
    });
    return requisicaoVettsel;
}

function ExecutarAlgoritmoSimplex() {

    var requisicaoVettsel = ObterDadosExecucaoAlgoritmo();
    var param = "objarray=" + encodeURIComponent(JSON.stringify(requisicaoVettsel));
    $.ajax({
        url: '/Simplex/Vettsel',
        type: 'POST',
        dataType: 'json',
        data: param,
        success: function (result) {
            if (!result[0].sucesso) {
                $.notify(result[0].mensagemErro, "error");
            } else {
                $('#gridReposta').find('.linhaRetorno').remove();
                $('#gridReposta').append(result[0].ListaRetorno);
                $('#modalRetornoAlgoritmo').modal('show');
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $.notify("Ocorreu um erro no processamento.", "error");
        }
    });

}
