import '../css/main.scss';
import {initToolbar} from "./toolbar";
import {initToolContext} from "./toolContext";
import {initToolRules} from "./toolRules";
import {initModals} from "./modals";
import {defineShortcuts} from "./shortcuts";
import {initToolTester} from "./toolTester";

let dt = require('datatables.net');


$(document).ready(function () {
    let svcUrl = CONFIG.svcUrl;
    initToolbar(svcUrl);
    initToolContext(svcUrl);
    initToolRules(svcUrl);
    initToolTester(svcUrl);
    initModals();
    defineShortcuts();
});
