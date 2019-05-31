package com.mobile.usoz;
import android.support.test.rule.ActivityTestRule;
import com.mobile.usoz.UserAccount.CreateAccountActivity;

import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;
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

public class CreateAccountActivityTest {
    @Rule
    public ActivityTestRule<CreateAccountActivity> rule = new ActivityTestRule<>(CreateAccountActivity.class);
    private CreateAccountActivity activity = null;

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    //Unit tests
    @Test
    public void onCreate() {
        assertNotNull(activity.findViewById(R.id.createEmailTextView));
        assertNotNull(activity.findViewById(R.id.PswdTextView));
        assertNotNull(activity.findViewById(R.id.registerButton));
    }

    @Test
    public void createAccount_registerErrorToastEmpty() {
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText("Pola nie mogą być puste!")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void createAccount_registerErrorToastEmptyPassword() {
        onView(withId(R.id.createEmailTextView)).perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText("Konto zostało założone pomyślnie!")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(doesNotExist());
    }

    @Test
    public void createAccount_registerErrorToastEmptyEmail() {
        onView(withId(R.id.PswdTextView)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText("Konto zostało założone pomyślnie!")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(doesNotExist());
    }

    @Test
    public void createAccount_registerErrorPasswordTooShort() {
        onView(withId(R.id.createEmailTextView)).perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.PswdTextView)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText("Hasło musi mieć co najmniej 6 znaków!")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void createAccount_registerErrorWrongEmail() {
        onView(withId(R.id.createEmailTextView)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.PswdTextView)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText("Autentykacja nieudana")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    //Instrumented tests
    @Test
    public void saveData_nullUser() throws Exception {
        try {
            Whitebox.invokeMethod(activity, "saveDefaultData");
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Ignore ("wpisac email do nieistniejacego konta")
    @Test
    public void createAccount_success() {
        onView(withId(R.id.createEmailTextView)).perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.PswdTextView)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText("Konto zostało założone pomyślnie!")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void createAccount_alreadyExists() {
        onView(withId(R.id.createEmailTextView)).perform(typeText("12@12.pl"), closeSoftKeyboard());
        onView(withId(R.id.PswdTextView)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText("Autentykacja nieudana")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }
}
