package com.wanbo.werb.ui.presenter;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wanbo.werb.R;
import com.wanbo.werb.ui.adapter.WeiBoPhotoAdapter;
import com.wanbo.werb.ui.view.ISendView;
import com.wanbo.werb.util.AccessTokenKeeper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Werb on 2016/8/3.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * Send Img with text
 */
public class SendPresenter extends BasePresenter<ISendView> {

    private Context context;
    private ISendView sendView;
    private List<String> photos = new ArrayList<>();
    private WeiBoPhotoAdapter photoAdapter;

    public SendPresenter(Context context) {
        this.context = context;
    }

    public void pickPhoto(){
        sendView = getView();
        photoAdapter = new WeiBoPhotoAdapter(context,photos);
        sendView.getPhotoGrid().setAdapter(photoAdapter);
        photos.clear();
        photoAdapter.paths.clear();
        sendView.permissionSetting();
    }

    //photo pick
    public void photoPick() {
        PhotoPicker.builder()
                .setPhotoCount(9)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start((Activity) context, PhotoPicker.REQUEST_CODE);
    }

    //更新数据适配器
    public void loadAdapter(ArrayList<String> paths) {
        if (photos == null) {
            photos = new ArrayList<>();
        }
        System.out.println("~~~"+paths.size());
        photos.clear();
        photos.addAll(paths);
        photoAdapter.notifyDataSetChanged();
    }

    public void sendWeiBo(String status){

        if(photoAdapter!=null && getPathFromAdapter().size()>0){
            weiBoApi.sendWeiBoWithImg(getTokenStr(),getTextStr(status),getImage())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Status -> {
                        getView().finishAndToast();
                    },this::loadError);
        }else {
            Oauth2AccessToken token = readToken(context);
            weiBoApi.sendWeiBoWithText(getSendMap(token.getToken(),status))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Status -> {
                                getView().finishAndToast();
                    },this::loadError);
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(context, R.string.load_error, Toast.LENGTH_SHORT).show();
    }

    // get request params
    private RequestBody getTokenStr() {
        Oauth2AccessToken token = readToken(context);
        RequestBody accessBody = RequestBody.create(
                MediaType.parse("text/plain"),
                token.getToken());
        return accessBody;
    }

    private RequestBody getTextStr(String text) {
        RequestBody contentBody = RequestBody.create(
                MediaType.parse("text/plain"),text);
        return contentBody;
    }

    /**
     * 由于接口限制，只能上传一张图片
     * @return
     */
    private RequestBody getImage() {
        List<String> stringList = getPathFromAdapter();
        File file = new File(stringList.get(0));
        RequestBody imageBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                file);
        return imageBody;
    }

    private Map<String,Object> getSendMap(String token,String status){
        Map<String,Object> map = new HashMap<>();
        map.put("access_token",token);
        map.put("status",status);
        return map;
    }

    private Oauth2AccessToken readToken(Context context) {
        return AccessTokenKeeper.readAccessToken(context);
    }

    private List<String> getPathFromAdapter(){
        List<String> paths =  new ArrayList<>();
        if(photoAdapter!=null) {
            List<Object> objects = photoAdapter.paths;
            for (Object object : objects) {
                if (object instanceof String) {
                    paths.add((String) object);
                }
            }
            return paths;
        }else {
            return null;
        }
    }

}
