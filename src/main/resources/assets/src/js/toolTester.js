import {loadTool} from "./tools";
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
            $(model.elements.result).html(response.responseText);
        },
        success: function (result) {
            processResult(result);
        }
    });

};

let processResult = function (data) {
    writeRequestRoute(data.results);
};

let writeRequestRoute = function (results) {
    let html = renderInitial($(model.input.requestURL).val());

    results.forEach(function (result) {
        if (result.rewrite && result.rewrite.target) {
            html += renderStepTransition(result.rewrite.code);
            html += renderStep(result.rewrite.target);
        }
    });

    $(model.elements.result).html(html);
};

let renderInitial = function (url) {
    let html = "<div class='redirect-step'>";
    html += "<p class='initial'> " + url + " </p>";
    html += "</div>";
    return html;
};

let renderStep = function (target) {

    if (!target) {
        return "";
    }

    let html = "<div class='redirect-step'>";
    html += "<p class='" + target.type + "'> " + target.url + " </p>";
    html += "</div>";
    return html;
};

let renderStepTransition = function (code) {
    let html = "<div class='transition'>";
    html += "<p>" + code + "</p>";
    html += "<i class=\"material-icons\">arrow_forward</i>";
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

