import {createActionServiceUrl, createModalSelector, createModalUrl} from "./serviceRegistry";
import {refreshDataTable} from "./dataTables";
import {showError, showInfo} from "./info-bar";
import {refreshDataElement} from "./dataElements";
import {displayModal, displayTableModal, initializeModalActions, openOverlay, setModalInputFocus} from "./modals";


export let enableActionButtons = function (svcUrl, toolSelector, toolKey, dataFunction) {

    let doEnableActionButtons = function () {

        let doExecuteToolActions = function (element, dataFunction, actionExecutedFunction) {
            const action = element.data('action-service-name');
            let actionText;
            switch (action) {
                case 'create-repo-context':
                    actionText = "create a vhost in the repository";
                    break;
                case 'delete-repo-context':
                    actionText = "delete the vhost from the repository"
                    break;
            }
            if (!actionText) {
                return;
            }
            let serviceUrl = createActionServiceUrl(svcUrl, toolKey, action);
            if (confirm("Are you sure that you want to " + actionText + "?")) {
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

        $(toolSelector).find("button.table-action")
            .off()
            .click(function (event) {
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
                    refreshDataElement(refreshDataSelector);
                };

                doExecuteToolActions(element, df, actionExecutedFunction);
            });

        $(toolSelector).find("button.table-modal")
            .off()
            .click(function (event) {
                event.preventDefault();
                event.stopPropagation();
                let element = $(this);

                let modalType = element.data('modal-type');

                let df = dataFunction ? dataFunction : function () {
                    let actionContext = element.data("action-context");
                    let identifier = element.data("action-identifier");
                    return {
                        actionContext: actionContext,
                        identifier: identifier
                    }
                };

                let modalSelector = createModalSelector(toolKey);
                let modalServiceUrl = createModalUrl(svcUrl, toolKey, modalType);
                displayTableModal(modalServiceUrl, svcUrl, modalSelector, df, element);
            });
    };

    doEnableActionButtons();
};
