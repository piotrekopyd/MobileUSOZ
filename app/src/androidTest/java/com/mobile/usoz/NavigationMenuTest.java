package com.mobile.usoz;

import android.support.design.widget.NavigationView;
import android.support.test.rule.ActivityTestRule;
import android.view.MenuItem;

import com.mobile.usoz.Calendar.Calendar.CalendarActivity;
import com.mobile.usoz.UserActivities.EditUserDataActivities.EditUserDataActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NavigationMenuTest {
    @Rule
    public ActivityTestRule<CalendarActivity> rule = new ActivityTestRule<>(CalendarActivity.class);
    private CalendarActivity activity = null;

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    @Test
    public void menu_profile() {
        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        boolean result = activity.onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_profile));
        assertTrue(result);
    }

    @Test
    public void menu_calendar() {
        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        boolean result = activity.onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_calendar));
        assertTrue(result);
    }

    @Test
    public void menu_lecturers() {
        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        boolean result = activity.onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_calendar));
        assertTrue(result);
    }

    @Test
    public void menu_map() {
        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        boolean result = activity.onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_maps));
        assertTrue(result);
    }

}
