package com.benchtimer.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.benchtimer.data.TimerDatabaseWorker;
import com.benchtimer.utils.Parameters;
import com.benchtimer.widgets.TimerViewPager;

import java.util.HashMap;
import java.util.Locale;


public class Dashboard extends ActionBarActivity {

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
    TimerViewPager mViewPager;

    private HashMap<Integer, String> mProtocolNames = new HashMap<Integer, String>(Parameters.TOTAL_NUM_OF_TIMERS);
    private int mTimerCount = 0;

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            setContentView(R.layout.activity_dashboard);
            // we need to init the database worker at the start of the program
            TimerDatabaseWorker.getInstance().init(this);
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            // Set up the ViewPager with the sections adapter.
            mViewPager = (TimerViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setOffscreenPageLimit(Parameters.TOTAL_NUM_OF_TIMERS - 1);
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    setActionBarTitle(getProtocolName(position));
                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //get the number of timers, if it is 0, use another layout
            mTimerCount = TimerDatabaseWorker.getInstance().getProtocolCount();
            Log.i(this.getClass().toString(),"Number of timers is " + Integer.toString(mTimerCount));
            mSectionsPagerAdapter.notifyDataSetChanged();
        }
    }


    private String getProtocolName(int id) {
        String name;
        if ((name = mProtocolNames.get(id)) != null) {
            return name;
        } else {

            name = TimerDatabaseWorker.getInstance().queryProtocolName(id);
            mProtocolNames.put(id, name);
            return name;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Parameters.PROTOCOL_PAGE_REQUEST_ID :
                if(resultCode == Activity.RESULT_OK) {
                    setActionBarTitle(data.getExtras().getString(Parameters.PROTOCOL_NAME));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        switch (item.getItemId()) {
   /*          case 0x102002C: //I don't know why R.id.home does not work
                  return true;*/
            case R.id.action_add:
                if (TimerDatabaseWorker.getInstance().getProtocolCount() < Parameters.TOTAL_NUM_OF_TIMERS) {
                    Intent addIntent = new Intent(this, ProtocolPage.class);
                    addIntent.putExtra(Parameters.PAGE_ID_NUM, Parameters.ADD_NEW_PROTOCOL);
                    startActivity(addIntent);
                    DashboardFragment.reDraw();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Cannot create more than 4 timers for now",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

                return true;
            case R.id.action_settings:
                Intent settingIntent = new Intent(this, ProtocolPage.class);
                settingIntent.putExtra(Parameters.PAGE_ID_NUM, mViewPager.getCurrentItem());
                settingIntent.putExtra(Parameters.PROTOCOL_NAME, getActionBar().getTitle());
                startActivityForResult(settingIntent, Parameters.PROTOCOL_PAGE_REQUEST_ID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
            // getItem is called to instantiate the fragment for the given page.
            // Return a Dashboard Fragment.
            return mTimerCount == 0 ? EmptyDashboardFragment.newInstance() : DashboardFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 4 total pages for now
            if (mTimerCount == 0) {
                // We still want to show an empty page
                return 1;
            }
            return mTimerCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            return ("Section " + Integer.toString(position)).toUpperCase(l);
        }
    }

}
