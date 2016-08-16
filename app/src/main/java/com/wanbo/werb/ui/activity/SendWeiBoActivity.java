package com.wanbo.werb.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wanbo.werb.R;
import com.wanbo.werb.ui.base.MVPBaseActivity;
import com.wanbo.werb.ui.presenter.SendPresenter;
import com.wanbo.werb.ui.view.ISendView;
import com.wanbo.werb.util.StringUtil;
import com.wanbo.werb.util.ViewUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by Werb on 2016/8/3.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * Send WeiBo
 */
public class SendWeiBoActivity extends MVPBaseActivity<ISendView, SendPresenter> implements ISendView {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;//

    @Bind(R.id.et_weibo)
    EditText et_weibo;
    @Bind(R.id.tv_weibo_number)
    TextView tv_weibo_number;
    @Bind(R.id.weibo_photo_grid)
    RecyclerView weibo_photo_grid;
    //表情
    @Bind(R.id.weibo_emotion)
    ImageView weibo_emotion;
    @Bind(R.id.ll_emotion_dashboard)
    LinearLayout ll_emotion_dashboard;
    @Bind(R.id.vp_emotion_dashboard)
    ViewPager vp_emotion_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected SendPresenter createPresenter() {
        return new SendPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_send_weibo;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    private void initView() {
        et_weibo.addTextChangedListener(StringUtil.textNumberListener(et_weibo,tv_weibo_number,this));
        weibo_photo_grid.setLayoutManager(new GridLayoutManager(this, 4));
        new ViewUtil(this,vp_emotion_dashboard,et_weibo).initEmotion();
    }

    @OnClick(R.id.weibo_photo)
    void pickphoto() {
        mPresenter.pickPhoto();
    }

    @OnClick(R.id.weibo_emotion) void insertEmotion() {
        //软键盘，显示或隐藏
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        if(ll_emotion_dashboard.getVisibility() == View.VISIBLE) {
            // 显示表情面板时点击,将按钮图片设为笑脸按钮,同时隐藏面板
            weibo_emotion.setImageResource(R.drawable.btn_insert_emotion);
            ll_emotion_dashboard.setVisibility(View.GONE);
            imm.showSoftInput(et_weibo, 0);
        } else {
            // 未显示表情面板时点击,将按钮图片设为键盘,同时显示面板
            weibo_emotion.setImageResource(R.drawable.btn_insert_keyboard);
            ll_emotion_dashboard.setVisibility(View.VISIBLE);
            imm.hideSoftInputFromWindow(et_weibo.getWindowToken(), 0);
        }
    }

    @OnClick(R.id.weibo_topic) void insertTopic(){
        int curPosition = et_weibo.getSelectionStart();
        StringBuilder sb = new StringBuilder(et_weibo.getText().toString());
        sb.insert(curPosition,"##");
        // 特殊文字处理,将表情等转换一下
        et_weibo.setText(sb);
        // 将光标设置到新增完表情的右侧
        et_weibo.setSelection(curPosition + 1);
    }

    /**
     * 判断当前权限是否允许,弹出提示框来选择
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void PermissionToVerify() {
        // 需要验证的权限
        int hasWriteContactsPermission =checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            // 弹窗询问 ，让用户自己判断
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        mPresenter.photoPick();

    }

    /**
     * 用户进行权限设置后的回调函数 , 来响应用户的操作，无论用户是否同意权限，Activity都会
     * 执行此回调方法，所以我们可以把具体操作写在这里
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取图片
                    mPresenter.photoPick();
                } else {
                    Toast.makeText(SendWeiBoActivity.this, "权限没有开启", Toast.LENGTH_SHORT).show();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> listExtra =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                mPresenter.loadAdapter(listExtra);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.car_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_send:
                if(et_weibo.getText().toString().equals("")){
                    Toast.makeText(this,R.string.not_null,Toast.LENGTH_SHORT).show();
                }else {
                    mPresenter.sendWeiBo(et_weibo.getText().toString());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishAndToast() {
        finish();
        Toast.makeText(this, R.string.comment_succe,Toast.LENGTH_LONG).show();
    }

    @Override
    public void permissionSetting() {
        PermissionToVerify();
    }

    @Override
    public RecyclerView getPhotoGrid() {
        return weibo_photo_grid;
    }

}
