package ssil.swin.com.sparrowremote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_drone);
    }

    public void connectToDrone(View view) {
        String serverAddress = ((EditText)findViewById(R.id.editTextServerAddress)).getText().toString();
        Intent intent = new Intent(getBaseContext(), ControllerActivity.class);
        intent.putExtra("serverAddress", serverAddress);
        startActivity(intent);
    }
}
