import {createActionServiceUrl, createDataServiceUrl, createModalSelector, createModalUrl, createToolRendererUrl} from "./serviceRegistry";
import {enableHelp, loadTool} from "./tools";
import {populateDataTable} from "./dataTables";
import {displayModal, postActionForm} from "./modals";
import {enableActionButtons} from "./tableActions";
import {fetch} from "./dataService";
import {populateDataElement} from "./dataElements";
import {model} from "./model";
import {showInfo} from "./info-bar";

const toolKey = "tool-rules";
const toolSelector = "#" + toolKey;
const ruleTableSelector = "#toolRulesRulesTable";
const contextSelectorSelector = "#toolRulesContextSelect";

let svcUrl;

const rulesTableSettings = {
    pageLength: 50,
    autoWidth: false,
    dom: 'Bfrtip',
    buttons: [
        'copyHtml5', 'excelHtml5', 'pdfHtml5', 'csvHtml5'
    ]
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

function triggerContextChanged() {
    $(contextSelectorSelector).change(function () {
        let context = $(contextSelectorSelector).val();
        loadRules(context);
        setRuleButtonState(context);
    });
}

function setRuleButtonState(contextKey) {

    function doSetButtonState(response) {

        if (response.readOnly) {
            $(model.buttons.rule.create).prop('disabled', true);
            $(model.buttons.rule.import).prop('disabled', true);
        } else {
            $(model.buttons.rule.create).prop('disabled', false);
            $(model.buttons.rule.import).prop('disabled', false);
            $(model.buttons.rule.export).prop('disabled', false);
        }
    }

    let providerInfoUrl = createDataServiceUrl(svcUrl, toolKey, "provider-info");
    let dataFunction = function () {
        return {
            contextKey: contextKey
        }
    };
    fetch(providerInfoUrl, dataFunction, doSetButtonState);
}

function setContextSelectorData() {
    let doRefreshContextSelector = function (selector, response) {
        let html = "<option disabled selected value> -- select a vhost --</option>";
        html += response;
        $(selector).html(html);
    };

    let doPopulateSelectorValues = function (response) {
        let html = "<option disabled selected value> -- select a vhost --</option>";
        html += response;
        $(contextSelectorSelector).html(html);
    };

    let serviceUrl = createDataServiceUrl(svcUrl, toolKey, "context-selector");
    let dataFunction = function () {
    };

    populateDataElement(contextSelectorSelector, serviceUrl, dataFunction, doPopulateSelectorValues, doRefreshContextSelector);
}

let onToolLoaded = function (result) {
    console.log("Tool [" + toolKey + "] loaded");
    $(toolSelector).html(result);
    triggerContextChanged();
    setContextSelectorData();
    enableHelp(toolSelector);
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
        tableConfig: rulesTableSettings,
        tableSelector: ruleTableSelector
    };

    populateDataTable(serviceConfig, onDataLoaded, onTableRefreshed);
};

let onDataLoaded = function (response) {
    console.log("Rule-data loaded successfully");
    enableRuleButtons();
    enableImportButton();
    enableExportButton();
};

let onTableRefreshed = function (response) {
    enableActionButtons(svcUrl, toolSelector, toolKey);
};

let enableRuleButtons = function () {
    $(model.buttons.rule.create).click(function () {
        let modalSelector = createModalSelector(toolKey);
        let modalServiceUrl = createModalUrl(svcUrl, toolKey, "create");
        displayModal(modalServiceUrl, svcUrl, modalSelector, contextSelectorDataContext)
    });
};

let enableImportButton = function () {
    $(model.buttons.rule.import).click(function () {
        let modalSelector = createModalSelector(toolKey);
        let modalServiceUrl = createModalUrl(svcUrl, toolKey, "import");
        displayModal(modalServiceUrl, svcUrl, modalSelector, contextSelectorDataContext, dryRunImportOnChange);
    });
};

let enableExportButton = function () {
    $(model.buttons.rule.export).click(function () {
        let modalSelector = createModalSelector(toolKey);
        let modalServiceUrl = createModalUrl(svcUrl, toolKey, "export");
        displayModal(modalServiceUrl, svcUrl, modalSelector, contextSelectorDataContext, function () {
        }, function (response) {
            let dataServiceUrl = createDataServiceUrl(svcUrl, toolKey, "export");
            $("body").append(
                "<iframe src='" + dataServiceUrl + "?data=" + response.data + "&fisk=ost" + "' style='display: none;' ></iframe>");
        });
    });


};


let setImportDryRun = function (dryRun) {
    $("#importRulesDryRun").val(dryRun);
};

let dryRunImportOnChange = function () {
    function renderImportPreview(response) {

        function renderImportResult(result) {
            function renderStatItem(label, value) {
                html += "<li><span class='label'>" + label + ": </span><span class='value'>" + value + "</span></li>";
            }

            let html = "<ul class='import-preview-stats'>";
            renderStatItem("New", result.importResult.new);
            renderStatItem("Error", result.importResult.errors);
            renderStatItem("Deleted", result.importResult.deleted);
            renderStatItem("Unsupported", result.importResult.unsupported);
            renderStatItem("Total", result.importResult.total);
            html += "</ul>";
            return html;
        }

        let html = "";
        html += "<p class='message'>" + response.message + ":</p>";
        html += renderImportResult(response.result);
        $("#tool-rules-import-result").html(html);
    }


    $(model.modals.forms.importRules).find(':input').change(function (index, value) {
        if ($("#toolRulesImportFile").val()) {
            let showDryRunInfo = function (response) {
                console.log("Response", response);
                renderImportPreview(response);
                setImportDryRun(false);
            };

            let actionServiceUrl = createActionServiceUrl(svcUrl, toolKey, "import");
            setImportDryRun(true);
            postActionForm(model.modals.forms.importRules, actionServiceUrl, showDryRunInfo);
        }
    });
};
