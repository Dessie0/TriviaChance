package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.icon;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class IconWrapper {
    private final Bitmap bitmap;
    private final ImageView view;

    public IconWrapper(Bitmap bitmap, ImageView view) {
        this.bitmap = bitmap;
        this.view = view;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public ImageView getView() {
        return view;
    }
}