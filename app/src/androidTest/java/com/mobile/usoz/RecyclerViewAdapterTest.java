package com.mobile.usoz;

import com.mobile.usoz.Calendar.Calendar.RecyclerViewAdapter;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RecyclerViewAdapterTest {
    RecyclerViewAdapter adapter = null;

    //Unit tests
    @Test
    public void constructor_null() {
        adapter = new RecyclerViewAdapter(null, null);
    }

    @Test
    public void constructor_arrayList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("01-01-2019");
        list.add("02-03-2018");
        list.add("01-07-2019");
        adapter = new RecyclerViewAdapter(null, list);
        int count = adapter.getItemCount();
        assertEquals(3, count);
    }

    @Test
    public void constructor_list() {
        List<String> list = new ArrayList<>();
        list.add("01-01-2019");
        list.add("01-07-2019");
        adapter = new RecyclerViewAdapter(null, list);
        int count = adapter.getItemCount();
        assertEquals(2, count);
    }

    @Test
    public void constructor_arrayListEmpty() {
        ArrayList<String> list = new ArrayList<>();
        adapter = new RecyclerViewAdapter(null, list);
        int count = adapter.getItemCount();
        assertEquals(0, count);
    }

    @Test
    public void constructor_arrayListRemove() {
        ArrayList<String> list = new ArrayList<>();
        list.add("01-01-2019");
        list.add("02-03-2018");
        list.add("01-07-2019");
        adapter = new RecyclerViewAdapter(null, list);
        list.remove(1);
        int count = adapter.getItemCount();
        assertEquals(2, count);
    }

    @Test
    public void constructor_arrayListRemoveAll() {
        ArrayList<String> list = new ArrayList<>();
        list.add("01-01-2019");
        list.add("02-03-2018");
        list.add("01-07-2019");
        adapter = new RecyclerViewAdapter(null, list);
        for(int i=0;i<3;i++)
            list.remove(0);
        int count = adapter.getItemCount();
        assertEquals(0, count);
    }

    @Test
    public void formatMonthToNumber_styczen() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Styczeń");
        assertEquals("1", result);
    }

    @Test
    public void formatMonthToNumber_luty() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Luty");
        assertEquals("2", result);
    }

    @Test
    public void formatMonthToNumber_marzec() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Marzec");
        assertEquals("3", result);
    }

    @Test
    public void formatMonthToNumber_kwiecien() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Kwiecień");
        assertEquals("4", result);
    }

    @Test
    public void formatMonthToNumber_maj() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Maj");
        assertEquals("5", result);
    }

    @Test
    public void formatMonthToNumber_czerwiec() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Czerwiec");
        assertEquals("6", result);
    }

    @Test
    public void formatMonthToNumber_lipiec() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Lipiec");
        assertEquals("7", result);
    }

    @Test
    public void formatMonthToNumber_sierpien() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Sierpień");
        assertEquals("8", result);
    }

    @Test
    public void formatMonthToNumber_wrzesien() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Wrzesień");
        assertEquals("9", result);
    }

    @Test
    public void formatMonthToNumber_pazdziernik() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Październik");
        assertEquals("10", result);
    }

    @Test
    public void formatMonthToNumber_listopad() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Listopad");
        assertEquals("11", result);
    }

    @Test
    public void formatMonthToNumber_grudzien() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Grudzień");
        assertEquals("12", result);
    }

    @Test
    public void formatMonthToNumber_empty() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "");
        assertEquals("0", result);
    }

    @Test
    public void formatMonthToNumber_prefix() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Lut");
        assertEquals("0", result);
    }

    @Test
    public void formatMonthToNumber_suffix() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "ipiec");
        assertEquals("0", result);
    }

    @Test
    public void formatMonthToNumber_mistake() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Czewriec");
        assertEquals("0", result);
    }

    @Test
    public void formatMonthToNumber_missOneChar() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Listpad");
        assertEquals("0", result);
    }

    @Test
    public void formatMonthToNumber_eng() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "February");
        assertEquals("0", result);
    }

    @Test
    public void formatMonthToNumber_wrzesienNoSpecialChars() throws Exception {
        adapter = new RecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Wrzesien");
        assertEquals("0", result);
    }

}
