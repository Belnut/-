package kr.ac.suwon.it402.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class PasswordModifyActivity extends Activity {
    Button btnPassword, btnCancel, btnOk;
    EditText pw_current, pw_new1, pw_new2;
    static int renew = 0;

    DBManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_modify);
        setTitle("비밀번호 변경");

        dbm = new DBManager(this);

        pw_current = (EditText)findViewById(R.id.pw_current);
        pw_new1 = (EditText)findViewById(R.id.pw_new1);
        pw_new2 = (EditText)findViewById(R.id.pw_new2);
        btnOk = (Button)findViewById(R.id.buttonOk);
        btnCancel = (Button)findViewById(R.id.buttonCancel);
        btnPassword = (Button)findViewById(R.id.buttonPassword);

        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                //비밀번호 변경
                pw_current.setVisibility(View.VISIBLE);
                pw_new1.setVisibility(View.VISIBLE);
                pw_new2.setVisibility(View.VISIBLE);
                btnOk.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //확인
                if(pw_current.getText().toString().length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "현재 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if((pw_new1.getText().toString().length() == 0) || (pw_new2.getText().toString().length() == 0))
                {
                    Toast.makeText(getApplicationContext(), "새 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i("아이디/비번", Login.sv_id + "/" + pw_current.getText().toString());
                dbm.renew_pw(Login.sv_id, pw_current.getText().toString());
                if(renew == 0) {
                    Toast.makeText(getApplicationContext(), "현재 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!checkPw(pw_new1.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!pw_new1.getText().toString().equals(pw_new2.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 재확인 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Log.i("아이디/비번 2", Login.sv_id + "/" + pw_new1.getText().toString());
                    dbm.change_pw(Login.sv_id, pw_new1.getText().toString());
                    Toast.makeText(getApplicationContext(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();

                    Intent homeIntent = new Intent(getApplicationContext(), Login.class);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //취소
                finish();
            }
        });
    }

    protected boolean checkPw(String str)
    {
        boolean result = Pattern.matches("^[a-zA-Z0-9!@.#$%^&*?_~]{8,16}$", str);

        if(str.length() > 0 && result)
            return true;
        else
            return false;
    }
}
