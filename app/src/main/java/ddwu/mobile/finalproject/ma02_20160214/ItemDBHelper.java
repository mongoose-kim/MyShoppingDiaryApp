package ddwu.mobile.finalproject.ma02_20160214;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "item_db";
    public final static String TABLE_NAME = "item_table";
    public final static String COL_ID = "_id";
    public final static String COL_TITLE = "title";
    public final static String COL_IMAGELINK = "imageLink";
    public final static String COL_LPRICE = "lprice";
    public final static String COL_HPRICE = "hprice";
    public final static String COL_BRAND = "brand";
    public final static String COL_PRODUCTID = "productId";
    public final static String COL_CATEGORY1 = "category1";
    public final static String COL_CATEGORY2 = "category2";
    public final static String COL_MEMO = "memo";


    public ItemDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement,"
                + COL_TITLE + " TEXT, " + COL_IMAGELINK + " TEXT, " + COL_LPRICE + " TEXT, " + COL_HPRICE
                + " TEXT, " + COL_BRAND + " TEXT, " + COL_PRODUCTID + " TEXT, " + COL_CATEGORY1
                + " TEXT, " + COL_CATEGORY2 + " TEXT, " + COL_MEMO + " TEXT);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
    }
}
