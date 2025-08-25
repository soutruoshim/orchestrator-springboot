package com.srhdp.orchestratorcommon.orchestrator;

import com.srhdp.orchestratorcommon.messages.Request;
import com.srhdp.orchestratorcommon.messages.Response;
import org.reactivestreams.Publisher;

public interface WorkflowOrchestrator {

    Publisher<Request> orchestrate(Response response);

}
