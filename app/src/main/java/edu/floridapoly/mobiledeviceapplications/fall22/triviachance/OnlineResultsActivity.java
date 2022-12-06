package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Comparator;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.icon.ProfileIconHelper;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;

public class OnlineResultsActivity extends AppCompatActivity {

    ImageButton homeButton;
    ImageView goldBar;
    ImageView silverBar;
    ImageView bronzeBar;
    ImageView bronzePlayerIco;
    ImageView silverPlayerIco;
    ImageView goldPlayerIco;
    TextView bronzeText;
    TextView silverText;
    TextView goldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_online_results);

        homeButton = findViewById(R.id.homeButton3);
        goldBar = findViewById(R.id.goldBar);
        silverBar = findViewById(R.id.silverBar);
        bronzeBar = findViewById(R.id.bronzeBar);

        bronzePlayerIco = findViewById(R.id.bronzePlayerIcon);
        silverPlayerIco = findViewById(R.id.silverPlayerIcon);
        goldPlayerIco = findViewById(R.id.goldPlayerIcon);

        bronzeText = findViewById(R.id.bronzeText);
        silverText = findViewById(R.id.silverText);
        goldText = findViewById(R.id.goldText);

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

        if(!this.getIntent().hasExtra("gameUUID")) return;

        MainMenu.getAPI().retrieveGameLeaderboard(this.getIntent().getExtras().getString("gameUUID")).thenAccept(players -> {
            //Sort players by most correct
            players.sort(Comparator.comparingInt(player -> ((Player) player).getStats().getCorrect()).reversed());

            for(int i = 0; i < Math.min(3, players.size()); i++) {
                ProfileIconHelper.reloadProfileIcon(players.get(i).getProfile(), i == 0 ? goldPlayerIco : i == 1 ? silverPlayerIco : bronzePlayerIco);
                changeText(i == 0 ? goldText : i == 1 ? silverText : bronzeText, players.get(i).getStats().getCorrect());
            }
        });
    }

    public void changeText(TextView view, int correct) {
        view.setText(correct + " Correct");
    }

    @Override
    protected void onStop() {
        super.onStop();

        MainMenu.getAPI().leaveGame(MainMenu.getLocalProfile(), MainMenu.getAPI().getCurrentGame());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OnlineResultsActivity.this, MainMenu.class);
        startActivity(intent);
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