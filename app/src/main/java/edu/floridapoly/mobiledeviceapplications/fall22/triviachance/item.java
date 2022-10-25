package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

public class item {

    enum rarity {
        COMMON,
        RARE
    }

    rarity RarityType;
    static String itemName;
    String imageFilePath;
    String Description;
    static Integer itemID;
    Integer quantity;

}
