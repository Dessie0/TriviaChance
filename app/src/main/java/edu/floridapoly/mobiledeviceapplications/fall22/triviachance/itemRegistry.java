package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class itemRegistry extends SQLiteOpenHelper {

    final String createTable = "CREATE TABLE IF NOT EXISTS items(itemId INTEGER PRIMARY KEY AUTOINCREMENT, itemName TEXT, imageFilePath TEXT, Description TEXT, Rarity TEXT)";
    Context context;
    SQLiteDatabase itemsRegistry;

    public itemRegistry(Context context, int version) {
        super(context, "items.db", null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTable);
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemOne\", \"This is the first item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemTwo\", \"This is the second item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemThree\", \"This is the third item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemFour\", \"This is the fourth item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemFive\", \"This is the fifth item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemSix\", \"This is the sixth item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemSeven\", \"This is the seventh item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemEight\", \"This is the eigth item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemNine\", \"This is the ninth item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemTen\", \"This is the tenth item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemEleven\", \"This is the eleventh item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemTwelve\", \"This is the twelfth item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemThirteen\", \"This is the thirteenth item.\", \"Rare\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemFourteen\", \"This is the fourteenth item.\", \"Rare\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemFifteen\", \"This is the fifteenth item.\", \"Rare\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"itemSixteen\", \"This is the sixteenth item.\", \"Rare\");"); //Repeat this for every item.
        itemsRegistry = this.getReadableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop Table if exists items");
        onCreate(sqLiteDatabase);
    }


    public String getItemName(int rowId){
        String query = "SELECT itemName FROM items WHERE itemId = " + rowId + ";";
        Cursor cursor = getWritableDatabase().rawQuery(query, null);

        if(cursor!=null) {
            cursor.moveToFirst();
            Log.d("Count",String.valueOf(cursor.getCount()));
            cursor.getCount();
            String result = cursor.getString(0);
            cursor.close();
            return result;
        }


        return "\n\n\nWell shit\n\n\n";

    }

    public String getItemDescription(int rowId){
        String query = "SELECT description FROM items WHERE itemId = " + rowId + ";";
        Cursor cursor = getWritableDatabase().rawQuery(query, null);

        if(cursor!=null) {
            cursor.moveToFirst();
            Log.d("Count",String.valueOf(cursor.getCount()));
            cursor.getCount();

        }
        assert cursor != null;
        String result = cursor.getString(0);
        cursor.close();
        return result;
    }

    public String getRarity(int rowId){
        String query = "SELECT rarity FROM items";
        Cursor cursor = getWritableDatabase().rawQuery(query, null);

        if(cursor!=null) {
            cursor.moveToPosition(rowId);
            Log.d("Count",String.valueOf(cursor.getCount()));
            cursor.getCount();

        }
        assert cursor != null;
        String result = cursor.getString(1);
        cursor.close();
        return result;
    }

    public String getImageFilePath(int rowId) {
        String imageFilePath = null;
        return null;
    }
}
