package com.ust.dtmu.smsc.appencuesta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ust.dtmu.smsc.appencuesta.entities.RespContact;
import com.ust.dtmu.smsc.appencuesta.network.ApiService;
import com.ust.dtmu.smsc.appencuesta.network.RetrofitBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {
    ApiService service;
    TokenManager tokenManager;
    Call<RespContact>call;
    Button btn_contacto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));
        if(tokenManager.getToken() == null){
            startActivity(new Intent(TestActivity.this, MainActivity.class));
            finish();
        }
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        btn_contacto = (Button)findViewById(R.id.btn_contact);
        btn_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call = service.contactos();
                call.enqueue(new Callback<RespContact>() {
                    @Override
                    public void onResponse(Call<RespContact> call, Response<RespContact> response) {

                            Log.w("TESTACTIVITY","onResponse"+response);
                            if (response.code() == 401){
                                tokenManager.deleteToken();
                                startActivity(new Intent(TestActivity.this, MainActivity.class));
                                finish();
                            }

                    }

                    @Override
                    public void onFailure(Call<RespContact> call, Throwable t) {
                        Log.w("FALLA","onFaliure "+t.getMessage());
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
            call = null;
        }
    }
}
