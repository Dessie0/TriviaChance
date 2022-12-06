package edu.floridapoly.mobiledeviceapps.fall22.api.socket;

import java.util.HashMap;
import java.util.Map;

public class SocketMessageGenerator {

    private final MessageType type;
    private final Map<String, String> params;

    public SocketMessageGenerator(MessageType type) {
        this.type = type;
        this.params = new HashMap<>();
    }

    public SocketMessageGenerator setParam(String key, String value) {
        this.getParams().put(key, value);
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public MessageType getType() {
        return type;
    }

    public String generate() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getType().toString());
        builder.append(";");
        for(Map.Entry<String, String> entry : this.getParams().entrySet()) {
            builder.append(entry.getKey()).append("¬").append(entry.getValue()).append(";");
        }

        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static SocketMessageGenerator fromString(String message) {
        String[] split = message.split(";");
        SocketMessageGenerator generator = new SocketMessageGenerator(MessageType.valueOf(split[0]));

        for(int i = 1; i < split.length; i++) {
            String param = split[i];
            String key = param.split("¬")[0];
            String value = param.split("¬")[1];

            generator.setParam(key, value);
        }

        return generator;
    }
}
