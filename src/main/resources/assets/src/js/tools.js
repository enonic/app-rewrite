import {initializeModalActions, initModalTriggers} from "./modals";
import {showError} from "./info-bar";
import {createToolRendererUrl} from "./serviceRegistry";
import {initDataTriggers} from "./dataTriggers";
import {initActionsTriggers} from "./actions";
import {initTableActions} from "./dataTableActions";


export let initLoadableTools = function (svcUrl) {
    $(".loadable-tool").each(function () {
        let toolKey = $(this).attr("id");
        console.log("Loading tool [" + toolKey + "]");
        doLoadTool(svcUrl, toolKey);
    });
};

export let refreshTool = function (svcUrl, toolKey) {
    doLoadTool(svcUrl, toolKey);
};

let doLoadTool = function (svcUrl, toolKey) {

    let data = {};
    let toolSelector = "#" + toolKey;

    jQuery.ajax({
        url: createToolRendererUrl(svcUrl, toolKey),
        cache: false,
        type: 'GET',
        data: data,
        error: function (response, status, error) {
            console.log("Error: ", response.responseText);
            showError("Cannot load tool: " + response.responseText)
        },
        success: function (result) {
            console.log("Tool [" + toolKey + "] loaded");
            $(toolSelector).html(result);
            initAllToolTriggers(toolKey, svcUrl);
        }
    });
};


let initAllToolTriggers = function (toolKey, svcUrl) {
    initModalTriggers(toolKey, svcUrl);
    initDataTriggers(toolKey, svcUrl);
    // initActionsTriggers(toolKey, svcUrl);
};
