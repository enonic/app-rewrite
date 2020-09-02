import {showError} from "./info-bar";

export let loadTool = function (toolRendererUrl, dataFunction, onToolLoaded) {

    jQuery.ajax({
        url: toolRendererUrl,
        cache: false,
        type: 'GET',
        data: dataFunction(),
        error: function (response, status, error) {
            console.log("Error: ", response.responseText);
            showError("Cannot load tool: " + response.responseText)
        },
        success: function (result) {
            onToolLoaded(result);
            enableHelp();
        }
    });


    let enableHelp = function () {

        $(".helptext").click(function () {
            $(this).toggleClass("collapsed");
        });


    }


};