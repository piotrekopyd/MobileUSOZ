package com.mobile.usoz;

import android.support.design.widget.NavigationView;
import android.support.test.rule.ActivityTestRule;
import android.widget.Spinner;

import com.mobile.usoz.Calendar.Calendar.DatesActivity;
import com.mobile.usoz.Calendar.Notes.AddNewNoteActivity;
import com.mobile.usoz.UserActivities.EditUserDataActivities.EditUserDataActivity;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AddNewNoteActivityTest {

    @Rule
    public ActivityTestRule<AddNewNoteActivity> rule = new ActivityTestRule<>(AddNewNoteActivity.class);
    private AddNewNoteActivity activity = null;

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    @Test
    public void onCreateTest() {
        assertNotNull(activity.findViewById(R.id.toolbar));
        assertNotNull(activity.findViewById(R.id.saveNoteSaveButton));
        assertNotNull(activity.findViewById(R.id.saveNoteNoteTextView));
        assertNotNull(activity.findViewById(R.id.AddNewNoteDaySpinner));
        assertNotNull(activity.findViewById(R.id.AddNewNoteMonthSpinner));
    }

    @Test
    public void spinnerDayDefault() {
        onView(withId(R.id.AddNewNoteDaySpinner)).check(matches(withSpinnerText(containsString("Dzień"))));
    }

    @Test
    public void spinnerMonthDefault() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).check(matches(withSpinnerText(containsString("Miesiąc"))));
    }

    @Test
    public void editTextDefault() {
        onView(withId(R.id.saveNoteNoteTextView)).check(matches(withHint("Twoja notatka")));
    }

    @Test
    public void spinnerMonthStyczen() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.AddNewNoteMonthSpinner)).check(matches(withSpinnerText(containsString("Styczeń"))));
    }

    @Test
    public void spinnerMonthLuty() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.AddNewNoteMonthSpinner)).check(matches(withSpinnerText(containsString("Luty"))));
    }

    @Test
    public void spinnerMonthMarzec() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(3).perform(click());
        onView(withId(R.id.AddNewNoteMonthSpinner)).check(matches(withSpinnerText(containsString("Marzec"))));
    }

    @Test
    public void spinnerMonthKwiecien() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(4).perform(click());
        onView(withId(R.id.AddNewNoteMonthSpinner)).check(matches(withSpinnerText(containsString("Kwiecień"))));
    }

    @Test
    public void spinnerMonthMaj() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(5).perform(click());
        onView(withId(R.id.AddNewNoteMonthSpinner)).check(matches(withSpinnerText(containsString("Maj"))));
    }

    @Test
    public void spinnerMonthCzerwiec() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(6).perform(click());
        onView(withId(R.id.AddNewNoteMonthSpinner)).check(matches(withSpinnerText(containsString("Czerwiec"))));
    }

    @Test
    public void spinnerMonthLipiec() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(7).perform(click());
        onView(withId(R.id.AddNewNoteMonthSpinner)).check(matches(withSpinnerText(containsString("Lipiec"))));
    }

    @Test
    public void spinnerMonthSierpien() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(8).perform(click());
        onView(withId(R.id.AddNewNoteMonthSpinner)).check(matches(withSpinnerText(containsString("Sierpień"))));
    }

    @Test
    public void spinnerMonthWrzesien() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(9).perform(click());
        onView(withId(R.id.AddNewNoteMonthSpinner)).check(matches(withSpinnerText(containsString("Wrzesień"))));
    }

    @Test
    public void spinnerMonthPazdziernik() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(10).perform(click());
        onView(withId(R.id.AddNewNoteMonthSpinner)).check(matches(withSpinnerText(containsString("Październik"))));
    }

    @Test
    public void spinnerMonthListopad() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(11).perform(click());
        onView(withId(R.id.AddNewNoteMonthSpinner)).check(matches(withSpinnerText(containsString("Listopad"))));
    }

    @Test
    public void spinnerMonthGrudzien() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(12).perform(click());
        onView(withId(R.id.AddNewNoteMonthSpinner)).check(matches(withSpinnerText(containsString("Grudzień"))));
    }

    @Test
    public void spinnerMonthChangeDefaultDayText() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(12).perform(click());
        onView(withId(R.id.AddNewNoteDaySpinner)).check(matches(withSpinnerText(containsString("Dzień"))));
    }

    @Test
    public void spinnerDay1() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(12).perform(click());
        onView(withId(R.id.AddNewNoteDaySpinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.AddNewNoteDaySpinner)).check(matches(withSpinnerText(containsString("1"))));
    }

    @Test
    public void spinnerDay31() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(12).perform(click());
        onView(withId(R.id.AddNewNoteDaySpinner)).perform(click());
        onData(anything()).atPosition(31).perform(click());
        onView(withId(R.id.AddNewNoteDaySpinner)).check(matches(withSpinnerText(containsString("31"))));
    }

    @Test
    public void spinnerDay31Wrong() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(11).perform(click());
        onView(withId(R.id.AddNewNoteDaySpinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.AddNewNoteDaySpinner)).check(matches(withSpinnerText(not(containsString("31")))));
    }

    @Test
    public void spinnerDay30Wrong() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.AddNewNoteDaySpinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.AddNewNoteDaySpinner)).check(matches(withSpinnerText(not(containsString("30")))));
    }

    @Test
    public void onClickSave_emptyAll() {
        onView(withId(R.id.saveNoteSaveButton)).perform(click());
        onView(withText("Pusta notka lub niepoprawna data!")).inRoot(withDecorView(Matchers.not(Matchers.is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onClickSave_emptyNote() {
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(12).perform(click());
        onView(withId(R.id.AddNewNoteDaySpinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.saveNoteSaveButton)).perform(click());
        onView(withText("Pusta notka lub niepoprawna data!")).inRoot(withDecorView(Matchers.not(Matchers.is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onClickSave_emptyMonth() {
        onView(withId(R.id.saveNoteNoteTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.saveNoteSaveButton)).perform(click());
        onView(withText("Pusta notka lub niepoprawna data!")).inRoot(withDecorView(Matchers.not(Matchers.is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onClickSave_emptyDay() {
        onView(withId(R.id.saveNoteNoteTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.saveNoteSaveButton)).perform(click());
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(12).perform(click());
        onView(withText("Pusta notka lub niepoprawna data!")).inRoot(withDecorView(Matchers.not(Matchers.is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onClickSave_success() {
        onView(withId(R.id.saveNoteNoteTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.saveNoteSaveButton)).perform(click());
        onView(withId(R.id.AddNewNoteMonthSpinner)).perform(click());
        onData(anything()).atPosition(12).perform(click());
        onView(withId(R.id.AddNewNoteDaySpinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withText("Pusta notka lub niepoprawna data!")).inRoot(withDecorView(Matchers.not(Matchers.is(rule.getActivity().getWindow().getDecorView())))).check(doesNotExist());
    }
}
