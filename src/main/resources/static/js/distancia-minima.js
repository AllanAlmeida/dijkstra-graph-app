$(document).ready(function () {

    get_tokem();

    $("#search-form").submit(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();
        
        fire_ajax_submit();

    });

});

function get_tokem(){
    $.ajax({
        type: "POST",
        url: "/logingraph",
        data: {
          "username": "radix",
          "password": "radix"
        },
        success: function(data) {
          localStorage.token = data
        },
        error: function() {
          alert("Login Failed");
        }
      });
}

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

        beforeSend: function(xhr) { 
          if (localStorage.token) {
            xhr.setRequestHeader('Authorization', localStorage.token);
          }
        },

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

