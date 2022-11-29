package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.icon;

import android.widget.ImageView;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.MainMenu;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class ProfileIconHelper {

    public static void reloadProfileIcon(Profile profile, ImageView icon) {
        if(profile.getIconURL() == null) return;

        //Attempt to use a cached version of the Bitmap image.
        //If the Bitmap is not cached, retrieve it from the server asynchronously and cache it
        //for future quick-load.
        if(!profile.getUUID().equals(MainMenu.getLocalProfile().getUUID()) || MainMenu.getInstancePackager().getProfileIcon() == null) {
            new LoadProfileIconAsync().execute(profile, icon);
        } else {
            icon.setImageBitmap(MainMenu.getInstancePackager().getProfileIcon());
        }
    }
}
