package com.srhdp.orchestratorcommon.orchestrator;

import com.srhdp.orchestratorcommon.messages.Response;

public interface WorkflowStep<T extends Response> extends
        RequestSender,
        RequestCompensator,
        ResponseProcessor<T>,
        WorkflowChain {
}
