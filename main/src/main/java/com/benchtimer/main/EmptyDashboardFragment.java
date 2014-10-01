package com.benchtimer.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by evert on 3/17/14.
 */
public class EmptyDashboardFragment extends Fragment {
    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(android.os.Bundle)} and {@link #onActivityCreated(android.os.Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_empty_dashboard,container,false);
        init(rootView);
        return rootView;
    }


    private void init(View rootView) {
        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.timerIndicatorContainer);
        ArrayList<TextView> allTextViews = new ArrayList<TextView>();
        for(int i = 0; i < linearLayout.getChildCount(); i++) {
            if(linearLayout.getChildAt(i) instanceof TextView) {
                linearLayout.getChildAt(i).setBackgroundResource(R.drawable.back_grey);
            }
        }
    }


    public static EmptyDashboardFragment newInstance() {
        return new EmptyDashboardFragment();
    }

}
