import {model} from "./model";
import {closeModals, closeOverlay, initializeModalActions, initializeModalTrigger, toggleOverlay} from "./modals";
import {showError, showInfo} from "./info-bar";
import {createActionServiceUrl, createDataServiceUrl, createToolRendererUrl} from "./serviceRegistry";
import {initDataTriggers} from "./dataTriggers";


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
            initToolTriggers(toolKey, svcUrl);
        }
    });
};


let initToolTriggers = function (toolKey, svcUrl) {
    initializeModalTrigger(toolKey, svcUrl);
    initDataTriggers(toolKey, svcUrl);
    initActions(toolKey, svcUrl);
};

let initActions = function (toolId, svcUrl) {

    let toolSelector = "#" + toolId;
    $(toolSelector).find(model.tool.table.action).click(function (event) {
        event.preventDefault();
        let elem = $(this);
        let actionType = elem.data("action-type");
        let dataId = elem.data("id");
        console.log("Execute action: " + actionType + " on id: " + dataId);
        doExecuteToolActions(toolId, actionType, dataId, svcUrl);
    });
};

let doExecuteToolActions = function (toolKey, actionType, actionId, svcUrl) {
    let serviceUrl = createActionServiceUrl(svcUrl, toolKey, actionType);

    let data = {
        actionId: actionId
    };

    if (confirm("Are you sure that you want to " + actionType + " [" + actionId + "]?")) {
        jQuery.ajax({
            url: serviceUrl,
            cache: false,
            type: 'POST',
            data: data,
            error: function (response, status, error) {
                console.log("ERROR: ", response);
                showError(response.responseText);
            },
            success: function (response, textStatus, jqXHR) {
                console.log("SUCCESS: ", response, textStatus, jqXHR);
                showInfo(response.message);
                closeModals();
                closeOverlay();
                refreshTool(svcUrl, toolKey);
            }
        });
    }


};