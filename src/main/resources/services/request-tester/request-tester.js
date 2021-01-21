let rewriteDao = require('/lib/rewrite-dao');
let thymeleaf = require('/lib/thymeleaf');

exports.get = function (req) {
    let result = rewriteDao.testRequest(req.params);

    if (result.error) {

        let view = resolve('request-tester-error.html');

        let model = {
            heading: {
                text: "Result: error",
                class: "error"
            },
            message: result.error
        };

        return {
            contentType: 'text/html',
            body: thymeleaf.render(view, model)
        }
    }

    const model = createModel(result);
    let view = resolve('request-tester-result.html');
    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)
    };
};

let createModel = function (result) {
    let model = {
        heading: {
            text: "Result: " + createHeadingText(result),
            class: result.state
        },
        steps: createStepsModel(result)
    };
    return model;
};

let createHeadingText = function (result) {

    if (result.state === "OK") {
        let redirects = 0;
        result.steps.forEach(function (step) {
            if (step.rewrite.target) {
                redirects++;
            }
        });
        return redirects + (redirects === 1 ? " redirect " : " redirects");

    } else {
        return result.state;
    }
};

let createStepsModel = function (result) {

    let steps = [];

    for (let i = 0; i < result.steps.length; i++) {
        steps.push(createStepModel(result.steps[i]));
    }

    return steps;
};

let createStepModel = function (step) {
    const incomingRequest = step.incomingRequest;
    const matchingVHost = incomingRequest.matchingVHost;
    const rewriteTarget = step.rewrite.target;
    const rewriteCode = step.rewrite.code;

    const stepModel = {
        request: {
            url: incomingRequest.url,
            vhost: matchingVHost ? matchingVHost.name : "no match"
        }
    };

    const stepIsRewrite = step.rewrite && step.rewrite.target;

    if (stepIsRewrite) {

        stepModel.target = {
            class: rewriteTarget.type,
            url: rewriteTarget.url,
            type: rewriteTarget.type
        };

        stepModel.rewrite = {
            code: rewriteCode
        };
    }

    stepModel.nextTransition = {
        hasNext: stepIsRewrite
    };

    return stepModel
};
