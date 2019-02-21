package com.newsviewsv2;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class AboutActivity extends AppCompatActivity {


    private TextView aboutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_about);
        aboutText = (TextView) findViewById(R.id.aboutText);


        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }
        String versionCode = "Version code: " + pInfo.versionCode;
        String VersionName = "Version name: " + pInfo.packageName;

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        String installDate = "Install date: " + df.format(pInfo.lastUpdateTime);

        Date buildDate = new Date(BuildConfig.BUILD_TIME);
        String buildDates = "Build date: " + df.format(buildDate);

        aboutText.append(
                versionCode + "\n" +
                        VersionName + "\n" +
                        installDate + "\n" +
                        buildDates + "\n"
        );
    }
}
