package com.ust.dtmu.smsc.appencuesta;


import com.ust.dtmu.smsc.appencuesta.entities.LoginError;
import com.ust.dtmu.smsc.appencuesta.network.RetrofitBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class Utils {

    public static LoginError loginErrorConver(ResponseBody response){
        Converter<ResponseBody, LoginError> converter = RetrofitBuilder.getRetrofit().responseBodyConverter(LoginError.class, new Annotation[0]);
        LoginError loginError = null;
        try {
            loginError = converter.convert(response);
        }catch (IOException e){
            e.printStackTrace();
        }
        return loginError;
    }

}
