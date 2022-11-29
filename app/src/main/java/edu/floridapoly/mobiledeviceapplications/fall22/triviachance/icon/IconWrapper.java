package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.icon;

import android.graphics.Bitmap;
import android.widget.ImageView;

import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class IconWrapper {
    private final Profile profile;
    private final Bitmap bitmap;
    private final ImageView view;

    public IconWrapper(Profile profile, Bitmap bitmap, ImageView view) {
        this.profile = profile;
        this.bitmap = bitmap;
        this.view = view;
    }

    public Profile getProfile() {
        return profile;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public ImageView getView() {
        return view;
    }
}