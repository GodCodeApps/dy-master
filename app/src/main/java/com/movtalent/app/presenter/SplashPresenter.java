package com.movtalent.app.presenter;

import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lib.common.util.DataInter;
import com.media.playerlib.widget.GlobalDATA;
import com.movtalent.app.App;
import com.movtalent.app.HomeActivity;
import com.movtalent.app.http.ApiService;
import com.movtalent.app.http.BaseApi;
import com.media.playerlib.model.AdConfigDto;
import com.movtalent.app.model.dto.TypeListDto;
import com.movtalent.app.model.vo.VideoTypeVo;
import com.movtalent.app.presenter.iview.ITypeView;

/**
 * @author huangyong
 * createTime 2019-09-14
 */
public class SplashPresenter {
    private ITypeView iTypeView;

    public SplashPresenter(ITypeView iTypeView) {
        this.iTypeView = iTypeView;
    }

    public void getTypeList() {
        Intent intent = new Intent( App.getContext(), HomeActivity.class);
        App.getContext().startActivity(intent);
        BaseApi.request(BaseApi.createApi(ApiService.class)
                        .getTypeList(), new BaseApi.IResponseListener<TypeListDto>() {
                    @Override
                    public void onSuccess(TypeListDto data) {
                        if (data != null && data.getData() != null && data.getData().size() > 0) {
                            iTypeView.loadDone(VideoTypeVo.from(data));
                        } else {
                            Toast.makeText(App.getContext(), "数据为空", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail() {
                        iTypeView.loadError();
                    }
                }
        );



    }

    public void getad() {
        BaseApi.request(BaseApi.createApi(ApiService.class)
                        .getAd(), new BaseApi.IResponseListener<AdConfigDto>() {
                    @Override
                    public void onSuccess(AdConfigDto data) {
                        if (data != null) {
                            String dataJson = new Gson().toJson(data.getData());
                            GlobalDATA.AD_INFO = dataJson;
                            iTypeView.loadAdDone();
                        } else {
                            GlobalDATA.AD_INFO = "";
                        }

                    }

                    @Override
                    public void onFail() {
                    }
                }
        );
    }
}
