package com.wanbo.werb.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.wanbo.werb.R;
import com.wanbo.werb.bean.Emotion;
import com.wanbo.werb.ui.adapter.EmotionGvAdapter;
import com.wanbo.werb.ui.adapter.EmotionPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Werb on 2016/8/7.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * 初始化EmojiView
 */
public class  ViewUtil implements AdapterView.OnItemClickListener {

    private Context context;
    private EmotionPagerAdapter emotionPagerGvAdapter;
    private ViewPager vp_emotion_dashboard;
    private EditText et_weibo;

    public ViewUtil(Context context, ViewPager vp_emotion_dashboard, EditText et_weibo) {
        this.context = context;
        this.vp_emotion_dashboard = vp_emotion_dashboard;
        this.et_weibo = et_weibo;
    }

    public  void initEmotion(){
        // 获取屏幕宽度
        int gvWidth = ScreenUtil.instance(context).getScreenWidth();
        // 表情边距
        int spacing = ScreenUtil.instance(context).dip2px(8);
        // GridView中item的宽度
        int itemWidth = (gvWidth - spacing * 8) / 7;
        int gvHeight = itemWidth * 3 + spacing * 4;

        List<GridView> gvs = new ArrayList<>();
        List<String> emotionNames = new ArrayList<>();
        // 遍历所有的表情名字
        for (String emojiName : Emotion.emojiMap.keySet()) {
            emotionNames.add(emojiName);
            // 每20个表情作为一组,同时添加到ViewPager对应的view集合中
            if (emotionNames.size() == 20) {
                GridView gv = createEmotionGridView(emotionNames, gvWidth, spacing, itemWidth, gvHeight);
                gvs.add(gv);
                // 添加完一组表情,重新创建一个表情名字集合
                emotionNames = new ArrayList<>();
            }
        }
        // 检查最后是否有不足20个表情的剩余情况
        if (emotionNames.size() > 0) {
            GridView gv = createEmotionGridView(emotionNames, gvWidth, spacing, itemWidth, gvHeight);
            gvs.add(gv);
        }
        // 将多个GridView添加显示到ViewPager中
        emotionPagerGvAdapter = new EmotionPagerAdapter(gvs);
        vp_emotion_dashboard.setAdapter(emotionPagerGvAdapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gvWidth, gvHeight);
        vp_emotion_dashboard.setLayoutParams(params);
    }

    /**
     * 创建显示表情的GridView
     */
    private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
        // 创建GridView
        GridView gv = new GridView(context);
        gv.setBackgroundResource(R.color.bg_gray);
        gv.setSelector(R.color.transparent);
        gv.setNumColumns(7);
        gv.setPadding(padding, padding, padding, padding);
        gv.setHorizontalSpacing(padding);
        gv.setVerticalSpacing(padding);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gv.setLayoutParams(params);
        // 给GridView设置表情图片
        EmotionGvAdapter adapter = new EmotionGvAdapter(context, emotionNames, itemWidth);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);
        return gv;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAdapter = parent.getAdapter();
        if (itemAdapter instanceof EmotionGvAdapter) {
            // 点击的是表情
            EmotionGvAdapter emotionGvAdapter = (EmotionGvAdapter) itemAdapter;

            if (position == emotionGvAdapter.getCount() - 1) {
                // 如果点击了最后一个回退按钮,则调用删除键事件
                et_weibo.dispatchKeyEvent(new KeyEvent(
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            } else {
                // 如果点击了表情,则添加到输入框中
                String emotionName = emotionGvAdapter.getItem(position);
                // 获取当前光标位置,在指定位置上添加表情图片文本
                int curPosition = et_weibo.getSelectionStart();
                StringBuilder sb = new StringBuilder(et_weibo.getText().toString());
                sb.insert(curPosition, emotionName);
                // 特殊文字处理,将表情等转换一下
                et_weibo.setText(StringUtil.getWeiBoText(
                        context,sb.toString()));
                // 将光标设置到新增完表情的右侧
                et_weibo.setSelection(curPosition + emotionName.length());
            }
        }
    }

    /**
     * 设置btn切换状态
     * @param activity
     * @param selectBtn
     * @param noBtn1
     * @param noBtn2
     */
    public static void selectBtn(Activity activity, Button selectBtn, Button noBtn1, Button noBtn2){
        selectBtn.setBackgroundColor(activity.getResources().getColor(R.color.blue));
        selectBtn.setTextColor(activity.getResources().getColor(R.color.white));
        noBtn1.setBackgroundColor(activity.getResources().getColor(R.color.white));
        noBtn1.setTextColor(activity.getResources().getColor(R.color.blue));
        noBtn2.setBackgroundColor(activity.getResources().getColor(R.color.white));
        noBtn2.setTextColor(activity.getResources().getColor(R.color.blue));
    }
}
