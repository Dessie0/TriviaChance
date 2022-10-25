package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Random;

public class RedeemActivity extends AppCompatActivity {

    HashMap<Integer, item> listOfItems; //List of all items in the game
    private int numOfCommonItems = 12, numOfRareItems = 4;

    void redeemItem(float gachaPercentage) { //Decides if item is a rare drop, selects an item from the list
        //of items, and sends it to addToInventory()
        item gachaReward;
        Random random = new Random();
        boolean rareDrop = random.nextFloat() < gachaPercentage; //Randomly decides whether output is rare based on gachaPercentage

        try {
            if (rareDrop) {
                listOfItems.size();
                gachaReward = listOfItems.get(random.nextInt(listOfItems.size() - numOfCommonItems) + numOfCommonItems); //Randomly selects one of the four rare items
                gachaReward.RarityType = item.rarity.RARE;
            } else {
                gachaReward = listOfItems.get(random.nextInt(listOfItems.size() - numOfRareItems)); //Randomly selects one of the 12 common items.
                gachaReward.RarityType = item.rarity.RARE;
            }

            InventoryActivity.addToInventory(gachaReward);
        } catch (NullPointerException nullPointerException) {
            System.out.println("'gachaReward' failed to acquire an item from the 'listOfItems' list.");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
    }
}