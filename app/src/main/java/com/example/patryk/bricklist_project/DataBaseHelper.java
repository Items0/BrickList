package com.example.patryk.bricklist_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kotlin.Pair;

public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.example.patryk.bricklist_project/databases/";

    private static String DB_NAME = "BrickListDB.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
            Log.d("DB", "DB Exists");
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");
            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Pair<String, String>> loadInventories() {
        ArrayList<Pair<String, String>> myList = new ArrayList<Pair<String, String>>();
        Cursor mCursor = myDataBase.rawQuery("SELECT _id, Name FROM Inventories ", null);

        if (mCursor.moveToFirst()) {
            int indexID = mCursor.getColumnIndex("_id");
            int indexName = mCursor.getColumnIndex("Name");
            while ( !mCursor.isAfterLast() ) {
                myList.add(new Pair(mCursor.getString(indexID),  mCursor.getString(indexName)));
                mCursor.moveToNext();
            }
        }
        return myList;
    }

    public ArrayList<Pair<String, String>> loadInventoriesParts (String InventoryID) {
        ArrayList<Pair<String, String>> myList = new ArrayList<Pair<String, String>>();
        Cursor mCursor = myDataBase.rawQuery("SELECT * FROM InventoriesParts where InventoryID=?", new String[]{InventoryID});

        if (mCursor.moveToFirst()) {
            int indexID = mCursor.getColumnIndex("ItemID");
            //int indexName = mCursor.getColumnIndex("Name");
            int indexQuantityInSet = mCursor.getColumnIndex("QuantityInSet");
            int indexQuantityInStore = mCursor.getColumnIndex("QuantityInStore");
            while ( !mCursor.isAfterLast() ) {
                myList.add(new Pair(mCursor.getString(indexID),  mCursor.getString(indexQuantityInSet)));
                mCursor.moveToNext();
            }
        }
        Log.e("Length:", String.valueOf(myList.size()));
        return myList;
    }

    public void insertInventory(List<Brick> brickList, String inventoryID, String name) {

        ContentValues insertValues = new ContentValues();
        insertValues.put("_id", inventoryID);
        insertValues.put("Name", name);
        insertValues.put("Active", "1");
        insertValues.put("LastAccessed", new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));

        myDataBase.insert("Inventories", null, insertValues);
        //Log.e("before for", "size =" +  brickList.size());
        insertValues.clear();
        for (Brick el : brickList) {
            //insertValues.put("_id", null);
            insertValues.put("InventoryID", inventoryID);
            insertValues.put("TypeID", el.getItemType());
            insertValues.put("ItemID", el.getItemID());
            insertValues.put("QuantityInSet", el.getQty());
            insertValues.put("QuantityInStore", "0");
            insertValues.put("ColorID", el.getColor());
            insertValues.put("Extra", el.getExtra());

            myDataBase.insert("InventoriesParts", null, insertValues);
            //Log.e("Insert", "Insert to the table");
        }
    }
    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
    public void clearDB() {
        //myDataBase.de
        myDataBase.delete("Inventories", "_id <> '615'", null);
        myDataBase.delete("InventoriesParts", "InventoryID <> '615'", null);

        //myDataBase.rawQuery("DELETE FROM Inventories WHERE _id != '615'", null).moveToFirst();
        //myDataBase.rawQuery("DELETE FROM InventoriesParts WHERE InventoryID <> '615'", null);
        Log.e("delete", "deleteOK");
    }
}