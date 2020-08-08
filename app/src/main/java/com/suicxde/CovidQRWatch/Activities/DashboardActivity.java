package com.suicxde.CovidQRWatch.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.suicxde.CovidQRWatch.R;
import java.util.Locale;


public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView addcarcard, listcard, logoutcard, profilecard;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDb = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_dashboard);

        //cardViews
        addcarcard = findViewById(R.id.soscard_res);
        listcard = findViewById(R.id.mapscard_res);
        logoutcard = findViewById(R.id.attatchcardid);
        profilecard = findViewById(R.id.profilecard_res);

        addcarcard.setOnClickListener(this);
        listcard.setOnClickListener(this);
        logoutcard.setOnClickListener(this);
        profilecard.setOnClickListener(this);





        Dexter.withContext(DashboardActivity.this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }

                    }

                    private void showSettingsDialog() {

                        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                        builder.setTitle("Need Permissions");
                        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
                        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                openSettings();
                            }

                            private void openSettings() {

                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, 101);
                            }

                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();

                    }


                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();




    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    //card OnClicks
    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.soscard_res) {
            Intent profilex = new Intent(this, OpenCameraActivty.class);

            startActivity(profilex);

        } else if (id == R.id.mapscard_res) {
            Intent profilex = new Intent(this, NotificationActivity.class);

            startActivity(profilex);


        } else if (id == R.id.profilecard_res) {
            Intent profilex = new Intent(this, ProfileActivity.class);
            startActivity(profilex);
            this.finish();
        } else if (id == R.id.attatchcardid) {
            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(this, LoginActivity.class);
            startActivity(loginActivity);

        }

    }

    //Language Set
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }

    //Load Lang
    public void loadLocale(){

        SharedPreferences prefs =getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);

    }



}
//TODO PUSH TO GIT- SUICXDE007