package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ImagePagerAdapter  extends FragmentPagerAdapter {
    private int[] imagesID = {R.drawable.bn1, R.drawable.bn2, R.drawable.bn3,R.drawable.bn1};
    public ImagePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(imagesID[position]);
    }

    @Override
    public int getCount() {
        return imagesID.length;
    }
}
