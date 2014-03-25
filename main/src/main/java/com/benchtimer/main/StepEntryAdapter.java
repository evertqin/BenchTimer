package com.benchtimer.main;

import android.content.Context;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.benchtimer.data.StepEntry;
import com.benchtimer.utils.BenchTimerApp;

import java.util.List;

/**
 * Created by evert on 3/13/14.
 */
public class StepEntryAdapter extends ArrayAdapter<StepEntry> {
    private final Context mContext;
    private final List<StepEntry> mStepEntries;
    private LayoutInflater mInflater;
    private boolean mIsSelected = false;
    private ActionMode mActionMode;

    public StepEntryAdapter(Context context, int resource, List<StepEntry> objects) {
        super(context, resource, objects);
        mContext = context;
        mStepEntries = objects;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.step_entry, parent, false);
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) rowView.findViewById(R.id.protocol_name);
        final EditText protocolSetTime = (EditText) rowView.findViewById(R.id.protocol_settime);
        autoCompleteTextView.setText(mStepEntries.get(position).name);
        protocolSetTime.setText(mStepEntries.get(position).setTime);
        final TextView stepMetaText = (TextView) rowView.findViewById(R.id.letter_text);
        if (!mStepEntries.get(position).name.isEmpty()) {
            stepMetaText.setText(mStepEntries.get(position).name.substring(0, 1));
        }
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsSelected) {
                    stepMetaText.setText(mStepEntries.get(position).name.substring(0, 1));
                    if (mActionMode != null) {
                        mActionMode.finish();
                    }


                } else {
                    stepMetaText.setText("");
                    if (mActionMode == null) {
                        mActionMode = parent.startActionMode(mActionModeCallback);
                        view.setSelected(true);
                    }
                }
                mIsSelected = !mIsSelected;
            }
        });

        if (mStepEntries.get(position).stepId == StepEntry.NEW_STEP_ENTRY) {
            // this is a new entry, we need to focus the keyboard
            autoCompleteTextView.selectAll();
            autoCompleteTextView.requestFocus();
            autoCompleteTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mActionMode = parent.startActionMode(mActionModeCallback);
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(autoCompleteTextView, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 50);
            // if user made no change, we need to update the new entry accordingly
            //mStepEntries.get(position).stepId = StepEntry.USER_GIVE_UP;
        }

        protocolSetTime.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i) {
                    case KeyEvent.FLAG_EDITOR_ACTION:
                        String name = autoCompleteTextView.getText().toString();
                        String setTime = protocolSetTime.getText().toString();
                        StepEntry stepEntry = new StepEntry(name, setTime);
                        int id = ((BenchTimerApp) (((ProtocolPage) mContext).getApplication())).getCurrentProtocolId();
                        Log.i("protocolSetTime.setOnKeyListener", Integer.toString(i));
                        //TimerDatabaseWorker.getInstance(mContext).addNewStep(id, StepEntry.NEW_STEP_ENTRY,stepEntry);
                        return true;
                    default:
                        return false;
                }

            }
        });
        return rowView;
    }


    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.step_contextual, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionMode = null;
        }
    };


    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {

        }
    };
}
