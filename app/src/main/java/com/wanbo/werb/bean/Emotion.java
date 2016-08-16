package com.wanbo.werb.bean;

import com.wanbo.werb.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
public class Emotion implements Serializable {
	
	public static Map<String, Integer> emojiMap;
	
	static {
		emojiMap = new HashMap<String, Integer>();
		emojiMap.put("[呵呵]", R.mipmap.d_hehe);
		emojiMap.put("[嘻嘻]", R.mipmap.d_xixi);
		emojiMap.put("[哈哈]", R.mipmap.d_haha);
		emojiMap.put("[爱你]", R.mipmap.d_aini);
		emojiMap.put("[挖鼻屎]", R.mipmap.d_wabishi);
		emojiMap.put("[吃惊]", R.mipmap.d_chijing);
		emojiMap.put("[晕]", R.mipmap.d_yun);
		emojiMap.put("[泪]", R.mipmap.d_lei);
		emojiMap.put("[馋嘴]", R.mipmap.d_chanzui);
		emojiMap.put("[抓狂]", R.mipmap.d_zhuakuang);
		emojiMap.put("[哼]", R.mipmap.d_heng);
		emojiMap.put("[可爱]", R.mipmap.d_keai);
		emojiMap.put("[怒]", R.mipmap.d_nu);
		emojiMap.put("[汗]", R.mipmap.d_han);
		emojiMap.put("[害羞]", R.mipmap.d_haixiu);
		emojiMap.put("[睡觉]", R.mipmap.d_shuijiao);
		emojiMap.put("[钱]", R.mipmap.d_qian);
		emojiMap.put("[偷笑]", R.mipmap.d_touxiao);
		emojiMap.put("[笑cry]", R.mipmap.d_xiaoku);
		emojiMap.put("[doge]", R.mipmap.d_doge);
		emojiMap.put("[喵喵]", R.mipmap.d_miao);
		emojiMap.put("[酷]", R.mipmap.d_ku);
		emojiMap.put("[衰]", R.mipmap.d_shuai);
		emojiMap.put("[闭嘴]", R.mipmap.d_bizui);
		emojiMap.put("[鄙视]", R.mipmap.d_bishi);
		emojiMap.put("[花心]", R.mipmap.d_huaxin);
		emojiMap.put("[鼓掌]", R.mipmap.d_guzhang);
		emojiMap.put("[悲伤]", R.mipmap.d_beishang);
		emojiMap.put("[思考]", R.mipmap.d_sikao);
		emojiMap.put("[生病]", R.mipmap.d_shengbing);
		emojiMap.put("[亲亲]", R.mipmap.d_qinqin);
		emojiMap.put("[怒骂]", R.mipmap.d_numa);
		emojiMap.put("[太开心]", R.mipmap.d_taikaixin);
		emojiMap.put("[懒得理你]", R.mipmap.d_landelini);
		emojiMap.put("[右哼哼]", R.mipmap.d_youhengheng);
		emojiMap.put("[左哼哼]", R.mipmap.d_zuohengheng);
		emojiMap.put("[嘘]", R.mipmap.d_xu);
		emojiMap.put("[委屈]", R.mipmap.d_weiqu);
		emojiMap.put("[吐]", R.mipmap.d_tu);
		emojiMap.put("[可怜]", R.mipmap.d_kelian);
		emojiMap.put("[打哈气]", R.mipmap.d_dahaqi);
		emojiMap.put("[挤眼]", R.mipmap.d_jiyan);
		emojiMap.put("[失望]", R.mipmap.d_shiwang);
		emojiMap.put("[顶]", R.mipmap.d_ding);
		emojiMap.put("[疑问]", R.mipmap.d_yiwen);
		emojiMap.put("[困]", R.mipmap.d_kun);
		emojiMap.put("[感冒]", R.mipmap.d_ganmao);
		emojiMap.put("[拜拜]", R.mipmap.d_baibai);
		emojiMap.put("[黑线]", R.mipmap.d_heixian);
		emojiMap.put("[阴险]", R.mipmap.d_yinxian);
		emojiMap.put("[打脸]", R.mipmap.d_dalian);
		emojiMap.put("[傻眼]", R.mipmap.d_shayan);
		emojiMap.put("[猪头]", R.mipmap.d_zhutou);
		emojiMap.put("[熊猫]", R.mipmap.d_xiongmao);
		emojiMap.put("[兔子]", R.mipmap.d_tuzi);
	}
	
	public static int getImgByName(String imgName) {
		Integer integer = emojiMap.get(imgName);
		return integer == null ? -1 : integer;
	}
}
