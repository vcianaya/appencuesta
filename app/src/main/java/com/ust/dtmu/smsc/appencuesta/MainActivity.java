package com.ust.dtmu.smsc.appencuesta;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.ust.dtmu.smsc.appencuesta.entities.AccessToken;
import com.ust.dtmu.smsc.appencuesta.entities.LoginError;
import com.ust.dtmu.smsc.appencuesta.network.ApiService;
import com.ust.dtmu.smsc.appencuesta.network.RetrofitBuilder;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button btn_login;
    TextInputLayout txt_usuario, txt_password;

    ApiService service;
    Call<AccessToken>call;
    AwesomeValidation validator;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SERVICE HTTP
        service = RetrofitBuilder.createService(ApiService.class);
        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));
        setupRules();
        if (tokenManager.getToken().getAccessToken() != null){
            startActivity(new Intent(MainActivity.this, PrincipalActivity.class));
            finish();
        }
        btn_login = (Button)findViewById(R.id.btn_login);
        txt_usuario = (TextInputLayout)findViewById(R.id.til_email);
        txt_password = (TextInputLayout)findViewById(R.id.til_password);



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.clear();
                if (validator.validate()) {
                    call = service.register(txt_usuario.getEditText().getText().toString(), txt_password.getEditText().getText().toString());
                    call.enqueue(new Callback<AccessToken>() {
                        @Override
                        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                            if (response.isSuccessful()) {
                                Log.w("RESPONSE", "onResponse: " + response.body());
                                tokenManager.saveToken(response.body());
                                startActivity(new Intent(MainActivity.this, PrincipalActivity.class));
                                finish();
                            } else {
                                handleErrors(response.errorBody());

                            }
                        }

                        @Override
                        public void onFailure(Call<AccessToken> call, Throwable t) {
                            Log.w("TAG", "onFaliuer: " + t.getMessage());
                        }
                    });
                }
            }
        });


    }

    public void setupRules(){
        validator.addValidation(MainActivity.this,R.id.til_email, RegexTemplate.NOT_EMPTY,R.string.required);
        validator.addValidation(MainActivity.this,R.id.til_password, RegexTemplate.NOT_EMPTY,R.string.required);
    }
    private void handleErrors(ResponseBody response){
         LoginError loginError = Utils.loginErrorConver(response);
         if (loginError.getUsername() != null)
             txt_usuario.setError(loginError.getUsername().get(0));
         if (loginError.getPassword() !=null)
             txt_password.setError(loginError.getPassword().get(0));
         if (loginError.getMessage() != null)
             Toast.makeText(getApplicationContext(), loginError.getMessage(), Toast.LENGTH_LONG).show();
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
