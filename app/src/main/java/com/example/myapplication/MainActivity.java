package com.example.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.example.myapplication.Info.CompanyInfo;
import com.example.myapplication.Info.GatherDangerInfo;
import com.example.myapplication.Info.HiddenInfo;
import com.example.myapplication.Info.RiskLevelInfo;
import com.example.myapplication.Info.TypeInfo;
import com.example.myapplication.Util.BarChartManager;
import com.example.myapplication.Util.OverlayManager;
import com.github.androidprogresslayout.ProgressLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends Activity {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private PieChart mChart1;
    private PieChart mChart2;
    private PieChart mChart3;
    private Disposable timeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        final ProgressLayout progressLayout = findViewById(R.id.progress_layout);
        progressLayout.showProgress();
        //初始化控件
        initView();
        Provider.getDangerDetailTime(75908, "", 0, 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<GatherDangerInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<GatherDangerInfo> value) {
                        progressLayout.showContent();
                        intiData(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        Provider.getCompanyList("1619")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CompanyInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        timeDisposable=d;
                    }
                    @Override
                    public void onNext(CompanyInfo value) {
                        getSafetyIndexFromCom(value.getComId());
                        getHiddenIllnessAccountObject(value.getEmid());
                        getHiddenIllnessRiskLevel(value.getEmid(),"getHiddenIllnessRiskLevel");
                        getHiddenIllnessClassify(value.getEmid(),"getHiddenIllnessClassify");
                        getHiddenIllnessInjurycategory(value.getEmid(), "getHiddenIllnessInjurycategory");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getSafetyIndexFromCom(final String id) {

        Provider.getSafetyIndexFromComTime(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<TypeInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        timeDisposable=d;
                    }

                    @Override
                    public void onNext(List<TypeInfo> value) {
                        BarChart barChart1 = findViewById(R.id.chart_bar);
                        BarChartManager barChartManager1 = new BarChartManager(barChart1);
                        //设置x轴的数据
                        ArrayList<Float> xValues = new ArrayList<>();
                        for (int i = 1; i < 13; i++) {
                            xValues.add((float) i);
                        }
                        //设置y轴的数据()
                        List<Float> yValue = new ArrayList<>();
                        for (int j = 0; j < 12; j++) {
                            yValue.add(Float.valueOf(value.get(j).getTypeValue()));
                        }
                        int color[] = {Color.rgb(46, 204, 113),
                                Color.rgb(241, 196, 15),
                                Color.rgb(231, 76, 60),
                                Color.rgb(52, 152, 219)};
                        barChartManager1.showBarChart(xValues, yValue, "安全秩序图", color);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                });


    }

    public void getHiddenIllnessAccountObject(final String uEmid) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.YEAR, -1);
        Date lastYear = ca.getTime();
        String str1 = formatter.format(lastYear);
        Provider.getHiddenIllnessAccountObjectTime(uEmid, "", str1, str, "0", "0")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<HiddenInfo>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                        timeDisposable = d;
                    }

                    @Override
                    public void onNext(List<HiddenInfo> value) {
                        TextView tv = findViewById(R.id.tv);
                        String result = "";
                        for (HiddenInfo map : value) {
                            result = result + map.getObjOrgName() + "本周发现隐患" + map.getTroubleCount()
                                    + "条，其中重大" + map.getGreat() + "条，已整改" + map.getFinishedCount() + "条。            ";
                            tv.setText(result);
                            tv.setSelected(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                });


    }

    private void showChart(PieChart pieChart, PieData pieData, String label, int leftSize, int rightSize) {
//        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字
        pieChart.setDrawHoleEnabled(true);
        pieChart.setExtraOffsets(leftSize, 0, rightSize, 0);
        pieChart.setRotationAngle(0); // 初始旋转角度
        pieChart.setRotationEnabled(true); // 可以手动旋转
        pieChart.setUsePercentValues(true);  //显示成百分比
        pieChart.getDescription().setText(label);
        pieChart.getDescription().setTextSize(5f);
        pieChart.animateXY(1000, 1000); //设置动画
        pieChart.setDrawEntryLabels(false);//设置饼上标签

        //设置数据
        pieChart.setData(pieData);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(0f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setFormSize(4f);
        l.setTextSize(1f);
        l.setFormToTextSpace(1f);


    }

    public static String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        return "#" + r + g + b;
    }

    private PieData getPieData(List<RiskLevelInfo> value) {
        ArrayList<PieEntry> yValues = new ArrayList<>();  //yVals用来表示封装每个饼块的实际数据
        // 饼图数据
        /*
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */
        for (RiskLevelInfo map : value) {
            yValues.add(new PieEntry(Float.parseFloat(map.getCount()), map.getName()));
        }

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, null);
        pieDataSet.setSliceSpace(0.5f); //设置个饼状图之间的距离
        ArrayList<Integer> colors = new ArrayList<>();
        // 饼图颜色
        List<String> list = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            list.add(getRandColorCode());
            for (int j = 0; j<list.size(); j++){
                if (list.get(j).equals(getRandColorCode())){
                    list.add(j,getRandColorCode());
                }
            }
            colors.add(Color.parseColor(list.get(i)));

        }
        pieDataSet.setValueTextSize(6f);
        pieDataSet.setColors(colors);
        pieDataSet.setValueLinePart1OffsetPercentage(90f);//数据连接线距图形片内部边界的距离，为百分数
        pieDataSet.setValueLinePart1Length(0.3f);//引导线长度
        pieDataSet.setValueLinePart2Length(0.3f);//引导线长度
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度
        PieData pieData = new PieData(pieDataSet);
        return pieData;
    }

    private void intiData(final List<GatherDangerInfo> mGatherDangerInfo) {
        mGatherDangerInfo.addAll(mGatherDangerInfo);
        OverlayManager mOverlayManager = new OverlayManager(mBaiduMap) {
            @Override
            public List<OverlayOptions> getOverlayOptions() {
                List<OverlayOptions> options = new ArrayList<>();
                for (GatherDangerInfo info : mGatherDangerInfo) {
                    if (info.getLecgoal() > 320) {
                        LatLng point = new LatLng(info.getLat(), info.getLon());
                        BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.mipmap.red);
                        OverlayOptions option = new MarkerOptions().position(point).icon(bdA);
                        options.add(option);
                    } else if (320 > info.getLecgoal() && info.getLecgoal() > 160) {
                        LatLng point = new LatLng(info.getLat(), info.getLon());
                        BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.mipmap.orange);
                        OverlayOptions option = new MarkerOptions().position(point).icon(bdA);
                        options.add(option);
                    } else if (160 > info.getLecgoal() && info.getLecgoal() > 70) {
                        LatLng point = new LatLng(info.getLat(), info.getLon());
                        BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.mipmap.yellow);
                        OverlayOptions option = new MarkerOptions().position(point).icon(bdA);
                        options.add(option);
                    } else if (70 > info.getLecgoal() && info.getLecgoal() > 20) {
                        LatLng point = new LatLng(info.getLat(), info.getLon());
                        BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.mipmap.blue);
                        OverlayOptions option = new MarkerOptions().position(point).icon(bdA);
                        options.add(option);
                    } else if (20 > info.getLecgoal() && info.getLecgoal() > 0) {
                        LatLng point = new LatLng(info.getLat(), info.getLon());
                        BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.mipmap.green);
                        OverlayOptions option = new MarkerOptions().position(point).icon(bdA);
                        options.add(option);
                    }

                }
                return options;
            }

            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }

            @Override
            public boolean onPolylineClick(Polyline polyline) {
                return false;
            }
        };
        mOverlayManager.addToMap();
        mOverlayManager.zoomToSpan();
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (GatherDangerInfo info : mGatherDangerInfo) {
                    if (info.getLat() == marker.getPosition().latitude && info.getLon() == marker.getPosition().longitude) {
                        InfoWindow mInfoWindow;
                        // 生成一个TextView用户在地图中显示InfoWindow
                        TextView location = new TextView(getApplicationContext());
                        location.setBackgroundResource(R.mipmap.location_tips);
                        location.setPadding(30, 20, 30, 50);
                        location.setTextSize(10f);
                        location.setTextColor(Color.WHITE);
                        location.setText(info.getUnname());
                        // 将marker所在的经纬度的信息转化成屏幕上的坐标
                        final LatLng ll = marker.getPosition();
                        Point p = mBaiduMap.getProjection().toScreenLocation(ll);
                        p.y -= 10;
                        p.x -= 5;
                        LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
                        // 为弹出的InfoWindow添加点击事件
                        BitmapDescriptor btv = BitmapDescriptorFactory.fromView(location);
// 为弹出的InfoWindow添加点击事件
                        mInfoWindow = new InfoWindow(btv, llInfo,
                                0, new OnInfoWindowClickListener() {

                            @Override
                            public void onInfoWindowClick() {
// 隐藏InfoWindow
                                mBaiduMap.hideInfoWindow();
                            }
                        });
// 显示InfoWindow
                        mBaiduMap.showInfoWindow(mInfoWindow);


                    }
                }
                return false;
            }
        });
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    private void initView() {
        //获取地图控件引用
        mMapView =  findViewById(R.id.mapView);
        mMapView.removeViewAt(1);
        //获取百度地图
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mChart1 = findViewById(R.id.spread_pie_chart1);
        mChart2 =  findViewById(R.id.spread_pie_chart2);
        mChart3 = findViewById(R.id.spread_pie_chart3);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        if (timeDisposable != null) {
            timeDisposable.dispose();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    public void getHiddenIllnessRiskLevel(String uEmid,String methodName) {
        Provider.getHiddenIllnessInfoTime(uEmid,methodName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<RiskLevelInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<RiskLevelInfo> value) {
                        List<RiskLevelInfo> list = new ArrayList<>();
                        for (RiskLevelInfo map : value) {
                            RiskLevelInfo info = new RiskLevelInfo();
                            info.setCount(map.getTroubleCount());
                            info.setName(map.getIgrade());
                            list.add(info);
                        }
                        PieData mPieData1 = getPieData(list);
                        showChart(mChart1, mPieData1, "危险等级图", 0, 0);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void getHiddenIllnessClassify(String uEmid,String methodName) {
        Provider.getHiddenIllnessInfoTime(uEmid,methodName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<RiskLevelInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<RiskLevelInfo> value) {
                        List<RiskLevelInfo> list = new ArrayList<>();
                        for (RiskLevelInfo map : value) {
                            RiskLevelInfo info = new RiskLevelInfo();
                            info.setCount(map.getTroubleCount());
                            info.setName(map.getInsename());
                            list.add(info);
                        }
                        PieData mPieData = getPieData(list);
                        showChart(mChart2, mPieData, "隐患分类图", 40, 15);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void getHiddenIllnessInjurycategory(String uEmid, String methodName) {
        Provider.getHiddenIllnessInfoTime(uEmid, methodName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<RiskLevelInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<RiskLevelInfo> value) {
                        List<RiskLevelInfo> list = new ArrayList<>();
                        for (RiskLevelInfo map : value) {
                            RiskLevelInfo info = new RiskLevelInfo();
                            info.setCount(map.getTroubleCount());
                            info.setName(map.getMalname());
                            list.add(info);
                        }
                        PieData mPieData = getPieData(list);
                        showChart(mChart3, mPieData, "伤害类别图", 0, 0);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
}

