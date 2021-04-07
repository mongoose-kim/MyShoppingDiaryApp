package ddwu.mobile.finalproject.ma02_20160214;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class BlogAdapter extends BaseAdapter {

    public static final String TAG = "BlogAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<BlogDto> list;
    private ItemNetworkManager networkManager = null;
    private ImageFileManager imageFileManager = null;
    BlogDto blog;


    public BlogAdapter(Context context, int resource, ArrayList<BlogDto> list) {
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
    public BlogDto getItem(int position) {
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
            viewHolder.tv_btitle = view.findViewById(R.id.tv_btitle);
            viewHolder.tv_bloggername = view.findViewById(R.id.tv_bloggername);
            viewHolder.tv_description = view.findViewById(R.id.tv_description);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        BlogDto dto = list.get(position);

        viewHolder.tv_btitle.setText(dto.getTitle());
        viewHolder.tv_bloggername.setText(dto.getBloggername());
        viewHolder.tv_description.setText(dto.getDescription());

        Button button = view.findViewById(R.id.btn_blog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(this, "버튼", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getLink()));
                context.startActivity(intent);
            }
        });

        return view;
    }

    public void setList(ArrayList<BlogDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //    ※ findViewById() 호출 감소를 위해 필수로 사용할 것
    static class ViewHolder {
        public TextView tv_btitle = null;
        public TextView tv_bloggername = null;
        public TextView tv_description = null;
    }



}
