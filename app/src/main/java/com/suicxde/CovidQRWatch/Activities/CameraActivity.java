package com.suicxde.CovidQRWatch.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.suicxde.CovidQRWatch.R;

public class CameraActivity extends AppCompatActivity {
    Button camerabtn;
   public static TextView cameratex;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        camerabtn = findViewById(R.id.camerabtn);
        cameratex = findViewById(R.id.camratext);
        String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        cameratex.setText(sessionId);
        camerabtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                Dexter.withContext(CameraActivity.this)
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                                Toast.makeText(CameraActivity.this, "dead", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),OpenCameraActivty.class));
                            }
                            @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                                if (response.isPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                                Toast.makeText(CameraActivity.this, "denied", Toast.LENGTH_SHORT).show();
                            }

                            private void showSettingsDialog() {
                       
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
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
                                Toast.makeText(CameraActivity.this, "need", Toast.LENGTH_SHORT).show();
                                token.continuePermissionRequest();
                            }
                        }).check();
                     
                       


            }
        });

    }
}