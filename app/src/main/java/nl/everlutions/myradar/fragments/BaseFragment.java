package nl.everlutions.myradar.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octo.android.robospice.SpiceManager;

import nl.everlutions.myradar.activities.BaseActivity;
import nl.everlutions.myradar.comm.ApiManager;
import nl.everlutions.myradar.utils.Constants;

public abstract class BaseFragment extends Fragment implements Constants {
    public final String TAG = this.getClass().getSimpleName();

    public View mRootFragmentView = null;

    public View findViewById(int viewId) {
        if (mRootFragmentView == null) {
            Log.e(TAG, "Your are trying to use findViewById but mRootFragmentView is not set");
        }
        return mRootFragmentView.findViewById(viewId);
    }

    public SpiceManager getSpiceManager() {
        return ((BaseActivity) getActivity()).getSpiceManager();
    }

    public ApiManager getAPI() {
        BaseActivity parent = ((BaseActivity) getActivity());
        if (parent == null)
            throw new RuntimeException("Attempt to use API before parent is attached to fragment");
        return parent.getAPI();
    }

    protected void showLoader(DialogInterface.OnCancelListener cancelListener) {
        BaseActivity parent = ((BaseActivity) getActivity());
        if (parent == null)
            return;
        parent.showLoader(cancelListener);
    }

    protected void hideLoader() {
        BaseActivity parent = ((BaseActivity) getActivity());
        if (parent == null)
            return;
        parent.hideLoader();
    }


    public BaseActivity getBaseActivity() {
        if (getActivity() == null)
            return null;
        return (BaseActivity) getActivity();
    }

    public void setTitle(int resId) {
        getBaseActivity().getSupportActionBar().setTitle(resId);
    }

    public void setTitle(String title) {
        getBaseActivity().getSupportActionBar().setTitle(title);
    }

    /**
     * =============================================
     * =============================================
     *LIFE CYCLE DEBUGGING
     * =============================================
     * =============================================
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (DEBUG_FRAGMENTS_LIFECYCLE)
            Log.e(TAG, getClass().getSimpleName() + ".OnAttach() " + activity.getClass().getSimpleName()
                    + " )");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG_FRAGMENTS_LIFECYCLE)
            Log.e(TAG, getClass().getSimpleName() + ".onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // This log trace won't be shown unless it's implemented in the subclasses of BaseFragment
        if (DEBUG_FRAGMENTS_LIFECYCLE)
            Log.e(TAG, getClass().getSimpleName() + ".onCreateView(...)");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (DEBUG_FRAGMENTS_LIFECYCLE)
            Log.e(TAG, getClass().getSimpleName() + ".onViewCreated(...)");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (DEBUG_FRAGMENTS_LIFECYCLE)
            Log.e(TAG, getClass().getSimpleName() + ".onActivityCreated()");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (DEBUG_FRAGMENTS_LIFECYCLE)
            Log.e(TAG, getClass().getSimpleName() + ".onSaveInstanceState()");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (DEBUG_FRAGMENTS_LIFECYCLE)
            Log.e(TAG, getClass().getSimpleName() + ".onViewStateRestored()");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (DEBUG_FRAGMENTS_LIFECYCLE)
            Log.e(TAG, getClass().getSimpleName() + ".onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DEBUG_FRAGMENTS_LIFECYCLE)
            Log.e(TAG, getClass().getSimpleName() + ".onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (DEBUG_FRAGMENTS_LIFECYCLE)
            Log.e(TAG, getClass().getSimpleName() + ".onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (DEBUG_FRAGMENTS_LIFECYCLE)
            Log.e(TAG, getClass().getSimpleName() + ".onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (DEBUG_FRAGMENTS_LIFECYCLE)
            Log.e(TAG, getClass().getSimpleName() + ".onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG_FRAGMENTS_LIFECYCLE)
            Log.e(TAG, getClass().getSimpleName() + ".onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (DEBUG_FRAGMENTS_LIFECYCLE)
            Log.e(TAG, getClass().getSimpleName() + ".onDetach()");

    }
}
