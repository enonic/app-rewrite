import {model} from "./model";


export let initModals = function (svcUrl) {

    console.log("Initializing modals");
    initializeTriggers(svcUrl);
    initializeOverlay();
};

let initializeModalActions = function (svcUrl) {

    console.log("Initialize modal-actions");

    $(model.modals.modalAction).click(function (event) {

        event.preventDefault();
        let serviceName = $(this).data("service");
        let formId = $(this).data("form");
        let actionType = $(this).data("action-type");

        console.log("FormId: " + formId);

        let data = $("#" + formId).serialize();

        console.log("data: " + data);

        jQuery.ajax({
            url: svcUrl + "/" + serviceName,
            cache: false,
            type: 'POST',
            data: data,
            error: function (response, status, error) {
                console.log("Result: ", response.responseText);
                $(model.elements.result).html(response.responseText);
            },
            success: function (result) {
                console.log("Result: ", JSON.stringify(result));
            }
        });

    });
};


let initializeOverlay = function () {
    $(model.modals.overlay).click(function () {
        closeModals();
        toggleOverlay();
    });
};

let initializeTriggers = function (svcUrl) {
    $(model.modals.triggers).click(function () {
        let modalTrigger = $(this);
        console.log("Enable modal-trigger", modalTrigger);
        let modalId = modalTrigger.data("modal");
        console.log("ModalId: " + modalId);
        displayModal(svcUrl, modalId);
    });
};

export let displayModal = function (svcUrl, name) {

    let selector = model.modals[name].selector;
    let modalService = model.modals[name].modalService;

    let data = {};

    jQuery.ajax({
        url: svcUrl + "/" + modalService,
        cache: false,
        type: 'GET',
        data: data,
        error: function (response, status, error) {
            console.log("Result: ", response.responseText);
            $(model.elements.result).html(response.responseText);
        },
        success: function (result) {
            console.log("Result: ", JSON.stringify(result));
            $(selector).html(result);
            initializeModalActions(svcUrl);
            $(selector).toggleClass("closed");
            toggleOverlay();
        }
    });


};

let closeModals = function () {
    $(model.modals.all).addClass("closed");
};

let toggleOverlay = function () {
    $(model.modals.overlay).toggleClass("closed");
};