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
    console.log("Tool [" + toolKey + "] loaded");
    $(toolSelector).html(result);
    initListeners();
    enableHelp(toolSelector);
};

let initListeners = function () {

    console.log("Adding listeners...");

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
            $(model.elements.redirectSteps).html(response.responseText);
        },
        success: function (result) {
            processResult(result);
        }
    });
};

let processResult = function (result) {
    writeRequestRoute(result);
};

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

let writeRequestRoute = function (result) {
    let html = renderHeading(result);
    html += renderSteps(result);
    html += "</div>";
    $(model.elements.requestTesterResult).html(html);
};

function renderSteps(result) {
    let html = '<div id="request-tester-steps">';

    const steps = result.steps;

    for (let i = 0; i < steps.length; i++) {

        html += renderStep(steps[i]);
        if (steps[i + 1]) {
            html += renderStepTransition();
        }
    }
    return html;
}

let renderStep = function (step) {

    if (!step || !step.rewrite) {
        return "";
    }

    const incomingRequest = step.incomingRequest;
    const matchingVHost = incomingRequest.matchingVHost;

    let html = "<div class='redirect-step'>";
    html += renderIncomingRequest(matchingVHost, incomingRequest);
    html += renderRewriteTarget(step);
    html += "</div>";

    return html;
};

function renderIncomingRequest(matchingVHost, incomingRequest) {
    let html = "<div class='step-result-box incoming-request'>";
    html += "<div class='vhost'><p class='label'>vhost:</p><p class='value'>"
    if (matchingVHost) {
        html += matchingVHost.name;
    } else {
        html += "No match";
    }
    html += "</p></div>";
    html += "<div class='url'><p class='label'>request:</p><p class='value'>" + incomingRequest.url + "</p></div>";
    html += "</div>";
    return html;
}

function renderRewriteTarget(step) {

    const rewriteTarget = step.rewrite.target;
    const rewriteCode = step.rewrite.code;
    let html = "";

    if (rewriteTarget) {
        html += "<div class='transition'><i class=\"material-icons\">arrow_forward</i><p>(" + rewriteCode + ")</p></div>";
    } else {
        html += "<div class='transition'></div>";
    }

    if (rewriteTarget) {
        html += "<div class='step-result-box rewrite-target'>";
        html += "<div class='type type-" + rewriteTarget.type + "'>";
        html += "<p class='label'>type:</p><p class='value'>" + rewriteTarget.type + "</p>";
        html += "</div>";
        html += "<div class='target'>";
        html += "<p class='label'>target:</p><p class='value'>" + rewriteTarget.url + "</p>";
        html += "</div>";
        html += "</div>";
    } else {
        html += "<div class='rewrite-target'></div>";
    }

    return html;
}

let renderStepTransition = function () {
    let html = "<div class='stepTransition'>";
    html += "<i class=\"material-icons\">arrow_drop_down</i>";
    html += "</div>";
    return html;
};

let delay = (function () {
    let timer = 0;
    return function (callback, ms) {
        clearTimeout(timer);
        timer = setTimeout(callback, ms);
    };
})();

