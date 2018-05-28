package com.example.admin.gogoalarm;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class MainActivity extends AppCompatActivity implements OnSeekBarChangeListener,OnItemSelectedListener{

    SeekBar seekBar1,seekBar2;
    Spinner spinner ;
    int gap,vol,maxVal;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar1=(SeekBar)findViewById(R.id.interval);
        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2=(SeekBar)findViewById(R.id.vol);
        seekBar2.setOnSeekBarChangeListener(this);
        spinner= (Spinner) findViewById(R.id.count);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.no_of_count, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


         audioManager =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
         maxVal = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        if(seekBar.equals(seekBar1))
            gap=progress;
        else
            vol=progress;
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar.equals(seekBar1))
            Toast.makeText(getApplicationContext(),"Interval of "+gap+" minutes", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"Volume : "+(int)((vol*maxVal)/40), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void setAlarm(View view)
    {
        EditText editText1 = (EditText)findViewById(R.id.hours);
        int h = Integer.parseInt(editText1.getText().toString());
        EditText editText2 = (EditText)findViewById(R.id.minutes);
        int m = Integer.parseInt(editText2.getText().toString());
        Switch vibrate = (Switch)findViewById(R.id.vibrate);
        boolean v = vibrate.isChecked();
        EditText editText3 = (EditText)findViewById(R.id.label);
        String label = editText3.getText().toString();
        int count = Integer.parseInt(spinner.getSelectedItem().toString());

        int volume = (vol*maxVal)/40;
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume,1);

        for(int i=1;i<=count;i++) {

            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                    .putExtra(AlarmClock.EXTRA_MESSAGE, label+"\nAlarm No. => "+i)
                    .putExtra(AlarmClock.EXTRA_HOUR, h)
                    .putExtra(AlarmClock.EXTRA_MINUTES, m)
                    .putExtra(AlarmClock.EXTRA_VIBRATE,v)
                    .putExtra(AlarmClock.EXTRA_SKIP_UI, true);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            m=m+gap;
            if(m>=60)
            {
                h++;
                m=m-60;
            }
        }
        finish();
        System.exit(0);
    }

}
