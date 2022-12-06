package edu.floridapoly.mobiledeviceapps.fall22.api.gameplay;

import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.utils.StringGenerator;

public class TriviaGame {

    private final UUID uuid;
    private final String code;
    private final boolean online;

    public TriviaGame(String uuid, String code, boolean online) {
        this.uuid = UUID.fromString(uuid);
        this.code = code;
        this.online = online;
    }

    public UUID getUUID() {
        return uuid;
    }
    public String getCode() {
        return this.code;
    }
    public boolean isOnline() {
        return online;
    }

    public static String getRandomCode() {
        return new StringGenerator(6).setUseNumeric(false).setUseLower(false).generate();
    }

}
