package org.flycraft.android.untildate.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.List;

public class OsUtils {

    private static final String GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=";

    public static void openAppInGooglePlay(Context context) {
        Intent intent;
        String appPackage = context.getPackageName();
        try {
            intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackage)
            );
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + appPackage)
            );
        }
        context.startActivity(intent);
    }

}
