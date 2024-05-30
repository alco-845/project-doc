package com.cristiawan.goparty.ui.account;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.cristiawan.goparty.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

    binding = FragmentAccountBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

      //  final TextView textView = binding.textHome;
        accountViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText(s);
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
        tvTitle.setText("Profile");

        ImageView ivBack = view.findViewById(R.id.btnBack);
        

        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_update_profile);

            dialog.show();
        });

    }

}