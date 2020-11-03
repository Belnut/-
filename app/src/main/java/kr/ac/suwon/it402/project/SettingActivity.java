package kr.ac.suwon.it402.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import kr.ac.suwon.it402.project.core.*;

public class SettingActivity extends BaseActivity {
    ListView listView_setting;
    ListViewAdapter adapter;

    DBManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("설정");

        dbm = new DBManager(this);

        //DBManager.get_status = 0;
        //dbm.getData("http://223.195.109.37/project/get_info.php?user_name=" + Login.sv_id);

        listView_setting = (ListView)findViewById(R.id.listView_setting);
        adapter = new ListViewAdapter();

        listView_setting.setAdapter(adapter);

        adapter.addItem("공지사항", "＞");
        adapter.addItem("로그인 정보", "＞");
        adapter.addItem("암호 잠금", "OFF");

        updateUI();

        listView_setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem)parent.getItemAtPosition(position);

                switch(item.getTitle())
                {
                    case "공지사항": {
                        Toast.makeText(getApplicationContext(), "공지사항", Toast.LENGTH_SHORT).show();
                    }
                        break;
                    case "로그인 정보": {
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                    }
                        break;
                    case "암호 잠금": {
                        int type = LockManager.getInstance().getAppLock().isPasscodeSet() ? AppLock.DISABLE_PASSLOCK
                                : AppLock.ENABLE_PASSLOCK;
                        Intent intent = new Intent(getApplicationContext(), AppLockActivity.class);
                        intent.putExtra(AppLock.TYPE, type);
                        startActivityForResult(intent, type);
                    }
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AppLock.DISABLE_PASSLOCK:
                break;
            case AppLock.ENABLE_PASSLOCK:
            case AppLock.CHANGE_PASSWORD:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "비밀번호가 설정되었습니다.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        updateUI();
    }

    private void updateUI() {
        if (LockManager.getInstance().getAppLock().isPasscodeSet()) {
            //비밀번호가 설정되어있을때
            adapter.modifyItem(2, "ON");
        } else {
            //비밀번호가 설정되어있지 않을때
            adapter.modifyItem(2, "OFF");
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume(){
        super.onResume();

        DBManager.get_status = 0;
        dbm.getData("http://223.195.109.37/project/get_info.php?user_name=" + Login.sv_id, 0);
    }
}
