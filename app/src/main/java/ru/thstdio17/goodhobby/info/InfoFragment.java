package ru.thstdio17.goodhobby.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import ru.thstdio17.goodhobby.R;

/**
 * Created by shcherbakov on 29.08.2017.
 */

public class InfoFragment extends Fragment {

    private static String EXTRA_ID_WEB = "index";

    public static InfoFragment newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_ID_WEB, index);
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.m_info_fragment, container, false);
        WebView web = (WebView) v.findViewById(R.id.Web);
        switch (getArguments().getInt(EXTRA_ID_WEB)) {
            case 0:
                web.loadUrl("file:///android_asset/infocomman.html");
                break;
            case 1:
                web.loadUrl("file:///android_asset/infodiary.html");
        }

        return v;

    }
}
