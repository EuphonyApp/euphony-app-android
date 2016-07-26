package euphony.com.euphony;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by pR0 on 24-06-2016.
 */
public class NearbyFrag extends Fragment {
    TabLayout tabLayout;
    ImageView searchBtn, back;
    EditText searchField;
    LinearLayout searchLay;
    Animation rotate;
    boolean search = false;
    DrawerLayout drawerLayout;
    ArtistFrag artistFrag;
    PrefManager prefManager;
    TextView heading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {
        return inflater.inflate(R.layout.nearby_frag, viewGroup, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchBtn = (ImageView) getActivity().findViewById(R.id.search);
        searchField = (EditText) getActivity().findViewById(R.id.searchField);
        artistFrag = new ArtistFrag();
        searchField.addTextChangedListener(artistFrag);

        searchLay = (LinearLayout) getActivity().findViewById(R.id.search_lay);

        heading = (TextView) getActivity().findViewById(R.id.heading);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerLayout);

        prefManager = new PrefManager(Application.ctx);
        tabLayout = (TabLayout) getView().findViewById(R.id.nearby_tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Artist"));
        tabLayout.addTab(tabLayout.newTab().setText("Bands"));


        tabLayout.setOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        switch (tab.getPosition()) {
                            case 0: {
                                getActivity().findViewById(R.id.select_city).setVisibility(View.GONE);
                                getActivity().findViewById(R.id.search).setVisibility(View.VISIBLE);
                                heading.setText("Nearby");
                                replaceFragment(new ArtistFrag());
                            }
                            break;
                            case 1: {
                                getActivity().findViewById(R.id.select_city).setVisibility(View.VISIBLE);
                                getActivity().findViewById(R.id.search).setVisibility(View.GONE);

                                BandFrag bandFrag = new BandFrag();
                                replaceFragment(bandFrag);
                            }
                            break;
                            default: {
                            }
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );

        back = (ImageView) getActivity().findViewById(R.id.menu);

        getActivity().findViewById(R.id.select_city).setVisibility(View.GONE);

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.SANS_SERIF);
                }
            }
        }

        rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_anim);
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (search) {
                    searchBtn.setVisibility(View.GONE);
                    searchLay.setVisibility(View.VISIBLE);
                    searchField.requestFocus();

                    tabLayout.setVisibility(View.GONE);
                    getActivity().findViewById(R.id.tab_layout).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.heading).setVisibility(View.GONE);
                } else {
                    searchBtn.setVisibility(View.VISIBLE);
                    searchLay.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.tab_layout).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.heading).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (search) {
                    back.setImageResource(R.drawable.ic_back);
                    back.setAlpha(0.9f);
                } else {
                    back.setImageResource(R.drawable.ic_menu);
                    back.setAlpha(1.0f);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = true;
                back.startAnimation(rotate);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search) {
                    search = false;
                    back.startAnimation(rotate);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.nearby_container, new ArtistFrag());
                    fragmentTransaction.commit();

                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.nearby_container, new ArtistFrag());
        fragmentTransaction.commit();
    }

    public void replaceFragment(Fragment f){
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.nearby_container, f);
        fm.commit();
    }
}