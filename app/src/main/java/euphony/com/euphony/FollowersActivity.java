package euphony.com.euphony;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by pR0 on 25-06-2016.
 */
public class FollowersActivity extends AppCompatActivity {

    TabLayout tabLayout;
    FollowerFollowingFrag followerFollowingFrag;
    PrefManager prefManager;
    ImageView back;
    TextView heading, loadingText;
    RelativeLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.following_follower_activity);

        prefManager = new PrefManager(Application.ctx);
        tabLayout = (TabLayout) findViewById(R.id.following_layout);
        back = (ImageView) findViewById(R.id.back);
        heading = (TextView) findViewById(R.id.heading);
        heading.setText(prefManager.getName());

        progress = (RelativeLayout) findViewById(R.id.progress);
        loadingText = (TextView) findViewById(R.id.loading_text);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Following"));
        tabLayout.addTab(tabLayout.newTab().setText("Followers"));

        tabLayout.setOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        switch (tab.getPosition()) {
                            case 0: {
                                loadingText.setText("Getting Following List");
                                progress.setVisibility(View.VISIBLE);
                                replaceFragment(FollowerFollowingFrag.newInstance("following"));
                            }
                            break;
                            case 1: {
                                progress.setVisibility(View.VISIBLE);
                                loadingText.setText("Getting Your Followers List");
                                replaceFragment(FollowerFollowingFrag.newInstance("followers"));
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

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.SANS_SERIF);
                }
            }
        }

        loadingText.setText("Getting Following List");
        progress.setVisibility(View.VISIBLE);

        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.follow_container, FollowerFollowingFrag.newInstance("following"));
        fragmentTransaction.commit();
    }
    public void replaceFragment(Fragment f){
        FragmentTransaction fm = this.getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.follow_container, f);
        fm.commit();
    }
}