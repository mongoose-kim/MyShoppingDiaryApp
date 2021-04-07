package ddwu.mobile.finalproject.ma02_20160214;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ShoppingActivity extends AppCompatActivity {

    ListView bList;
    String apiAddress;

    TextView title;
    TextView productId;
    TextView category1;
    TextView category2;
    TextView brand;
    TextView lprice;
    TextView hprice;
    ImageView ivImage;

    ItemDto item;
    BlogAdapter adapter;
    ArrayList<BlogDto> resultList;
    BlogXmlParser parser;
    ItemNetworkManager networkManager;
    ImageFileManager imgFileManager;
    ItemDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_detail);
        helper = new ItemDBHelper(this);
        imgFileManager = new ImageFileManager(this);
        bList = findViewById(R.id.bList);

        resultList = new ArrayList();
        adapter = new BlogAdapter(this, R.layout.listview_blog, resultList);
        bList.setAdapter(adapter);

        item = (ItemDto) getIntent().getSerializableExtra("item");

        title = findViewById(R.id.tv_title);
        productId = findViewById(R.id.tv_productId);
        category1 = findViewById(R.id.tv_category1);
        category2 = findViewById(R.id.tv_category2);
        brand = findViewById(R.id.tv_brand);
        lprice = findViewById(R.id.tv_lprice);
        hprice = findViewById(R.id.tv_hprice);
        ivImage = findViewById(R.id.ivImage);

        title.setText(item.getTitle());
        productId.setText("상품번호 : " + item.getProductId());
        category1.setText(item.getCategory1() + " / ");
        category2.setText(item.getCategory2());
        brand.setText("브랜드 : " + item.getBrand());
        lprice.setText("최저가 : " + item.getLprice());
        hprice.setText("최고가 : " + item.getHprice());

        //Toast.makeText(this, item.getImgFileName() + item.getImageLink(), Toast.LENGTH_SHORT).show();
        Bitmap bitmap = imgFileManager.getBitmapFromTemporary(item.getImageLink());
        if(bitmap != null){
            ivImage.setImageBitmap(bitmap);
        }

        apiAddress = getResources().getString(R.string.api_burl);
        parser = new BlogXmlParser();
        networkManager = new ItemNetworkManager(this);
        networkManager.setClientId(getResources().getString(R.string.client_id));
        networkManager.setClientSecret(getResources().getString(R.string.client_secret));

        try {
            new ShoppingActivity.NetworkAsyncTask().execute(apiAddress + URLEncoder.encode(item.getTitle(), "UTF-8"));//한글
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    class NetworkAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(ShoppingActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String result = null;
            // networking
            result = networkManager.downloadContents(address);
            if(result == null)
                return "Error!";
            // parsing
            resultList = parser.parse(result);

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            adapter.setList(resultList);    // Adapter 에 결과 List 를 설정 후 notify
            progressDlg.dismiss();
        }

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_add:
                AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingActivity.this);
                builder.setTitle("쇼핑리스트담기");
                builder.setMessage("내 쇼핑리스트에 담으시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues value = new ContentValues();

                        value.put(helper.COL_TITLE, item.getTitle());
                        value.put(helper.COL_IMAGELINK, item.getImageLink());
                        value.put(helper.COL_LPRICE, item.getLprice());
                        value.put(helper.COL_HPRICE, item.getHprice());
                        value.put(helper.COL_BRAND, item.getBrand());
                        value.put(helper.COL_PRODUCTID, item.getProductId());
                        value.put(helper.COL_CATEGORY1, item.getCategory1());
                        value.put(helper.COL_CATEGORY2, item.getCategory2());
                        value.put(helper.COL_MEMO, "");

                        db.insert(helper.TABLE_NAME, null, value);
                        helper.close();
                        Toast.makeText(ShoppingActivity.this, "새아이템 추가 완료", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.setCancelable(false);
                builder.show();

                break;
        }
    }

}
