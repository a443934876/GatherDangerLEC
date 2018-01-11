package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.myapplication.Info.CompanyInfo;
import com.example.myapplication.Info.GlobalDataProvider;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 公司列表
 * Created by cqj on 17/5/21.
 */

public class CompanyListActivity extends BaseActivity{

    private static final String EXTRA_KEY_COMPANY_LIST = "companyList";
    private RecyclerBaseAdapter<CompanyInfo, CompanyHolder> mAdapter;
    private final CompositeDisposable mItemClickDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        initView();
        initEvent();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.company_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new RecyclerItemDecoration());
        mAdapter = new RecyclerBaseAdapter<>(R.layout.item_company, CompanyHolder.class);
        recyclerView.setAdapter(mAdapter);
        ArrayList<CompanyInfo> infoList = getIntent().getParcelableArrayListExtra(EXTRA_KEY_COMPANY_LIST);
        mAdapter.addAll(infoList);
    }

    private void initEvent() {
        mAdapter.itemClickObserve()
                .subscribe(new Observer<CompanyInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mItemClickDisposable.add(d);
                    }

                    @Override
                    public void onNext(CompanyInfo value) {
                        GlobalDataProvider.INSTANCE.setCompanyInfo(value);
                        String companySave = value.toString();
                        SharedPreferences sp = getSharedPreferences(AppConstant.SP_CONFIG, Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString(AppConstant.SP_KEY_COMPANY_INFO, companySave);
                        edit.apply();
                        MainActivity.start(CompanyListActivity.this);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            exitWarning();
//        }
//        return true;
//    }
//
//    private void exitWarning() {
//        new SweetAlertDialog(this,
//                SweetAlertDialog.WARNING_TYPE)
//                .setTitleText(
//                        getResources().getString(R.string.dialog_default_title))
//                .setContentText("你确定退出吗？").setCancelText("点错了")
//                .setConfirmText("是的！").showCancelButton(true)
//                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        sweetAlertDialog.cancel();
//                    }
//                }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                sweetAlertDialog.cancel();
//                finish();
//            }
//        }).show();
//    }
//
//    @Override
//    public void onBackPressed() {
//        exitWarning();
//    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mItemClickDisposable.dispose();
    }

    public static void start(Context context, ArrayList<CompanyInfo> infoList) {
        Intent starter = new Intent(context, CompanyListActivity.class);
        starter.putParcelableArrayListExtra(EXTRA_KEY_COMPANY_LIST, infoList);
        context.startActivity(starter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                LoginActivity.start(this);
                finish();
                break;
        }
        return true;
    }
}
