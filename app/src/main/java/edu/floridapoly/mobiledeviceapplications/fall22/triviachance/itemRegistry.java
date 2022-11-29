package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class itemRegistry extends SQLiteOpenHelper {

    final String createTable = "CREATE TABLE IF NOT EXISTS items(itemId INTEGER PRIMARY KEY AUTOINCREMENT, itemName TEXT, description TEXT, rarity TEXT)";
    Context context;

    public itemRegistry(Context context, int version) {
        super(context, "items.db", null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTable);
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"Light Theme\", \"This is the first item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"Gold Theme\", \"This is the second item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"Green Theme\", \"This is the third item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"Red Theme\", \"This is the fourth item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"Bamboo Theme\", \"This is the fifth item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"Sugar Theme\", \"This is the sixth item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"Purple Theme\", \"This is the seventh item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"Unknown Theme 8\", \"This is the eigth item.\", \"Common\");"); //Repeat this for every item.
        sqLiteDatabase.execSQL("INSERT into items (itemName, description, rarity) VALUES (\"Unknown Theme 9\", \"This is the ninth item.\", \"Common\");"); //Repeat this for every item.
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop Table if exists items");
        onCreate(sqLiteDatabase);
    }


    public String getItemName(int rowId){
        String query = "SELECT itemName FROM items WHERE itemId = " + rowId + ";";
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        System.out.println(cursor.moveToFirst());
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
        Cursor cursor = getReadableDatabase().rawQuery(query, null);

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
}
