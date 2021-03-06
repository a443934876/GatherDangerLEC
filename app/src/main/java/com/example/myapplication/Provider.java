package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.Info.CompanyInfo;
import com.example.myapplication.Info.GatherDangerInfo;
import com.example.myapplication.Info.HiddenInfo;
import com.example.myapplication.Info.RiskLevelInfo;
import com.example.myapplication.Info.TypeInfo;
import com.example.myapplication.Util.WebServiceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 项目名称：
 * 创建时间：2017/12/26 8:51
 * 注释说明：
 */

class Provider {

    static Observable<List<GatherDangerInfo>>
    getDangerDetailTime(final int Emid, final String induval, final int hurttypeid, final int insid) {
        return Observable.interval(10, 1000*60*60, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, ObservableSource<List<GatherDangerInfo>>>() {
                    @Override
                    public ObservableSource<List<GatherDangerInfo>> apply(Long aLong) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<GatherDangerInfo>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<GatherDangerInfo>> e) throws Exception {
                                String keys2[] = {"Emid", "induval", "hurttypeid", "insid"};
                                Object values2[] = {Emid, induval, hurttypeid, insid};
                                ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                                        "gatherDangerLEC", WebServiceUtil.HUIWEI_SAFE_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                                if (result.size() >0) {
                                    Log.e("TAG", "gatherDangerLEC" );
                                    for (int i = 0 ;i< result.size();i++){
                                        Log.e("TAG", "unname: "+result.get(i).get("unname")+"----"+
                                                "lecgoal: "+result.get(i).get("lecgoal")+"----"+
                                                "lon: "+result.get(i).get("lon")+"----"+
                                                "lat: "+result.get(i).get("lat") );
                                    }


                                    List<GatherDangerInfo> list = GatherDangerInfo.fromMap(result);
                                    e.onNext(list);
                                }else {
                                    e.onError(new Throwable("无法连接服务器，请重试！"));
                                }
                                e.onComplete();
                            }
                        });
                    }
                });
    }
    static Observable<List<RiskLevelInfo>>
    getHiddenIllnessInfoTime(final String uEmid, final String methodName) {
        return Observable.interval(200, 1000*60*60, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, ObservableSource<List<RiskLevelInfo>>>() {
                    @Override
                    public ObservableSource<List<RiskLevelInfo>> apply(Long aLong) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<RiskLevelInfo>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<RiskLevelInfo>> e) throws Exception {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Calendar ca = Calendar.getInstance();
                                ca.setTime(new Date());
                                Date now = ca.getTime();
                                String nowTime = formatter.format(now);
                                ca.add(Calendar.YEAR, -1);
                                Date lastYear = ca.getTime();
                                String lastYearTime = formatter.format(lastYear);
                                String keys2[] = {"uEmid", "hgrade", "examStart", "examEnd", "areaRangeID", "objOrgId"};
                                Object values2[] = {uEmid, "", lastYearTime, nowTime, "0", "0"};
                                ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                                        methodName, WebServiceUtil.HUIWEI_SAFE_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                                if (result.size()>0) {
                                    Log.e("TAG", methodName );
                                    List<RiskLevelInfo> list = RiskLevelInfo.fromMap(result);
                                    e.onNext(list);

                                }

                            }


                        });
                    }
                });

    }
    static Observable<List<TypeInfo>> getSafetyIndexFromComTime(final String Comid) {
        return Observable.interval(300, 1000*60*60, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, ObservableSource<List<TypeInfo>>>() {
                    @Override
                    public ObservableSource<List<TypeInfo>> apply(Long aLong) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<TypeInfo>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<TypeInfo>> e) throws Exception {
                                List<TypeInfo> list = new ArrayList<>();
                                for (int i = 1; i <= 12; i++) {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    Calendar ca = Calendar.getInstance();
                                    ca.setTime(new Date());
                                    ca.add(Calendar.MONTH, i - 13);
                                    Date lastYear = ca.getTime();
                                    String str1 = formatter.format(lastYear);
                                    ca.add(Calendar.MONTH, 1);
                                    Date lastYear1 = ca.getTime();
                                    String str2 = formatter.format(lastYear1);
                                    String keys2[] = {"Comid", "nStart", "nEnd", "staIndexID"};
                                    Object values2[] = {Comid, str1, str2, "7"};
                                    ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                                            "getSafetyIndexFromCom", WebServiceUtil.HUIWEI_SAFE_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                                    if (result.size() >0) {
                                        Log.e("TAG",  "getSafetyIndexFromCom" );
                                        TypeInfo info = new TypeInfo();
                                        info.setData(str1);
                                        info.setTypeValue(String.valueOf(result.get(0).get("comindex")));
                                        list.add(info);
                                    }
                                    Thread.sleep(500);
                                }
                                e.onNext(list);
                                e.onComplete();
                            }
                        });
                    }
                });

    }
    static Observable<List<HiddenInfo>>
    getHiddenIllnessAccountObjectTime(final String uEmid, final String hgrade,
                                      final String examStart, final String examEnd,
                                      final String areaRangeID, final String objOrgId) {
        return Observable.interval(400, 1000*60*60, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, ObservableSource<List<HiddenInfo>>>() {
                    @Override
                    public ObservableSource<List<HiddenInfo>> apply(Long aLong) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<HiddenInfo>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<HiddenInfo>> e) throws Exception {
                                String keys2[] = {"uEmid", "hgrade", "examStart", "examEnd", "areaRangeID", "objOrgId"};
                                Object values2[] = {uEmid, hgrade, examStart, examEnd, areaRangeID, objOrgId};
                                ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                                        "getHiddenIllnessAccountObject", WebServiceUtil.HUIWEI_SAFE_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                                if (result != null) {
                                    Log.e("TAG",  "getHiddenIllnessAccountObject" );
                                    List<HiddenInfo> list = HiddenInfo.fromMap(result);
                                    e.onNext(list);
                                }
                                e.onComplete();
                            }
                        });
                    }
                });

    }
    static Observable<CompanyInfo> getCompanyList(final String uId) {
        return Observable.create(new ObservableOnSubscribe<CompanyInfo>() {
            @Override
            public void subscribe(ObservableEmitter<CompanyInfo> e) throws Exception {
                try {
                    String[] keys = {"uPersonalID", "sState"};
                    Object[] values = {uId, "在职"};
                    ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys, values,
                            "getMoreComs",WebServiceUtil.HUIWEI_URL,WebServiceUtil.HUIWEI_NAMESPACE);

                    for (HashMap<String, Object> map : result) {
                        CompanyInfo info = CompanyInfo.fromData(map);
                        e.onNext(info);
                    }
                    e.onComplete();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    e.onError(new Throwable("该帐号没有关联公司，请更换帐号。"));
                }
            }
        });
    }
}
