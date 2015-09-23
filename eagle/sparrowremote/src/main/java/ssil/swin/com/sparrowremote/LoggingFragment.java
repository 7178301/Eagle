package ssil.swin.com.sparrowremote;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoggingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoggingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoggingFragment extends Fragment {

    private WebView webViewLog;
    private static Vector<String> logMessages = new Vector<String>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoggingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoggingFragment newInstance() {
        LoggingFragment fragment = new LoggingFragment();
        return fragment;
    }

    public LoggingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logging, container, false);
        webViewLog = (WebView) view.findViewById(R.id.webViewLog);
        return view;
    }

    void appendLog(String message) {
        logMessages.add(message);
        updateLog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        updateLog();
    }

    private void updateLog() {
        if (isAdded()) {
            getActivity().runOnUiThread(
                    new Runnable() {

                        @Override
                        public void run() {
                            if (webViewLog != null)

                            {
                                StringBuilder html = new StringBuilder();
                                html.append("<html>");
                                html.append("<head>");

                                html.append("</head>");
                                html.append("<body>");
                                for (String mess : logMessages) {
                                    html.append("<p>" + mess + "</p>");
                                }
                                html.append("</body></html>");

                                webViewLog.loadDataWithBaseURL("file:///android_asset/", html.toString(), "text/html", "UTF-8", "");
                            }
                        }
                    }
            );
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
