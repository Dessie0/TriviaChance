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
import java.util.Random;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.item;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class RedeemActivity extends AppCompatActivity {

    //itemRegistry itemRegistry = new itemRegistry(this, 1);
    private final int numOfCommonItems = 12;
    private final int numOfRareItems = 4;
    Integer unlocksAvailable = MainMenu.getInstancePackager().getLocalProfile().getNumberOfUnlocks();
    Button unlockButton;
    Button increaseButton;
    Button decreaseButton;

    ImageButton homeButton;
    ImageButton inventoryButton;

    TypedValue typedValue;

    int selectedAmount = 0;
    int colorPrimary, colorSecondary;


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
            Intent intent = new Intent(RedeemActivity.this, MainMenu.class);
            startActivity(intent);
        });

        inventoryButton = findViewById(R.id.inventoryButton);
        inventoryButton.setOnClickListener(view -> {
            Intent intent = new Intent(RedeemActivity.this, InventoryActivity.class);
            startActivity(intent);
        });

        increaseButton = findViewById(R.id.increaseButton);
        decreaseButton = findViewById(R.id.decreaseButton);


        unlockButton = findViewById(R.id.unlockButton);
        unlockButton.setOnClickListener(view -> {

            item gachaReward = new item();
            Profile profile = MainMenu.getInstancePackager().getLocalProfile();
            Random random = new Random();
            boolean rareDrop = random.nextFloat() < 0.3; //Randomly decides whether output is rare based on gachaPercentage


            try {
                int itemId;//Randomly selects one of the four rare items
                if (rareDrop) {
                    itemId = random.nextInt(numOfCommonItems - numOfRareItems) + numOfCommonItems;
                } else {
                    itemId = random.nextInt(numOfCommonItems - numOfRareItems);
                }
                gachaReward.setItemID(itemId);

                for(int i = 0; i < profile.getInventory().size(); i++){ //Increases quantity if duplicate is found
                    if(gachaReward == profile.getInventory().get(i)){
                        profile.getInventory().get(i).incrementQuantity();
                        Toast.makeText(getApplicationContext(), "You received a duplicate. Adding duplicate to inventory!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                Toast.makeText(getApplicationContext(), "You received: " + "gachaReward.getIt" + "!", Toast.LENGTH_SHORT).show();
                profile.getInventory().add(gachaReward);

            } catch (NullPointerException nullPointerException) {
                System.out.println("'gachaReward' failed to acquire an item from the 'listOfItems' list.");
            }


            //itemRegistry.getItemName(profile.getInventory().get(1).getItemID());



        });

        //checkColors();


        increaseButton.setOnClickListener(view -> {
            if (selectedAmount < unlocksAvailable) {
                unlockButton.setText(String.format(Locale.ENGLISH,"Unlock %d", ++selectedAmount));

                checkColors();
            }

            else {
                Toast.makeText(getApplicationContext(), "Not Enough Unlocks!", Toast.LENGTH_SHORT).show();
            }
            if(selectedAmount > 0){
                unlockButton.setEnabled(true);
            }
        });

        decreaseButton.setOnClickListener(view -> {
            if (selectedAmount > 0) {
                unlockButton.setText(String.format(Locale.ENGLISH,"Unlock %d", --selectedAmount));
                checkColors();
            }
            if(selectedAmount < 1){
                unlockButton.setEnabled(false);
            }
        });
    }

    public void checkColors() {
        /*if (ResultsActivity.unlocks == 0) {
            decreaseButton.setBackgroundColor(Color.LTGRAY);
            increaseButton.setBackgroundColor(Color.LTGRAY);
        }*/
        if (selectedAmount == 0) {
            decreaseButton.setBackgroundColor(Color.LTGRAY);
            increaseButton.setBackgroundColor(getResources().getColor(colorPrimary));
        }
        else if (selectedAmount > 0 && selectedAmount < unlocksAvailable) {
            decreaseButton.setBackgroundColor(getResources().getColor(colorPrimary));
            increaseButton.setBackgroundColor(getResources().getColor(colorPrimary));
        }
        else if (selectedAmount == unlocksAvailable) {
            increaseButton.setBackgroundColor(Color.LTGRAY);
            decreaseButton.setBackgroundColor(getResources().getColor(colorPrimary));
        }
    }
}