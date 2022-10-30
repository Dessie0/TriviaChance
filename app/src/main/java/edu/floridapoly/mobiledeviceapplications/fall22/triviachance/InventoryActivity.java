package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.profile.Profile;

public class InventoryActivity extends AppCompatActivity {

    static void addToInventory(item gachaReward) { //Add item from redeemItem to user's inventory
        for (item collectable : Profile.inventory) {
            if (collectable == gachaReward) {
                collectable.quantity++;
                break;
            }

            if(gachaReward.RarityType == item.rarity.RARE) Profile.inventory.add(0, gachaReward); //Adds the item to the beginning of the list if RARE item
            else Profile.inventory.add(gachaReward);                                                //Else adds the item to the end of the list
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