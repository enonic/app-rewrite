import {model} from "./model";

export let selectTool = function (selector) {
    let allNavs = $(model.toolbar.toolNav);
    allNavs.removeClass("selected");
    $(selector).addClass("selected");
    let activates = $(selector).data("activator");
    toggleTool(activates);
};


let toggleTool = function (id) {
    $(model.components.tool).each(function () {
        if ($(this).attr('id') === id) {
            $(this).addClass("selected");
        } else {
            $(this).removeClass("selected");
        }
    });
};

export let initToolbar = function (svcUrl) {

    let allNavs = $(model.toolbar.toolNav);

    allNavs.each(function () {
        $(this).click(function () {
            allNavs.removeClass("selected");
            $(this).addClass("selected");
            let activates = $(this).data("activator");
            toggleTool(activates);
        })
    });

    $.ajax({
        url: svcUrl + "check-vhost-state",
        cache: false,
        type: 'GET',
        error: function (request, status, error) {
        },
        success: function (result) {
            let $vhost = $('#vhost-state');
            if (result === false) {
                $vhost.show();
            } else {
                $vhost.hide();
            }
        }
    });
};
