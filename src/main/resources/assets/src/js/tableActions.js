import {createActionServiceUrl} from "./serviceRegistry";
import {refreshDataTable} from "./dataTables";
import {showError, showInfo} from "./info-bar";


export let enableActionButtons = function (svcUrl, toolSelector, toolKey, dataFunction) {

    let doEnableActionButtons = function () {

        let doExecuteToolActions = function (element, dataFunction, actionExecutedFunction) {

            const action = element.data('action-service-name');
            let serviceUrl = createActionServiceUrl(svcUrl, toolKey, action);
            if (confirm("Are you sure that you want to " + action + "?")) {
                jQuery.ajax({
                    url: serviceUrl,
                    cache: false,
                    type: 'POST',
                    data: dataFunction(element),
                    error: function (response, status, error) {
                        console.log("ERROR: ", response);
                        showError(response.responseText);
                    },
                    success: function (response, textStatus, jqXHR) {
                        showInfo(response.message);
                        actionExecutedFunction(svcUrl, toolSelector, toolKey);
                    }
                });
            }
        };

        $(toolSelector).find("button.table-action").click(function (event) {
            event.preventDefault();
            event.stopPropagation();
            let element = $(this);

            let refreshDataSelector = element.data("refresh-data-selector");

            let df = dataFunction ? dataFunction : function (element) {
                let actionContext = element.data("action-context");
                let identifier = element.data("action-identifier");
                return {
                    actionContext: actionContext,
                    identifier: identifier
                }
            };

            let actionExecutedFunction = function (result) {
                refreshDataTable(refreshDataSelector, doEnableActionButtons);
            };

            doExecuteToolActions(element, df, actionExecutedFunction);
        });
    };

    doEnableActionButtons();
};
