package com.example.weatherforecastd9k.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weatherforecastd9k.R;
import com.example.weatherforecastd9k.viewmodel.RecommendViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RecommendWeatherFragment extends Fragment {
    private SwipeRefreshLayout swipeRefresh;
    private ImageView bingImage;
    private TextInputEditText qrContentInput;
    private MaterialButton generateQrButton;
    private ImageView qrCodeImage;
    private RecommendViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_weather, container, false);
        
        initViews(view);
        setupViewModel();
        setupQrCodeGeneration();
        loadBingImage();
        
        swipeRefresh.setOnRefreshListener(this::loadBingImage);
        
        return view;
    }

    private void initViews(View view) {
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        bingImage = view.findViewById(R.id.bingImage);
        qrContentInput = view.findViewById(R.id.qrContentInput);
        generateQrButton = view.findViewById(R.id.generateQrButton);
        qrCodeImage = view.findViewById(R.id.qrCodeImage);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(RecommendViewModel.class);
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), 
            isLoading -> swipeRefresh.setRefreshing(isLoading));
            
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), 
            message -> {
                if (message != null) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void setupQrCodeGeneration() {
        generateQrButton.setOnClickListener(v -> {
            String content = qrContentInput.getText().toString().trim();
            LiveData<Bitmap> qrCode = viewModel.generateQrCode(content);
            if (qrCode != null) {
                qrCode.observe(getViewLifecycleOwner(), 
                    bitmap -> {
                        if (bitmap != null) {
                            qrCodeImage.setImageBitmap(bitmap);
                        }
                    });
            }
        });
    }

    private void loadBingImage() {
        viewModel.getBingImage().observe(getViewLifecycleOwner(), 
            bitmap -> {
                if (bitmap != null) {
                    bingImage.setImageBitmap(bitmap);
                }
            });
    }
} 