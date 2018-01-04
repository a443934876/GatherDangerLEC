package com.example.myapplication.Util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * 项目名称：GatherDangerLEC
 * 创建时间：2017/12/29 14:44
 * 注释说明：
 */

public class StringAxisValueFormatter implements IAxisValueFormatter {

    private List<String> xValues;

    public StringAxisValueFormatter(List<String> xValues) {
        this.xValues = xValues;
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        try{
            if (v < 0 || v > (xValues.size() - 1)){//使得两侧柱子完全显示
                return "";
            }
            return xValues.get((int)v);
        }catch (Exception e){
            return "";
        }
    }
}