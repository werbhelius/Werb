package com.wanbo.werb.api;

import com.wanbo.werb.bean.CommentsTimeLine;
import com.wanbo.werb.bean.Favorites;
import com.wanbo.werb.bean.FriendShips;
import com.wanbo.werb.bean.FriendsTimeLine;
import com.wanbo.werb.bean.MentionComment;
import com.wanbo.werb.bean.PostComments;
import com.wanbo.werb.bean.Status;
import com.wanbo.werb.bean.User;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Werb on 2016/7/26.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * get WeiBo with retrofit
 */
public interface WeiBoApi {

    @GET("users/show.json")
    Observable<User> getUserInfo(@QueryMap Map<String,String> params);

    @GET("statuses/friends_timeline.json")
    Observable<FriendsTimeLine> getFriendsTimeLine(@QueryMap Map<String,Object> params);

    @GET("comments/show.json")
    Observable<CommentsTimeLine> getCommentsById(@QueryMap Map<String,Object> params);

    @GET("statuses/mentions.json")
    Observable<FriendsTimeLine> getMessageAtWeiBo(@QueryMap Map<String,Object> params);

    @GET("comments/mentions.json")
    Observable<MentionComment> getMessageAtComment(@QueryMap Map<String,Object> params);

    @GET("comments/to_me.json")
    Observable<MentionComment> getMessageGetComment(@QueryMap Map<String,Object> params);

    @GET("comments/by_me.json")
    Observable<MentionComment> getMessageSendComment(@QueryMap Map<String,Object> params);

    @GET("friendships/friends.json")
    Observable<FriendShips> getFriendById(@QueryMap Map<String,Object> params);

    @GET("friendships/followers.json")
    Observable<FriendShips> getFollowerById(@QueryMap Map<String,Object> params);

    @GET("friendships/friends/bilateral.json")
    Observable<FriendShips> getBilateralById(@QueryMap Map<String,Object> params);

    @GET("favorites.json")
    Observable<Favorites> getFavorites(@QueryMap Map<String,Object> params);

    @GET("statuses/user_timeline.json")
    Observable<FriendsTimeLine> getUserWeiBoTimeLine(@QueryMap Map<String,Object> params);

    @FormUrlEncoded
    @POST("comments/create.json")
    Observable<PostComments> setComment(@FieldMap Map<String,Object> params);

    @FormUrlEncoded
    @POST("statuses/repost.json")
    Observable<PostComments> setRepost(@FieldMap Map<String,Object> params);

    @FormUrlEncoded
    @POST("comments/reply.json")
    Observable<PostComments> setCommentToReply(@FieldMap Map<String,Object> params);

    @FormUrlEncoded
    @POST("attitudes/create.json")
    Observable<PostComments> setLike(@FieldMap Map<String,Object> params);

    @Multipart
    @POST("statuses/upload.json")
    Observable<Status> sendWeiBoWithImg(
            @Part("access_token") RequestBody accessToken,
            @Part("status") RequestBody context,
            @Part("pic\";filename=\"file") RequestBody requestBody);

    @FormUrlEncoded
    @POST("statuses/update.json")
    Observable<Status> sendWeiBoWithText(@FieldMap Map<String,Object> params);
}
