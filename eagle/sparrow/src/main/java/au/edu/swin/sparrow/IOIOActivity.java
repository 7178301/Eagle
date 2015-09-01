package au.edu.swin.sparrow;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import eagle.sdkInterface.controllerAdaptors.IOIO.IOIOEagleActivity;

/**
 * Created by Lardi on 01/09/2015.
 */
public class IOIOActivity extends IOIOEagleActivity {

    private SeekBar sb;
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_ioio);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sb = (SeekBar) findViewById(R.id.seekBarValue);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tv = (TextView) findViewById(R.id.textViewValue);
                tv.setText(String.valueOf(progress));
                try {
                    setPulseWidth(1000 + (progress * 10));
                }catch (Exception e){}
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
