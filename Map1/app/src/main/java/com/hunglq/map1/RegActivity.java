package com.hunglq.map1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hunglq.map1.model.Account;

import java.util.HashMap;
import java.util.Map;

public class RegActivity extends AppCompatActivity {

    EditText edtUID, edtPwd, edtRetype;
    Button btnReg;
    public static boolean isSuccess = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        edtPwd = findViewById(R.id.edtPasswordRegister);
        edtUID = findViewById(R.id.edtUserNameRegister);
        edtRetype = findViewById(R.id.edtRetype);
        btnReg = findViewById(R.id.btnRegReg);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtRetype.getText().toString().equals("")
                        || edtUID.getText().toString().equals("")
                        || edtPwd.getText().toString().equals("")){
                    Toast.makeText(RegActivity.this, "Bạn hãy điền đủ thông tin để đăng kí!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(!edtRetype.getText().toString().equals(edtPwd.getText().toString())){
                    Toast.makeText(RegActivity.this, "Mật khẩu nhập lại không chính xác!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                sendToServer(MainActivity.url+"regAccount.php");
            }
        });
    }

    private void sendToServer(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){
                            isSuccess = true;
                            finish();
                        } else {
                            Toast.makeText(RegActivity.this,
                                    "Tên đăng nhập đã tồn tại!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegActivity.this, error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){

                Map<String, String> param = new HashMap<>();
                param.put("uid", edtUID.getText().toString().trim());
                param.put("pwd", edtPwd.getText().toString().trim());

                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}
