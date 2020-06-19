import {createDataServiceUrl, createToolRendererUrl} from "./serviceRegistry";
import {loadTool} from "./tools";
import {populateDataTable} from "./dataTables";

const toolKey = "tool-rules";
const toolSelector = "#" + toolKey;
const ruleTableSelector = "#toolRulesRulesTable";
const contextSelectorSelector = "#toolRulesContextSelect";
let svcUrl;

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

let onToolLoaded = function (result) {
    console.log("Tool [" + toolKey + "] loaded");
    $(toolSelector).html(result);

    // Trigger data-table when context-selector is changing
    $(contextSelectorSelector).change(function () {
        console.log("Detected change in context selector = " + $(contextSelectorSelector).val());
        loadRules($(contextSelectorSelector).val())
    });
};

let createTableConfig = function () {
    return {
        pageLength: 10
    };
};

let loadRules = function () {

    let dataFunction = function () {
        return {
            contextKey: $(contextSelectorSelector).val()
        }
    };

    let serviceConfig = {
        dataServiceUrl: createDataServiceUrl(svcUrl, toolKey, "rules"),
        dataFunction: dataFunction,
        tableConfig: createTableConfig(),
        tableSelector: ruleTableSelector
    };

    populateDataTable(serviceConfig, onDataLoaded)
};

let onDataLoaded = function (response) {
    console.log("Rule-data loaded successfully");
};

