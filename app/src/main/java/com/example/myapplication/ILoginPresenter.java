package com.example.myapplication;

import android.content.Context;

import com.example.myapplication.Info.CompanyInfo;

import java.util.List;

/**
 * Created by mini on 17/5/21.
 */

public interface ILoginPresenter extends IBasePresenter {

    void login(Context context, String account, String pwd);

    interface IView extends IBasePresenter.IView {
        void changeButtonStyle(boolean enable, String msg);

        void toHomePage();

        void toCompanyChoose(List<CompanyInfo> info);
    }

}
