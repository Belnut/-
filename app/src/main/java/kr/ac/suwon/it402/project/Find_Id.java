package kr.ac.suwon.it402.project;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class Find_Id extends Activity {
    TextView msg_text;
    EditText input_stdnum, input_name;
    Button find_id_btn, cancel_btn;
    String txt, out_id, myJSON;
    private static final String TAG_RESULTS="result";

    android.app.AlertDialog mAlterDialog = null;

    JSONArray JSONm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id);

        input_stdnum = (EditText)findViewById(R.id.stdnum_edit);
        input_name = (EditText)findViewById(R.id.name_edit);

        find_id_btn = (Button)findViewById(R.id.find_id_btn);
        cancel_btn = (Button)findViewById(R.id.cancel_btn);
    }

    protected boolean checkStdnum(String str)
    {
        boolean result = Pattern.matches("^[0-9]{8,8}$", str);

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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_id_btn: {
                if (!checkStdnum(input_stdnum.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "학번을 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!checkName(input_name.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "이름을 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                getData("http://223.195.109.37/project/find_id.php?st_number=" + input_stdnum.getText().toString() +"&user_name=" + input_name.getText().toString());
                //Log.i("실행2", "checked_id = " + checked_id);
                break;
            }
            case R.id.cancel_btn: {
                Toast.makeText(getApplicationContext(), "아이디 찾기를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
        }
    }

    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;

                //Log.i("실행", "실행1");
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                        //Log.i("실행", "실행3");
                    }
                    //Log.i("sb값", sb + "");
                    return sb.toString().trim();

                }catch(Exception e){
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result){
                myJSON = result;
                get_Id();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    protected void get_Id(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            JSONm = jsonObj.getJSONArray(TAG_RESULTS);

            //Log.i("데이터 결과 값", JSONm.length()+"");
            if(JSONm.length() == 0){
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
                mAlterDialog = builder.setTitle("입력하신 정보와 일치하는 아이디가 없습니다.").
                        setPositiveButton("확인", listener).show();
                builder.show();
            }

            JSONObject get_Id = JSONm.getJSONObject(0);
            out_id = get_Id.getString("id");

            //Log.i("get_id", out_id);
            if(JSONm.length() == 1){
                //Log.i("실행", "getId");
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    //dialoginterface = 취소, 종료를 위한 인터페이스 제공 // which = 어떤 버튼 혹은 아이템을 클릭 했는지 구분
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case android.app.AlertDialog.BUTTON_POSITIVE:
                                finish();
                                break;
                        }
                    }
                };
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                mAlterDialog = builder.setTitle("찾으시는 아이디는 " + out_id + "입니다.").
                        setPositiveButton("확인", listener).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            //dialoginterface = 취소, 종료를 위한 인터페이스 제공 // which = 어떤 버튼 혹은 아이템을 클릭 했는지 구분
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case android.app.AlertDialog.BUTTON_POSITIVE:
                        finish();
                        break;
                    case android.app.AlertDialog.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        mAlterDialog = builder.setTitle("아이디 찾기를 종료하시겠습니까?").
                setPositiveButton("확인", listener).setNegativeButton("취소", listener).show();
    }
}
