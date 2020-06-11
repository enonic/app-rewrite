import {model} from "./model";
import {showError, showInfo} from "./info-bar";
import {createActionServiceUrl, createModalSelector, createModalUrl} from "./serviceRegistry";
import {refreshDataTable} from "./dataTables";


export let initModals = function (svcUrl) {
    initializeOverlay();
};

let initializeOverlay = function () {
    $(model.modals.overlay).click(function () {
        closeModals();
        toggleOverlay();
    });
};

export let initModalTriggers = function (toolKey, svcUrl) {

    console.log("initializeTriggers for toolId", toolKey);

    let toolSelector = "#" + toolKey;

    $(toolSelector).find(model.modals.triggers).click(function () {
        let modalTrigger = $(this);
        let modalType = modalTrigger.data("modal-type");
        let context;

        let contextSelector = modalTrigger.data("modal-context-selector");
        if (contextSelector) {
            context = $(contextSelector).serialize();
        }
        displayModal(svcUrl, toolKey, modalType, context);
    });
};

export let displayModal = function (svcUrl, toolKey, modalType, context) {

    let data = context;
    let modalSelector = createModalSelector(toolKey, modalType);

    jQuery.ajax({
        url: createModalUrl(svcUrl, toolKey, modalType),
        cache: false,
        type: 'GET',
        data: data,
        error: function (response, status, error) {
            console.log("Result: ", response.responseText);
            showError(response.responseText);
        },
        success: function (result) {
            $(modalSelector).html(result);
            initializeModalActions(svcUrl);
            $(modalSelector).toggleClass("closed");
            toggleOverlay();
        }
    });
};

let initializeModalActions = function (svcUrl) {

    console.log("Initialize modal-actions");

    $(model.modals.modalAction).click(function (event) {
        event.preventDefault();
        let thisElem = $(this);
        let toolKey = thisElem.data("tool-key");
        let actionType = thisElem.data("action-type");
        let formId = thisElem.data("form");
        let refreshDataSelector = thisElem.data("refresh-data-selector");
        let data = $("#" + formId).serialize();

        let actionServiceUrl = createActionServiceUrl(svcUrl, toolKey, actionType);

        jQuery.ajax({
            url: actionServiceUrl,
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
                toggleOverlay();
                refreshDataTable(refreshDataSelector);
            }
        });

    });
};

export let closeModals = function () {
    $(model.modals.all).addClass("closed");
};

export let closeOverlay = function () {
    $(model.modals.overlay).addClass("closed");
};
export let toggleOverlay = function () {
    $(model.modals.overlay).toggleClass("closed");
};