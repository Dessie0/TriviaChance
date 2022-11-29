package edu.floridapoly.mobiledeviceapps.fall22.api.gameplay;

import java.io.Serializable;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.utils.StringGenerator;

public class TriviaGame implements Serializable {

    private final UUID uuid;
    private final String code;

    public TriviaGame(String uuid, String code) {
        this.uuid = UUID.fromString(uuid);
        this.code = code;
    }

    public UUID getUUID() {
        return uuid;
    }
    public String getCode() {
        return this.code;
    }

    public static String getRandomCode() {
        return new StringGenerator(6).setUseNumeric(false).setUseLower(false).generate();
    }

}
