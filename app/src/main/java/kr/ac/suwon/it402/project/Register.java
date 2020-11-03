package kr.ac.suwon.it402.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Register extends Activity {

    AlertDialog mAlterDialog = null;
    DatePickerDialog mDatePickerDialog = null;

    EditText input_stdnum, input_id, input_pw, input_name, input_email;
    EditText get_ph_mid, get_ph_last;
    EditText second_pwd;
    ImageView chk_pwd;

    Button birth_btn;
    int myear = 0, mmonth = 0, mday = 0;
    String input_ph, input_birth;
    Spinner get_ph_first;

    static int checked_birth_btn = 0;
    static int checked_same_id = 0;

    DBManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        input_stdnum = (EditText)findViewById(R.id.reg_stdnum_edit);
        input_id = (EditText)findViewById(R.id.reg_id_edit);
        input_pw = (EditText)findViewById(R.id.reg_pwd_edit);
        input_name = (EditText)findViewById(R.id.reg_name_edit);
        input_email = (EditText)findViewById(R.id.reg_mail_edit);
        second_pwd = (EditText)findViewById(R.id.reg_check_pwd);

        chk_pwd = (ImageView)findViewById(R.id.same_or_not);


        get_ph_mid = (EditText)findViewById(R.id.reg_phnum_mid_edit);
        get_ph_last = (EditText)findViewById(R.id.reg_phnum_last_edit);

        get_ph_first = (Spinner)findViewById(R.id.spin_phnum);

        birth_btn = (Button)findViewById(R.id.reg_birth_btn);

        dbm = new DBManager(this);

        input_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checked_same_id = 0;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((input_pw.getText().toString()).equals((second_pwd.getText().toString())))
                    chk_pwd.setImageDrawable(getResources().getDrawable(R.drawable.same_img));
                else if((second_pwd.getText().toString().equals("") && input_pw.getText().toString().equals("")))
                    chk_pwd.setImageDrawable(null);
                else
                    chk_pwd.setImageDrawable(getResources().getDrawable(R.drawable.cancel_img));
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        second_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((second_pwd.getText().toString()).equals((input_pw.getText().toString())))
                    chk_pwd.setImageDrawable(getResources().getDrawable(R.drawable.same_img));
                else if((second_pwd.getText().toString().equals("") && input_pw.getText().toString().equals("")))
                    chk_pwd.setImageDrawable(null);
                else
                    chk_pwd.setImageDrawable(getResources().getDrawable(R.drawable.cancel_img));
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    protected boolean checkStdnum(String str)
    {
        boolean result = Pattern.matches("^[0-9]{8,8}$", str);

        if(str.length() > 0 && result)
            return true;
        else
            return false;
    }
    protected boolean checkId(String str)
    {
        boolean result = Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9]{4,11}$", str);

        if(str.length() > 0 && result)
            return true;
        else
            return false;
    }

    protected boolean checkPw(String str)
    {
        boolean result = Pattern.matches("^(?=.*[a-zA-Z0-9])(?=.*[!@#$%^*+=-])(?=.*).{8,16}$", str);

        if(str.length() > 0 && result)
            return true;
        else
            return false;
    }

    protected boolean checkName(String str)
    {
        boolean result = Pattern.matches("^[ㄱ-ㅎㅏ-ㅣ가-힣]{2,5}$", str);

        if(str.length() > 0 && result)
            return true;
        else
            return false;
    }

    protected boolean checkMail(String str)
    {
        boolean result = Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+.[a-zA-Z]{2,3}$", str);

        if(str.length() > 0 && result)
            return true;
        else
            return false;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_birth_btn: {
                DatePickerDialog.OnDateSetListener call = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Toast.makeText(Register.this, year + "년" + (monthOfYear + 1) + "월" + dayOfMonth + "일", Toast.LENGTH_LONG).show();
                        myear = year;
                        mmonth = monthOfYear + 1;
                        mday = dayOfMonth;
                        checked_birth_btn = 1;
                        birth_btn.setText(year + "년" + (monthOfYear+1) + "월" + dayOfMonth + "일");
                    }
                };
                mDatePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, call, 2000, 0, 1);
                mDatePickerDialog.show();
                break;
            }
            case R.id.reg_check_id: {
                dbm.chk_Id(input_id.getText().toString());
                if(!checkId(input_id.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "아이디를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(checked_same_id == 1)
                {
                    Toast.makeText(getApplicationContext(), "중복된 아이디가 존재합니다.", Toast.LENGTH_SHORT).show();
                    checked_same_id = 0;
                    return;
                }
                else if(checked_same_id == 2) {
                    Toast.makeText(getApplicationContext(), "사용가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            }
            case R.id.register_btn: {
                if(!checkStdnum(input_stdnum.getText().toString())){
                    Toast.makeText(getApplicationContext(), "학번을 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!checkId(input_id.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "아이디를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }  else if(!checkStdnum(input_stdnum.getText().toString()) || checked_same_id == 0) {
                    Toast.makeText(getApplicationContext(), "아이디 중복 체크를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!checkPw(input_pw.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!(second_pwd.getText().toString()).equals((input_pw.getText().toString()))) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 재확인 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!checkName(input_name.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "이름을 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (checked_birth_btn == 0) {
                    Toast.makeText(getApplicationContext(), "생년월일을 정해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(get_ph_first.getSelectedItemPosition() == 0 || !(get_ph_mid.length() == 4 && get_ph_last.length() == 4)) {
                    Toast.makeText(getApplicationContext(), "전화번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!(checkMail(input_email.getText().toString()))){
                    Toast.makeText(getApplicationContext(), "이메일을 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if( get_ph_first.getSelectedItemPosition() == 1)
                {
                    input_ph = "010";
                } else if( get_ph_first.getSelectedItemPosition() == 2)
                {
                    input_ph = "011";
                } else if( get_ph_first.getSelectedItemPosition() == 3)
                {
                    input_ph = "016";
                } else if( get_ph_first.getSelectedItemPosition() == 4)
                {
                    input_ph = "017";
                } else if( get_ph_first.getSelectedItemPosition() == 5)
                {
                    input_ph = "019";
                }

                input_ph += get_ph_mid.getText().toString() + get_ph_last.getText().toString();
                input_birth = "" + myear + mmonth + mday;

                Log.i("값 확인",input_stdnum.getText().toString() + input_id.getText().toString() + input_pw.getText().toString() +
                        input_name.getText().toString() +  input_birth + input_ph + input_email.getText().toString());

                dbm.account(input_stdnum.getText().toString(), input_id.getText().toString(), input_pw.getText().toString(),
                        input_name.getText().toString(), input_birth, input_ph, input_email.getText().toString());
                dbm.account_table(input_id.getText().toString());
                input_ph = "";
                input_birth = "";
                Toast.makeText(getApplicationContext(), "회원가입이 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
            case R.id.reg_cancel_btn: {
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
                mAlterDialog = builder.setTitle("회원가입을 종료하시겠습니까?").
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
        mAlterDialog = builder.setTitle("회원가입을 종료하시겠습니까?").
                setPositiveButton("확인", listener).setNegativeButton("취소", listener).show();
    }
}
