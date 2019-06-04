package com.mobile.usoz;

import com.mobile.usoz.Administrator.Administrator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AdministratorTest {
    private static boolean result;

    public static void checkCallback(boolean b) {
        result = b;
    }

    @Test
    public void isAdministrator_false() {
        Administrator.isAdministrator((b) -> {
            if (b)
                throw new NullPointerException();
        }, "5");
    }

    @Test
    public void isAdministrator_falseWithCallback() {
        Administrator.isAdministrator((b) -> {
            AdministratorTest.checkCallback(b);
        }, "1");
        assertFalse(result);
    }

    @Test
    public void isAdministrator_true() {
        Administrator.isAdministrator((b) -> {
            if (!b)
                throw new NullPointerException();
        }, "ZFB3eZ4PyYb6wmnJsCr2pkuttLH2");
    }
}
