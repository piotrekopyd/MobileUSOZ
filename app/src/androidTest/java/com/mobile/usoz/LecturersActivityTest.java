package com.mobile.usoz;

import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.mobile.usoz.LecturersActivities.LecturersActivity;
import com.mobile.usoz.Maps.MapsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class LecturersActivityTest {
    @Rule
    public ActivityTestRule<LecturersActivity> rule = new ActivityTestRule<>(LecturersActivity.class);
    private LecturersActivity activity = null;

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    @Test
    public void onCreate_components() {
        assertNotNull(activity.findViewById(R.id.lecturers_text_name));
        assertNotNull(activity.findViewById(R.id.lecturers_text_surname));
        assertNotNull(activity.findViewById(R.id.lecturers_text_university));
        assertNotNull(activity.findViewById(R.id.lecturers_button_save));
        assertNotNull(activity.findViewById(R.id.toolbar));
    }

    //onOptionsItemSelected

    @Test
    public void onBackPressed_foreground() {
        Drawable temp = activity.findViewById(R.id.lecturers_edit_relative_layout).getForeground();
        if(temp != null)
            assertEquals(0, temp.getAlpha());
    }

    @Test
    public void onBackPressed_exitButtonVisibility() {
        int expected = 4;
        View v = activity.findViewById(R.id.included_exit_layout);
        assertEquals(expected, v.getVisibility());
    }

    @Test
    public void onBackPressed_exitButtonClickable() {
        View v = activity.findViewById(R.id.included_exit_layout);
        assertFalse(v.isClickable());
    }

    @Test
    public void onOptionsItemSelected_null() {
        activity.onOptionsItemSelected(null);
    }

    @Test
    public void onOptionsItemSelected_settingsHide() {
        activity.onOptionsItemSelected(activity.findViewById(R.id.lecturers_settings_add));
        assertFalse(activity.findViewById(R.id.lecturers_edit_relative_layout).isClickable());
    }

    //instrumented tests
    @Ignore ("odpalac na koncie admina")
    @Test
    public void downloadLecturers() {
        activity.downloadLecturers();
        onView(withText("Nie udało się pobrać listy prowadzących")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(doesNotExist());
    }

    @Test
    public void updateGrade_success() {
        activity.updateGrade("0", "Lecturer", 5.0, activity);
        onView(withText("Twoja ocena została dodana")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(doesNotExist());
    }

    @Test
    public void updateGrade_fail() {
        activity.updateGrade("11", "Krzysztof Byrski", 5.0, activity);
        onView(withText("Niestety oceniłeś już wcześniej tego prowadzącego. Jeżeli uważasz, że to błąd aplikacji skontaktuj się z nami")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(doesNotExist());
    }

    @Ignore ("odpalac na koncie admina")
    @Test
    public void onCreateOptionsMenu_admin() {
        assertNotNull(activity.findViewById(R.menu.lecturers_edit_menu));
    }
}
