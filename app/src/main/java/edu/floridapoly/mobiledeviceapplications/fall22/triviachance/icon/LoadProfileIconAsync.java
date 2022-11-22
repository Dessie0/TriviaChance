package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.icon;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.MainMenu;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class LoadProfileIconAsync extends AsyncTask<Object, Void, IconWrapper> {
    @Override
    protected IconWrapper doInBackground(Object... objects) {
        Profile profile = (Profile) objects[0];
        ImageView view = (ImageView) objects[1];

        try {
            if(profile.getIconURL() == null) return new IconWrapper(null, view);

            Bitmap bitmap = Picasso.get().load(profile.getIconURL()).get();
            return new IconWrapper(bitmap, view);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(IconWrapper wrapper) {
        if(wrapper == null) {
            //Image was deleted from the server, so reset their Icon URL to be null.
            Profile profile = MainMenu.getInstancePackager().getLocalProfile();
            MainMenu.getInstancePackager().getAPI().updateIcon(profile, null);
            profile.setIconURL(null);
            return;
        }

        //Dont need to set the profile icon if there's no uploaded Bitmap
        if(wrapper.getBitmap() == null) return;

        MainMenu.getInstancePackager().setProfileIcon(wrapper.getBitmap());
        wrapper.getView().setImageBitmap(wrapper.getBitmap());
    }
}
