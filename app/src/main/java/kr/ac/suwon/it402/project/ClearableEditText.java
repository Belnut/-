package kr.ac.suwon.it402.project;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class ClearableEditText extends RelativeLayout {
    LayoutInflater inflater = null;
    EditText editText;
    Button btnClear;

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayout();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayout();
    }

    public ClearableEditText(Context context) {
        super(context);
        setLayout();
    }

    private void setLayout() {
        //레이아웃을 설정
        inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.clearable_edit_text, this, true);

        editText = (EditText) findViewById(R.id.clearable_edit);
        btnClear = (Button) findViewById(R.id.clearable_button_clear);
        btnClear.setVisibility(RelativeLayout.INVISIBLE);

        clearText();
        showHideClearButton();
    }

    //X버튼이 나타났다 사라지게하는 메소드
    private void showHideClearButton() {
        //TextWatcher를 사용해 에디트 텍스트 내용이 변경 될 때 동작할 코드를 입력
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //에디트 텍스트 안 내용이 변경될 때마다 호출되는 메소드
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btnClear.setVisibility(RelativeLayout.VISIBLE);
                } else {
                    btnClear.setVisibility(RelativeLayout.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //에디트 텍스트의 내용을 없애는 메소드
    private void clearText() {
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    //메소드 호출 시 에디트 텍스트 내용을 받아올 수 있는 메소드도 만들어놓는다
    public Editable getText() {
        Editable text = editText.getText();
        return text;
    }
}
