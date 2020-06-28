import {model} from "./model";
import {showError, showInfo} from "./info-bar";
import {createActionServiceUrl} from "./serviceRegistry";
import {refreshDataElement} from "./dataElements";

export let initModals = function (toolUrl) {
    initializeOverlay();
};

let initializeOverlay = function () {
    $(model.modals.overlay).click(function () {
        closeModals();
        closeOverlay();
    });
};

export let displayModal = function (modalServiceUrl, svcUrl, modalSelector, dataFunction) {

    jQuery.ajax({
        url: modalServiceUrl,
        cache: false,
        type: 'GET',
        data: dataFunction(),
        error: function (response, status, error) {
            console.log("Result: ", response.responseText);
            showError(response.responseText);
        },
        success: function (result) {
            console.log("Fetched modal " + modalServiceUrl);
            $(modalSelector).html(result);
            initializeModalActions(svcUrl);
            $(modalSelector).removeClass("closed");
            openOverlay();
        }
    });
};

let initializeModalActions = function (svcUrl) {

    $(model.modals.modalCancel).click(function (event) {
        event.preventDefault();
        closeModals();
        closeOverlay();
    });

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
                closeOverlay();
                refreshDataElement(refreshDataSelector);
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
export let openOverlay = function () {
    $(model.modals.overlay).removeClass("closed");
};