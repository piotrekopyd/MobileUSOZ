package com.mobile.usoz;

import com.mobile.usoz.Calendar.Calendar.CalendarRecyclerViewAdapter;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RecyclerViewAdapterTest {
    CalendarRecyclerViewAdapter adapter = null;

    //Unit tests
    @Test
    public void constructor_null() {
        adapter = new CalendarRecyclerViewAdapter(null, null);
    }

    @Test
    public void constructor_arrayList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("01-01-2019");
        list.add("02-03-2018");
        list.add("01-07-2019");
        adapter = new CalendarRecyclerViewAdapter(null, list);
        int count = adapter.getItemCount();
        assertEquals(3, count);
    }

    @Test
    public void constructor_list() {
        List<String> list = new ArrayList<>();
        list.add("01-01-2019");
        list.add("01-07-2019");
        adapter = new CalendarRecyclerViewAdapter(null, list);
        int count = adapter.getItemCount();
        assertEquals(2, count);
    }

    @Test
    public void constructor_arrayListEmpty() {
        ArrayList<String> list = new ArrayList<>();
        adapter = new CalendarRecyclerViewAdapter(null, list);
        int count = adapter.getItemCount();
        assertEquals(0, count);
    }

    @Test
    public void constructor_arrayListRemove() {
        ArrayList<String> list = new ArrayList<>();
        list.add("01-01-2019");
        list.add("02-03-2018");
        list.add("01-07-2019");
        adapter = new CalendarRecyclerViewAdapter(null, list);
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
        adapter = new CalendarRecyclerViewAdapter(null, list);
        for(int i=0;i<3;i++)
            list.remove(0);
        int count = adapter.getItemCount();
        assertEquals(0, count);
    }

    @Test
    public void formatMonthToNumber_styczen() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Styczeń");
        assertEquals("1", result);
    }

    @Test
    public void formatMonthToNumber_luty() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Luty");
        assertEquals("2", result);
    }

    @Test
    public void formatMonthToNumber_marzec() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Marzec");
        assertEquals("3", result);
    }

    @Test
    public void formatMonthToNumber_kwiecien() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Kwiecień");
        assertEquals("4", result);
    }

    @Test
    public void formatMonthToNumber_maj() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Maj");
        assertEquals("5", result);
    }

    @Test
    public void formatMonthToNumber_czerwiec() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Czerwiec");
        assertEquals("6", result);
    }

    @Test
    public void formatMonthToNumber_lipiec() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Lipiec");
        assertEquals("7", result);
    }

    @Test
    public void formatMonthToNumber_sierpien() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Sierpień");
        assertEquals("8", result);
    }

    @Test
    public void formatMonthToNumber_wrzesien() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Wrzesień");
        assertEquals("9", result);
    }

    @Test
    public void formatMonthToNumber_pazdziernik() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Październik");
        assertEquals("10", result);
    }

    @Test
    public void formatMonthToNumber_listopad() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Listopad");
        assertEquals("11", result);
    }

    @Test
    public void formatMonthToNumber_grudzien() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Grudzień");
        assertEquals("12", result);
    }

    @Test
    public void formatMonthToNumber_empty() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "");
        assertEquals("0", result);
    }

    @Test
    public void formatMonthToNumber_prefix() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Lut");
        assertEquals("0", result);
    }

    @Test
    public void formatMonthToNumber_suffix() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "ipiec");
        assertEquals("0", result);
    }

    @Test
    public void formatMonthToNumber_mistake() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Czewriec");
        assertEquals("0", result);
    }

    @Test
    public void formatMonthToNumber_missOneChar() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Listpad");
        assertEquals("0", result);
    }

    @Test
    public void formatMonthToNumber_eng() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "February");
        assertEquals("0", result);
    }

    @Test
    public void formatMonthToNumber_wrzesienNoSpecialChars() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = Whitebox.invokeMethod(adapter, "formatMonthToNumber", "Wrzesien");
        assertEquals("0", result);
    }

    @Test
    public void formatNumberToMonth_1() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(1);
        assertEquals("Styczeń", result);
    }

    @Test
    public void formatNumberToMonth_2() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(2);
        assertEquals("Luty", result);
    }

    @Test
    public void formatNumberToMonth_3() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(3);
        assertEquals("Marzec", result);
    }

    @Test
    public void formatNumberToMonth_4() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(4);
        assertEquals("Kwiecień", result);
    }

    @Test
    public void formatNumberToMonth_5() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(5);
        assertEquals("Maj", result);
    }

    @Test
    public void formatNumberToMonth_6() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(6);
        assertEquals("Czerwiec", result);
    }

    @Test
    public void formatNumberToMonth_7() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(7);
        assertEquals("Lipiec", result);
    }

    @Test
    public void formatNumberToMonth_8() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(8);
        assertEquals("Sierpień", result);
    }

    @Test
    public void formatNumberToMonth_9() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(9);
        assertEquals("Wrzesień", result);
    }

    @Test
    public void formatNumberToMonth_10() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(10);
        assertEquals("Październik", result);
    }

    @Test
    public void formatNumberToMonth_11() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(11);
        assertEquals("Listopad", result);
    }

    @Test
    public void formatNumberToMonth_12() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(12);
        assertEquals("Grudzień", result);
    }

    @Test
    public void formatNumberToMonth_13() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(13);
        assertEquals("none", result);
    }

    @Test
    public void formatNumberToMonth_0() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(0);
        assertEquals("none", result);
    }

    @Test
    public void formatNumberToMonth_minus1() throws Exception {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatNumberToMonth(-1);
        assertEquals("none", result);
    }

    @Test
    public void formatDateToDayOfWeek_failMonth() {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatDateToDayOfWeek("miesiac", "3");
        assertEquals("none", result);
    }

    @Test
    public void formatDateToDayOfWeek_failDay() {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatDateToDayOfWeek("1", "trzeci");
        assertEquals("none", result);
    }

    @Test
    public void formatDateToDayOfWeek_failBoth() {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatDateToDayOfWeek("styczeń", "trzeci");
        assertEquals("none", result);
    }

    @Test
    public void formatDateToDayOfWeek_poniedzialek() {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatDateToDayOfWeek("5", "6");
        assertEquals("Poniedziałek", result);
    }

    @Test
    public void formatDateToDayOfWeek_wtorek() {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatDateToDayOfWeek("5", "7");
        assertEquals("Wtorek", result);
    }

    @Test
    public void formatDateToDayOfWeek_sroda() {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatDateToDayOfWeek("5", "8");
        assertEquals("Środa", result);
    }

    @Test
    public void formatDateToDayOfWeek_czwartek() {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatDateToDayOfWeek("5", "9");
        assertEquals("Czwartek", result);
    }

    @Test
    public void formatDateToDayOfWeek_piatek() {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatDateToDayOfWeek("5", "10");
        assertEquals("Piątek", result);
    }

    @Test
    public void formatDateToDayOfWeek_sobota() {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatDateToDayOfWeek("5", "11");
        assertEquals("Sobota", result);
    }

    @Test
    public void formatDateToDayOfWeek_niedziela() {
        adapter = new CalendarRecyclerViewAdapter(null, null);
        String result = adapter.formatDateToDayOfWeek("5", "12");
        assertEquals("Niedziela", result);
    }

}
