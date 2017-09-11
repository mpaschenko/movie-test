package com.movies.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;
import com.movies.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by michaelpaschenko on 9/10/17.
 * filter dialog which holds twe way range bar
 */

public class RangeDialogFragment extends DialogFragment {

    public static final String TAG = RangeDialogFragment.class.getSimpleName();
    private static final String KEY_TAG = "key-tag";
    private static final String KEY_MIN = "key-min";
    private static final String KEY_MAX = "key-max";
    private static final String KEY_TITLE = "key-title";

    private RangeFilterListener mListener;

    @Bind(R.id.textview_title)
    TextView mTitle;

    @Bind(R.id.textview_min_value)
    TextView mMinValue;

    @Bind(R.id.textview_max_value)
    TextView mMaxValue;

    @Bind(R.id.rangebar)
    RangeBar mRangeBar;

    public static RangeDialogFragment newInstance(int min, int max, String title, int tag) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TAG, tag);
        bundle.putInt(KEY_MIN, min);
        bundle.putInt(KEY_MAX, max);
        bundle.putString(KEY_TITLE, title);
        RangeDialogFragment rangeDialogFragment = new RangeDialogFragment();
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
        View root = inflater.inflate(R.layout.fragment_range, container, false);
        ButterKnife.bind(this, root);
        Bundle bundle = getArguments();
        final int minValue = bundle.getInt(KEY_MIN);
        final int maxValue = bundle.getInt(KEY_MAX);
        mTitle.setText(bundle.getString(KEY_TITLE));
        mRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int min, int max) {
                mMinValue.setText(minValue + min + "");
                mMaxValue.setText(minValue + max + "");
            }
        });
        mRangeBar.setTickHeight(0);
        mRangeBar.setTickCount(maxValue - minValue + 1);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RangeFilterListener) {
            mListener = (RangeFilterListener) context;
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
        final int minValue = bundle.getInt(KEY_MIN);
        mListener.onFilter(mRangeBar.getLeftIndex() + minValue, mRangeBar.getRightIndex() + minValue, bundle.getInt(KEY_TAG));
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
    public interface RangeFilterListener {
        void onFilter(int min, int max, int tag);
    }
}
