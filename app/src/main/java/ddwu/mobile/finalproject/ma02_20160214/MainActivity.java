package ddwu.mobile.finalproject.ma02_20160214;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvList;
    String apiAddress;

    EditText et_search;
    String query;
    ItemDto item;

    MyItemAdapter adapter;
    ArrayList<ItemDto> resultList;
    ItemXmlParser parser;
    ItemNetworkManager networkManager;
    ImageFileManager imgFileManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvList = findViewById(R.id.lvList);

        et_search = findViewById(R.id.et_search);

        resultList = new ArrayList();
        adapter = new MyItemAdapter(this, R.layout.listview_item, resultList);
        lvList.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.api_url);
        parser = new ItemXmlParser();
        networkManager = new ItemNetworkManager(this);
        networkManager.setClientId(getResources().getString(R.string.client_id));
        networkManager.setClientSecret(getResources().getString(R.string.client_secret));
        imgFileManager = new ImageFileManager(this);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = resultList.get(i);
                Intent intentDetail = new Intent(MainActivity.this, ShoppingActivity.class);
                intentDetail.putExtra("item", item);
                startActivity(intentDetail);
            }
        });

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_search:
                query = et_search.getText().toString();
                try {
                    new MainActivity.NetworkAsyncTask().execute(apiAddress + URLEncoder.encode(query, "UTF-8"));//??????
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.mylist:
                Intent intent = new Intent(this, MyListActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("????????????")
                        .setMessage("?????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("??????", null)
                        .setCancelable(false)
                        .show();
                break;
        }
        return true;
    }


    class NetworkAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(MainActivity.this, "Wait", "Loading...");
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

            /* ???????????? ?????????????????? ??? ?????????????????? ?????? ??? ????????????. ????????? ????????? ???????????? ?????? ???????????? ??? ??????????????? ??????.
            for(NaverBookDto dto : resultList){
                Bitmap bitmap = networkManager.downloadImage(dto.getImageLink());
                if(bitmap != null)
                    imgFileManager.saveBitmapToTemporary(bitmap, dto.getImageLink());
            }
            */

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            adapter.setList(resultList);    // Adapter ??? ?????? List ??? ?????? ??? notify
            progressDlg.dismiss();
        }

    }

}