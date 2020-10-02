import {enableHelp, loadTool} from "./tools";
import {createToolRendererUrl} from "./serviceRegistry";
import {model} from "./model";

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
    $(model.input.requestURL).keyup(function () {
        delay(function () {
            testRequest();
        }, 150);
    });
};

let testRequest = function () {

    let fieldVal = $(model.input.requestURL).val();
    if (fieldVal === "") {
        return;
    }

    let data = {
        requestURL: fieldVal
    };

    jQuery.ajax({
        url: svcUrl + "/request-tester",
        cache: false,
        type: 'GET',
        data: data,
        error: function (response, status, error) {
            renderError(response);

        },
        success: function (result) {
            $(model.elements.requestTesterResult).html(result);
        }
    });
};


function renderError(result) {

}

function renderHeading(result) {
    const headingText = buildHeadingText(result);
    let html = "";
    html += "<h2 class='state-" + result.state + "'>Result: " + headingText + "</h2>";
    return html;
}

let buildHeadingText = function (result) {
    if (result.state === "OK") {
        let redirects = 0;
        result.steps.forEach(function (step) {
            if (step.rewrite.target) {
                redirects++;
            }
        });
        return redirects + " redirects";

    } else {
        return result.state;
    }
};

let delay = (function () {
    let timer = 0;
    return function (callback, ms) {
        clearTimeout(timer);
        timer = setTimeout(callback, ms);
    };
})();

