import {model} from "./model";

export let initModals = function (svcUrl) {

    console.log("Initializing modals");

    $(model.modals.triggers).click(function () {
        let modalTrigger = $(this);
        console.log("Enable modal-trigger", modalTrigger);
        let modalId = modalTrigger.data("modal");
        console.log("ModalId: " + modalId);
        displayModal(svcUrl, modalId);
    });

    $(model.modals.overlay).click(function () {
        closeModals();
        toggleOverlay();

    });

};


export let displayModal = function (svcUrl, name) {

    let selector = model.modals[name];
    console.log("Selector: ", selector);

    let data = {};

    jQuery.ajax({
        url: svcUrl + "/modal-create-context",
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
        }
    });

    $(selector).toggleClass("closed");
    toggleOverlay();

};

let closeModals = function () {
    $(model.modals.all).addClass("closed");
};

let toggleOverlay = function () {
    $(model.modals.overlay).toggleClass("closed");
};