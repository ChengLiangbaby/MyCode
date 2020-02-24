package com.example.air.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.air.R;

import java.util.List;

public class DeleteCityAdapter extends BaseAdapter {
    Context context;
    List<String> mDaras;
    List<String> deleteCity;

    public DeleteCityAdapter(Context context, List<String> mDaras,List<String> deleteCity) {
        this.deleteCity = deleteCity;
        this.context = context;
        this.mDaras = mDaras;
    }

    @Override
    public int getCount() {
        return mDaras.size();
    }

    @Override
    public Object getItem(int position) {
        return mDaras.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_delete,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String city = mDaras.get(position);
        viewHolder.tv.setText(city);
        viewHolder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDaras.remove(city);
                deleteCity.add(city);
                notifyDataSetChanged();//删除请求更新

            }
        });
        return convertView;
    }
    class ViewHolder{
        ImageView iv;
        TextView tv;
        public ViewHolder(View view){
            tv = view.findViewById(R.id.item_delete_Tv);
            iv = view.findViewById(R.id.item_delete_Iv);

        }
    }
}
