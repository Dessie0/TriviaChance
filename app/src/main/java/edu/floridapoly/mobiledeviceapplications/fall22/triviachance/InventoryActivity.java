package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Comparator;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.item;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class InventoryActivity extends AppCompatActivity {

    ImageButton homeButton;
    ImageButton mainThemeButton;
    ImageButton goldThemeButton;
    ImageButton greenThemeButton;
    ImageButton redThemeButton;
    ImageButton bambooThemeButton;
    ImageButton sugarThemeButton;
    ImageButton purpleThemeButton;
    ImageButton orangeThemeButton;
    ImageButton forestThemeButton;
    Button redeemPrizesButton;

    Profile profile = MainMenu.getInstancePackager().getLocalProfile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_inventory);

        mainThemeButton = findViewById(R.id.mainThemeButton);
        goldThemeButton = findViewById(R.id.goldThemeButton);
        greenThemeButton = findViewById(R.id.greenThemeButton);
        redThemeButton = findViewById(R.id.redThemeButton);
        bambooThemeButton = findViewById(R.id.bambooThemeButton);
        sugarThemeButton = findViewById(R.id.sugarThemeButton);
        purpleThemeButton = findViewById(R.id.purpleThemeButton);
        orangeThemeButton = findViewById(R.id.orangeThemeButton);
        forestThemeButton = findViewById(R.id.forestThemeButton);

        ArrayList<ImageButton> imageButtonArrayList = new ArrayList<>();

        mainThemeButton.setEnabled(true);
        mainThemeButton.setImageResource(0);
        mainThemeButton.setTag((Integer)1);
        imageButtonArrayList.add(mainThemeButton);

        goldThemeButton.setEnabled(false);
        goldThemeButton.setTag((Integer)2);
        imageButtonArrayList.add(goldThemeButton);

        greenThemeButton.setEnabled(false);
        greenThemeButton.setTag((Integer)3);
        imageButtonArrayList.add(greenThemeButton);

        redThemeButton.setEnabled(false);
        redThemeButton.setTag((Integer)4);
        imageButtonArrayList.add(redThemeButton);

        bambooThemeButton.setEnabled(false);
        bambooThemeButton.setTag((Integer)5);
        imageButtonArrayList.add(bambooThemeButton);

        sugarThemeButton.setEnabled(false);
        sugarThemeButton.setTag((Integer)6);
        imageButtonArrayList.add(sugarThemeButton);

        purpleThemeButton.setEnabled(false);
        purpleThemeButton.setTag((Integer)7);
        imageButtonArrayList.add(purpleThemeButton);

        orangeThemeButton.setEnabled(false);
        orangeThemeButton.setTag((Integer)8);
        imageButtonArrayList.add(orangeThemeButton);

        forestThemeButton.setEnabled(false);
        forestThemeButton.setTag((Integer)9);
        imageButtonArrayList.add(forestThemeButton);

        profile.getInventory().sort(Comparator.comparing(item::getItemID));
        setLockStatus(imageButtonArrayList); //Searches for theme items in inventory and enables the buttons for all found themes

        redeemPrizesButton = findViewById(R.id.redeemPrizesButton);
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

    public void setLockStatus(ArrayList<ImageButton> imageButtonArrayList){ //Iterates through inventory to determine
                                                                            //what themes the user has access to
        for (item i1 : profile.getInventory()) {
                for(ImageButton b1 : imageButtonArrayList){
                    if (i1.getItemID() == b1.getTag()){ //Each ImageButton in the List has an
                        b1.setEnabled(true);            //id tag that matches the itemId associated with it
                        System.out.println(b1.getTag() + " is Enabled.\n");
                        b1.setImageResource(0);

                    }
                }
            }
    }

    public void changeTheme(View view) { //Functionality for each theme button on-screen
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