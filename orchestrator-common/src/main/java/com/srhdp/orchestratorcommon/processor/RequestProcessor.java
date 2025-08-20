package com.srhdp.orchestratorcommon.processor;

import com.srhdp.orchestratorcommon.messages.Request;
import com.srhdp.orchestratorcommon.messages.Response;
import reactor.core.publisher.Mono;

public interface RequestProcessor<T extends Request, R extends Response> {

    Mono<R> process(T request);

}
