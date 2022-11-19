package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;


public class PlayerGalleryAdapter extends RecyclerView.Adapter<PlayerViewHolder> {

    List<Player> list = Collections.emptyList();
    Context context;

    public PlayerGalleryAdapter(List<Player> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public PlayerViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
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
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Tada", Toast.LENGTH_SHORT).show();
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
