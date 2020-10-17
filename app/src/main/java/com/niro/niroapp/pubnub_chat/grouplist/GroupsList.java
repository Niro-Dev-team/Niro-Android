package com.niro.niroapp.pubnub_chat.grouplist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.niro.niroapp.R;


public class GroupsList extends Fragment {
    public GroupsList() {

    }
    private View view;
    GroupListData[] GroupListData = new GroupListData[] {
            new GroupListData("Testing Group 1", android.R.drawable.ic_dialog_email),
            new GroupListData("Testing Group 2", android.R.drawable.ic_dialog_info),
            new GroupListData("Testing Group 3", android.R.drawable.ic_delete),
            new GroupListData("Testing Group 4", android.R.drawable.ic_dialog_dialer),
            new GroupListData("Testing Group 5", android.R.drawable.ic_dialog_alert),
            new GroupListData("Testing Group 6", android.R.drawable.ic_dialog_map),
            new GroupListData("Testing Group 7", android.R.drawable.ic_dialog_email),
            new GroupListData("Testing Group 8", android.R.drawable.ic_dialog_info),
            new GroupListData("Testing Group 9", android.R.drawable.ic_delete),
            new GroupListData("Testing Group 10", android.R.drawable.ic_dialog_dialer),
            new GroupListData("Testing Group 11", android.R.drawable.ic_dialog_alert),
            new GroupListData("Testing Group 12", android.R.drawable.ic_dialog_map),
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.list_of_groups, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        GroupListAdapter adapter = new GroupListAdapter(GroupListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }
}