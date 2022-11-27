package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class InventoryActivity extends AppCompatActivity {

    itemRegistry registryDatabase;
    Button redeemPrizesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_inventory);
        registryDatabase = new itemRegistry(this, 1);
        registryDatabase.getReadableDatabase();
            ImageButton item1 = findViewById(R.id.itemBox1);
            ImageButton item2 = findViewById(R.id.itemBox2);
            ImageButton item3 = findViewById(R.id.itemBox3);
            ImageButton item4 = findViewById(R.id.itemBox4);
            ImageButton item5 = findViewById(R.id.itemBox5);
            ImageButton item6 = findViewById(R.id.itemBox6);
            ImageButton item7 = findViewById(R.id.itemBox7);
            ImageButton item8 = findViewById(R.id.itemBox8);
            ImageButton item9 = findViewById(R.id.itemBox9);
            ImageButton item10 = findViewById(R.id.itemBox10);
            ImageButton item11 = findViewById(R.id.itemBox11);
            ImageButton item12 = findViewById(R.id.itemBox12);
            ImageButton item13 = findViewById(R.id.itemBox13);
            ImageButton item14 = findViewById(R.id.itemBox14);
            ImageButton item15 = findViewById(R.id.itemBox15);
            ImageButton item16 = findViewById(R.id.itemBox16);
        //System.out.println(registryDatabase.getItemName(1));
        //System.out.println(registryDatabase.getItemName(2));
        //System.out.println(registryDatabase.getItemDescription(1));
        System.out.println(registryDatabase.getRarity(2));
                redeemPrizesButton = findViewById(R.id.redeemPrizesButton);
        registryDatabase.close();
        redeemPrizesButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(InventoryActivity.this, RedeemActivity.class);
            startActivity(intent);
        });

        item1.setOnClickListener((View v) -> {

        });

        item2.setOnClickListener((View v) -> {

        });

        item3.setOnClickListener((View v) -> {

        });

        item4.setOnClickListener((View v) -> {

        });

        item5.setOnClickListener((View v) -> {

        });

        item6.setOnClickListener((View v) -> {

        });

        item7.setOnClickListener((View v) -> {

        });

        item8.setOnClickListener((View v) -> {

        });

        item9.setOnClickListener((View v) -> {

        });

        item10.setOnClickListener((View v) -> {

        });

        item11.setOnClickListener((View v) -> {

        });

        item12.setOnClickListener((View v) -> {

        });

        item13.setOnClickListener((View v) -> {

        });

        item14.setOnClickListener((View v) -> {

        });

        item15.setOnClickListener((View v) -> {

        });

        item16.setOnClickListener((View v) -> {

        });
    }


    public void displayInventory(){

    }









}