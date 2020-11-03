package kr.ac.suwon.it402.project.Dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import kr.ac.suwon.it402.project.CalendarSchadule.AddSchdule;
import kr.ac.suwon.it402.project.R;

/**
 * Created by WHJ on 2016-08-17.
 */
public class ColorSelectDialog extends Activity{

    ImageButton color00 ,color01, color02,color03;
    ImageView selectcolor;
    String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_selecet_dialog);

        color00 = (ImageButton) findViewById(R.id.color1);
        color01 = (ImageButton) findViewById(R.id.color2);
        color02 = (ImageButton) findViewById(R.id.color3);
        color03 = (ImageButton) findViewById(R.id.color4);

        selectcolor = (ImageView) findViewById(R.id.selected_color);

        color00.setOnClickListener(colorClickListener);
        color01.setOnClickListener(colorClickListener);
        color02.setOnClickListener(colorClickListener);
        color03.setOnClickListener(colorClickListener);

        Intent intent= getIntent();
        color = intent.getStringExtra("color");

        if(color.equals("#FF78c8e6"))
        {
            selectcolor.setBackground(getDrawable(R.drawable.circleblue));
        }
        else if(color.equals("#ff99cc00"))
        {
            selectcolor.setBackground(getDrawable(R.drawable.circlegreen));
        }
        else if(color.equals("#ffff4444"))
            selectcolor.setBackground(getDrawable(R.drawable.circlered));
        else
            selectcolor.setBackground(getDrawable(R.drawable.circleyellow));

    }




    View.OnClickListener colorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ImageButton clickedView = (ImageButton)view;

            Drawable color_resource = clickedView.getBackground();

            selectcolor.setBackground(color_resource);

            switch (view.getId())
            {
                case R.id.color1:
                    color = "#ffffbb33";
                    break;
                case R.id.color2 :
                    color = "#FF78c8e6";
                    break;
                case R.id.color3:
                    color = "#ff99cc00";
                    break;
                case R.id.color4:
                    color = "#ffff4444";
                    break;
            }

        }


    };

    public void onOkCencelClickListenFunc(View v)
    {
        Intent sendColorIntent = new Intent(this, AddSchdule.class);
        switch (v.getId())
        {
            case R.id.select_color_okbtn:
                sendColorIntent.putExtra("color", color);
                setResult(RESULT_OK, sendColorIntent);

                Log.i("color_from", color);
                break;
            case R.id.select_color_cencelbtn :
                sendColorIntent.putExtra("color", "");
                setResult(RESULT_CANCELED);
                break;
        }
        finish();
    }


}
