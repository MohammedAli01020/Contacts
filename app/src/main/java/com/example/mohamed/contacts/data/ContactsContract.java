package com.example.mohamed.contacts.data;

import android.provider.BaseColumns;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public final class ContactsContract {

    public static final class PersonEntry implements BaseColumns {
        static final String TABLE_NAME = "person";

        @DataType(INTEGER)
        @PrimaryKey
        @AutoIncrement
        public static final String _ID = BaseColumns._ID;
        @DataType(TEXT)
        @NotNull
        public static final String COLUMN_NAME = "name";
        @DataType(TEXT)
        @NotNull
        public static final String COLUMN_PHONE_NUMBER = "phone";
    }
}
