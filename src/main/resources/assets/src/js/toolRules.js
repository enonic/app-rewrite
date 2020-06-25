import {createDataServiceUrl, createModalSelector, createModalUrl, createToolRendererUrl} from "./serviceRegistry";
import {loadTool} from "./tools";
import {populateDataTable, serializeDataTable} from "./dataTables";
import {displayModal} from "./modals";
import {enableActionButtons} from "./tableActions";

const toolKey = "tool-rules";
const toolSelector = "#" + toolKey;
const ruleTableSelector = "#toolRulesRulesTable";
const ruleTableFormSelector = "#toolRulesRulesTableForm";
const contextSelectorSelector = "#toolRulesContextSelect";

const createRuleButton = "#btnCreateRule";

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


let contextSelectorDataContext = function () {
    return {
        contextKey: $(contextSelectorSelector).val()
    }
};

let loadRules = function () {

    let serviceConfig = {
        dataServiceUrl: createDataServiceUrl(svcUrl, toolKey, "rules"),
        dataFunction: contextSelectorDataContext,
        tableConfig: createTableConfig(),
        tableSelector: ruleTableSelector
    };

    populateDataTable(serviceConfig, onDataLoaded)
};

let onDataLoaded = function (response) {
    console.log("Rule-data loaded successfully");
    enableRuleButtons();
    enableActionButtons(svcUrl, toolSelector, toolKey, ruleTableSelector);
};

let enableRuleButtons = function () {

    $(createRuleButton).click(function () {
        let modalSelector = createModalSelector(toolKey, "create");
        let modalServiceUrl = createModalUrl(svcUrl, toolKey, "create");
        displayModal(modalServiceUrl, svcUrl, modalSelector, contextSelectorDataContext)
    });
};

