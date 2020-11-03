package kr.ac.suwon.it402.project;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by 원기 on 2016-08-01.
 */
public class DBManager {
    private String urlPath;
    public ArrayList<String> results;

    String myJSON;
    String out_name, out_mail, out_ph;
    private static final String TAG_RESULTS="result";
    JSONArray JSONm = null;

    private final String accout_php = "http://223.195.109.37/project/account.php";
    private final String chk_Id_php = "http://223.195.109.37/project/chk_id.php";
    private final String login_php = "http://223.195.109.37/project/login.php";
    private final String account_table_php  = "http://223.195.109.37/project/account_table.php";
    private final String chk_Id_Mail_php = "http://223.195.109.37/project/chk_id_mail.php";
    private final String change_pw_php = "http://223.195.109.37/project/change_pw.php";
    private final String chk_Pw_php = "http://223.195.109.37/project/chk_pw.php";
    private final String account_delete_php =  "http://223.195.109.37/project/account_delete.php";
    private final String account_table_delete_php =  "http://223.195.109.37/project/account_table_delete.php";
    private final String chk_Mail_php = "http://223.195.109.37/project/chk_mail.php";
    private final String change_mail_php = "http://223.195.109.37/project/change_mail.php";
    private final String change_ph_php = "http://223.195.109.37/project/change_ph.php";
    private final String insert_schadule_php = "http://223.195.109.37/project/insert_cal_db.php";
    private final String update_schadule_php = "http://223.195.109.37/project/update_cal_db.php";
    private final String delete_schadule_php = "http://223.195.109.37/project/delete_cal_db.php";

    /*  php파일 uri 설정
    private final String sign_up_Path = "http://223.195.109.37/cartapp/signup_user.php";
    */

    private String st_number, user_id, user_pw, user_name, user_birth, ph_number, user_mail;
    private String title, time, content, repeat, color;
    private int list_id, year, month, day, alarm;


    CalendarDBManager mcalDB;
    Context mContext;

    static int status = 0;
    static int get_status = 0;
    //상태에 따라서 DB실행


    public DBManager(Context val)
    {
        mContext = val;
    }

    class Manager extends AsyncTask<Void, Void, ArrayList<String>> {
        protected ArrayList<String> doInBackground(Void... voids) {
            if(status == 0) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "st_number=" + st_number + "&id=" + user_id + "&pw=" + user_pw +
                            "&name=" + user_name + "&birth=" + user_birth + "&ph_number=" + ph_number + "&mail=" + user_mail;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(status == 1) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "id=" + user_id;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line.length() + "");
                        if (line.length() == 1) {
                            Register.checked_same_id = 1;
                        } else
                            Register.checked_same_id = 2;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(status == 2) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "id=" + user_id + "&pw=" + user_pw;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line.length() + "");
                        if (line.length() == 1) {
                            Login.chk = 1;
                        } else
                            Login.chk = 0;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(status == 3) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "id=" + user_id;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line.length() + "");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(status == 4) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "id=" + user_id + "&mail=" + user_mail;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line.length() + "");
                        if (line.length() == 1) {
                            Find_Pwd.chk = 1;
                        } else
                            Find_Pwd.chk = 0;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(status == 5) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "pw=" + user_pw + "&id=" + user_id;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line.length() + "");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(status == 100) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "pw=" + user_pw + "&id=" + user_id;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line.length() + "");
                        if (line.length() == 1) {
                            PasswordModifyActivity.renew = 1;
                        } else
                            PasswordModifyActivity.renew = 0;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(status == 101) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "id=" + user_id;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line.length() + "");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(status == 102) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "id=" + user_id;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line.length() + "");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(status == 103) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "mail=" + user_mail + "&id=" + user_id;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line.length() + "");
                        if (line.length() == 1) {
                            PhoneEmailActivity.chk = 1;
                        } else
                            PhoneEmailActivity.chk = 0;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(status == 104) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "mail=" + user_mail + "&id=" + user_id;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line.length() + "");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(status == 105) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "ph_number=" + ph_number + "&id=" + user_id;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line.length() + "");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(status == 201) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "user_id=" + user_id + "&_id=" + list_id +"&year="+year +"&month="+month+"&day="+day+"&time="+time+"&title="+title+"&content="+content+ "&repeat="+repeat+"&alert="+alarm+"&color="+color;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    Log.i("DB insert start", "param = " + param);

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(status == 202) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "user_id=" + user_id + "&_id=" + list_id +"&year="+year +"&month="+month+"&day="+day+"&time="+time+"&title="+title+"&content="+content+ "&repeat="+repeat+"&alert="+alarm+"&color="+color;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    Log.i("DB update start", "param = " + param);

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(status == 203) {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");

                    String param = "user_id=" + user_id + "&_id=" + list_id;

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    Log.i("DB delete start", "param = " + param);

                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("BufferdReader", line);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        protected void onPostExecute(ArrayList<String> qResults) {super.onPostExecute(qResults);}
    }

    protected ArrayList<String> account(String st_number, String user_id, String user_pw, String user_name, String user_birth, String ph_number, String user_mail) {
        urlPath = accout_php;
        this.st_number = st_number;
        this.user_id = user_id;
        this.user_pw = user_pw;
        this.user_name = user_name;
        this.user_birth = user_birth;
        this.ph_number = ph_number;
        this.user_mail = user_mail;

        //Log.i("디비 값", st_number + user_id + user_pw + user_name + user_birth + ph_number + user_mail);

        status = 0;

        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }


    protected ArrayList<String> chk_Id(String user_id) {
        urlPath = chk_Id_php;
        this.user_id = user_id;
        status = 1;
        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }

    protected ArrayList<String> login(String user_id, String user_pw) {
        urlPath = login_php;
        this.user_id = user_id;
        this.user_pw = user_pw;
        status = 2;
        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }

    protected ArrayList<String> account_table(String user_id) {
        urlPath = account_table_php;
        this.user_id = user_id;
        status = 3;
        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }

    protected ArrayList<String> chk_id_mail(String user_id, String user_mail) {
        urlPath = chk_Id_Mail_php;
        this.user_id = user_id;
        this.user_mail = user_mail;
        status = 4;
        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }

    protected ArrayList<String> change_pw(String user_id, String user_pw) {
        urlPath = change_pw_php;
        this.user_pw = user_pw;
        this.user_id = user_id;
        status = 5;
        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }

    protected ArrayList<String> renew_pw(String user_id, String user_pw) {
        urlPath = chk_Pw_php;
        this.user_pw = user_pw;
        this.user_id = user_id;
        status = 100;
        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }

    protected ArrayList<String> account_delete(String user_id) {
        urlPath = account_delete_php;
        this.user_id = user_id;
        status = 101;
        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }

    protected ArrayList<String> account_table_delete(String user_id) {
        urlPath = account_table_delete_php;
        this.user_id = user_id;
        status = 102;
        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }

    protected ArrayList<String> chk_mail(String user_id, String user_mail) {
        urlPath = chk_Mail_php;
        this.user_mail = user_mail;
        this.user_id = user_id;
        status = 103;
        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }

    protected ArrayList<String> change_mail(String user_id, String user_mail) {
        urlPath = change_mail_php;
        this.user_mail = user_mail;
        this.user_id = user_id;
        status = 104;
        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }

    protected ArrayList<String> change_ph(String user_id, String ph_number) {
        urlPath = change_ph_php;
        this.ph_number = ph_number;
        this.user_id = user_id;
        status = 105;
        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> insert_schadule_db(String user_id, int list_id, int year, int month, int day, String title, String time, String content, String repeat, int alarm, String color) {
        urlPath = insert_schadule_php;
        this.user_id = user_id;
        this.list_id = list_id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
        this.time = time;
        this. content = content;
        this.repeat = repeat;
        this.alarm = alarm;
        this.color = color;

        status = 201;

        Log.i("DB insert start", "DB insert func");
        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> update_schadule_db(String user_id, int list_id, int year, int month, int day, String title, String time, String content, String repeat, int alarm, String color) {
        urlPath = update_schadule_php;
        this.user_id = user_id;
        this.list_id = list_id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
        this.time = time;
        this. content = content;
        this.repeat = repeat;
        this.alarm = alarm;
        this.color = color;

        status = 202;

        Log.i("DB update start", "DB insert func");
        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> delete_schadule_db(String user_id, int list_id) {
        urlPath = delete_schadule_php;
        this.user_id = user_id;
        this.list_id = list_id;

        status = 203;

        Log.i("DB delete start", "DB insert func");
        try {
            results = new Manager().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }

    public void getData(String url, final int flag){

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

                switch (flag) {
                    case 0:
                        myJSON = result;
                        //상태 값만 바꿔주면서 디비 실행 가능할 듯
                        get_Info();
                    case 1:
                        myJSON = result;
                        get_DB();
                }

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    private void get_DB() {
        int id, year , month, day, alarm;
        String title, time, content, repeat, color;
        mcalDB = CalendarDBManager.getInstance(mContext);
        mcalDB.delete(null,null);

        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            JSONm = jsonObj.getJSONArray(TAG_RESULTS);

            //Log.i("데이터 결과 값", JSONm.length()+"");

            //Log.i("get DB start", "length : "+JSONm.length());

            for(int i = 0 ; i<JSONm.length(); i++) {
                JSONObject c = JSONm.getJSONObject(i);

                //Log.i("get DB start", "c = "+c.isNull("id") + ", i = " + i);

                id = c.getInt("id");
                year = c.getInt("year");
                month = c.getInt("month");
                day = c.getInt("day");
                title = c.getString("title");
                time = c.getString("time");
                content = c.getString("content");
                repeat = c.getString("repeat");
                alarm = c.getInt("alarm");
                color = c.getString("color");

                String[] columns = new String[]{"_id", "year", "month", "day", "time","title", "contents", "date_repeat", "alert" , "color", "owner"};
                ContentValues addRowValue = new ContentValues();
                addRowValue.put("_id", id);
                addRowValue.put("year", year);
                addRowValue.put("month", month);
                addRowValue.put("day", day);
                addRowValue.put("time", time);
                addRowValue.put("title", title);
                addRowValue.put("contents", content);
                addRowValue.put("date_repeat", repeat);
                addRowValue.put("alert", alarm);
                addRowValue.put("color", color);
                addRowValue.put("owner", "me");

                //Log.i("get DB start", ""+id+year+month+day+time+title+content+repeat+alarm+color);

                mcalDB.insert(addRowValue);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void get_Info(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            JSONm = jsonObj.getJSONArray(TAG_RESULTS);

            //Log.i("데이터 결과 값", JSONm.length()+"");

            JSONObject c = JSONm.getJSONObject(0);
            out_name = c.getString("name");
            out_ph = c.getString("ph_number");
            out_mail = c.getString("mail");

            MainActivity.side_id = out_name;
            ProfileModifyActivity.out_name = out_name;
            ProfileActivity.out_mail = out_mail;
            ProfileModifyActivity.out_ph = out_ph;

            Log.i("정보 값", ""+ out_name + out_mail + out_ph);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
