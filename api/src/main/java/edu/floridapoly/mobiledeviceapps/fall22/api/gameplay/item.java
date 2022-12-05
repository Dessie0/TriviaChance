package edu.floridapoly.mobiledeviceapps.fall22.api.gameplay;

public class item {

    private Integer id;
    private Integer quantity;

    public item(){
        this.id = 0;
        this.quantity = 1;
    }

    public item(Integer id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Integer getItemID(){ return id; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer q){ this.quantity = q;}
    public void setItemID(Integer id){ this.id = id; }
    public void incrementQuantity() { quantity++;}
}