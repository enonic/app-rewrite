import {model} from "./model";
import {showError, showInfo} from "./info-bar";
import {createActionServiceUrl, createModalSelector, createModalUrl} from "./serviceRegistry";
import {refreshTool} from "./tools";


export let initModals = function (svcUrl) {
    initializeOverlay();
};

let initializeOverlay = function () {
    $(model.modals.overlay).click(function () {
        closeModals();
        toggleOverlay();
    });
};

export let initializeModalTrigger = function (toolKey, svcUrl) {

    console.log("initializeTriggers for toolId", toolKey);

    let toolSelector = "#" + toolKey;

    $(toolSelector).find(model.modals.triggers).click(function () {
        let modalTrigger = $(this);
        let modalType = modalTrigger.data("modal-type");
        displayModal(svcUrl, toolKey, modalType);
    });
};

export let displayModal = function (svcUrl, toolKey, modalType) {

    let data = {};
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
        let toolKey = $(this).data("tool-key");
        let actionType = $(this).data("action-type");
        let formId = $(this).data("form");
        let data = $("#" + formId).serialize();

        jQuery.ajax({
            url: createActionServiceUrl(svcUrl, toolKey, actionType),
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
                refreshTool(svcUrl, toolKey);
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