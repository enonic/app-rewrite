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

        toogleMatch(model.elements.vhosts, "#vhost_" + data.result.virtualHost.name);

        let matchingRule = data.result.rewrite.matchId;

        if (matchingRule != null) {
            toogleMatch(model.elements.rows, "#" + data.result.virtualHost.name + "_" + matchingRule, true);
        } else {
            toogleMatch(model.elements.rows, null)
        }

    } else {
        toogleMatch(model.elements.vhosts, null, false);
        toogleMatch(model.elements.rows, null, false);
    }
};

let toogleMatch = function (selector, matchId, scrollTo) {

    if (!matchId) {
        $(selector).removeClass("match");
        return;
    }

    let matching = $(matchId);
    $(selector).not(matching).removeClass("match");
    matching.addClass("match");

    if (scrollTo) {
        matching.get(0).scrollIntoView(false);
    }

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






