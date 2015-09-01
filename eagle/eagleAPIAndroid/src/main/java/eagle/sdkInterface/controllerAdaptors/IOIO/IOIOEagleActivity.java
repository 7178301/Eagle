package eagle.sdkInterface.controllerAdaptors.IOIO;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.ToggleButton;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

/**
 * Android IOIO Activity Extender
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 01/09/2015
 * <p/>
 * Date Modified	01/09/2015 - Nicholas
 */

public class IOIOEagleActivity extends IOIOActivity {

    PwmOutput pwm1;
    PwmOutput pwm2;
    PwmOutput pwm3;
    PwmOutput pwm4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    class Looper extends BaseIOIOLooper {
        private DigitalOutput led_;

        @Override
        protected void setup() throws ConnectionLostException {
            showVersions(ioio_, "IOIO connected!");
            led_ = ioio_.openDigitalOutput(0, true);

            pwm1 = ioio_.openPwmOutput(34, 200);
            pwm2 = ioio_.openPwmOutput(35, 200);
            pwm3 = ioio_.openPwmOutput(36, 200);
            pwm4 = ioio_.openPwmOutput(37, 200);
            setPulseWidth(0);
        }

        @Override
        public void loop() throws ConnectionLostException, InterruptedException {
            led_.write(false);
        }

        @Override
        public void disconnected() {
            toast("IOIO disconnected");
        }
        @Override
        public void incompatible() {
            showVersions(ioio_, "Incompatible firmware version!");
        }
    }

    @Override
    protected IOIOLooper createIOIOLooper() {
        return new Looper();
    }

    private void showVersions(IOIO ioio, String title) {
        toast(String.format("%s\n" +
                        "IOIOLib: %s\n" +
                        "Application firmware: %s\n" +
                        "Bootloader firmware: %s\n" +
                        "Hardware: %s",
                title,
                ioio.getImplVersion(IOIO.VersionType.IOIOLIB_VER),
                ioio.getImplVersion(IOIO.VersionType.APP_FIRMWARE_VER),
                ioio.getImplVersion(IOIO.VersionType.BOOTLOADER_VER),
                ioio.getImplVersion(IOIO.VersionType.HARDWARE_VER)));
    }

    private void toast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setPulseWidth(int range) throws ConnectionLostException{
        if(range>1000&&range<2023) {
            pwm1.setPulseWidth(range);
            pwm2.setPulseWidth(range);
            pwm3.setPulseWidth(range);
            pwm4.setPulseWidth(range);
        }
    }
}