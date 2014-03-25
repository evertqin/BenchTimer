package com.benchtimer.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.benchtimer.main.R;

/**
 * Created by evert on 3/9/14.
 */
public class LeadingTextView extends TextView {

    private String mLeadingText;

    public LeadingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
        init();

    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LeadingTextView);
        mLeadingText = ta.getString(R.styleable.LeadingTextView_leadingText);
        ta.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(Html.fromHtml(mLeadingText + text), type);
    }

    private void init() {
        this.setText(Html.fromHtml(mLeadingText + getText()));
    }



}
