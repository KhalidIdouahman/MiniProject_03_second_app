package com.example.specialtasksofminiproject_03;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class spinnerAdapter extends BaseAdapter {

    ArrayList<Contact> contactsList;

    public spinnerAdapter(ArrayList<Contact> contactsList) {
        this.contactsList = contactsList;
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = contactsList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_contact_spinner , parent , false);
        }
        TextView contactName = convertView.findViewById(R.id.contact_name);
        TextView contactNum = convertView.findViewById(R.id.contact_num);

        contactName.setText(contact.getName());
        contactNum.setText(contact.getNumber());

        return convertView;
    }
}
