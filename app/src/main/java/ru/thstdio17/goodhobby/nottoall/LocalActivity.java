package ru.thstdio17.goodhobby.nottoall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import ru.thstdio17.goodhobby.R;
import ru.thstdio17.goodhobby.sqlite.SqliteBD;

public class LocalActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
  Button toGenerator,toLog,clearbs;
  Switch swSeting,swLogConsole,swLogBs;
    protected SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_local);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        toGenerator=(Button)findViewById(R.id.buttonGenerator);
        toGenerator.setOnClickListener(this);
        toLog=(Button)findViewById(R.id.buttonLog);
        toLog.setOnClickListener(this);
        clearbs=(Button)findViewById(R.id.buttonClearBs);
        clearbs.setOnClickListener(this);
        swSeting =(Switch) findViewById(R.id.switchSet);
        swSeting.setOnCheckedChangeListener(this);
        swLogConsole =(Switch) findViewById(R.id.switchConsole);
        swLogConsole.setChecked(prefs.getBoolean("isLogConsole", false));
        swLogConsole.setOnCheckedChangeListener(this);
        swLogBs =(Switch) findViewById(R.id.switchBs);
        swLogBs.setChecked(prefs.getBoolean("isLogBs", false));
        swLogBs.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        Intent intent;
        switch(id){
            case R.id.buttonGenerator :
                intent = new Intent(this, GeneratorActivity.class);
          startActivity(intent);
          break;
            case R.id.buttonLog :
                 intent = new Intent(this, LogActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonClearBs :
                SqliteBD bs = SqliteBD.getInstance(this);
                bs.delLog();
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id=compoundButton.getId();
        SharedPreferences.Editor editor = prefs.edit();
          switch(id){
            case R.id.switchSet :
                editor.putBoolean("isLocalShow", b);
                break;
            case R.id.switchConsole :
                editor.putBoolean("isLogConsole", b);
                break;
            case R.id.switchBs :
                editor.putBoolean("isLogBs", b);
                break;
        }
        editor.commit();
    }
}
