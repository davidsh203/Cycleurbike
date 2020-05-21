package com.example.cycleurbike.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cycleurbike.R;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.cycleurbike.activities.MainActivity;
import com.example.cycleurbike.classes.OnSwipeTouchListener;

import java.lang.reflect.Method;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_1, container, false);
        final Animation rotateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);

        final Button enterButtonFrag1 = (Button) view.findViewById(R.id.enterButtonFrag1);
        enterButtonFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // inner class
                MainActivity mainActivity = (MainActivity) getActivity();
               //mainActivity.loadMainPage(); //no need this, its called in startButtonAnimation function
              startButtonAnimation(enterButtonFrag1,rotateAnim,mainActivity);

            }
        });
        enterButtonFrag1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.startAnimation(rotateAnim);
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadMainPage();
                return true;
            }
        });

        final Animation myAnim2 = AnimationUtils.loadAnimation(getContext(), R.anim.scale);
        Button registerButtonFrag1 = (Button) view.findViewById(R.id.registerButtonFrag1);
        registerButtonFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(myAnim2);
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadSecFragment(); //
            }
        });

        final Animation myAnim3 = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        Button forgetPasswordButtonFrag1 = (Button) view.findViewById(R.id.forgetPasswordButtonFrag1);
        forgetPasswordButtonFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(myAnim3);
            }
        });

        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {


            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadSecFragment();

            }
        });

        return view;
    }

    private void startButtonAnimation(Button btn, Animation anim, final MainActivity funcRun ) {
        btn.setAnimation(anim);
        btn.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                funcRun.loadMainPage();
            }
        });
    }


}

