package com.example.mohamed.contacts.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mohamed.contacts.R;
import com.example.mohamed.contacts.data.ContactsContract;
import com.example.mohamed.contacts.data.ContactsProvider;
import com.example.mohamed.contacts.sync.ContactsIntentService;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_ID = 201 ;
    private EditText mNameEditText;
    private EditText mPhoneNumberEditText;
    private int mId;
    private Toast mToast;

    private boolean mPetHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mNameEditText = findViewById(R.id.et_name);
        mPhoneNumberEditText = findViewById(R.id.et_phone_number);

        mNameEditText.setOnTouchListener(mTouchListener);
        mPhoneNumberEditText.setOnTouchListener(mTouchListener);

        Intent intent = getIntent();
        if (intent == null) throw new NullPointerException("intent not found!");

        mId = intent.getIntExtra(Intent.EXTRA_TEXT, -1);

        if (mId == -1) {
            setTitle("Add");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit");
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mId == -1) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete: {
                deleteItem();
                return true;
            }
            case R.id.action_add: {
                if (mId == -1) {
                    addItem();

                } else {
                    updateItem();
                }
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteItem() {

        Intent intent = new Intent(this, ContactsIntentService.class);
        intent.setAction(ContactsIntentService.ACTION_DELETE_ITEM);

        intent.putExtra("id", mId);
        startService(intent);

        mNameEditText.getText().clear();
        mPhoneNumberEditText.getText().clear();
        finish();
    }

    private void updateItem() {
        if (TextUtils.isEmpty(mNameEditText.getText()) ||
                TextUtils.isEmpty(mPhoneNumberEditText.getText())) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "name or phone number is empty!", Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("name", mNameEditText.getText().toString());
        bundle.putString("phoneNumber", mPhoneNumberEditText.getText().toString());
        bundle.putInt("id", mId);

        Intent intent = new Intent(this, ContactsIntentService.class);
        intent.setAction(ContactsIntentService.ACTION_UPDATE_ITEM);

        intent.putExtras(bundle);
        startService(intent);

        mNameEditText.getText().clear();
        mPhoneNumberEditText.getText().clear();

        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this, "contact updated successfully", Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void cancel() {
        if (!mPetHasChanged) {
            mNameEditText.getText().clear();
            mPhoneNumberEditText.getText().clear();

            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "canceled", Toast.LENGTH_SHORT);
            mToast.show();
            finish();
        } else {
            DialogInterface.OnClickListener discardButtonClickListener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // User clicked "Discard" button, navigate to parent activity.
                            finish();
                        }
                    };

            // Show a dialog that notifies the user they have unsaved changes
            showUnsavedChangesDialog(discardButtonClickListener);
        }

    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("unsaved");
        builder.setPositiveButton("discard", discardButtonClickListener);
        builder.setNegativeButton("keep editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private void addItem() {
        if (TextUtils.isEmpty(mNameEditText.getText()) ||
                TextUtils.isEmpty(mPhoneNumberEditText.getText())) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "name or phone number is empty!", Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("name", mNameEditText.getText().toString());
        bundle.putString("phoneNumber", mPhoneNumberEditText.getText().toString());

        Intent intent = new Intent(this, ContactsIntentService.class);
        intent.setAction(ContactsIntentService.ACTION_INSERT_ITEM);

        intent.putExtras(bundle);
        startService(intent);

        mNameEditText.getText().clear();
        mPhoneNumberEditText.getText().clear();

        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this, "contact added successfully", Toast.LENGTH_SHORT);
        mToast.show();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                ContactsProvider.Persons.CONTENT_URI,
                null,
                ContactsContract.PersonEntry._ID + "=?",
                new String[] {String.valueOf(mId)},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) return;

        mNameEditText.setText(data.getString
                (data.getColumnIndexOrThrow(ContactsContract.PersonEntry.COLUMN_NAME)));
        mPhoneNumberEditText.setText(data.getString
                (data.getColumnIndexOrThrow(ContactsContract.PersonEntry.COLUMN_PHONE_NUMBER)));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
