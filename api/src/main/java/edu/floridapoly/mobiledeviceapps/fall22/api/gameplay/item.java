package edu.floridapoly.mobiledeviceapps.fall22.api.gameplay;

public class item {

    private Integer itemID;
    private Integer quantity;

    public item(){
        this.itemID = 0;
        this.quantity = 1;
    }

    public item(Integer id, Integer quantity) {
        this.itemID = id;
        this.quantity = quantity;
    }

    public Integer getItemID(){ return itemID; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer q){ this.quantity = q;}
    public void setItemID(Integer id){ this.itemID = id; }
    public void incrementQuantity() { quantity++;}
}