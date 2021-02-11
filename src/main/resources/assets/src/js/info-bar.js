import {model} from "./model";

const types = ["info", "warn", "error"];

const defaultTimeout = 5000;

function doDisplayInfoBar(msg, type, timeout = defaultTimeout, callback) {
    let infoBar = $(model.infoBar.selector);
    infoBar.removeClass(types);
    infoBar.addClass(type);
    infoBar.removeClass("hidden");
    infoBar.html("<p>" + msg + "</p>");

    setTimeout(function () {
        infoBar.addClass("hidden");
        if (callback) {
            callback();
        }
    }, timeout);
}

export let showError = function (msg, timeout, callback) {
    doDisplayInfoBar(msg, "error", timeout, callback);
};
export let showInfo = function (msg, timeout, callback) {
    doDisplayInfoBar(msg, "info", timeout, callback);
};
