package com.example.myapplication;

/**
 * Created by cqj on 2017-05-22.
 */
public interface IParamContainer {

    Object get(String key);

    void set(String key, Object object);
}
