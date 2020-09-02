import {showError} from "./info-bar";

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

export let enableHelp = function (parentSelector) {
    $(parentSelector).find(".helptext > .heading").click(function (event) {
        event.preventDefault();
        console.log("Clicking on help");
        const heading = $(this);
        const helptext = heading.parent();
        helptext.toggleClass("collapsed");
        if (helptext.hasClass("collapsed")) {
            heading.text("[ display help ]");
        } else {
            heading.text("[ hide help ]");
        }
    });
};