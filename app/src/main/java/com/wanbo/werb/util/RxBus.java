package com.wanbo.werb.util;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Werb on 2016/7/13.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * RxJava 异步响应类
 */
public class RxBus {

    public static RxBus mInstance;

    private final Subject<Object, Object> mBus = new SerializedSubject<>(PublishSubject.create());

    // region Constructors

    public static RxBus getInstance() {
        if (mInstance == null) {
            mInstance = new RxBus();
        }
        return mInstance;
    }

    // endregion

    // region Public methods

    public void send(Object object) {
        mBus.onNext(object);
    }

    public Observable<Object> toObserverable() {
        return mBus;
    }
}
