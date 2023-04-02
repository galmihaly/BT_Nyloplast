package hu.logcontrol.wasteprogram.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import hu.logcontrol.wasteprogram.fragments.GeneralSettingsFragment;
import hu.logcontrol.wasteprogram.fragments.UploadFileSettingsFragment;

public class SettingViewPagerAdapter extends FragmentStateAdapter {

    private List<Fragment> fragments;
    private int currentPage;

    public SettingViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: {
                currentPage = position;
                return new GeneralSettingsFragment();
            }
            case 1: {
                currentPage = position;
                return new UploadFileSettingsFragment();
            }
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public void setFragmentList(List<Fragment> fragments){
        this.fragments = fragments;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }
}
