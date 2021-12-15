package com.precloud.deliverystar.Api;

import com.precloud.deliverystar.Model.GetRiderStatusResponse;
import com.precloud.deliverystar.Model.GetRiderStatusWrapper;
import com.precloud.deliverystar.Model.GetUserProfileWrapper;
import com.precloud.deliverystar.Model.LoginWrapper;
import com.precloud.deliverystar.Model.OrderWrapper;
import com.precloud.deliverystar.Model.ProfileUpdateWrapper;
import com.precloud.deliverystar.Model.UpdateOrderStatusWrapper;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login")
    Call<LoginWrapper> login(@Field("mobile") String mobile,
                             @Field("password") String password,
                             @Field("device_token") String device_token,
                             @Field("device_type") String device_type );

    @FormUrlEncoded
    @POST("getUserById")
    Call<GetUserProfileWrapper>getUserProfile(@Field("user_id") String user_id);



    @FormUrlEncoded
    @POST("updateProfile")
    Call<ProfileUpdateWrapper>updateProfile(@Field("user_id") String user_id,
                                            @Field("name") String name,
                                            @Field("email") String email,
                                            @Field("address") String address);


    @FormUrlEncoded
    @POST("updateRiderStatus")
    Call<UpdateOrderStatusWrapper>updateRiderStatus(@Field("user_id") String user_id,
                                                   @Field("online_status") String name,
                                                    @Field("latitude") String latitude,
                                                    @Field("longitude") String longitude  );

    @FormUrlEncoded
    @POST("updateOrderStatus")
    Call<UpdateOrderStatusWrapper>updateOrderStatus(@Field("user_id") String user_id,
                                                    @Field("order_id")String orderId,
                                                    @Field("order_status") String name,
                                               @Field("latitude") String latitude,
                                               @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("getOrderByRiderId")
    Call<OrderWrapper>getOrderByRiderId(@Field("user_id") String user_id,
                                        @Field("order_status") String name);

    @FormUrlEncoded
    @POST("getOrderByRiderId")
    Call<OrderWrapper>getOrderByDate(@Field("user_id") String user_id,
                                        @Field("order_status") String name,
                                     @Field("order_date") String date);
    @FormUrlEncoded
    @POST("generateOtp")
    Call<UpdateOrderStatusWrapper>generateOtp(@Field("mobile") String user_id);

    @FormUrlEncoded
    @POST("updateRiderLocation")
    Call<UpdateOrderStatusWrapper>updateRiderLocation(@Field("user_id") String user_id,
                                                      @Field("latitude") String latitide,
                                                      @Field("longitude")  String longitude);
    @FormUrlEncoded
    @POST("getRiderStatus")
    Call<GetRiderStatusWrapper> getRiderStatus(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getUnassinedOrderByRiderId")
    Call<OrderWrapper>getUnassinedOrderByRiderId(@Field("user_id") String user_id,
                                        @Field("order_status") String name);

    @FormUrlEncoded
    @POST("acceptOrderStatus")
    Call<UpdateOrderStatusWrapper>acceptOrderStatus(@Field("user_id") String user_id,
                                                    @Field("order_id")String orderId,
                                                    @Field("order_status") String name,
                                                    @Field("latitude") String latitude,
                                                    @Field("longitude") String longitude);

}
