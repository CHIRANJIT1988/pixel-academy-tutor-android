package pixel.academy.tutor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pixel.academy.tutor.helper.Helper;
import pixel.academy.tutor.helper.MyViewPager;

public class EditProfileActivity extends AppCompatActivity
{
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public MyViewPager mViewPager;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("");

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        mViewPager = (MyViewPager) findViewById(R.id.pager);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Fixes bug for disappearing fragment content
        mViewPager.setOffscreenPageLimit(12);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPagingEnabled(false);
        setupViewPager(mViewPager);

        set_up_page(getIntent().getIntExtra("INDEX", 0));
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    private void setupViewPager(MyViewPager viewPager)
    {

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new EditAddressFragment(), "Address");
        adapter.addFrag(new EditOccupationFragment(), "Occupation");
        adapter.addFrag(new EditPreferenceFragment(), "Preferences");
        adapter.addFrag(new EditEducationFragment(), "Education");

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new MyViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        private SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragmentList.size();
        }

        private void addFrag(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mFragmentTitleList.get(position);
        }
    }

    /*private void redirectFragment(String target)
    {

        Fragment fragment = new Fragment();

        switch (target)
        {

            case "ADDRESS":

                fragment = new EditAddressFragment();
                break;

            case "EDUCATION":

                fragment = new EditEducationFragment();
                break;

            case "OCCUPATION":

                fragment = new EditOccupationFragment();
                break;

            case "PREFERENCES":

                fragment = new EditPreferenceFragment();
                break;

        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {

            case android.R.id.home:

                {
                    finish();
                    break;
                }
        }

        return super.onOptionsItemSelected(item);
    }

    private void set_up_page(int index)
    {
        mViewPager.setCurrentItem(index, true);
        toolbar_title.setText(Helper.toCamelCase(mFragmentTitleList.get(index)));
    }
}