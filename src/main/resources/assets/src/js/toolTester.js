import {enableHelp, loadTool} from "./tools";
import {createToolRendererUrl} from "./serviceRegistry";
import {model} from "./model";
import {showError} from "./info-bar";

const toolKey = "tool-tester";
const toolSelector = "#" + toolKey;
let svcUrl;

export let initToolTester = function (svc) {
    svcUrl = svc;
    let data = function () {
    };
    loadTool(createToolRendererUrl(svcUrl, toolKey), data, onToolLoaded);
};


let onToolLoaded = function (result) {
    $(toolSelector).html(result);
    initListeners();
    enableHelp(toolSelector);
};

let initListeners = function () {
    $(model.input.rewriteContext).change(function () {
        delay(function () {
            testRequest();
        }, 150);
    });

    $(model.input.requestURL).keyup(function () {
        delay(function () {
            testRequest();
        }, 150);
    });
};

let testRequest = function () {
    let rewriteContext = $(model.input.rewriteContext).val();
    if (rewriteContext === null || rewriteContext === "") {
        showError("VirtualHost is not selected.");
        return;
    }

    let fieldVal = $(model.input.requestURL).val();
    if (fieldVal === "") {
        return;
    }

    let data = {
        requestURL: rewriteContext + (fieldVal.startsWith("/") ? fieldVal : "/" + fieldVal)
    };

    jQuery.ajax({
        url: svcUrl + "/request-tester",
        cache: false,
        type: 'GET',
        data: data,
        error: function (response, status, error) {
            showError(response.responseText);
        },
        success: function (result) {
            $(model.elements.requestTesterResult).html(result);
        }
    });
};

let delay = (function () {
    let timer = 0;
    return function (callback, ms) {
        clearTimeout(timer);
        timer = setTimeout(callback, ms);
    };
})();

