package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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

    ImageButton mainThemeButton;
    ImageButton goldThemeButton;
    ImageButton greenThemeButton;
    ImageButton redThemeButton;
    ImageButton bambooThemeButton;
    ImageButton sugarThemeButton;
    ImageButton purpleThemeButton;

    Button redeemPrizesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_inventory);

        redeemPrizesButton = findViewById(R.id.redeemPrizesButton);
        mainThemeButton = findViewById(R.id.mainThemeButton);
        goldThemeButton = findViewById(R.id.goldThemeButton);
        greenThemeButton = findViewById(R.id.greenThemeButton);
        redThemeButton = findViewById(R.id.redThemeButton);
        bambooThemeButton = findViewById(R.id.bambooThemeButton);
        sugarThemeButton = findViewById(R.id.sugarThemeButton);
        purpleThemeButton = findViewById(R.id.purpleThemeButton);

        redeemPrizesButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(InventoryActivity.this, RedeemActivity.class);
            startActivity(intent);
        });

    }

    public void changeTheme(View view) {
        switch (view.getId()) {
            case R.id.mainThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance);
                break;
            case R.id.goldThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance_Gold);
                break;
            case R.id.greenThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance_Green);
                break;
            case R.id.redThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance_Red);
                break;
            case R.id.bambooThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance_Bamboo);
                break;
            case R.id.sugarThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance_Sugar);
                break;
            case R.id.purpleThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance_Purple);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InventoryActivity.this, MainMenu.class);
        startActivity(intent);
    }
}