package com.benchtimer.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benchtimer.data.StepEntry;
import com.benchtimer.data.TimerDatabaseWorker;
import com.benchtimer.time.ProtocolTimer;
import com.benchtimer.utils.Parameters;
import com.benchtimer.utils.Utils;
import com.benchtimer.widgets.CircleProgress;
import com.benchtimer.widgets.LeadingTextView;

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
    private static SparseArray<ProtocolTimer> mProtocolTimers = new SparseArray<ProtocolTimer>(Parameters.TOTAL_NUM_OF_TIMERS);
    private static SparseArray<View> mConstructedViews = new SparseArray<View>(Parameters.TOTAL_NUM_OF_TIMERS);
    private static SparseArray<String> mProtocolNames = new SparseArray<String>(Parameters.TOTAL_NUM_OF_TIMERS);
    private ArrayList<TextView> mTimerIndicators = new ArrayList<TextView>();
    private int mTimerCount = 0;
    private int mId = 0;

    private CircleProgress mCircleProgress;
    private ProtocolTimer mProtocolTimer;
    private LeadingTextView mCurrentProtocolText;
    private LeadingTextView mNextProtocolView;
    private int mCurrentStep = 0;

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
        mId = getArguments().getInt(ARG_SECTION_NUMBER);
        final View rootView;
        if (mConstructedViews.get(mId) == null) {
            System.out.println("Creating Dashboard Fragment");
            rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        } else {
            rootView = mConstructedViews.get(mId);
            ((ViewGroup)rootView.getParent()).removeView(rootView);
        }

        if(mProtocolNames.get(mId) == null) {
            getActivity().getActionBar().setTitle(getProtocolName(mId));
        } else {
            getActivity().getActionBar().setTitle(mProtocolNames.get(mId));
        }

        mTimerCount = TimerDatabaseWorker.getInstance(getActivity()).getProtocolCount();
        initTextViews(rootView);
        TextView textView = chooseTimerIndicator(rootView);

        mCurrentProtocolText = (LeadingTextView) rootView.findViewById(R.id.current_protocol);
        mNextProtocolView = (LeadingTextView) rootView.findViewById(R.id.next_protocol);
        mCircleProgress = (CircleProgress) rootView.findViewById(R.id.circle_progress);
        initSteps();
        mConstructedViews.put(mId, rootView);
        return rootView;

    }

    private void initTextViews(View rootView) {
        mTimerIndicators.add((TextView) rootView.findViewById(R.id.timer0));
        mTimerIndicators.add((TextView) rootView.findViewById(R.id.timer1));
        mTimerIndicators.add((TextView) rootView.findViewById(R.id.timer2));
        mTimerIndicators.add((TextView) rootView.findViewById(R.id.timer3));
        System.out.println(mTimerCount);
        for (int i = mTimerIndicators.size() - 1; i >= mTimerCount; i--) {
            mTimerIndicators.get(i).setBackgroundResource(R.drawable.back_grey);
        }
    }

    private TextView chooseTimerIndicator(View rootView) {
        TextView textView;
        switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 0:
                textView = (TextView) rootView.findViewById(R.id.timer0);
                textView.setBackgroundColor(Color.parseColor("#4697d0"));
                break;
            case 1:
                textView = (TextView) rootView.findViewById(R.id.timer1);
                textView.setBackgroundColor(Color.parseColor("#b67676"));
                break;
            case 2:
                textView = (TextView) rootView.findViewById(R.id.timer2);
                textView.setBackgroundColor(Color.parseColor("#a59442"));

                break;
            case 3:
                textView = (TextView) rootView.findViewById(R.id.timer3);
                textView.setBackgroundColor(Color.parseColor("#708850"));
                break;
            default:
                throw new NullPointerException("Cannot find the Correct Timer View");

        }
        return textView;
    }

    private String getProtocolName(int id) {
        String name;
        if ((name = mProtocolNames.get(id)) != null) {
            return name;
        } else {
            TimerDatabaseWorker timerDatabaseWorker = TimerDatabaseWorker.getInstance(getActivity());
            name = timerDatabaseWorker.queryProtocolName(id);
            mProtocolNames.put(id, name);
            return name;
        }
    }

    private void initCircleProgress(long stepTime) {
        System.out.println("In initCircleProgress");
        if (mProtocolTimers.get(mId) == null) {
            mProtocolTimer = new BenchTimer(stepTime, 10);
            mProtocolTimers.put(mId, mProtocolTimer);

        } else {
            mProtocolTimer = mProtocolTimers.get(mId);
        }

        mCircleProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Called");
                if (mProtocolTimer.isPaused()) {
                    mProtocolTimer.start();
                } else {
                    mProtocolTimer.pause();
                }
            }
        });
    }


    private void initSteps() {
        StepEntry ret = TimerDatabaseWorker.getInstance(getActivity()).getStep(mId, mCurrentStep);
        // System.out.println(ret.name);
        if (ret == null) {
            mCurrentProtocolText.setText("(Empty)");
            mNextProtocolView.setText("(Empty)");
            initCircleProgress(0);
        } else {
            mCurrentProtocolText.setText(ret.name + " - " + ret.setTime);
            StepEntry nextRet = TimerDatabaseWorker.getInstance(getActivity()).getStep(mId, mCurrentStep + 1);
            mNextProtocolView.setText(nextRet == null ? "No more steps" : nextRet.name + " - " + nextRet.setTime);
            mCircleProgress.changeView(100, ret.setTime + ".00");
            initCircleProgress(Utils.convertToTime(ret.setTime));

        }
    }


    class BenchTimer extends ProtocolTimer {

        private long mMillisInFuture = 0;

        public BenchTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.mMillisInFuture = millisInFuture;
        }

        /**
         * Callback when CountUpTimer is firing
         *
         * @param millis
         */
        @Override
        public void onUpTick(long millis) {
            String formattedTime = Utils.formatTimeIntoDisplay(millis);
            mCircleProgress.changeView(0,formattedTime);
        }

        /**
         * Callback fired on regular interval.
         *
         * @param millisUntilFinished The amount of time until finished.
         */
        @Override
        public void onTick(long millisUntilFinished) {
            String formattedTime = Utils.formatTimeIntoDisplay(millisUntilFinished);
            float percent = millisUntilFinished / (float) mMillisInFuture * 100;
            mCircleProgress.changeView(percent, formattedTime);
        }

        /**
         * Callback fired when the time is up.
         */
        @Override
        public void onFinish() {
            String formattedTime = Utils.formatTimeIntoDisplay(0);
            mCircleProgress.changeView(0f, formattedTime);
            // when finish, start the CountUpTimer

        }
    }

}
