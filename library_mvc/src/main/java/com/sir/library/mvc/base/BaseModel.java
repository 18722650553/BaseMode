package com.sir.library.mvc.base;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.IntDef;

import com.sir.library.retrofit.event.LiveBus;
import com.sir.library.retrofit.event.ResState;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * MVC 模式的 Model
 * Created by zhuyinan on 2017/4/5.
 */
public class BaseModel {

    public static final int ON_LOADING = 0x11;
    public static final int ON_SUCCESS = 0x22;
    public static final int ON_FAILURE = 0x33;

    public MutableLiveData<ResState> loadState;

    protected CompositeDisposable mCompositeDisposable;

    public BaseModel() {
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
    protected void postState(@BaseModel.state int state) {
        switch (state) {
            case ON_SUCCESS:
                postState(state, "完成");
                break;
            case ON_FAILURE:
                postState(state, "失败");
                break;
            case ON_LOADING:
                postState(state, "正在加载..");
                break;
        }
    }

    /**
     * 发布状态
     *
     * @param state
     */
    protected void postState(@BaseModel.state int state, String msg) {
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
     * 订阅
     *
     * @param eventKey
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> MutableLiveData<T> subscribe(Object eventKey, Class<T> tClass) {
        return LiveBus.getDefault().subscribe(eventKey, tClass);
    }

    /**
     * 订阅
     *
     * @param eventKey
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> MutableLiveData<T> subscribe(Object eventKey, String tag, Class<T> tClass) {
        return LiveBus.getDefault().subscribe(eventKey, tag, tClass);
    }

    /**
     * 添加一个订阅
     */
    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /**
     * 解除订阅
     */
    public void unDisposable() {
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }

    @IntDef({ON_SUCCESS, ON_FAILURE, ON_LOADING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface state {

    }
}
