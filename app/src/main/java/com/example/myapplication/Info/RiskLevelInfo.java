package com.example.myapplication.Info;

import com.example.myapplication.Util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：GatherDangerLEC
 * 创建时间：2018/1/2 1:34
 * 注释说明：
 */

public class RiskLevelInfo {
    private static final String KEY_INSENAME = "insename";
    private static final String KEY_MALNAME = "malname";
    private static final String KEY_TROUBLECOUNT = "troubleCount";
    private static final String KEY_IGRADE = "igrade";

    private String insename;
    private String troubleCount;
    private String igrade;
    private String malname;
    private String count;
    private String name;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInsename() {
        return insename;
    }

    public void setInsename(String insename) {
        this.insename = insename;
    }

    public String getMalname() {
        return malname;
    }

    public void setMalname(String malname) {
        this.malname = malname;
    }

    public String getIgrade() {
        return igrade;
    }

    public void setIgrade(String igrade) {
        this.igrade = igrade;
    }

    public String getTroubleCount() {
        return troubleCount;
    }

    public void setTroubleCount(String troubleCount) {
        this.troubleCount = troubleCount;
    }

    public static List<RiskLevelInfo> fromMap(ArrayList<HashMap<String, Object>> result) {
        List<RiskLevelInfo> list = new ArrayList<>();
        for (HashMap<String, Object> map : result) {
            RiskLevelInfo info = new RiskLevelInfo();
            info.setIgrade(StringUtils.noNull(map.get(KEY_IGRADE)));
            info.setTroubleCount(StringUtils.noNull(map.get(KEY_TROUBLECOUNT)));
            info.setInsename(StringUtils.noNull(map.get(KEY_INSENAME)));
            info.setMalname(StringUtils.noNull(map.get(KEY_MALNAME)));
            list.add(info);
        }
        return list;
    }


}
