import {model} from "./model";
import {createActionServiceUrl} from "./serviceRegistry";
import {showError, showInfo} from "./info-bar";
import {closeModals, closeOverlay} from "./modals";
import {refreshTool} from "./tools";

export let initActionsTriggers = function (toolId, svcUrl) {

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
            }
        });
    }
};