package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.temporal.Temporal;

public class OnlineResultsActivity extends AppCompatActivity {

    ImageButton homeButton;
    ImageView goldBar;
    ImageView silverBar;
    ImageView bronzeBar;
    ImageView bronzePlayerIco;
    ImageView silverPlayerIco;
    ImageView goldPlayerIco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_online_results);



        homeButton = findViewById(R.id.homeButton3);
        goldBar = findViewById(R.id.goldBar);
        silverBar = findViewById(R.id.silverBar);
        bronzeBar = findViewById(R.id.bronzeBar);

        


        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnlineResultsActivity.this, MainMenu.class);
                startActivity(intent);
            }
        });

        Animation bronzeScale = new ShowAnimation(bronzeBar, 400);
        bronzeScale.setDuration(500);
        bronzeBar.startAnimation(bronzeScale);

        Animation silverScale = new ShowAnimation(silverBar, 600);
        silverScale.setDuration(700);
        silverBar.startAnimation(silverScale);

        Animation goldScale = new ShowAnimation(goldBar, 800);
        goldScale.setDuration(900);
        goldBar.startAnimation(goldScale);

    }
}

class ShowAnimation extends Animation{
    float finalHeight;
    View imageview;

    public ShowAnimation(View view,float deltaheight){
        this.imageview=view;
        this.finalHeight=deltaheight;
    }

    protected void applyTransformation(float interpolatedtime,Transformation t){
        imageview.getLayoutParams().height=(int)(finalHeight*interpolatedtime);
        imageview.requestLayout();
    }
    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}