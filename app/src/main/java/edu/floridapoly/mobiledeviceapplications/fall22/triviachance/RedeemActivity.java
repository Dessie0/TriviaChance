package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.item;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class RedeemActivity extends AppCompatActivity {

    itemRegistry items = new itemRegistry(this, 4);
    int selectedAmount = 0;
    int colorPrimary, colorSecondary;
    int unlocksAvailable = MainMenu.getInstancePackager().getLocalProfile().getNumberOfUnlocks();

    Button unlockButton;
    Button increaseButton;
    Button decreaseButton;

    ImageButton homeButton;
    ImageButton inventoryButton;

    TypedValue typedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_redeem);

        typedValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
        colorPrimary = typedValue.resourceId;
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
        colorSecondary = typedValue.resourceId;

        TextView unlocktext = findViewById(R.id.unlocksAvaliableText);
        unlocktext.setText(String.format(Locale.ENGLISH,"Unlocks Available: %d", unlocksAvailable));

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(RedeemActivity.this, InventoryActivity.class);
            startActivity(intent);
        });

        inventoryButton = findViewById(R.id.inventoryButton);
        inventoryButton.setOnClickListener(view -> {
            Intent intent = new Intent(RedeemActivity.this, InventoryActivity.class);
            startActivity(intent);
        });

        unlockButton = findViewById(R.id.unlockButton);
        unlockButton.setEnabled(false);
        unlockButton.setOnClickListener(view -> {

            redeemItem(selectedAmount);

            unlocksAvailable -= selectedAmount;

            if(unlocksAvailable < 1){
                unlocksAvailable = 0;
                selectedAmount = 0;
                unlockButton.setEnabled(false);
                decreaseButton.setEnabled(false);
                checkColors();
            }
            else if(unlocksAvailable < selectedAmount){
                selectedAmount = unlocksAvailable;
                checkColors();
            }

            unlockButton.setText(String.format(Locale.ENGLISH, "Unlock %d Items", selectedAmount));
            unlocktext.setText(String.format(Locale.ENGLISH,"Unlocks Available: %d", unlocksAvailable));

        });

        increaseButton = findViewById(R.id.increaseButton);
        if(unlocksAvailable < 1) increaseButton.setEnabled(false);
        increaseButton.setOnClickListener(view -> {

            if (selectedAmount < unlocksAvailable) {
                unlockButton.setText(String.format(Locale.ENGLISH,"Unlock %d Items", ++selectedAmount));
                unlockButton.setEnabled(true);
                checkColors();
            }

            else {
                Toast.makeText(getApplicationContext(), "Not Enough Unlocks!", Toast.LENGTH_SHORT).show();
            }
            if(selectedAmount > 0){
                unlockButton.setEnabled(true);
                decreaseButton.setEnabled(true);
            }
        });

        decreaseButton = findViewById(R.id.decreaseButton);
        decreaseButton.setEnabled(false);
        decreaseButton.setOnClickListener(view -> {

            if (selectedAmount > 0) {
                unlockButton.setText(String.format(Locale.ENGLISH,"Unlock %d Items", --selectedAmount));
                checkColors();
            }
            if(selectedAmount < 1){
                unlockButton.setEnabled(false);
            }
        });
    }


    public void redeemItem(Integer selectedAmount){
        Profile profile = MainMenu.getInstancePackager().getLocalProfile();

        boolean duplicate = false;
        int itemId;

        for(int i = 0; i < selectedAmount; i++) {
            item gachaReward = new item();

            try {
                itemId = determineItemId();
                gachaReward.setItemID(itemId);
                duplicate = determineIfDuplicate(gachaReward, profile);
            } catch (NullPointerException nullPointerException) {
                System.out.println("'gachaReward' failed to acquire an item from the 'listOfItems' list.");
            }

            if(!duplicate){
                Toast.makeText(getApplicationContext(), "You received: " + items.getItemName(gachaReward.getItemID()) + "!", Toast.LENGTH_SHORT).show();
                System.out.println(items.getItemName(gachaReward.getItemID()) + " Added to Inventory");
                profile.getInventory().add(gachaReward);
                
                //Update the server about the new item.
                MainMenu.getAPI().updateItem(profile, gachaReward.getItemID(), gachaReward.getQuantity());
            }
        }
    }


    int determineItemId(){
        Random random = new Random();
        boolean rareDrop = random.nextFloat() < 0.3; //Randomly decides whether output is rare based on gachaPercentage

        int itemId;
        int numOfCommonItems = 6;
        int numOfRareItems = 3;

        if(rareDrop) { //Sets itemId to a random row in the bounds of common items, or a random row in the bounds of rare items.
            itemId = random.nextInt(numOfCommonItems - numOfRareItems) + numOfCommonItems + 1;
            System.out.println("RARE itemId = " + itemId + "\n\n");
        } else {
            itemId = random.nextInt(numOfCommonItems) + 1;
            System.out.println("COMMON itemId = " + itemId + "\n\n");
        }
        return itemId;
    }

    boolean determineIfDuplicate(item gachaReward, Profile profile) { //Goes through inventory and increases quantity if duplicate is found

        boolean duplicate = false;

        for (item inventoryItem : profile.getInventory()) {
            if (Objects.equals(gachaReward.getItemID(), inventoryItem.getItemID())) {
                inventoryItem.incrementQuantity();
                duplicate = true;
                break;
            }
        }
        return duplicate;
    }

    public void checkColors() {
        if (selectedAmount > 0 && selectedAmount < unlocksAvailable) {
            decreaseButton.setBackgroundColor(getResources().getColor(colorPrimary));
            increaseButton.setBackgroundColor(getResources().getColor(colorPrimary));
        }
        else if (selectedAmount == unlocksAvailable) {
            increaseButton.setBackgroundColor(Color.LTGRAY);
            decreaseButton.setBackgroundColor(getResources().getColor(colorPrimary));
        }
        else if (selectedAmount == 0) {
            decreaseButton.setBackgroundColor(Color.LTGRAY);
            increaseButton.setBackgroundColor(getResources().getColor(colorPrimary));
        }
        if (unlocksAvailable == 0) {
            decreaseButton.setBackgroundColor(Color.LTGRAY);
            increaseButton.setBackgroundColor(Color.LTGRAY);
        }
    }
}