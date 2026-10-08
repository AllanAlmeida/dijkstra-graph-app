$(document).ready(function () {

    $("#search-form").submit(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();
        
        fire_ajax_submit();

    });

});

function fire_ajax_submit() {

    var graphId = $("#graphId").val();
    var town1 = $("#town1").val();
    var town2 = $("#town2").val();
    
    var formedUrl = "/distance/"+graphId+"/from/"+town1+"/to/"+town2;

    $("#btn-search").prop("disabled", true);

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: formedUrl,
        dataType: 'json',
        cache: false,
        timeout: 600000,

        success: function (data) {

            var json = "<pre>" + "Distancia: " + data.distance + "<br>Caminho: " + obterCaminho(data.path) + "</pre>";
            $('#feedback').html(json);
            $("#btn-search").prop("disabled", false);

        },

        error: function (e) {

            var json = "<pre>" + e.responseText + "</pre>";
            $('#feedback').html(json);
            $("#btn-search").prop("disabled", false);

        }
    });
}

function obterCaminho(array) {

    var arrayString = "";
    var stringCaminho = "";

    if(typeof array !== 'undefined' && array.length > 0){
    
        arrayString = array.toString();
        stringCaminho = arrayString.replace(/,/g, " => ");
    }
    
    return stringCaminho;
}

