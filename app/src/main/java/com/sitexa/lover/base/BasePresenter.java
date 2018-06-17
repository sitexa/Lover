package com.sitexa.lover.base;

/**
 * Created By sitexa on 2018/6/17
 */
public abstract class BasePresenter<T>{
    public T view;

    public void attach(T view){
        this.view = view;
    }

    public void detach(){
        this.view = null;
    }
}