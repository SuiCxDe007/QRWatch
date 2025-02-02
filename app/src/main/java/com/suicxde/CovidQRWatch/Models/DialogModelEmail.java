package com.suicxde.CovidQRWatch.Models;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.suicxde.CovidQRWatch.R;


public class DialogModelEmail extends AppCompatDialogFragment {
    private EditText email1;
    private dlglistner2 listneremail;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialogemail,null);
        builder.setView(view).setIcon(R.drawable.resetemailsent).setTitle(R.string.email).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String fireupdateemail = email1.getText().toString();
                if(fireupdateemail.isEmpty()){

                }else {
                    listneremail.infosz(fireupdateemail);
                }
            }
        });

        email1 = view.findViewById(R.id.fireupemail);
        return builder.create();

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listneremail = (dlglistner2) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +"mustimplement");
        }
    }

    public interface dlglistner2 {
        void infosz(String email1);

    }




}