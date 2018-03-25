package com.example.mohamed.contacts.sync;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.mohamed.contacts.data.ContactsContract;
import com.example.mohamed.contacts.data.ContactsProvider;

public class ContactsIntentService extends IntentService {

    public static final String ACTION_INSERT_DUMMY_DATA = "action-insertDummyData";
    public static final String ACTION_INSERT_ITEM = "action-insertItem";
    public static final String ACTION_DELETE_ALL_DATA = "action-deleteAllData";
    public static final String ACTION_DELETE_ITEM = "action-deleteItem";
    public static final String ACTION_UPDATE_ITEM = "action-updateItem";


    public ContactsIntentService() {
        super("ContactsIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        String action = intent.getAction();

        assert action != null;
        switch (action) {
            case ACTION_INSERT_DUMMY_DATA: {
                insertDummyData();
                break;
            }

            case ACTION_DELETE_ALL_DATA: {
                deleteAllData();
                break;
            }

            case ACTION_DELETE_ITEM: {
                int id = intent.getIntExtra("id", 0);
                deleteItemById(id);
                break;
            }

            case ACTION_INSERT_ITEM: {
                Bundle bundle = intent.getExtras();
                insertItem(bundle);
                break;
            }

            case ACTION_UPDATE_ITEM: {
                Bundle bundle = intent.getExtras();
                updateItem(bundle);
                break;
            }

        }

    }

    synchronized private void updateItem(Bundle bundle) {
        String name = bundle.getString("name");
        String phoneNumber = bundle.getString("phoneNumber");
        int id = bundle.getInt("id");

        ContentValues values = new ContentValues();
        values.put(ContactsContract.PersonEntry.COLUMN_NAME, name);
        values.put(ContactsContract.PersonEntry.COLUMN_PHONE_NUMBER, phoneNumber);

        getContentResolver().update(ContactsProvider.Persons.CONTENT_URI, values,
                ContactsContract.PersonEntry._ID + "=?",
                new String[] {String.valueOf(id)});

    }

    synchronized private void insertItem(Bundle bundle) {
        String name = bundle.getString("name");
        String phoneNumber = bundle.getString("phoneNumber");

        ContentValues values = new ContentValues();
        values.put(ContactsContract.PersonEntry.COLUMN_NAME, name);
        values.put(ContactsContract.PersonEntry.COLUMN_PHONE_NUMBER, phoneNumber);

        getContentResolver().insert(
                ContactsProvider.Persons.CONTENT_URI,
                values);
    }


    synchronized private void deleteItemById(int id) {
        getContentResolver().delete(ContactsProvider.Persons.CONTENT_URI,
                ContactsContract.PersonEntry._ID + "=?",
                new String[] {String.valueOf(id)});
    }

    synchronized private void insertDummyData() {

        try {
            ContentValues[] values = ContactsUtils.getFakeData();
            if (values != null && values.length != 0) {
                getContentResolver().bulkInsert(
                        ContactsProvider.Persons.CONTENT_URI,
                        values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    synchronized private void deleteAllData() {
        getContentResolver().delete(
                ContactsProvider.Persons.CONTENT_URI,
                null,
                null);
    }
}
