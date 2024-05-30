package com.cristiawan.goparty.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cristiawan.goparty.Item;
import com.cristiawan.goparty.ItemAdapter;
import com.cristiawan.goparty.Login;
import com.cristiawan.goparty.R;
import com.cristiawan.goparty.databinding.FragmentHomeBinding;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    View root;
    RecyclerView recyclerView;
    ItemAdapter adapter;
    List<Item> itemList;

    private HomeViewModel homeViewModel;
private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

    binding = FragmentHomeBinding.inflate(inflater, container, false);
    root = binding.getRoot();

       // final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
        tvTitle.setText("Home");

        ImageView ivBack = view.findViewById(R.id.btnBack);
        ivBack.setOnClickListener(v -> new Login());


        load();
        isiData();
    }

    public void load(){
        recyclerView = root.findViewById(R.id.recHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void isiData(){
        itemList = new ArrayList<Item>();
        itemList.add(new Item("Terop 25X25", "Rp.500000","Tenda Panjang Tanpa Bocor"));
        itemList.add(new Item("Terop 25X25", "Rp.500000","Tenda Panjang Tanpa Bocor"));
        itemList.add(new Item("Terop 25X25", "Rp.500000","Tenda Panjang Tanpa Bocor"));
        itemList.add(new Item("Terop 25X25", "Rp.500000","Tenda Panjang Tanpa Bocor"));
        itemList.add(new Item("Terop 25X25", "Rp.500000","Tenda Panjang Tanpa Bocor"));
        itemList.add(new Item("Terop 25X25", "Rp.500000","Tenda Panjang Tanpa Bocor"));
        itemList.add(new Item("Terop 25X25", "Rp.500000","Tenda Panjang Tanpa Bocor"));
        itemList.add(new Item("Terop 25X25", "Rp.500000","Tenda Panjang Tanpa Bocor"));
        itemList.add(new Item("Terop 25X25", "Rp.500000","Tenda Panjang Tanpa Bocor"));
        adapter = new ItemAdapter(getActivity(),itemList);
        recyclerView.setAdapter(adapter);
    }

}