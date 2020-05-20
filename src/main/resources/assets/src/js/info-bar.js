import {model} from "./model";

const types = ["info", "warn", "error"];

function doDisplayInfoBar(msg, type) {
    let infoBar = $(model.infoBar.selector);
    infoBar.removeClass(types);
    infoBar.addClass(type);
    infoBar.removeClass("hidden");
    infoBar.html("<p>" + msg + "</p>");

    setTimeout(function () {
        infoBar.addClass("hidden")
    }, 5000);
}

export let showError = function (msg) {
    doDisplayInfoBar(msg, "error");
};
export let showInfo = function (msg) {
    doDisplayInfoBar(msg, "info");
};
