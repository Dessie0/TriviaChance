package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class InventoryActivity extends AppCompatActivity {




    //The below function is never used and shares identical functionality with the redeemItem function.

    ImageButton mainThemeButton;
    ImageButton goldThemeButton;
    ImageButton greenThemeButton;
    ImageButton redThemeButton;
    ImageButton bambooThemeButton;
    ImageButton sugarThemeButton;
    ImageButton purpleThemeButton;
    ImageButton orangeThemeButton;
    ImageButton forestThemeButton;
    ImageButton homeButton;
    Button redeemPrizesButton;
    Profile profile = MainMenu.getInstancePackager().getLocalProfile();
    itemRegistry items = new itemRegistry(this, 2);

        /*public void addToInventory(Profile profile, item gachaReward) { //Add item from redeemItem to user's inventory
        for (item collectable : profile.getInventory()) {
            if (collectable == gachaReward) {
                collectable.incrementQuantity();
                break;
            }

            if(items.getRarity(gachaReward.getItemID()) == "rare") profile.getInventory().add(0, gachaReward);  //Adds the item to the beginning of the list if RARE item
            else profile.getInventory().add(gachaReward);                                                               //Else adds the item to the end of the list
                                                                                                                        //Side Note: Could consider adding a sort by alphabetical order
        }
    }

    */



    void displayItems(){ //Displays all items in inventory item frames. Assumes inventory list is sorted by rares first, then commons

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_inventory);
        ArrayList<ImageButton> imageButtonArrayList = new ArrayList<>();

        redeemPrizesButton = findViewById(R.id.redeemPrizesButton);

        mainThemeButton = findViewById(R.id.mainThemeButton);
        mainThemeButton.setEnabled(false);
        imageButtonArrayList.add(mainThemeButton);

        goldThemeButton = findViewById(R.id.goldThemeButton);
        goldThemeButton.setEnabled(false);
        imageButtonArrayList.add(goldThemeButton);


        greenThemeButton = findViewById(R.id.greenThemeButton);
        greenThemeButton.setEnabled(false);
        imageButtonArrayList.add(greenThemeButton);


        redThemeButton = findViewById(R.id.redThemeButton);
        redThemeButton.setEnabled(false);
        imageButtonArrayList.add(redThemeButton);


        bambooThemeButton = findViewById(R.id.bambooThemeButton);
        bambooThemeButton.setEnabled(false);
        imageButtonArrayList.add(bambooThemeButton);

        sugarThemeButton = findViewById(R.id.sugarThemeButton);
        sugarThemeButton.setEnabled(false);
        imageButtonArrayList.add(sugarThemeButton);


        purpleThemeButton = findViewById(R.id.purpleThemeButton);
        purpleThemeButton.setEnabled(false);
        imageButtonArrayList.add(purpleThemeButton);

        orangeThemeButton = findViewById(R.id.orangeThemeButton);
        orangeThemeButton.setEnabled(false);
        imageButtonArrayList.add(orangeThemeButton);

        forestThemeButton = findViewById(R.id.forestThemeButton);
        forestThemeButton.setEnabled(false);
        imageButtonArrayList.add(forestThemeButton);


        //Sorts the inventory by itemId
        profile.getInventory().sort((i1, i2) -> i1.getItemID().compareTo(i2.getItemID()));

        for(int i = 0; i < profile.getInventory().size(); i++){
            if(Objects.equals(items.getItemName(profile.getInventory().get(i).getItemID()), items.getItemName(i))){
                imageButtonArrayList.get(i).setEnabled(true);
                System.out.println("Button " + i + " is Enabled.\n");
                imageButtonArrayList.get(i).setImageResource(0);
            }
            else {
                imageButtonArrayList.get(i).setEnabled(false);
                System.out.println("Button " + i + " is Disabled.\n");
            }
        }

        redeemPrizesButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(InventoryActivity.this, RedeemActivity.class);
            startActivity(intent);
        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(InventoryActivity.this, MainMenu.class);
            startActivity(intent);
        });

    }



    public void changeTheme(View view) {
        switch (view.getId()) {
            case R.id.mainThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance);
                Toast.makeText(this,"Swapping to Main Theme", LENGTH_SHORT).show();
                break;
            case R.id.goldThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance_Gold);
                Toast.makeText(this,"Swapping to Gold Theme", LENGTH_SHORT).show();
                break;
            case R.id.greenThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance_Green);
                Toast.makeText(this,"Swapping to Green Theme", LENGTH_SHORT).show();
                break;
            case R.id.redThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance_Red);
                Toast.makeText(this,"Swapping to Red Theme", LENGTH_SHORT).show();
                break;
            case R.id.bambooThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance_Bamboo);
                Toast.makeText(this,"Swapping to Bamboo Theme", LENGTH_SHORT).show();
                break;
            case R.id.sugarThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance_Sugar);
                Toast.makeText(this,"Swapping to Sugar Theme", LENGTH_SHORT).show();
                break;
            case R.id.purpleThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance_Purple);
                Toast.makeText(this,"Swapping to Purple Theme", LENGTH_SHORT).show();
                break;
            case  R.id.orangeThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance_Orange);
                Toast.makeText(this,"Swapping to Orange Theme", LENGTH_SHORT).show();
                break;
            case R.id.forestThemeButton:
                ThemeUtil.changeToTheme(InventoryActivity.this, R.style.Theme_TriviaChance_Forest);
                Toast.makeText(this,"Swapping to Forest Theme", LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InventoryActivity.this, MainMenu.class);
        startActivity(intent);
    }
}