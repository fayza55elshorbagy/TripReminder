package com.example.tripreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripreminder.adapters.SliderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeScreen extends AppCompatActivity {
    private ViewPager viewPager;
    private LinearLayout mdotsLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mdots;
    TextView skip;
    int flag;
    SharedPreferences write;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        write = getSharedPreferences("welcome", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = write.edit();
            editor.putInt("flag",1);
            flag = write.getInt("flag",0);
            editor.commit();

        if(flag == 1)
        {
           // Toast.makeText(this, "firstTime", Toast.LENGTH_SHORT).show();
            editor.putInt("flag",++flag);
            flag = 2;
        }
        if(flag == 2)
        {
            //Toast.makeText(this, "secondTime", Toast.LENGTH_SHORT).show();
            Intent m = new Intent(getApplicationContext(),SignUp.class);
            startActivity(m);
            finish();
        }

        viewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mdotsLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        addDotsindicator(0);
        viewPager.addOnPageChangeListener(viewListener);
        skip = (TextView) findViewById(R.id.skip);
    }
    public void addDotsindicator(int position)
    {
        mdots = new TextView[3];
        mdotsLayout.removeAllViews();
        for(int i =0; i< mdots.length;i++)
        {
            mdots[i] = new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextColor(getResources().getColor(R.color.gray));
            mdots[i].setTextSize(35);
            mdotsLayout.addView(mdots[i]);

        }
        if(mdots.length > 0)
        {
            mdots[position].setTextColor(getResources().getColor(R.color.appyelow));
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsindicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void skip(View view) {
        Intent MyIntent = new Intent(this,SignUp.class);
        startActivity(MyIntent);
        finish();
    }

}