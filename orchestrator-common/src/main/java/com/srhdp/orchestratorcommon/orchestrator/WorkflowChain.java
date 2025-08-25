package com.srhdp.orchestratorcommon.orchestrator;

public interface WorkflowChain {

    void setPreviousStep(RequestCompensator previousStep);

    void setNextStep(RequestSender nextStep);

}
