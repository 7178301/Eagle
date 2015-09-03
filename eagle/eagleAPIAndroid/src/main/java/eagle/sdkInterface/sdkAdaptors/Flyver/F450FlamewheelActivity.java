package eagle.sdkInterface.sdkAdaptors.Flyver;

import android.os.Bundle;
import android.widget.Toast;

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

public class F450FlamewheelActivity extends IOIOActivity {

    private IOIO ioio;

    private PwmOutput motorFC;
    private PwmOutput motorFCC;
    private PwmOutput motorRC;
    private PwmOutput motorRCC;

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
            motorFC = ioio_.openPwmOutput(34, 200);
            motorFCC = ioio_.openPwmOutput(35, 200);
            motorRC = ioio_.openPwmOutput(36, 200);
            motorRCC = ioio_.openPwmOutput(37, 200);
            ioio=ioio_;
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

    public void setPulseWidth(int fcRange, int fccRange, int rcRange, int rccRange) throws ConnectionLostException {
        if (fcRange >= 1000 && fcRange <= 2023 &&
                fccRange >= 1000 && fccRange <=2023 &&
                rcRange >= 1000 && rcRange <=2023 &&
                rccRange >= 1000 && rccRange <=2023) {
            motorFC.setPulseWidth(fcRange);
            motorFCC.setPulseWidth(fccRange);
            motorRC.setPulseWidth(rcRange);
            motorRCC.setPulseWidth(rccRange);
        }else
            throw new IllegalArgumentException();
    }

    public IOIO getIOIO(){
        return ioio;
    }
}