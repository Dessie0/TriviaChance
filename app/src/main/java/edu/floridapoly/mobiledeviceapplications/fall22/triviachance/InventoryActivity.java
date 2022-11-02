package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.item;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class InventoryActivity extends AppCompatActivity {

    static void addToInventory(Profile profile, item gachaReward) { //Add item from redeemItem to user's inventory
        for (item collectable : profile.getInventory()) {
            if (collectable == gachaReward) {
                collectable.quantity++;
                break;
            }

            if(gachaReward.RarityType == item.rarity.RARE) profile.getInventory().add(0, gachaReward); //Adds the item to the beginning of the list if RARE item
            else profile.getInventory().add(gachaReward);                                                //Else adds the item to the end of the list
                                                                                                    //Side Note: Could consider adding a sort by alphabetical order
        }
    }

    void displayItems(){ //Displays all items in inventory item frames. Assumes inventory list is sorted by rares first, then commons

    }


    Button redeemPrizesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_inventory);

        redeemPrizesButton = findViewById(R.id.redeemPrizesButton);

        redeemPrizesButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(InventoryActivity.this, RedeemActivity.class);
            startActivity(intent);
        });

    }
}