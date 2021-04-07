package ddwu.mobile.finalproject.ma02_20160214;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyItemAdapter extends BaseAdapter {
    /* 검색한 패션잡화 상품이 보여지는 어댑터 */
    public static final String TAG = "MyItemAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<ItemDto> list;
    private ItemNetworkManager networkManager = null;
    private ImageFileManager imageFileManager = null;
    ItemDto item;


    public MyItemAdapter(Context context, int resource, ArrayList<ItemDto> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        imageFileManager = new ImageFileManager(context);
        networkManager = new ItemNetworkManager(context);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public ItemDto getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView with position : " + position);
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = view.findViewById(R.id.tvTitle);
            viewHolder.tvMallName = view.findViewById(R.id.tvMallName);
            viewHolder.tvLprice = view.findViewById(R.id.tvLprice);
            viewHolder.tvHprice = view.findViewById(R.id.tvHprice);
            viewHolder.ivImage = view.findViewById(R.id.ivImage);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        ItemDto dto = list.get(position);

        viewHolder.tvTitle.setText(dto.getTitle());
        viewHolder.tvMallName.setText(dto.getBrand());
        viewHolder.tvLprice.setText(dto.getLprice());
        viewHolder.tvHprice.setText(dto.getHprice());

        /*작성할 부분*/
//         dto의 이미지 주소에 해당하는 이미지 파일이 내부저장소에 있는지 확인
//         ImageFileManager 의 내부저장소에서 이미지 파일 읽어오기 사용
//         이미지 파일이 있을 경우 bitmap, 없을 경우 null 을 반환하므로 bitmap 이 있으면 이미지뷰에 지정
//         없을 경우 GetImageAsyncTask 를 사용하여 이미지 파일 다운로드 수행


        // 파일에 있는지 확인
        if(dto.getImageLink() == null){
            viewHolder.ivImage.setImageResource(R.mipmap.ic_launcher);
            return view;
        }

        // dto 의 이미지 주소 정보로 이미지 파일 읽기
        Bitmap savedBitmap = imageFileManager.getBitmapFromTemporary(dto.getImageLink());

        if(savedBitmap != null){
            viewHolder.ivImage.setImageBitmap(savedBitmap);
        }
        else{
            item = dto;
            viewHolder.ivImage.setImageResource(R.mipmap.ic_launcher);
            new GetImageAsyncTask(viewHolder).execute(dto.getImageLink());//보여지면서 바로바로 저장하느라 리스트에서 읽어들이는데 시간 걸림.
        }
        return view;
    }


    public void setList(ArrayList<ItemDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //    ※ findViewById() 호출 감소를 위해 필수로 사용할 것
    static class ViewHolder {
        public TextView tvTitle = null;
        public TextView tvMallName = null;
        public TextView tvLprice = null;
        public TextView tvHprice = null;
        public ImageView ivImage = null;
    }


    /* 책 이미지를 다운로드 후 내부저장소에 파일로 저장하고 이미지뷰에 표시하는 AsyncTask */
    // 1. 네트워크에서 이미지 다운
    // 2. 뷰홀더에 이미지 설정
    // 3. 이미지 파일 저장
    class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        ViewHolder viewHolder;
        String imageAddress;

        public GetImageAsyncTask(ViewHolder holder) {
            viewHolder = holder;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            imageAddress = params[0];
            Bitmap result = null;

            result = networkManager.downloadImage(imageAddress);
            return result;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            /*작성할 부분*/
            /*네트워크에서 다운 받은 이미지 파일을 ImageFileManager 를 사용하여 내부저장소에 저장
             * 다운받은 bitmap 을 이미지뷰에 지정*/
            if(bitmap != null){
                viewHolder.ivImage.setImageBitmap(bitmap);
                String fileName = imageFileManager.saveBitmapToTemporary(bitmap, imageAddress);
                item.setImageFileName(fileName);
            }


        }



    }
}
