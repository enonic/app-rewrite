import '../css/main.scss';
import {initToolbar} from "./toolbar";
import {initToolContext} from "./toolContext";
import {initToolRules} from "./toolRules";
import {initModals} from "./modals";
import {defineShortcuts} from "./shortcuts";
import {initToolTester} from "./toolTester";


let dt = require('datatables.net');

let svcUrl;


$(document).ready(function () {
    svcUrl = CONFIG.svcUrl;
    initButtons();
    initToolbar();
    initToolContext(svcUrl);
    initToolRules(svcUrl);
    initToolTester(svcUrl);
    initModals();
    defineShortcuts();
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






