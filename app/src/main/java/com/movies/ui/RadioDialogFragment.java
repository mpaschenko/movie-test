package com.movies.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;
import com.movies.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by michaelpaschenko on 9/10/17.
 * filter dialog which holds list of radio buttons
 */

public class RadioDialogFragment extends DialogFragment {

    public static final String TAG = RadioDialogFragment.class.getSimpleName();
    private static final String KEY_TAG = "key-tag";
    private static final String KEY_VALUES = "key-values";

    private static final String KEY_TITLE = "key-title";

    RadioFilterListener mListener;

    @Bind(R.id.textview_title)
    TextView mTitle;

    @Bind(R.id.radiogroup)
    RadioGroup mRadioGroup;

    public static RadioDialogFragment newInstance(ArrayList<String> values, String title, int tag) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TAG, tag);
        bundle.putStringArrayList(KEY_VALUES, values);
        bundle.putString(KEY_TITLE, title);
        RadioDialogFragment rangeDialogFragment = new RadioDialogFragment();
        rangeDialogFragment.setArguments(bundle);
        return rangeDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_radio, container, false);
        ButterKnife.bind(this, root);
        Bundle bundle = getArguments();

        mTitle.setText(bundle.getString(KEY_TITLE));
        ArrayList<String> values = bundle.getStringArrayList(KEY_VALUES);
        for (int i = 0; i < values.size(); i++) {
            RadioButton radioButton = new RadioButton(this.getActivity());
            radioButton.setId(i+1);
            radioButton.setText(values.get(i));
            radioButton.setTextColor(getResources().getColor(R.color.white));
            mRadioGroup.addView(radioButton);
        }
        //((ViewGroup) findViewById(R.id.radiogroup)).addView(ll);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RadioFilterListener) {
            mListener = (RadioFilterListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RangeFilterListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.button_ok)
    void onOk() {
        Bundle bundle = getArguments();
        ArrayList<String> values = bundle.getStringArrayList(KEY_VALUES);
        final String value = values.get(mRadioGroup.getCheckedRadioButtonId() - 1);
        mListener.onFilter(value, bundle.getInt(KEY_TAG));
        dismiss();
    }

    @OnClick(R.id.button_cancel)
    void onCancel() {
        dismiss();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity
     */
    public interface RadioFilterListener {
        void onFilter(String value, int tag);
    }
}
