package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PlayerViewHolder extends RecyclerView.ViewHolder {
    TextView user_name;
    ImageView profile_image;
    View view;

    PlayerViewHolder(View itemView) {
        super(itemView);
        user_name = itemView.findViewById(R.id.user_name);
        profile_image = itemView.findViewById(R.id.profile_image);
        view = itemView;
    }
}
