import '../css/main.scss';
import {model} from "./model";

let dt = require('datatables.net');

let svcUrl;

let initListeners = function () {

    console.log("Adding listeners...");

    $(model.input.requestURL).keyup(function () {
        delay(function () {
            testRequest();
        }, 150);
    });
};

let testRequest = function () {

    cleanTestState();

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
        error: function (request, status, error) {
            $(model.elements.result).html(request.responseText);
        },
        success: function (result) {
            console.log("Result: ", JSON.stringify(result));
            processResult(result);
        }
    });

};

let handleError = function (result) {

    $(model.elements.result).html("<p>Error: " + result.message + "</p>");
};

let processResult = function (data) {


    if (data.result.virtualHost.name) {

        let matchingVhost = $("#vhost_" + data.result.virtualHost.name);
        matchingVhost.addClass("match");

        $(model.elements.result).html(generateResultHtml(data.result));
    }

};

let cleanTestState = function () {
    $(model.elements.vhosts).removeClass("match");
    $(model.elements.result).html("");
};

let generateResultHtml = function (result) {
    let html = "";

    if (result.virtualHost.name) {
        html += "<p>vhost: " + result.virtualHost.name + "</p>";
    }

    if (result.rewrite.target) {
        html += "<p>target: " + result.rewrite.target + "</p>";
    }
    return html;
};

let delay = (function () {
    let timer = 0;
    return function (callback, ms) {
        clearTimeout(timer);
        timer = setTimeout(callback, ms);
    };
})();

$(document).ready(function () {
    console.log("Document ready...");

    svcUrl = CONFIG.svcUrl;

    $('#myTable').DataTable();

    initListeners();
});






