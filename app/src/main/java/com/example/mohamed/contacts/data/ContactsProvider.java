package com.example.mohamed.contacts.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = ContactsProvider.AUTHORITY, database = ContactsDatabase.class)
public final class ContactsProvider {
    static final String AUTHORITY = "com.example.mohamed.contacts";

    @TableEndpoint(table = ContactsDatabase.TABLE_PERSON)
    public static class Persons {

        @ContentUri(
                path = ContactsContract.PersonEntry.TABLE_NAME,
                type = "vnd.android.cursor.dir/" + ContactsContract.PersonEntry.TABLE_NAME,
                defaultSort = ContactsContract.PersonEntry.COLUMN_NAME + " ASC")
        public static final Uri CONTENT_URI =
                Uri.parse("content://" + AUTHORITY + "/" + ContactsContract.PersonEntry.TABLE_NAME);

    }
}
