package com.example.air.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.air.Bean.WeatherBean;
import com.example.air.DB.DataBaseBean;
import com.example.air.R;
import com.google.gson.Gson;

import java.util.List;

public class CityManagerAdapter extends BaseAdapter {
    Context context;
    List<DataBaseBean> list;

    public CityManagerAdapter(Context context, List<DataBaseBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.intm_city_manager,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
           viewHolder = (ViewHolder)convertView.getTag();
        }
        DataBaseBean dataBaseBean = list.get(position);
        viewHolder.cityTv.setText(dataBaseBean.getCity());
        WeatherBean weatherBean = new Gson().fromJson(dataBaseBean.getContent(), WeatherBean.class);
        WeatherBean.ResultsBean.WeatherDataBean dataBean = weatherBean.getResults().get(0).getWeather_data().get(0);
        viewHolder.conTv.setText("天气："+dataBean.getWeather());
        String[] split = dataBean.getDate().split("：");
        String todayTemp = split[1].replace(")","");
        viewHolder.currentTempTv.setText(todayTemp);
        viewHolder.windTv.setText(dataBean.getWind());
        viewHolder.tempRangeTv.setText(dataBean.getTemperature());
        return convertView;
    }
    class ViewHolder{
        TextView cityTv,conTv,currentTempTv,windTv,tempRangeTv;
        public ViewHolder(View itemView){
            cityTv = itemView.findViewById(R.id.item_city_tv_city);
            conTv = itemView.findViewById(R.id.item_city_tv_condition);
            currentTempTv = itemView.findViewById(R.id.item_city_tv_temp);
            windTv = itemView.findViewById(R.id.item_city_tv_wind);
            tempRangeTv = itemView.findViewById(R.id.item_city_temprange);

        }
    }
}
