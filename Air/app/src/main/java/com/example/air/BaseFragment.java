package com.example.air;

import androidx.fragment.app.Fragment;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class BaseFragment extends Fragment implements Callback.CommonCallback<String> {
    public void LoadData(String url){
        RequestParams params = new RequestParams(url);
        x.http().get(params,this);
    }
//获取数据成功时会回调的接口
    @Override
    public void onSuccess(String result) {

    }
//获取数据失败时会回调的接口

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }
//取消请求时会回调的接口

    @Override
    public void onCancelled(CancelledException cex) {

    }
//请求完成时会回调的接口

    @Override
    public void onFinished() {

    }
}
