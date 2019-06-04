package com.mobile.usoz;

import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.mobile.usoz.Maps.MapsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapsActivityTest {
    @Rule
    public ActivityTestRule<MapsActivity> rule = new ActivityTestRule<>(MapsActivity.class);
    private MapsActivity activity = null;

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    @Test
    public void onCreateComponents() {
        assertNotNull(activity.findViewById(R.id.map_marker_title));
        assertNotNull(activity.findViewById(R.id.map_marker_snippet));
        assertNotNull(activity.findViewById(R.id.map_marker_save));
        assertNotNull(activity.findViewById(R.id.map_marker_delete));
        assertNotNull(activity.findViewById(R.id.toolbar));
        assertNotNull(activity.findViewById(R.id.map_color_spinner));
    }

    @Ignore("odpalic test z konta usera")
    @Test
    public void onCreateOptionsMenu_noAdmin() {
        assertNotNull(activity.findViewById(R.menu.map_settings_user_menu));
    }

    @Ignore("odpalic test z konta admina")
    @Test
    public void onCreateOptionsMenu_admin() {
        assertNotNull(activity.findViewById(R.menu.map_settings_menu));
    }

    @Test
    public void onOptionsItemSelected_null() {
        activity.onOptionsItemSelected(null);
    }

    @Test
    public void onOptionsItemSelected_settingsHide() {
        activity.onOptionsItemSelected(activity.findViewById(R.id.map_settings_hide));
        assertFalse(activity.findViewById(R.id.maps_edit_relative_layout).isClickable());
    }

    @Test
    public void onOptionsItemSelected_settingsShow() {
        activity.onOptionsItemSelected(activity.findViewById(R.id.map_settings_show));
        assertTrue(activity.findViewById(R.id.map_marker_save).isClickable());
    }

    @Test
    public void onBackPressed_foreground() {
        Drawable temp = activity.findViewById(R.id.maps_relative_layout).getForeground();
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
}
