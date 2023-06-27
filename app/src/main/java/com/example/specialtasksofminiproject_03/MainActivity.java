package com.example.specialtasksofminiproject_03;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String[] permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissions = new String[]{ Manifest.permission.READ_CONTACTS , Manifest.permission.SEND_SMS };

        if (hasPermission()) {
            // what i ganna do when i have the permission
            Toast.makeText(this, "permissions granted !!", Toast.LENGTH_SHORT).show();
        } else {
            if (shouldShowRequestPermissionRationale(permissions[0]) || shouldShowRequestPermissionRationale(permissions[1])) {
                showAlertDialogue("Request for permissions" , "this app need them \n do you want to grant them ?" ,
                        "Ok", ((dialog, which) -> {
                    requestMultiPermissionsLauncher.launch(permissions);
                }) , "Cancel" , null);
            } else  {
                Toast.makeText(this, "else of show rational permission", Toast.LENGTH_SHORT).show();
                requestMultiPermissionsLauncher.launch(permissions);
            }
        }
    }

    private boolean hasPermission() {
        return checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(permissions[1]) == PackageManager.PERMISSION_GRANTED;
    }

    private ActivityResultLauncher<String[] > requestMultiPermissionsLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), (stringBooleanMap) -> {

                boolean isAllGranted = true;
                for (String key : stringBooleanMap.keySet()) {
                   isAllGranted = isAllGranted && stringBooleanMap.get(key);
                }

                if (isAllGranted) {
                    // use the permission that you ask
                    Log.e("TAG", "granted !" );
                } else {
                    if (!shouldShowRequestPermissionRationale(permissions[0])) {
                        showAlertDialogue("Request for permissions" ,
                                "The app won't work properly without these permissions . \nGo to settings !!" ,
                                null , null, null , null);
                        Log.e("Activity launch result", "not granted 1 !" );
                    }
                    else {
                        showAlertDialogue("Request for permissions" ,
                                "The app won't work properly without these permissions !!" ,
                                null , null, null , null);
                        Log.e("Activity launch result", "not granted 2 !" );
                    }
                }
    });

    private void showAlertDialogue(String title , String message , String positiveTitle , AlertDialog.OnClickListener positiveClick
            , String negativeTitle , AlertDialog.OnClickListener negativeClick ) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title).setMessage(message).setPositiveButton(positiveTitle , positiveClick)
                .setNegativeButton(negativeTitle , negativeClick);

        alert.show();
    }
}