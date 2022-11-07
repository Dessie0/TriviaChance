package edu.floridapoly.mobiledeviceapps.fall22.api.gameplay;

public class item {

    public enum rarity {
        COMMON,
        RARE
    }

    public rarity RarityType;
    public Integer quantity;

    public String itemName;
    public String imageFilePath;
    public String Description;
    public Integer itemID;

    public item(Integer id, Integer quantity) {
        this.itemID = id;
        this.quantity = quantity;
    }
}
