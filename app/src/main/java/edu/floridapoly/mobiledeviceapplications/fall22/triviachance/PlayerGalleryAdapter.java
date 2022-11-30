package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
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
    boolean isHost;

    public PlayerGalleryAdapter(List<Player> list, Context context, boolean isHost) {
        this.list = list;
        this.context = context;
        this.isHost = isHost;
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
        viewHolder.user_name.setText(list.get(position).getProfile().getUsername());

        if (list.get(position).getProfile().getIconURL() != null) {
            ProfileIconHelper.reloadProfileIcon(list.get(position).getProfile(), viewHolder.profile_image);
        }

        if (isHost) {
            viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q && MainMenu.getInstancePackager().getPreferences().getBoolean("vibration", false)) {
                        final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
                    }

                    // make sure user does not have option to kick themself
                    if (!MainMenu.getLocalProfile().getUsername().equals(list.get(viewHolder.getAdapterPosition()).getProfile().getUsername())) {
                        // Make pop-up for kicking user
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Kick User?");
                        builder.setMessage("Are you sure you would like to kick \"" + list.get(viewHolder.getAdapterPosition()).getProfile().getUsername() + "\"?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Kick", (DialogInterface.OnClickListener) (dialog, which) -> {
                            //Kick player here ******************************************
                            Toast.makeText(view.getContext(), "User has been kicked" , Toast.LENGTH_SHORT).show();
                        });
                        builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
                            dialog.cancel();
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                    return false;
                }
            });
        }

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
