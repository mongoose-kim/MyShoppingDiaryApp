package ddwu.mobile.finalproject.ma02_20160214;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class MyListDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    final static int PERMISSION_REQ_CODE = 100;
    private GoogleMap mGoogleMap;
    private MarkerOptions markerOptions;
    private PlacesClient placesClient;

    EditText et_memo;
    ItemDBHelper helper;
    ItemDto item;

    String target;
    TextView tv_map;
    TextView title;
    TextView productId;
    TextView category1;
    TextView category2;
    TextView brand;
    TextView lprice;
    TextView hprice;
    ImageView ivImage;
    ImageView imageViewdd;

    ImageFileManager imgFileManager;

    private AddressResultReceiver addressResultReceiver;
    String addressOutput = null;
    Double mlatitude;
    Double mlongitude;
    ProgressDialog progressDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylist_detail);
        helper = new ItemDBHelper(this);
        imgFileManager = new ImageFileManager(this);
        addressResultReceiver = new AddressResultReceiver(new Handler());
        mlatitude = Double.valueOf(getResources().getString(R.string.init_lat));
        mlongitude = Double.valueOf(getResources().getString(R.string.init_lng));

        et_memo = findViewById(R.id.et_memo);

        item = (ItemDto) getIntent().getSerializableExtra("item");
        tv_map = findViewById(R.id.tv_map);
        title = findViewById(R.id.tv_title);
        productId = findViewById(R.id.tv_productId);
        category1 = findViewById(R.id.tv_category1);
        category2 = findViewById(R.id.tv_category2);
        brand = findViewById(R.id.tv_brand);
        lprice = findViewById(R.id.tv_lprice);
        hprice = findViewById(R.id.tv_hprice);
        ivImage = findViewById(R.id.ivImage);
        imageViewdd = findViewById(R.id.imageViewdd);
        imageViewdd.setImageResource(R.mipmap.ddbar);

        target = item.getBrand();
        title.setText(item.getTitle());
        productId.setText("???????????? : " + item.getProductId());
        category1.setText(item.getCategory1() + " / ");
        category2.setText(item.getCategory2());
        brand.setText(item.getBrand());
        lprice.setText("????????? : " + item.getLprice());
        hprice.setText("????????? : " + item.getHprice());
        et_memo.setText(item.getMemo());

        Bitmap bitmap = imgFileManager.getBitmapFromTemporary(item.getImageLink());
        if(bitmap != null){
            ivImage.setImageBitmap(bitmap);
        }

        if(target != null) {
            String s = "???????????? ????????? " + target + "?????????";
            tv_map.setText(s);
            mapLoad();

            Places.initialize(getApplicationContext(), getString(R.string.api_key));
            placesClient = Places.createClient(this);

            allSearch();

        }
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnldetailUpdate:
//			DB ????????? ?????? ?????? ??????
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues row = new ContentValues();
                row.put(helper.COL_MEMO, et_memo.getText().toString());

                String whereClause = "_id=?";
                String[] whereArgs = new String[] { String.valueOf(item.get_id()) };
                db.update(helper.TABLE_NAME, row, whereClause, whereArgs);
                helper.close();

                Toast.makeText(this, "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.btnldetailClose:
//			DB ????????? ?????? ?????? ??????
                Toast.makeText(this, "??????", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }



    /*????????? ????????? ?????? ????????? ??????*/
    private void searchStart(String type) {
        new NRPlaces.Builder().listener(placesListener)
                .key(getResources().getString(R.string.api_key))
                .latlng(mlatitude,
                        mlongitude)
                .radius(300)
                .type(type)
                .build()
                .execute();
    }

    private void allSearch(){
        searchStart(PlaceType.SHOPPING_MALL);
        searchStart(PlaceType.STORE);
        searchStart(PlaceType.CLOTHING_STORE);
        searchStart(PlaceType.JEWELRY_STORE);
        searchStart(PlaceType.SHOE_STORE);
    }


    PlacesListener placesListener = new PlacesListener() {
        @Override
        public void onPlacesFailure(PlacesException e) {

        }

        @Override
        public void onPlacesStart() {

        }

        @Override
        public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {//????????? ??????????????? ?????? ?????????????????? final??????
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    for(noman.googleplaces.Place place : places){

                        if(place.getName().contains(item.getBrand()) == true) {
                            Toast.makeText(MyListDetailActivity.this, place.getName() + "(???)??? ????????? ????????????." + item.getBrand(), Toast.LENGTH_SHORT).show();
                            markerOptions.title(place.getName());
                            markerOptions.position(new LatLng(place.getLatitude(), place.getLongitude()));
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                            Marker newMarker = mGoogleMap.addMarker(markerOptions);
                        }
                    }
                    //Log.d("??????:", "?????????");
                }
            });

        }

        @Override
        public void onPlacesFinished() {

        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        markerOptions = new MarkerOptions();

        if(checkPermission()) {
            mGoogleMap.setMyLocationEnabled(true);
        }

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                //Toast.makeText(MyListDetailActivity.this, "?????? ??????", Toast.LENGTH_SHORT).show();
                Location loc = googleMap.getMyLocation();
                mlatitude = loc.getLatitude();
                mlongitude = loc.getLongitude();
                allSearch();
                progressDlg = ProgressDialog.show(MyListDetailActivity.this, "?????? ????????? ?????? ???", "Loading...");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                progressDlg.dismiss();

                            }
                        };

                        Timer timer = new Timer();
                        timer.schedule(task, 4000);
                    }
                });
                thread.start();
                return false;
            }

        });

        mGoogleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                Toast.makeText(MyListDetailActivity.this,
                        String.format("?????? : %f, ?????? : %f", location.getLatitude(), location.getLongitude()),
                        Toast.LENGTH_SHORT).show();

            }
        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                startAddressService(marker.getPosition().latitude, marker.getPosition().longitude);
                Toast.makeText(MyListDetailActivity.this, addressOutput, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*???????????? ??????????????? ??????*/
    private void mapLoad() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);      // ???????????? this: MainActivity ??? OnMapReadyCallback ??? ???????????????
    }

    private void startAddressService(double lat, double lon) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, addressResultReceiver);
        intent.putExtra(Constants.LAT_DATA_EXTRA, lat);
        intent.putExtra(Constants.LNG_DATA_EXTRA, lon);
        startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {


            if (resultCode == Constants.SUCCESS_RESULT) {
                if (resultData == null) return;
                addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                if (addressOutput == null) addressOutput = "";
            }
        }
    }


    /* ?????? permission ?????? */
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQ_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // ???????????? ??????????????? ?????? ??? ?????? ??????
                mapLoad();
            } else {
                // ????????? ????????? ??? ???????????? ??????
                Toast.makeText(this, "??? ????????? ?????? ?????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }



}
