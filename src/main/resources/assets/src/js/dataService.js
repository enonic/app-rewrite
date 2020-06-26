import {showError} from "./info-bar";


export let fetch = function (serviceUrl, dataFunction, onLoaded) {

    jQuery.ajax({
        url: serviceUrl,
        cache: false,
        type: 'GET',
        data: dataFunction(),
        error:
            function (response, status, error) {
                console.log("ERROR: ", response);
                showError(response.responseText);
            },
        success: function (response, textStatus, jqXHR) {
            onLoaded(response);
        }
    });
};



