package edu.floridapoly.mobiledeviceapps.fall22.api.utils;

import java.util.Random;

public class StringGenerator {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERIC = "0123456789";
    private static final String LOWER = "abcdefghijklmnopqrstuvxyz";

    private final int length;
    private boolean useUpper;
    private boolean useNumeric;
    private boolean useLower;

    public StringGenerator(int length) {
        this.length = length;

        this.useUpper = true;
        this.useNumeric = true;
        this.useLower = true;
    }

    public String generate() {
        String availableChars = (this.isUseUpper() ? UPPER : "") + (this.isUseNumeric() ? NUMERIC : "") + (this.isUseLower() ? LOWER : "");

        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < this.getLength(); i++) {
            builder.append(availableChars.charAt(random.nextInt(availableChars.length())));
        }

        return builder.toString();
    }

    public int getLength() {
        return length;
    }

    public StringGenerator setUseLower(boolean useLower) {
        this.useLower = useLower;
        return this;
    }

    public StringGenerator setUseNumeric(boolean useNumeric) {
        this.useNumeric = useNumeric;
        return this;
    }

    public StringGenerator setUseUpper(boolean useUpper) {
        this.useUpper = useUpper;
        return this;
    }

    public boolean isUseLower() {
        return useLower;
    }

    public boolean isUseNumeric() {
        return useNumeric;
    }

    public boolean isUseUpper() {
        return useUpper;
    }
}
