package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.icon.ProfileIconHelper;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;


public class PlayerGalleryAdapter extends RecyclerView.Adapter<PlayerViewHolder> {

    List<Player> list;
    Context context;

    public PlayerGalleryAdapter(List<Player> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View photoView = inflater.inflate(R.layout.player_card, parent, false);
        PlayerViewHolder viewHolder = new PlayerViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PlayerViewHolder viewHolder, final int position) {
        //final index = viewHolder.getAdapterPosition();
        viewHolder.user_name.setText(list.get(position).getProfile().getUsername());
        if (list.get(position).getProfile().getIconURL() != null) {
            ProfileIconHelper.reloadProfileIcon(list.get(position).getProfile(), viewHolder.profile_image);
        }

        viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q && MainMenu.getInstancePackager().getPreferences().getBoolean("vibration", false)) {
                    final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
                }
                // Make pop-up for kicking user
                Toast.makeText(view.getContext(), list.get(viewHolder.getAdapterPosition()).getProfile().getUsername() , Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
