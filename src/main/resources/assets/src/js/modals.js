import {model} from "./model";
import {showError, showInfo} from "./info-bar";
import {createActionServiceUrl} from "./serviceRegistry";
import {refreshDataElement} from "./dataElements";

export let initModals = function () {
    initializeOverlay();
};

let initializeOverlay = function () {
    $(model.modals.overlay).click(function () {
        closeModals();
        closeOverlay();
    });
};

export let displayModal = function (modalServiceUrl, svcUrl, modalSelector, dataFunction, onDisplayed, onActionSuccess) {

    jQuery.ajax({
        url: modalServiceUrl,
        cache: false,
        type: 'GET',
        data: dataFunction(),
        error: function (response, status, error) {
            console.error("Error: ", response.responseText);
            showError(response.responseText);
        },
        success: function (result) {
            $(modalSelector).html(result);
            initializeModalActions(svcUrl, onActionSuccess);
            $(modalSelector).removeClass("closed");
            openOverlay();
            setModalInputFocus(modalSelector);
            if (onDisplayed) {
                onDisplayed();
            }
        }
    });
};

export let displayTableModal = function (modalServiceUrl, svcUrl, modalSelector, dataFunction, element, onDisplayed, onActionSuccess) {
    jQuery.ajax({
        url: modalServiceUrl,
        cache: false,
        type: 'GET',
        data: dataFunction(element),
        error: function (response, status, error) {
            console.error("Result: ", response.responseText);
            showError(response.responseText);
        },
        success: function (result) {
            $(modalSelector).html(result);
            initializeModalActions(svcUrl, onActionSuccess);
            $(modalSelector).removeClass("closed");
            openOverlay();
            setModalInputFocus(modalSelector);
            if (onDisplayed) {
                onDisplayed();
            }
        }
    });
};

export let modalOpen = function (selector) {
    return $(selector).is(":visible");
};

let setModalInputFocus = function (modalSelector) {
    $(modalSelector).focus();
    $(modalSelector).find('input:enabled:visible:first').focus();
};

export let postActionForm = function (formSelector, serviceUrl, successFunction) {

    let form = $(formSelector)[0];
    let data = new FormData(form);
    data.append('__host', location.host);

    jQuery.ajax({
        url: serviceUrl,
        cache: false,
        type: 'POST',
        data: data,
        processData: false,
        contentType: false,
        error: function (response, status, error) {
            console.log("ERROR: ", response);
            showError(response.responseText);
        },
        success: function (response, textStatus, jqXHR) {
            successFunction(response, textStatus, jqXHR);
        }
    });
};

let initializeModalActions = function (svcUrl, onActionSuccess) {

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

        let actionServiceUrl = createActionServiceUrl(svcUrl, toolKey, actionType);
        let formId = thisElem.data("form");
        const formSelector = "#" + formId;
        let refreshDataSelector = thisElem.data("refresh-data-selector");

        let onSuccess = function (response, textStatus, jqXHR) {
            if (onActionSuccess) {
                onActionSuccess(response);
            } else {
                showInfo(response.message);
                closeModals();
                closeOverlay();
                refreshDataElement(refreshDataSelector);
            }
        };

        postActionForm(formSelector, actionServiceUrl, onSuccess);
    });

    $(model.modals.insertStrategy).change(function () {
        let $elements = $('label[for="' + model.modals.insertPosition + '"], #' + model.modals.insertPosition);
        if ('position' === $(this).val()) {
            $elements.toggle();
        } else {
            $elements.hide();
        }
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
