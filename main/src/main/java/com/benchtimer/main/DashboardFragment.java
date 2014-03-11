package com.benchtimer.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benchtimer.come.benchtimer.time.ProtocolTimer;
import com.benchtimer.widgets.CircleProgress;

import java.util.ArrayList;

/**
 * Created by evert on 3/8/14.
 */
public class DashboardFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static ArrayList<ProtocolTimer> mProtocolTimers;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    public DashboardFragment() {
    }

    public static DashboardFragment newInstance(int sectionNumber) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        TextView textView = chooseTimerIndicator(rootView);
        CircleProgress circleProgress = (CircleProgress) rootView.findViewById(R.id.circle_progress);
        ProtocolTimer protocolTimer = new ProtocolTimer(5000, 10, getActivity(), circleProgress);
        protocolTimer.start();
        return rootView;
    }

    private TextView chooseTimerIndicator(View rootView) {
        TextView textView;
        switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1:
                textView = (TextView) rootView.findViewById(R.id.timer1);
                textView.setBackgroundColor(Color.parseColor("#4697d0"));
                ((Dashboard) getActivity()).setActionBarTitle("Western Block");
                break;
            case 2:
                textView = (TextView) rootView.findViewById(R.id.timer2);
                textView.setBackgroundColor(Color.parseColor("#b67676"));
                ((Dashboard) getActivity()).setActionBarTitle("Western Block");
                break;
            case 3:
                textView = (TextView) rootView.findViewById(R.id.timer3);
                textView.setBackgroundColor(Color.parseColor("#a59442"));
                ((Dashboard) getActivity()).setActionBarTitle("Eastern Block");
                break;
            case 4:
                textView = (TextView) rootView.findViewById(R.id.timer4);
                textView.setBackgroundColor(Color.parseColor("#708850"));
                ((Dashboard) getActivity()).setActionBarTitle("Eastern Block");
                break;
            default:
                throw new NullPointerException("Cannot find the Correct Timer View");

        }
        return textView;
    }

    public void test() {

    }


}
