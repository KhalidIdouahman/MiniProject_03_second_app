package com.example.specialtasksofminiproject_03;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.specialtasksofminiproject_03.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mainBinding;
    View root;
    String[] permissions;
    ArrayList<Contact> contactList;
    spinnerAdapter spinnerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        root = mainBinding.getRoot();
        setContentView(root);

        permissions = new String[]{ Manifest.permission.READ_CONTACTS , Manifest.permission.SEND_SMS };

        if (hasPermission()) {
            // what i ganna do when i have the permission
            contactList = getContactsList();
            setDataToSpinner(contactList);
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
                    contactList = getContactsList();
                    setDataToSpinner(contactList);
                } else {
                    if (!shouldShowRequestPermissionRationale(permissions[0])) {
                        showAlertDialogue("Request for permissions" ,
                                "The app won't work properly without these permissions . \nGo to settings !!" ,
                                null , null, null , null);
                    }
                    else {
                        showAlertDialogue("Request for permissions" ,
                                "The app won't work properly without these permissions !!" ,
                                null , null, null , null);
                    }
                }
    });

    private ArrayList<Contact> getContactsList() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri , null , null ,  null , null);
        Log.e("contacts nums ", "getContactsList: " + cursor.getCount());

        ArrayList<Contact> contacts = new ArrayList<>();
        while (cursor.moveToNext()) {
            contacts.add(new Contact(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))));
        }
        return contacts;
    }

    private void setDataToSpinner(ArrayList<Contact> contacts) {
        spinnerAdapter = new spinnerAdapter(contacts);
        mainBinding.contactSpinner.setAdapter(spinnerAdapter);
    }

    private void showAlertDialogue(String title , String message , String positiveTitle , AlertDialog.OnClickListener positiveClick
            , String negativeTitle , AlertDialog.OnClickListener negativeClick ) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title).setMessage(message).setPositiveButton(positiveTitle , positiveClick)
                .setNegativeButton(negativeTitle , negativeClick);

        alert.show();
    }
}