package kr.ac.suwon.it402.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Pattern;

public class PhoneEmailActivity extends Activity {
    Spinner phone_first;
    EditText currentEmail, newEmail, phone_mid, phone_last;
    Button emailModify, phoneModify;
    String new_phone;

    static int chk = 0;

    DBManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_email);
        setTitle("연락처 및 이메일 수정");

        dbm = new DBManager(this);

        currentEmail = (EditText)findViewById(R.id.CurrentEmail);
        newEmail = (EditText)findViewById(R.id.NewEmail);
        phone_first = (Spinner)findViewById(R.id.phone_first);
        phone_mid = (EditText)findViewById(R.id.phone_mid);
        phone_last = (EditText)findViewById(R.id.phone_last);
        emailModify = (Button)findViewById(R.id.buttonEmail);
        phoneModify = (Button)findViewById(R.id.buttonPhone);

        emailModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이메일 수정버튼
                dbm.chk_mail(Login.sv_id, currentEmail.getText().toString()); //현재 이메일 디비 검사
                //chk가 1이면 이메일 값 확인 완료
                if(chk != 1)
                {
                    Toast.makeText(getApplicationContext(), "현재 이메일 주소가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(checkMail(newEmail.getText().toString()))){
                    Toast.makeText(getApplicationContext(), "이메일을 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbm.change_mail(Login.sv_id, newEmail.getText().toString()); //새로 입력한 이메일로 수정

                Toast.makeText(getApplicationContext(), "이메일 주소가 변경되었습니다.", Toast.LENGTH_SHORT).show();

                //나중에 바로 바뀔수 있도록 수정
                Intent homeIntent = new Intent(getApplicationContext(), SettingActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
            }
        });

        phoneModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //휴대전화 수정버튼
                if(phone_first.getSelectedItemPosition() == 0 || !(phone_mid.length() == 4 && phone_last.length() == 4)) {
                    Toast.makeText(getApplicationContext(), "전화번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if( phone_first.getSelectedItemPosition() == 1)
                {
                    new_phone = "010";
                } else if( phone_first.getSelectedItemPosition() == 2)
                {
                    new_phone = "011";
                } else if( phone_first.getSelectedItemPosition() == 3)
                {
                    new_phone = "016";
                } else if( phone_first.getSelectedItemPosition() == 4)
                {
                    new_phone = "017";
                } else if( phone_first.getSelectedItemPosition() == 5)
                {
                    new_phone = "019";
                }

                //new_phone -> 새로운 휴대전화번호(String)
                new_phone += phone_mid.getText().toString() + phone_last.getText().toString();
                dbm.change_ph(Login.sv_id, new_phone); //새로운 번호로 변경

                Toast.makeText(getApplicationContext(), "휴대전화 번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();

                //나중에 수정
                Intent homeIntent = new Intent(getApplicationContext(), SettingActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
            }
        });
    }

    protected boolean checkMail(String str)
    {
        boolean result = Pattern.matches("^[0-9a-zA-Z_\\-]+@[.0-9a-zA-Z_\\-]+$", str);

        if(str.length() > 0 && result)
            return true;
        else
            return false;
    }
}
