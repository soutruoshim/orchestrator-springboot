package com.srhdp.orchestratorcommon.orchestrator;

import com.srhdp.orchestratorcommon.messages.Request;
import org.reactivestreams.Publisher;

import java.util.UUID;

public interface RequestSender {

    Publisher<Request> send(UUID id);

}
