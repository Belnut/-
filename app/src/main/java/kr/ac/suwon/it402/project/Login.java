package kr.ac.suwon.it402.project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

import static java.lang.Thread.sleep;

public class Login extends AppCompatActivity {

    EditText id;
    EditText pwd;
    TextView chk_sv;

    Button login;
    Button register;
    Button find_id;
    Button find_pwd;

    String result;

    ImageView chk_server_img;

    static int chk = 0;
    static String sv_id;
    int change_img = 0;

    DBManager dbm;
    CheckServerTask mCheckServerTask = null;

    CalendarDBManager calDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText)findViewById(R.id.id_etext);
        pwd = (EditText)findViewById(R.id.pwd_etext);
        chk_sv = (TextView)findViewById(R.id.chk_server);

        login = (Button)findViewById(R.id.login_btn);
        register = (Button)findViewById(R.id.reg_btn);
        find_id = (Button)findViewById(R.id.find_id);
        find_pwd = (Button)findViewById(R.id.find_pwd);

        chk_server_img = (ImageView)findViewById(R.id.server_chk_img);

        dbm = new DBManager(this);
        calDB = CalendarDBManager.getInstance(this);

        Thread chk_sv_Thread = new HandlerThread("chk_server")
        {
            public void run()
            {
                while (true){
                    try{
                        mCheckServerTask = new CheckServerTask();
                        mCheckServerTask.execute();
                        Thread.sleep(15000);
                        //mCheckServerTask.cancel(false);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    mCheckServerTask.cancel(true);
                }
            }
        };

        chk_sv_Thread.start();

        pwd.setImeOptions(EditorInfo.IME_ACTION_GO);

        pwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId) {
                    case EditorInfo.IME_ACTION_GO:
                        login.performClick();
                        break;
                }
                return false;
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn: {
                dbm.login(id.getText().toString(), pwd.getText().toString());
                if(chk == 0){
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
                    id.setText("");
                    pwd.setText("");
                } else if(chk == 1) {
                    sv_id = id.getText().toString();
                    Intent goToMain = new Intent(this, MainActivity.class);
                    goToMain.putExtra("id", id.getText().toString());
                    goToMain.putExtra("pwd", pwd.getText().toString());

                    dbm.getData("http://223.195.109.37/project/get_info.php?user_name=" + id.getText().toString(), 0);
                    dbm.getData("http://223.195.109.37/project/get_cal_db.php?user_id=" + id.getText().toString(), 1);

                    id.setText("");
                    pwd.setText("");

                    startActivity(goToMain);

                }
                break;
            }
            case R.id.reg_btn: {
                Intent goToRegister = new Intent(Login.this, Register.class);
                startActivity(goToRegister);
                break;
            }
            case R.id.find_id: {
                Intent goToFindId = new Intent(this, Find_Id.class);
                startActivity(goToFindId);
                break;
            }
            case R.id.find_pwd: {
                Intent goToFindPwd = new Intent(this, Find_Pwd.class);
                startActivity(goToFindPwd);
                break;
            }
        }
    }

    private class CheckServerTask extends AsyncTask<Integer, Integer, Integer>
    {
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Integer doInBackground(Integer... arg0) {
            result = request("http://223.195.109.37");
            return null;
        }

        protected  void onPostExecute(Integer a) {
            chk_sv.setText(result);
        }
    }

    private String request(String urlStr) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                int resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line != null) {
                            output.append("현재 서버 접속이 원활합니다.");
                            handler.sendEmptyMessage(0);
                            break;
                        }
                    }

                    reader.close();
                    conn.disconnect();
                }
            }
        } catch(Exception ex) {
            Log.e("SampleHTTP", "Exception in processing response.", ex);
            output.append("서버 장애로 인하여 접속에 어려움이 있습니다.");
            handler.sendEmptyMessage(1);
            ex.printStackTrace();
        }

        return output.toString();
    }

    Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 0:
                    chk_server_img.setImageDrawable(getResources().getDrawable(R.drawable.roundgreen));
                    break;

                default:
                    chk_server_img.setImageDrawable(getResources().getDrawable(R.drawable.roundred));
                    break;
            }
        }
    };


    void updateCalenderDB()
    {
        calDB.delete(null, null);

    }
}
