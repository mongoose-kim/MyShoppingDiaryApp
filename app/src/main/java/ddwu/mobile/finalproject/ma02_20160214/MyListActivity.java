package ddwu.mobile.finalproject.ma02_20160214;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MyListActivity extends AppCompatActivity {

    ListView lv;
    MyListAdapter adapter;
    ItemDBHelper helper;
    Cursor cursor;
    ItemDto item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        lv = (ListView)findViewById(R.id.lvList);

        helper = new ItemDBHelper(this);

        adapter = new MyListAdapter(this, R.layout.listview_item, null);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                //Toast.makeText(MyListActivity.this, "id는 " + id + ", " + l, Toast.LENGTH_LONG).show();
                SQLiteDatabase db = helper.getReadableDatabase();

                Cursor cursor = db.rawQuery("SELECT title, imageLink, lprice, hprice, brand, productId, category1, category2, memo FROM "
                        + helper.TABLE_NAME + " WHERE _id = "
                        + id + ";", null);

                while(cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndex(helper.COL_TITLE));
                    String imageLink = cursor.getString(cursor.getColumnIndex(helper.COL_IMAGELINK));
                    String lprice = cursor.getString(cursor.getColumnIndex(helper.COL_LPRICE));
                    String hprice = cursor.getString(cursor.getColumnIndex(helper.COL_HPRICE));
                    String brand = cursor.getString(cursor.getColumnIndex(helper.COL_BRAND));
                    String productId = cursor.getString(cursor.getColumnIndex(helper.COL_PRODUCTID));
                    String category1 = cursor.getString(cursor.getColumnIndex(helper.COL_CATEGORY1));
                    String category2 = cursor.getString(cursor.getColumnIndex(helper.COL_CATEGORY2));
                    String memo = cursor.getString(cursor.getColumnIndex(helper.COL_MEMO));
                    item = new ItemDto((int)id, title, imageLink, lprice, hprice, brand, productId, category1, category2, memo);
                }
                Intent intentDetail = new Intent(MyListActivity.this, MyListDetailActivity.class);
                intentDetail.putExtra("item", item);
                startActivityForResult(intentDetail, RESULT_OK);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyListActivity.this);
                builder.setTitle("삭제확인");
                builder.setMessage("삭제하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        db.execSQL("delete from " + helper.TABLE_NAME + " where _id = " + id + ";");
                        Toast.makeText(MyListActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.setCancelable(false);
                builder.show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + ItemDBHelper.TABLE_NAME, null);
        adapter.changeCursor(cursor);
        helper.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        cursor 사용 종료
        if (cursor != null) cursor.close();
    }
}
