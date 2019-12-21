package com.example.weatherapp;

import androidx.annotation.MainThread;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WeatherFragment.OnFragmentDataListener {
    final Context context = this;
    String cityName="";
    MyPageAdapter  adapterViewPager;

    private void cityInputDialog()
    {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.promt_addcity, null);
        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);
        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:
                                cityName=userInput.getText().toString();

                                Log.d("city", cityName);
                                System.out.println(cityName);
                                if(cityName.equals("")){
                                    Toast.makeText(context,"Вы ничего не ввели", Toast.LENGTH_LONG).show();}
                                else{
                                adapterViewPager.add(cityName);}
                                cityName=null;
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        //Создаем AlertDialog:
        AlertDialog alertDialog = mDialogBuilder.create();
        //и отображаем его:
        alertDialog.show();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_city:
                cityInputDialog();
                return true;
            case R.id.exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar(); //Открытие тулбара
        ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
        adapterViewPager = new MyPageAdapter (getSupportFragmentManager());
        vpPager.setOffscreenPageLimit(1);
        vpPager.setAdapter(adapterViewPager);
        vpPager.setSaveFromParentEnabled(false);
        cityInputDialog();
    }
        //Интерфейс для получения данных с фрагмента
    @Override
    public void sendData(int Num) {
        adapterViewPager.del(Num);
    }

    public class MyPageAdapter extends FragmentPagerAdapter {

        private final List<String> mFragmentList  = new ArrayList<>();
        private long baseId = 0;

        public MyPageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return mFragmentList .size();
        }

        @Override
        public Fragment getItem(int position) {
           return WeatherFragment.newInstance(cityName,position);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
       public long getItemId(int position) {
            return baseId + position;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
        public void removeSavedState(int position) {
            if(position < mFragmentList.size()) {
                mFragmentList.set(position, null);
            }
        }

        public void add(String city) {
            mFragmentList .add(city);
            notifyDataSetChanged();
        }
        public void del(int pos) {
            mFragmentList.remove(pos-1);
            removeSavedState(pos);
            notifyDataSetChanged();
        }
    }
}
