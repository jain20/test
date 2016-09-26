package com.example.akankshajain.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.List;
import com.example.akankshajain.myapplication.R;
import com.example.akankshajain.myapplication.sqlite.database;
import com.example.akankshajain.myapplication.model.TaskModel;
import com.example.akankshajain.myapplication.adapter.TaskAdapter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;



public class ListTask extends Fragment implements ActionDialogListener, AdapterView.OnItemClickListener {
    private ListView lvTaskList;
    private List<TaskModel> taskList;
    private TaskAdapter taskAdapter;
    private database database;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_list, container, false);
        lvTaskList = (ListView) view.findViewById(R.id.lv_task);
        lvTaskList.setOnItemClickListener(this);

        database = new database(getContext());

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionDialog addDlg = new ActionDialog();
                addDlg.show(getFragmentManager(), "AddDlg");
            }
        });

        taskList = database.getAllTasks();
        taskAdapter = new TaskAdapter(this.getContext(), taskList);
        lvTaskList.setAdapter(taskAdapter);

        return view;
    }

    @Override
    public void onFinish(Intent data) {
        Snackbar.make(getView(), "Task has been added!", Snackbar.LENGTH_SHORT).show();

        TaskModel task = (TaskModel) data.getExtras().getSerializable("task");

        database.addTask(task);
        taskAdapter.updateAdapter(task);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("task", taskList.get(position));

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new TaskDetail();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.content_frg, fragment, "TaskDetail")
                .addToBackStack("TaskDetail")
                .commit();
    }
}
