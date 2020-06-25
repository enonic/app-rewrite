import {createActionServiceUrl} from "./serviceRegistry";
import {refreshDataTable} from "./dataTables";
import {showError, showInfo} from "./info-bar";


export let enableActionButtons = function (svcUrl, toolSelector, toolKey, dataTableSelector, dataFunction) {

    let doEnableActionButtons = function () {

        let doExecuteToolActions = function (element, dataFunction, actionExecutedFunction) {
            const action = element.data('action-service-name');
            let serviceUrl = createActionServiceUrl(svcUrl, toolKey, action);
            console.log("Created serviceUrk: " + serviceUrl);
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
                        console.log("SUCCESS: ", response, textStatus, jqXHR);
                        showInfo(response.message);
                        console.log("Execute function actionExecutedFunction", actionExecutedFunction);
                        actionExecutedFunction(svcUrl, toolSelector, toolKey, dataTableSelector);
                    }
                });
            }
        };

        $(toolSelector).find("button.table-action").click(function (event) {
            event.preventDefault();
            event.stopPropagation();
            let element = $(this);

            let df = dataFunction ? dataFunction : function (element) {
                let actionContext = element.data("action-context");
                let identifier = element.data("action-identifier");
                return {
                    actionContext: actionContext,
                    identifier: identifier
                }
            };

            let actionExecutedFunction = function (result) {
                console.log("In actionExecutedFunction");
                refreshDataTable(dataTableSelector, doEnableActionButtons);
            };

            doExecuteToolActions(element, df, actionExecutedFunction);
        });
    };

    doEnableActionButtons();
};
