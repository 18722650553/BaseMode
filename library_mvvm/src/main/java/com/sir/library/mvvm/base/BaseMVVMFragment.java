package com.sir.library.mvvm.base;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.sir.library.base.BaseFragmentV4;
import com.sir.library.mvvm.ContractProxy;
import com.sir.library.mvvm.event.LiveBus;
import com.sir.library.mvvm.event.ResState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuyinan on 2019/6/21.
 */
public abstract class BaseMVVMFragment<T extends BaseViewModel> extends BaseFragmentV4 {

    protected T mViewModel;

    /**
     * 状态页面监听
     */
    protected Observer observer = new Observer<ResState>() {
        @Override
        public void onChanged(@Nullable ResState state) {
            if (!TextUtils.isEmpty(state.getMsg())) {
                Toast.makeText(getContext(), state.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private List<Object> eventKeys = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        mViewModel = VMProviders(this, (Class<T>) ContractProxy.getInstance(this, 0));
        if (null != mViewModel) {
            dataObserver();
            mViewModel.mRepository.loadState.observe(this, observer);
        }
    }

    protected <T extends ViewModel> T VMProviders(BaseFragmentV4 fragment, @NonNull Class<T> modelClass) {
        return ViewModelProviders.of(fragment).get(modelClass);
    }

    protected void dataObserver() {

    }

    protected <T> MutableLiveData<T> registerSubscriber(Object eventKey, Class<T> tClass) {
        return registerSubscriber(eventKey, null, tClass);
    }

    protected <T> MutableLiveData<T> registerSubscriber(Object eventKey, String tag, Class<T> tClass) {
        String event;
        if (TextUtils.isEmpty(tag)) {
            event = (String) eventKey;
        } else {
            event = eventKey + tag;
        }
        eventKeys.add(event);
        return LiveBus.getDefault().subscribe(eventKey, tag, tClass);
    }

}
