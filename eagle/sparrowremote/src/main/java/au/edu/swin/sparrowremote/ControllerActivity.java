package au.edu.swin.sparrowremote;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import eagle.Log;
import eagle.LogCallback;
import eagle.network.protocolBuffer.ProtocolBufferClient;
import au.edu.swin.sparrowremote.Fragment.LoggingFragment;
import au.edu.swin.sparrowremote.Fragment.OnFragmentInteractionListener;
import au.edu.swin.sparrowremote.Fragment.RemoteControlFragment;

public class ControllerActivity extends AppCompatActivity implements ActionBar.TabListener, OnFragmentInteractionListener, LogCallback {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private String serverAddress;
    private ProtocolBufferClient commandConnection;
    private RemoteControlFragment remoteControlFragment;
    private LoggingFragment logFragment;
    private ControllerActivity ca = this;
    private Timer myTimer;

    private ProtocolBufferClient.ResponseCallBack rcb = new ProtocolBufferClient.ResponseCallBack() {
        @Override
        public void handleResponse(final String response) {

            if (response.compareTo("SUCCESS") != 0) {
                ca.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.log("ControllerActivity","Command failed (" + response + "). Perhaps you need to connect first?");
                        Toast toast = Toast.makeText(ca, "Command failed (" + response + "). Perhaps you need to connect first?", Toast.LENGTH_LONG);
                        toast.show();

                    }
                });
            }

        }
    };

    private double bearingAngle = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        serverAddress = getIntent().getStringExtra("serverAddress");
        commandConnection = new ProtocolBufferClient(serverAddress);

        remoteControlFragment = RemoteControlFragment.newInstance();
        logFragment = LoggingFragment.newInstance();

        commandConnection.connectToServer();
        if (!commandConnection.isConnected()) {
            Toast toast = Toast.makeText(this, "Failed to connect to drone", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

        MyTimerTask myTask = new MyTimerTask();
        myTimer = new Timer();
        myTimer.schedule(myTask, 1000, 100);

        Log.addVerboseCallback(this);

    }

    protected void onStart(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        Log.removeCallback("ControllerActivity",this);
        myTimer.cancel();
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        double longitude;
        double latitude;
        switch (uri.getPath()) {
            case "/buttonConnect":
                commandConnection.sendMessage("CONNECTTODRONE", rcb);
                break;
            case "/buttonDisconnect":
                commandConnection.sendMessage("DISCONNECTFROMDRONE", rcb);
                break;
            case "/buttonUp":
                commandConnection.sendMessage("CHANGEALTITUDE -D 1", rcb);
                break;
            case "/buttonDown":
                commandConnection.sendMessage("CHANGEALTITUDE -D -1", rcb);
                break;
            case "/buttonRotateLeft":
                commandConnection.sendMessage("CHANGEYAW -D -10", rcb);
                break;
            case "/buttonRotateRight":
                commandConnection.sendMessage("CHANGEYAW -D 10", rcb);
                break;
            case "/buttonLeft":
                latitude = 1 * Math.sin(Math.toRadians(bearingAngle));
                longitude = -1 * Math.cos(Math.toRadians(bearingAngle));
                commandConnection.sendMessage("FLYTO -D " + latitude + " " + longitude + " 0 0", rcb);
                break;
            case "/buttonRight":
                latitude = -1 * Math.sin(Math.toRadians(bearingAngle));
                longitude = 1 * Math.cos(Math.toRadians(bearingAngle));
                commandConnection.sendMessage("FLYTO -D " + latitude + " " + longitude + " 0 0", rcb);
                break;
            case "/buttonForward":
                latitude = 1 * Math.cos(Math.toRadians(bearingAngle));
                longitude = 1 * Math.sin(Math.toRadians(bearingAngle));
                commandConnection.sendMessage("FLYTO -D " + latitude + " " + longitude + " 0 0", rcb);
                break;
            case "/buttonBackward":
                latitude = -1 * Math.cos(Math.toRadians(bearingAngle));
                longitude = -1 * Math.sin(Math.toRadians(bearingAngle));
                commandConnection.sendMessage("FLYTO -D " + latitude + " " + longitude + " 0 0", rcb);
                break;
            case "/buttonGoHome":
                commandConnection.sendMessage("GOHOME", rcb);
                break;
            case "/finish":
                finish();
                break;
            default:
        }
    }


    @Override
    public void onLogEntry(String tag, String message) {
        logFragment.appendLog(message);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return remoteControlFragment;
                case 2:
                    return logFragment;
                default:
                    return PlaceholderFragment.newInstance(position + 1);

            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    void updatePositionFragment() {
        boolean connected = commandConnection.sendMessage("GETPOSITIONINFLIGHT", new ProtocolBufferClient.ResponseCallBack() {
            @Override
            public void handleResponse(String position) {
                if (position != null&&!position.equals("POSITION IN FLIGHT NOT AVAILABLE")) {
                    String parts[] = position.split(" ");
                    if (parts.length != 6) {
                        return;
                    }
                    remoteControlFragment.setPosition(parts[0], parts[1], parts[2], parts[5]);

                    bearingAngle = Double.parseDouble(parts[5]);
                }
            }
        });
        if (!connected) {
            Toast toast = Toast.makeText(this, "Lost connection to drone", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_controller, container, false);
            return rootView;
        }
    }

    class MyTimerTask extends TimerTask {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updatePositionFragment();
                }
            });
        }
    }
}
