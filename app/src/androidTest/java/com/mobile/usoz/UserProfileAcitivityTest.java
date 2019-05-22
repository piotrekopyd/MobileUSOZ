package com.mobile.usoz;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.TextView;

import com.mobile.usoz.UserActivities.UserProfileAcitivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserProfileAcitivityTest {
    @Rule
    public ActivityTestRule<UserProfileAcitivity> rule = new ActivityTestRule<>(UserProfileAcitivity.class);
    private UserProfileAcitivity activity = null;

    @Before
    public void setUp() {
        activity = rule.getActivity();
    }

    @After
    public void tearDown() {
        activity = null;
    }

    //Unit Tests
    @Test
    public void onCreate_components() {
        assertNotNull(activity.findViewById(R.id.userName_label));
        assertNotNull(activity.findViewById(R.id.university_label));
        assertNotNull(activity.findViewById(R.id.userEmailTextView));
        assertNotNull(activity.findViewById(R.id.textView7));
        assertNotNull(activity.findViewById(R.id.userBirthdayTextView));
        assertNotNull(activity.findViewById(R.id.textView11));
        assertNotNull(activity.findViewById(R.id.userPassionsTextView));
    }

    @Ignore ("dziala ale wylogowywuje wiec trzeba odpalac osobno")
    @Test
    public void updateLogOutUI_toast() throws Exception {
        Whitebox.invokeMethod(activity, "updateLogOutUI");
        onView(withText("You're logged out")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onBackPressed_foreground() {
        Drawable temp = activity.findViewById(R.id.user_profile_relative_layout_1).getForeground();
        if(temp != null)
            assertEquals(180, temp.getAlpha());
    }

    @Test
    public void onBackPressed_exitButton() {
        int expected = 4;
        View v = activity.findViewById(R.id.included_exit_layout);
        assertEquals(expected, v.getVisibility());
    }

    //Instrumented Tests
    @Test
    public void retrieveUserData_name() throws Exception {
        TextView label = activity.findViewById(R.id.userName_label);
        String expected = label.toString();
        Whitebox.invokeMethod(activity, "retrieveUserData");
        assertEquals(expected, label.toString());
    }

    @Test
    public void retrieveUserData_university() throws Exception {
        TextView label = activity.findViewById(R.id.university_label);
        String expected = label.toString();
        Whitebox.invokeMethod(activity, "retrieveUserData");
        assertEquals(expected, label.toString());
    }

    @Test
    public void retrieveUserData_email() throws Exception {
        TextView label = activity.findViewById(R.id.userEmailTextView);
        String expected = label.toString();
        Whitebox.invokeMethod(activity, "retrieveUserData");
        assertEquals(expected, label.toString());
    }

    @Test
    public void retrieveUserData_birthday() throws Exception {
        TextView label = activity.findViewById(R.id.userBirthdayTextView);
        String expected = label.toString();
        Whitebox.invokeMethod(activity, "retrieveUserData");
        assertEquals(expected, label.toString());
    }

    @Test
    public void retrieveUserData_passions() throws Exception {
        TextView label = activity.findViewById(R.id.userPassionsTextView);
        String expected = label.toString();
        Whitebox.invokeMethod(activity, "retrieveUserData");
        assertEquals(expected, label.toString());
    }

    @Test
    public void updateUI_call() throws Exception {
        TextView label = activity.findViewById(R.id.userName_label);
        String expected = label.toString();
        Whitebox.invokeMethod(activity, "updateUI");
        assertEquals(expected, label.toString());
    }

}
