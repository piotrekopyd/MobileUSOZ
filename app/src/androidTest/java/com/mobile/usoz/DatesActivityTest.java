package com.mobile.usoz;

import android.support.test.rule.ActivityTestRule;


import androidx.test.filters.Suppress;

import com.mobile.usoz.Calendar.Calendar.DatesActivity;
import com.mobile.usoz.Calendar.Calendar.DatesModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;

import static org.junit.Assert.assertNotNull;

public class DatesActivityTest {
    @Rule
    public ActivityTestRule<DatesActivity> rule = new ActivityTestRule<>(DatesActivity.class);
    private DatesActivity activity = null;

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    //Unit tests
   /* @Test
    public void onCreate_components() {
        assertNotNull(activity.findViewById(R.id.toolbar));
        assertNotNull(activity.findViewById(R.id.addNoteButton));
        assertNotNull(activity.findViewById(R.id.calendar_recycle_view));
    }

    @Test
    public void getDataFromCalendarActivity_month() throws Exception {
        Field field = Whitebox.getField(activity.getClass(), "model");
        field.setAccessible(true);
        DatesModel model = (DatesModel) field.get(activity);
        assertNotNull(model.month);
    }*/
}
