import '../css/main.scss';
import {model} from "./model";
import {initToolbar} from "./toolbar";
import {initModals} from "./modals";
import {initLoadableTools} from "./tools";
import {initToolContext} from "./toolContext";
import {initToolRules} from "./toolRules";

let dt = require('datatables.net');

let svcUrl;


$(document).ready(function () {
    svcUrl = CONFIG.svcUrl;
    initButtons();
    initToolbar();
    initToolContext(svcUrl);
    initToolRules(svcUrl);
});


let initButtons = function () {

    $('.btnCreate').click(function () {
        doCreatePosting($(this));
    });

};

let doCreatePosting = function (context) {

    let data = {};

    context.parent(".rule-creator").children(":input").each(function () {
        let elem = $(this);
        data[elem.attr("name")] = elem.val();
    });

    jQuery.ajax({
        url: svcUrl + "/rule-creator-service",
        cache: false,
        type: 'POST',
        data: data,
        error: function (request, status, error) {
            console.log("ERROR HAPPENED ON CREATE");
        },
        success: function (result) {
            console.log("Result: ", JSON.stringify(result));
        }
    });

};






