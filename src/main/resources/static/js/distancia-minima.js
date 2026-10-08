document.addEventListener('DOMContentLoaded', function () {

    var form = document.getElementById('search-form');
    var button = document.getElementById('btn-search');
    var feedback = document.getElementById('feedback');

    form.addEventListener('submit', function (event) {
        event.preventDefault();
        calcular();
    });

    function calcular() {

        var graphId = document.getElementById('graphId').value.trim();
        var town1 = document.getElementById('town1').value.trim();
        var town2 = document.getElementById('town2').value.trim();

        if (!graphId || !town1 || !town2) {
            aviso('warning', 'Preencha o id do grafo, a origem e o destino.');
            return;
        }

        var url = '/distance/' + encodeURIComponent(graphId)
                + '/from/' + encodeURIComponent(town1)
                + '/to/' + encodeURIComponent(town2);

        button.disabled = true;

        fetch(url, { headers: { 'Accept': 'application/json' } })
            .then(function (response) {

                if (response.status === 404) {
                    aviso('warning', 'Não há caminho de ' + town1 + ' até ' + town2
                            + ' no grafo ' + graphId + '.');
                    return null;
                }

                if (!response.ok) {
                    aviso('danger', 'A consulta falhou (HTTP ' + response.status + ').');
                    return null;
                }

                return response.json();
            })
            .then(function (data) {
                if (data) {
                    resultado(data);
                }
            })
            .catch(function () {
                aviso('danger', 'Não foi possível falar com o servidor.');
            })
            .then(function () {
                button.disabled = false;
            });
    }

    function resultado(data) {

        feedback.replaceChildren();

        var card = document.createElement('div');
        card.className = 'card';

        var body = document.createElement('div');
        body.className = 'card-body';

        body.appendChild(linha('Distância', String(data.distance)));
        body.appendChild(linha('Caminho', caminho(data.path)));

        card.appendChild(body);
        feedback.appendChild(card);
    }

    function linha(rotulo, valor) {

        var p = document.createElement('p');
        p.className = 'mb-1';

        var strong = document.createElement('strong');
        strong.textContent = rotulo + ': ';

        p.appendChild(strong);
        // textContent, e não innerHTML: o conteúdo vem da resposta da API
        p.appendChild(document.createTextNode(valor));

        return p;
    }

    function caminho(path) {
        return Array.isArray(path) ? path.join(' => ') : '';
    }

    function aviso(tipo, mensagem) {

        feedback.replaceChildren();

        var alerta = document.createElement('div');
        alerta.className = 'alert alert-' + tipo;
        alerta.setAttribute('role', 'alert');
        alerta.textContent = mensagem;

        feedback.appendChild(alerta);
    }
});
