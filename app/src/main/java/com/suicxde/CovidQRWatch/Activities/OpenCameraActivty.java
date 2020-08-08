package com.suicxde.CovidQRWatch.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.suicxde.CovidQRWatch.R;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class OpenCameraActivty extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private FirebaseFirestore mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_camera_activty);

        ScannerView = new ZXingScannerView(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDb = FirebaseFirestore.getInstance();
        Dexter.withContext(OpenCameraActivty.this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(OpenCameraActivty.this, "dead", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(),OpenCameraActivty.class));

                }

                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                        Toast.makeText(OpenCameraActivty.this, "denied", Toast.LENGTH_SHORT).show();
                    }

                    private void showSettingsDialog() {

                        AlertDialog.Builder builder = new AlertDialog.Builder(OpenCameraActivty.this);
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
                        Toast.makeText(OpenCameraActivty.this, "need", Toast.LENGTH_SHORT).show();
                        token.continuePermissionRequest();
                    }
                }).check();

        setContentView(ScannerView);


    }

    @Override
    public void handleResult(final Result result) {
      //  CameraActivity.cameratex.setText(result.getText());
      //  onBackPressed();

        mDb.collection("Users")
                .whereEqualTo("email", currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //What to be added to Scans
                                String nameuserx = document.getString("user_id");


                                Map<String, Object> city = new HashMap<>();
                                city.put("id", nameuserx);
                               // city.put("tpno", usertp);
                                city.put("location",result.getText());
                                city.put("timestamp", FieldValue.serverTimestamp());


                                mDb.collection(result.getText()).add(city).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(OpenCameraActivty.this, "ggez", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(OpenCameraActivty.this, "FAUED Ez", Toast.LENGTH_SHORT).show();
                                            }
                                        });



                            }
                        } else {

                        }
                    }
                });

        Intent intent = new Intent(getBaseContext(), CameraActivity.class);
        intent.putExtra("EXTRA_SESSION_ID", result.getText());
        startActivity(intent);


    }

    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }
}