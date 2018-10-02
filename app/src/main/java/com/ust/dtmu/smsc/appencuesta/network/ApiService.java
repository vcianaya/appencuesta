package com.ust.dtmu.smsc.appencuesta.network;

import com.ust.dtmu.smsc.appencuesta.entities.AccessToken;
import com.ust.dtmu.smsc.appencuesta.entities.RespContact;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("username") String name, @Field("password") String password);

    @POST("logout")
    @FormUrlEncoded
    Call<ResponseBody> logout(@Field("token") String token);

    @POST("refresh")
    @FormUrlEncoded
    Call<AccessToken>refresh(@Field("refresh_token") String refreshToken);

    @GET("contacto")
    Call<RespContact>contactos();
}
