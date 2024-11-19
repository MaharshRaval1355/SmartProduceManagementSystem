package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView statusTextView;
    private int taskCounter = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        progressBar = view.findViewById(R.id.progress_bar);
        statusTextView = view.findViewById(R.id.status_text);

        // Simulate task progression
        view.findViewById(R.id.start_task_button).setOnClickListener(v -> handleTaskProgress());
    }

    private void handleTaskProgress() {
        if (taskCounter < 10) {
            taskCounter++;
            statusTextView.setText("Tasks Completed: " + taskCounter);

            // Show progress and Toast
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(taskCounter * 10);
            Toast.makeText(getContext(), "Task " + taskCounter + " completed!", Toast.LENGTH_SHORT).show();

            // Hide progress bar after 10 tasks
            if (taskCounter == 10) {
                progressBar.setVisibility(View.GONE);
                statusTextView.setText("All tasks completed!");
            }
        } else {
            Toast.makeText(getContext(), "All tasks are already completed.", Toast.LENGTH_SHORT).show();
        }
    }
}

