package com.mobile.usoz;

import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.mobile.usoz.Calendar.Calendar.CalendarActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class CalendarActivityTest {

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

    //Unit tests
    @Test
    public void onCreate_components() {
        assertNotNull(activity.findViewById(R.id.toolbar));
        assertNotNull(activity.findViewById(R.id.addNoteButton));
        assertNotNull(activity.findViewById(R.id.calendar_recycle_view));
    }

    @Test
    public void onBackPressed_foreground() {
        Drawable temp = activity.findViewById(R.id.calendar_relative_layout_1).getForeground();
        if(temp != null)
            assertEquals(180, temp.getAlpha());
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
}
