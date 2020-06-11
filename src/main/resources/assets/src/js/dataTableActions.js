import {model} from "./model";
import {createActionServiceUrl, createDataServiceUrl} from "./serviceRegistry";
import {populateDataTable, refreshDataTable} from "./dataTables";
import {showError, showInfo} from "./info-bar";

export let initTableActions = function (toolKey, svcUrl) {
    let toolSelector = "#" + toolKey;

    $(toolSelector).find(model.tool.dataTriggers.tableAction).click(function () {
        let elem = $(this);
        let targetSelector = elem.data("target-selector");
        let actionServiceName = elem.data("action-service-name");
        let actionContext = elem.data("action-context");
        let actionServiceUrl = createActionServiceUrl(svcUrl, toolKey, actionServiceName);

        let data = {
            actionContext: actionContext
        };

        jQuery.ajax({
            url: actionServiceUrl,
            cache: false,
            type: 'POST',
            data: data,
            error: function (response) {
                showError(response.responseText);
            },
            success: function (response) {
                showInfo(response.message);
                refreshDataTable(targetSelector);
            }
        });
    });


};
