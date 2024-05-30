package com.cristiawan.goparty.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cristiawan.goparty.R;
import com.cristiawan.goparty.databinding.FragmentHistoryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
private FragmentHistoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);

    binding = FragmentHistoryBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

//        final TextView textView = binding;
        historyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar appbar = view.findViewById(R.id.customToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(appbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = view.findViewById(R.id.tvToolbar);
        tvTitle.setText("History");

        ImageView ivBack = view.findViewById(R.id.btnBack);


    }
}