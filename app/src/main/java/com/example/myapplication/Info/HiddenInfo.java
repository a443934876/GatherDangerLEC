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

public class HiddenInfo {

    private static final String KEY_TROUBLECOUNT = "troubleCount";
    private static final String KEY_FINISHEDCOUNT = "finishedCount";
    private static final String KEY_OBJORGNAME = "objorgname";
    private static final String KEY_GREAT = "重大";

    private String troubleCount;
    private String finishedCount;
    private String objOrgName;
    private String great;

    public String getGreat() {
        return great;
    }

    public void setGreat(String great) {
        this.great = great;
    }

    public String getTroubleCount() {
        return troubleCount;
    }

    public void setTroubleCount(String troubleCount) {
        this.troubleCount = troubleCount;
    }

    public String getFinishedCount() {
        return finishedCount;
    }

    public void setFinishedCount(String finishedCount) {
        this.finishedCount = finishedCount;
    }


    public String getObjOrgName() {
        return objOrgName;
    }

    public void setObjOrgName(String objOrgName) {
        this.objOrgName = objOrgName;
    }

    public static List< HiddenInfo> fromMap(ArrayList<HashMap<String, Object>> result) {
        List<HiddenInfo> list = new ArrayList<>();
        for (HashMap<String, Object> map : result) {
            HiddenInfo info = new HiddenInfo();
            info.setFinishedCount(StringUtils.noNull(map.get(KEY_FINISHEDCOUNT)));
            info.setGreat(StringUtils.noNull(map.get(KEY_GREAT)));
            info.setObjOrgName(StringUtils.noNull(map.get(KEY_OBJORGNAME)));
            info.setTroubleCount(StringUtils.noNull(map.get(KEY_TROUBLECOUNT)));
            list.add(info);
        }
        return list;
    }


}
