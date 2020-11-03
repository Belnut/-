package kr.ac.suwon.it402.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileModifyActivity extends Activity {
    Button btnModify, btnEsc;
    TextView textEmail, textPhone, textName;
    public static String out_name, out_ph;

    DBManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modify);
        setTitle("내 정보");

        dbm = new DBManager(this);

        textName = (TextView)findViewById(R.id.textViewName);
        textEmail = (TextView)findViewById(R.id.textViewEmail);
        textPhone = (TextView)findViewById(R.id.textViewPhone);
        btnModify = (Button)findViewById(R.id.buttonModify);
        btnEsc = (Button)findViewById(R.id.buttonEsc);

        textName.setText(out_name);
        textEmail.setText(ProfileActivity.out_mail);
        textPhone.setText(PhoneNumberUtils.formatNumber(out_ph));

        btnModify.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                //수정
                Intent intent = new Intent(getApplicationContext(), PhoneEmailActivity.class);
                startActivity(intent);
            }
        });

        btnEsc.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                dbm.account_delete(Login.sv_id);
                dbm.account_table_delete(Login.sv_id);

                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                };

                DialogInterface.OnClickListener escListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(getApplication(), "회원탈퇴 완료", Toast.LENGTH_LONG).show();

                        Intent homeIntent = new Intent(getApplicationContext(), Login.class);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                };
                new AlertDialog.Builder(ProfileModifyActivity.this)
                        .setTitle("현재 계정을 탈퇴하시겠습니까?")
                        .setPositiveButton("탈퇴", escListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            }
        });
    }
}
