package com.sir.library.mvvm.base;

import android.arch.lifecycle.MutableLiveData;

import com.sir.library.mvvm.event.LiveBus;
import com.sir.library.mvvm.event.ResState;

import java.util.UUID;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 知识库
 * Created by zhuyinan on 2019/6/21.
 */
public abstract class BaseRepository {

    public MutableLiveData<ResState> loadState;

    protected CompositeSubscription mCompositeSubscription;

    public BaseRepository() {
        this.loadState = new MutableLiveData<>();
    }

    /**
     * 获取事件密钥
     *
     * @return
     */
    protected static String getEventKey() {
        return UUID.randomUUID().toString();
    }

    /**
     * 发布状态
     *
     * @param state
     */
    protected void postState(int state, String msg) {
        if (loadState != null) {
            loadState.postValue(new ResState(state, msg));
        }
    }

    /**
     * 发布数据
     *
     * @param eventKey
     * @param t
     */
    public void postData(Object eventKey, Object t) {
        postData(eventKey, null, t);
    }

    /**
     * 发布数据
     *
     * @param eventKey
     * @param t
     */
    public void postData(Object eventKey, String tag, Object t) {
        LiveBus.getDefault().postEvent(eventKey, tag, t);
    }

    /**
     * 添加一个订阅
     */
    protected void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    /**
     * 解除订阅
     */
    public void unDisposable() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
