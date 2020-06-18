import {createDataServiceUrl, createToolRendererUrl} from "./serviceRegistry";
import {selectTool} from "./toolbar";
import {loadTool} from "./tools";
import {populateDataTable} from "./dataTableTools";

const toolKey = "tool-rules";
const toolSelector = "#" + toolKey;
const ruleTableSelector = "#toolRulesRulesTable";
const contextSelectorSelector = "#toolRulesContextSelect";
let svcUrl;

let onToolLoaded = function (result) {
    console.log("Tool [" + toolKey + "] loaded");
    $(toolSelector).html(result);

    $(contextSelectorSelector).change(function () {
        console.log("Detected change in context selector = " + $(contextSelectorSelector).val());
        loadRules($(contextSelectorSelector).val())
    });

};

export let initToolRules = function (svc) {

    svcUrl = svc;

    let data = function () {
    };

    loadTool(createToolRendererUrl(svcUrl, toolKey), data, onToolLoaded);
};

export let setRuleContext = function (contextKey) {
    let contextSelector = $(contextSelectorSelector);
    contextSelector.val(contextKey).change();
};

let createTableConfig = function () {
    return {
        pageLength: 10
    };
};

function loadRules(contextSelector) {

    let dataFunction = function () {
        return {
            contextKey: $(contextSelectorSelector).val()
        }
    };

    let onDataLoaded = function (response) {
        console.log("Rule-data loaded successfully");
    };

    let serviceConfig = {
        svcUrl: svcUrl,
        dataServiceUrl: createDataServiceUrl(svcUrl, toolKey, "rules"),
        dataFunction: dataFunction,
        tableConfig: createTableConfig(),
        tableSelector: ruleTableSelector
    };

    populateDataTable(serviceConfig, onDataLoaded)
    // Load data and populate data-table
}


