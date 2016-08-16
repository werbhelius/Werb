package com.wanbo.werb.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wanbo.werb.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Werb on 2016/7/18.
 * Email：1025004680@qq.com
 * Weibo 选择图片适配器
 */
public class WeiBoPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> photos;
    public List<Object> paths = new ArrayList<>();

    public WeiBoPhotoAdapter(Context ctx,List<String> list) {
        context = ctx;
        photos = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.activity_send_item_photopick,null);
        return new PhotoPickviewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PhotoPickviewHolder pickviewHolder = (PhotoPickviewHolder) holder;
        String photoUrl = photos.get(position);
        Uri uri = Uri.fromFile(new File(photoUrl));
        pickviewHolder.iv_delete.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(uri)
                .centerCrop()
                .thumbnail(0.1f)
                .into(pickviewHolder.iv_photo);
        getBitmapFromUri(uri,false,position);
        pickviewHolder.iv_delete.setOnClickListener(v->{
            Glide.clear(pickviewHolder.iv_photo);
            getBitmapFromUri(null,true,position);
            pickviewHolder.iv_delete.setVisibility(View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class PhotoPickviewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.iv_photo)
        ImageView iv_photo;
        @Bind(R.id.iv_delete)
        ImageView iv_delete;

        public PhotoPickviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private void getBitmapFromUri(Uri uri,boolean isDelete,int position){
        String path = null;
        if(uri!=null) {
            path = uri.getPath();
        }
        if(!isDelete){
            paths.add(path);
        }else {
            paths.set(position,1);
        }
    }
}
