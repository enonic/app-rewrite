export const model = {

    input: {
        requestURL: "#requestURL"
    },
    elements: {
        vhosts: ".vhost",
        result: "#result",
        rows: "tr",
        errorMsg: "#errorMsg"
    },
    components: {
        tool: ".tool"
    },
    toolbar: {
        toolNav: ".tool-nav"
    },
    buttons: {
        tool: {
            action: "button.action"
        }
    },
    modals: {
        all: ".modal",
        overlay: "#modal-overlay",
        triggers: ".modal-trigger",
        modalCancel: ".modal-cancel",
        modalAction: ".modal-action",
        contextCreate: {
            selector: "#modalContextCreate",
        }
    },
    infoBar: {
        selector: "#bottom-bar"
    },
    tool: {
        dataTriggers: {
            select: ".data-trigger-select",
            auto: ".auto-load",
            tableAction: ".table-action"
        },
        select: {
            selector: ".select"
        }
    }


};