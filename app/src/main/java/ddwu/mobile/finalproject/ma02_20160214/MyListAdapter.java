package ddwu.mobile.finalproject.ma02_20160214;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MyListAdapter extends CursorAdapter {
    /* 담기버튼을 눌러 따로 저장된 상품 리스트를 보여주는 어댑터 */


    LayoutInflater inflater;
    int layout;
    ImageFileManager imgFileManager;

    public MyListAdapter(Context context, int layout, Cursor c) {
            super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layout = R.layout.listview_item;
            imgFileManager = new ImageFileManager(context);
    }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View listItemLayout = inflater.inflate(layout, parent, false);
            ViewHolder holder = new ViewHolder();
            listItemLayout.setTag(holder);
            return listItemLayout;
        }


        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            ViewHolder holder = new ViewHolder();
            if(holder.tvTitle == null){
                holder.tvTitle = view.findViewById(R.id.tvTitle);
                holder.tvLprice = view.findViewById(R.id.tvLprice);
                holder.tvHprice = view.findViewById(R.id.tvHprice);
                holder.tvMallName = view.findViewById(R.id.tvMallName);
                holder.ivImage = view.findViewById(R.id.ivImage);
            }

            holder.tvTitle.setText(cursor.getString(1));
            holder.tvLprice.setText(cursor.getString(3));
            holder.tvHprice.setText(cursor.getString(4));
            holder.tvMallName.setText(cursor.getString(5));
            Bitmap savedBitmap = imgFileManager.getBitmapFromTemporary(cursor.getString(2));

            if(savedBitmap != null){
                holder.ivImage.setImageBitmap(savedBitmap);
            }

        }

        static class ViewHolder {

            public ViewHolder() {
                tvTitle = null;
                tvLprice = null;
                tvHprice = null;
                tvMallName = null;
                ivImage = null;
            }

            TextView tvTitle;
            TextView tvLprice;
            TextView tvHprice;
            TextView tvMallName;
            public ImageView ivImage = null;
        }
    }