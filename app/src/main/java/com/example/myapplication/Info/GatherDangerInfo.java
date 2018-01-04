package com.example.myapplication.Info;

import com.example.myapplication.Util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：MyApplication7
 * 创建时间：2017/12/25 11:36
 * 注释说明：
 */

public class GatherDangerInfo {
    private static final String KEY_LECGOAL = "lecgoal";
    private static final String KEY_UNORGID = "unorgid";
    private static final String KEY_UNNAME = "unname";
    private static final String KEY_LON = "lon";
    private static final String KEY_LAT = "lat";
    private double lecgoal;
    private String unorgid;
    private String unname;
    private double lon;
    private double lat;

    public double getLecgoal() {
        return lecgoal;
    }

    public void setLecgoal(double lecgoal) {
        this.lecgoal = lecgoal;
    }

    public String getUnorgid() {
        return unorgid;
    }

    public void setUnorgid(String unorgid) {
        this.unorgid = unorgid;
    }

    public String getUnname() {
        return unname;
    }

    public void setUnname(String unname) {
        this.unname = unname;
    }

    @Override
    public String toString() {
        return "GatherDangerInfo{" + '\'' +
                ", lecgoal='" + lecgoal + '\'' +
                ", unorgid='" + unorgid + '\'' +
                ", unname='" + unname + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                '}';
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }



    public static List<GatherDangerInfo> fromMap(ArrayList<HashMap<String, Object>> result) {
        List<GatherDangerInfo> list = new ArrayList<>();
        for (HashMap<String,Object> map :result ) {
            GatherDangerInfo info = new GatherDangerInfo();
            info.setUnname(StringUtils.noNull(map.get(KEY_UNNAME)));
            info.setUnorgid(StringUtils.noNull(map.get(KEY_UNORGID)));
            try {
                String lecGoalStr = StringUtils.noNull(map.get(KEY_LECGOAL));
                double lecGoal =Double.parseDouble(lecGoalStr);
                info.setLecgoal(lecGoal);
                String latStr = StringUtils.noNull(map.get(KEY_LAT));
                double lat = Double.parseDouble(latStr);
                info.setLat(lat);
                String lonStr = StringUtils.noNull(map.get(KEY_LON));
                double lon = Double.parseDouble(lonStr);
                info.setLon(lon);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            list.add(info);
        }
        return list;
    }


}
