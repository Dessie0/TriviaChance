package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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


    TextView unlockText;
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

        unlockText = findViewById(R.id.unlocksAvaliableText);
        unlockText.setText(String.format("Unlocks Avaliable: %d", ResultsActivity.unlocksAvaliable));

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RedeemActivity.this, MainMenu.class);
                startActivity(intent);
            }
        });

        inventoryButton = findViewById(R.id.inventoryButton);
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RedeemActivity.this, InventoryActivity.class);
                startActivity(intent);
            }
        });

        increaseButton = findViewById(R.id.increaseButton);
        decreaseButton = findViewById(R.id.decreaseButton);


        unlockButton = findViewById(R.id.unlockButton);
        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        checkColors();

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAmount < ResultsActivity.unlocksAvaliable) {
                    unlockButton.setText(String.format("Unlock %d", ++selectedAmount));
                    checkColors();
                }
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAmount > 0) {
                    unlockButton.setText(String.format("Unlock %d", --selectedAmount));
                    checkColors();
                }

            }
        });
    }

    public void checkColors() {
        if (ResultsActivity.unlocksAvaliable == 0) {
            decreaseButton.setBackgroundColor(Color.LTGRAY);
            increaseButton.setBackgroundColor(Color.LTGRAY);
        }
        else if (selectedAmount == 0) {
            decreaseButton.setBackgroundColor(Color.LTGRAY);
            increaseButton.setBackgroundColor(getResources().getColor(colorPrimary));
        }
        else if (selectedAmount > 0 && selectedAmount < ResultsActivity.unlocksAvaliable) {
            decreaseButton.setBackgroundColor(getResources().getColor(colorPrimary));
            increaseButton.setBackgroundColor(getResources().getColor(colorPrimary));
        }
        else if (selectedAmount == ResultsActivity.unlocksAvaliable) {
            increaseButton.setBackgroundColor(Color.LTGRAY);
            decreaseButton.setBackgroundColor(getResources().getColor(colorPrimary));
        }
    }
}