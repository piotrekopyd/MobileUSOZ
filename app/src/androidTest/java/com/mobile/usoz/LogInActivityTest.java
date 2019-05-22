package com.mobile.usoz;

import com.mobile.usoz.UserAccount.LogInActivity;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.TextView;

import androidx.test.filters.Suppress;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class LogInActivityTest {

    @Rule
    public ActivityTestRule<LogInActivity> rule = new ActivityTestRule<>(LogInActivity.class);
    private LogInActivity activity = null;

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
    public void onCreate_elementsNotNull() {
        assertNotNull(activity.findViewById(R.id.loginTextView));
        assertNotNull(activity.findViewById(R.id.PswdTextView));
        assertNotNull(activity.findViewById(R.id.logInButton));
        assertNotNull(activity.findViewById(R.id.createAccountButton));
        assertNotNull(activity.findViewById(R.id.fbLogin_button));
        assertNotNull(activity.findViewById(R.id.googleSignInButton));
    }

    @Test
    public void onClick_null() {
        activity.onClick(null);
    }

    @Test
    public void onClick_correct() {
        View view = activity.findViewById(R.id.fbLogin_button);
        activity.onClick(view);
    }

    @Test
    public void signInEmailPassword_emptyPassword() {
        try {
            Whitebox.invokeMethod(activity, "singInEmailPassword", "temp@temp.pl", "");
            fail();
        }
        catch(Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void signInEmailPassword_emptyEmail() {
        try {
            Whitebox.invokeMethod(activity, "singInEmailPassword", "", "temp");
            fail();
        }
        catch(Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void signInEmailPassword_empty() {
        try {
        Whitebox.invokeMethod(activity, "signInEmailPassword", "", "");
            fail();
        }
        catch(Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void signInEmailPassword_null() {
        try {
        Whitebox.invokeMethod(activity, "singInEmailPassword", "temp@temp.pl", null);
            fail();
        }
        catch(Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void signInEmailPassword_wrongData() throws Exception {
        try {
        Whitebox.invokeMethod(activity, "singInEmailPassword", "temp@temp.pl", "1234");
            fail();
        }
        catch(Exception e) {
            assertTrue(true);
        }
    }

    //onView(allOf(withId(R.string.mssage), isDisplayed()));

    //Instrumented tests
    @Test
    public void loginWithEmptyData_PasswordCheck() {
        onView(withId(R.id.logInButton)).perform(click());
        TextView temp = activity.findViewById(R.id.PswdTextView);
        assertEquals("Required.", temp.getError());
    }

    @Test
    public void loginWithEmptyData_EmailCheck() {
        onView(withId(R.id.logInButton)).perform(click());
        TextView temp = activity.findViewById(R.id.loginTextView);
        assertEquals("Required.", temp.getError());
    }

    @Test
    public void loginWithEmptyPassword() {
        onView(withId(R.id.loginTextView)).perform(typeText("temp@temp.pl"), closeSoftKeyboard());
        onView(withId(R.id.logInButton)).perform(click());
        TextView temp = activity.findViewById(R.id.PswdTextView);
        assertEquals("Required.", temp.getError());
    }

    @Test
    public void loginWithEmptyEmail(){
        TextView temp = activity.findViewById(R.id.loginTextView);
        onView(withId(R.id.PswdTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.logInButton)).perform(click());
        assertEquals("Required.", temp.getError());
    }

    @Test
    public void loginFail() {
        onView(withId(R.id.loginTextView)).perform(typeText("12@12.pl"), closeSoftKeyboard());
        onView(withId(R.id.PswdTextView)).perform(typeText("1234"), closeSoftKeyboard());
        onView(withId(R.id.logInButton)).perform(click());
        TextView temp = activity.findViewById(R.id.PswdTextView);
        assertNull(temp.getError());
        onView(withText("Authentication failed.")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Ignore("Zaloguje i nie bedzie dostepu do kolejnych testow wiec odpalac osobno")
    @Test
    public void loginSuccess() {
        onView(withId(R.id.loginTextView)).perform(typeText("12@12.pl"), closeSoftKeyboard());
        onView(withId(R.id.PswdTextView)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.logInButton)).perform(click());
        TextView temp = activity.findViewById(R.id.PswdTextView);
        assertNull(temp.getError());
    }
}