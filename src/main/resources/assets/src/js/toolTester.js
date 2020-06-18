import {model} from "./model";

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
            console.log("Result: ", JSON.stringify(result));
            processResult(result);
        }
    });

};

let processResult = function (data) {

    writeRequestRoute(data.results);

    let anyResult = data.results[0] != null;
    if (anyResult) {
        data.results.forEach(function (result) {
            doMarkMatch(result);
        });
    } else {
        toggleMatch(model.elements.vhosts, null, false);
        toggleMatch(model.elements.rows, null, false);
    }
};

let doMarkMatch = function (result) {

    console.log("Matching: ", result);

    toggleMatch(model.elements.vhosts, "#vhost_" + result.virtualHost.name);

    let matchingRule = result.rewrite ? result.rewrite.matchId : null;

    if (matchingRule != null) {
        toggleMatch(model.elements.rows, "#" + result.virtualHost.name + "_" + matchingRule, true);
    } else {
        toggleMatch(model.elements.rows, null)
    }
};

let writeRequestRoute = function (results) {

    let html = $(model.input.requestURL).val();

    results.forEach(function (result) {
        if (result.rewrite && result.rewrite.target) {
            html += " -> " + result.rewrite.target;
        }
    });

    $("#resultPath").html(html);
};

let toggleMatch = function (selector, matchId, scrollTo) {

    console.log("Toggeling match for selector", selector, matchId, scrollTo);

    if (!matchId) {
        $(selector).removeClass("match");
        return;
    }

    let matching = $(matchId);
    $(selector).not(matching).removeClass("match");
    matching.addClass("match");

    if (scrollTo) {
        matching.get(0).scrollIntoView({behavior: "smooth", block: "center", inline: "nearest"});
    }
};


let delay = (function () {
    let timer = 0;
    return function (callback, ms) {
        clearTimeout(timer);
        timer = setTimeout(callback, ms);
    };
})();
