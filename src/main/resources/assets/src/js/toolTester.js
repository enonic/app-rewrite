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

    console.log("REULST", JSON.stringify(result));

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
            console.log("Result: ", response.responseText);
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

    let html = $(model.input.requestURL).val();

    results.forEach(function (result) {
        if (result.rewrite && result.rewrite.target) {
            html += "<br/> -> " + result.rewrite.target;
        }
    });

    $("#resultPath").html(html);
};

let delay = (function () {
    let timer = 0;
    return function (callback, ms) {
        clearTimeout(timer);
        timer = setTimeout(callback, ms);
    };
})();

