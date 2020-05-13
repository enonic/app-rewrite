import {model} from "./model";


export let initLoadableTools = function (svcUrl) {

    console.log("Initializing loadable tools");

    $(".loadable-tool").each(function () {

        let toolId = $(this).attr("id");
        console.log("Loading tool [" + toolId + "]");
        loadTool(svcUrl, toolId);

    });

};

export let initToolActions = function () {

    $(model.buttons.tool.action).each(function () {

        $(this).click(function () {
            let context = $(this).data("context");
            let action = $(this).data("action");
            console.log("invoking action: " + action + " on context: " + context);
        })


    });


};

let loadTool = function (svcUrl, toolId) {

    let url = svcUrl + "/" + toolId;

    let data = {};
    let elementId = "#" + toolId;


    jQuery.ajax({
        url: url,
        cache: false,
        type: 'GET',
        data: data,
        error: function (response, status, error) {
            console.log("Result: ", response.responseText);
            $(model.elements.result).html(response.responseText);
        },
        success: function (result) {
            console.log("Result: ", JSON.stringify(result));

            console.log("Element found: ", $(elementId));
            $(elementId).html(result);
        }
    });


};
