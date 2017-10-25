package br.com.damasceno.agenda.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Switch;

import br.com.damasceno.agenda.fragment.ContactFragment;
import br.com.damasceno.agenda.fragment.EventFragment;
import br.com.damasceno.agenda.fragment.TaskFragment;

/**
 * Created by dmscn on 24/10/17.
 */

public class PageAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public PageAdapter(FragmentManager manager, int numberOfTabs) {
        super(manager);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TaskFragment taskFragment = new TaskFragment();
                return taskFragment;

            case 1:
                EventFragment eventFragment = new EventFragment();
                return eventFragment;

            case 2:
                ContactFragment contactFragment = new ContactFragment();
                return contactFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
