package com.newsviewsv2;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class AboutActivity extends AppCompatActivity {


    private TextView aboutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String installDate = df.format(pInfo.lastUpdateTime);

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
