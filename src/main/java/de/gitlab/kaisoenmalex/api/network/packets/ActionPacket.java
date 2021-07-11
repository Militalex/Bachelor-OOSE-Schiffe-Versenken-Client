package de.gitlab.kaisoenmalex.api.network.packets;

import java.util.List;

public class ActionPacket {
    private final String methodName;
    private final List<Object> parameters;

    public ActionPacket(String methodName, List<Object> parameters) {
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<Object> getParameters() {
        return parameters;
    }
}
