package com.benchtimer.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.benchtimer.data.StepEntry;
import com.benchtimer.data.TimerDatabaseWorker;
import com.benchtimer.utils.BenchTimerApp;
import com.benchtimer.utils.Parameters;

import java.util.List;


public class ProtocolPage extends ActionBarActivity {

    private int mId = -1;
    private String mName;
    private StepEntry mSelectedStep;
    private ListView mListView;
    private List<StepEntry> mStepEntries;
    private StepEntryAdapter mAdapter;

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Parameters.PROTOCOL_NAME, mName);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
        System.out.println("onPause finished");
    }


    /**
     * Save all appropriate fragment state.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(Parameters.PROTOCOL_ID, mId);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_protocol_page);
        getActionBar().setHomeButtonEnabled(true);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mId = extras.getInt(Parameters.PAGE_ID_NUM);
                getActionBar().setTitle(extras.getString(Parameters.PROTOCOL_NAME));
            }
        } else {
            mId = (Integer) savedInstanceState.getSerializable(Parameters.PROTOCOL_ID);
        }


        initListView();
        ((BenchTimerApp) this.getApplication()).setCurrentProtocolId(mId);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.protocol_page, menu);
        MenuItem menuItem = menu.findItem(R.id.action_edit);

        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                View view = MenuItemCompat.getActionView(item);
                final EditText editText = (EditText) view.findViewById(R.id.action_bar_edit);
                editText.setText(getActionBar().getTitle());
                editText.requestFocus();
                editText.selectAll();
                editText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    }
                }, 50);

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                View view = MenuItemCompat.getActionView(item);
                final EditText editText = (EditText) view.findViewById(R.id.action_bar_edit);
                mName = editText.getText().toString();
                if (mId == -1) {
                    TimerDatabaseWorker.getInstance().addNewProtocol(mName);
                } else {
                    TimerDatabaseWorker.getInstance().updateProtocol(mId, mName);
                }
                getActionBar().setTitle(mName);
                editText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    }
                }, 50);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
/*            case R.id.action_edit:
                setupActionBarEditText(item);
                return true;*/
            case R.id.action_new:
                Log.i("onOptionsItemSelected", "Called action add");
                addNewStep();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    private ListView initListView() {
        mListView = (ListView) findViewById(R.id.protocol_page);
        mStepEntries = TimerDatabaseWorker.getInstance().queryProtocolDetail(mId);
        mAdapter = new StepEntryAdapter(this, R.layout.activity_protocol_page, mStepEntries);
        mListView.setAdapter(mAdapter);
        return mListView;
    }


    private void addNewStep() {
        StepEntry newStepEntry = new StepEntry();
        mStepEntries.add(newStepEntry);
        mAdapter.notifyDataSetChanged();

    }


}
