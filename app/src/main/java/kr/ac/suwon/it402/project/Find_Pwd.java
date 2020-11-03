package kr.ac.suwon.it402.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.regex.Pattern;


public class Find_Pwd extends Activity {

    AlertDialog mAlterDialog = null;

    Button temp, pwd_cancel;
    EditText pwd_input_id, pwd_input_mail;
    GmailSender sender;
    String txt, imsi_out_pwd, myJSON;
    DBManager dbm;
    private static final String TAG_RESULTS="result";

    static int chk = 0;

    JSONArray JSONm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_pwd);

        pwd_input_id = (EditText)findViewById(R.id.pwd_id_edit);
        pwd_input_mail = (EditText)findViewById(R.id.pwd_mail_edit);

        temp = (Button)findViewById(R.id.find_pwd_btn);
        pwd_cancel = (Button)findViewById(R.id.cancel_btn);

        dbm = new DBManager(this);
    }

    protected boolean checkId(String str)
    {
        boolean result = Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9]{4,11}$", str);

        if(str.length() > 0 && result)
            return true;
        else
            return false;
    }

    protected boolean checkMail(String str)
    {
        boolean result = Pattern.matches("^[0-9a-zA-Z_\\-]+@[.0-9a-zA-Z_\\-]+$", str);

        if(str.length() > 0 && result)
            return true;
        else
            return false;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_pwd_btn: {
                int[] a = new int[6];
                int i ;
                for(i=0; i<6; i++)
                    a[i] = (int) (Math.random() * 10);

                if(!checkId(pwd_input_id.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "아이디를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!(checkMail(pwd_input_mail.getText().toString()))){
                    Toast.makeText(getApplicationContext(), "이메일을 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbm.chk_id_mail(pwd_input_id.getText().toString(), pwd_input_mail.getText().toString());
                if(chk == 0) {
                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                        @Override
                        //dialoginterface = 취소, 종료를 위한 인터페이스 제공 // which = 어떤 버튼 혹은 아이템을 클릭 했는지 구분
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case android.app.AlertDialog.BUTTON_POSITIVE:
                                    break;
                            }
                        }
                    };
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    mAlterDialog = builder.setTitle("입력하신 내용과 일치하는 정보가 없습니다.").
                            setPositiveButton("확인", listener).show();
                    builder.show();
                } else {

                    //아이디 이메일이 정확한지 확인
                    //임시 비밀번호 전송
                    imsi_out_pwd = "it" + a[0] + a[1] + a[2] + a[3] + a[4] + a[5]; //임시 비밀번호 값
                    dbm.change_pw(pwd_input_id.getText().toString() ,imsi_out_pwd);
                    sender = new GmailSender("it402lab@gmail.com", "ylcho402");
                    try {
                        sender.sendMail("IT402 Calendar 임시 비밀번호 지급 메일입니다.",
                                pwd_input_id.getText().toString() + "님의 임시 비밀번호는" + imsi_out_pwd + "입니다.",
                                "it402lab@gmail.com",
                                pwd_input_mail.getText().toString());
                        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                            @Override
                            //dialoginterface = 취소, 종료를 위한 인터페이스 제공 // which = 어떤 버튼 혹은 아이템을 클릭 했는지 구분
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case AlertDialog.BUTTON_POSITIVE:
                                        finish();
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        mAlterDialog = builder.setTitle("임시 비밀번호 발송을 완료하였습니다.").
                                setPositiveButton("확인", listener).show();
                    } catch (Exception e) {
                        Log.e("SendMail", e.getMessage(), e);
                    }
                }
                break;
            }
            case R.id.cancel_btn: {
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    //dialoginterface = 취소, 종료를 위한 인터페이스 제공 // which = 어떤 버튼 혹은 아이템을 클릭 했는지 구분
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case AlertDialog.BUTTON_POSITIVE:
                                finish();
                                break;
                            case AlertDialog.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                mAlterDialog = builder.setTitle("비밀번호 찾기를 종료하시겠습니까?").
                        setPositiveButton("확인", listener).setNegativeButton("취소", listener).show();
                break;
            }
        }
    }

    public void onBackPressed() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            //dialoginterface = 취소, 종료를 위한 인터페이스 제공 // which = 어떤 버튼 혹은 아이템을 클릭 했는지 구분
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case AlertDialog.BUTTON_POSITIVE:
                        finish();
                        break;
                    case AlertDialog.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlterDialog = builder.setTitle("비밀번호 찾기를 종료하시겠습니까?").
                setPositiveButton("확인", listener).setNegativeButton("취소", listener).show();
    }
}