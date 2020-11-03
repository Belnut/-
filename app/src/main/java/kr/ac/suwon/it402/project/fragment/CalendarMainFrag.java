package kr.ac.suwon.it402.project.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kr.ac.suwon.it402.project.MainActivity;
import kr.ac.suwon.it402.project.R;

/**
 * Created by ohj84_000 on 2016-08-08.
 */
public class CalendarMainFrag extends Fragment {

    private static final int PAGE_LEFT = 0;
    private static final int PAGE_MIDDLE = 1;
    private static final int PAGE_RIGHT = 2;

    private static final String CHILD_FRAGMENT_TAG = "child_fragment_";


    private int mSelectedPageIndex = 0;

    FragmentTransaction transaction = null;
    ViewPager mViewPager = null;
    CalendarFragmentAdapter mAdapter = null;
    FragmentManager mFragmentChildManager = null;


    int year, month;
    int td_year, td_month, td_day;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        td_year = Integer.parseInt(curYearFormat.format(date));
        td_month = Integer.parseInt(curMonthFormat.format(date));
        td_day = Integer.parseInt(curDayFormat.format(date));




        Bundle BundleForMainActivity = getArguments();
        year = BundleForMainActivity.getInt("year");
        month = BundleForMainActivity.getInt("month");

        Log.i("실행됨 main frag ", ""+year + " " + month);

        //커스텀 뷰페이지 아이디 설정
        mViewPager = (ViewPager) view.findViewById(R.id.calendar_viewpager);

        //viewpager의 fragment를 관리할 transaction

        setPagerAdapterView();

    }
    //






    public void setPagerAdapterView()
    {

        //mFragmentChildManager = getChildFragmentManager();
        mAdapter = new CalendarFragmentAdapter(getChildFragmentManager());

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(500, false);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                if (state == ViewPager.SCROLL_STATE_IDLE)
                {
                    mSelectedPageIndex = mViewPager.getCurrentItem();
                }
                if (mSelectedPageIndex != mViewPager.getCurrentItem())
                {

                    int mYear = year, mMonth= month;

                    int movePosition =  mViewPager.getCurrentItem() - 500 ;

                    mMonth +=movePosition;

                    while (mMonth <= 0 || mMonth >= 13)
                    {
                        if (mMonth >= 13)
                        {
                            mMonth -= 12;
                            mYear += 1;
                        }

                        else
                        {
                            mMonth += 12;
                            mYear -= 1;
                        }
                    }

                    ((MainActivity)getActivity()).calenderToolbarSetText(mYear, mMonth);
                }

            }
        });

    }

    private CalendarFragment makeNewChildFragment(int position)
    {

        int mYear = year, mMonth= month;

        int movePosition =  position - 500 ;

        mMonth +=movePosition;

        Log.i("실행됨 main frag 만드는중 ", "movePos : "+mYear + " " + mMonth);

        while (mMonth <= 0 || mMonth >= 13)
        {
            if (mMonth >= 13)
            {
                mMonth -= 12;
                mYear += 1;
            }

            else
            {
                mMonth += 12;
                mYear -= 1;
            }
            Log.i("실행됨 main frag 만드는중 ", "while 돌리는중 : "+mYear + " " + mMonth);
        }

        Log.i("실행됨 main frag 만드는중 ", ""+mYear + " " + mMonth);

        CalendarFragment mFragment = new CalendarFragment();

        Bundle bundleYearMonth = new Bundle();
        bundleYearMonth.putInt("year", mYear);
        bundleYearMonth.putInt("month", mMonth);

        mFragment.setArguments(bundleYearMonth);


        return mFragment;
    }

    public FragmentManager getChildFragmentManagerFunc()
    {
        return getChildFragmentManager();
    }







    public class CalendarFragmentAdapter extends FragmentStatePagerAdapter {

        public CalendarFragmentAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override

        public Fragment getItem(int position) {

            return makeNewChildFragment(position);
        }

        @Override
        public int getCount() {
            return 1000;
        }

        //notiftySetDataChange 함수 호출시
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    private void initPageFragment()
    {
    }



    /*
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.calendar, container, false);
            VerticalViewPager pager = (VerticalViewPager) rootView.findViewById(R.id.calendar_viewpager);


            CalendarFragment mCalendarFragment = new CalendarFragment(int year, int month);








            CalendarFragmentAdapter pagerAdapter = new CalendarFragmentAdapter( getChildFragmentManager() );
            pager.setAdapter( new CalendarFragmentAdapter( getChildFragmentManager() )

            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            return super.onCreateView(inflater, container, savedInstanceState);
        }
    */


}
