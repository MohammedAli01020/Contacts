package com.example.mohamed.contacts.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = ContactsDatabase.VERSION)
final class ContactsDatabase {

    static final int VERSION = 1;

    @Table(ContactsContract.PersonEntry.class)
    static final String TABLE_PERSON = ContactsContract.PersonEntry.TABLE_NAME;

}
