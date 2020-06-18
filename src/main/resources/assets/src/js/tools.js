import {initializeModalActions, initModalTriggers} from "./modals";
import {showError} from "./info-bar";
import {createToolRendererUrl} from "./serviceRegistry";
import {initDataTriggers} from "./dataTriggers";
import {initActionsTriggers} from "./actions";
import {initTableActions} from "./dataTableActions";

export let loadTool = function (toolRendererUrl, dataFunction, onToolLoaded) {

    jQuery.ajax({
        url: toolRendererUrl,
        cache: false,
        type: 'GET',
        data: dataFunction(),
        error: function (response, status, error) {
            console.log("Error: ", response.responseText);
            showError("Cannot load tool: " + response.responseText)
        },
        success: function (result) {
            onToolLoaded(result);
        }
    });
};