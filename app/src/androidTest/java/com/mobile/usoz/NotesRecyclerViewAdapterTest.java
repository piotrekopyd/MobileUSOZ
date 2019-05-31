package com.mobile.usoz;

import com.mobile.usoz.Calendar.Notes.EventsRecyclerViewAdapter;
import com.mobile.usoz.Calendar.Notes.NotesRecyclerViewAdapter;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class NotesRecyclerViewAdapterTest {
    NotesRecyclerViewAdapter adapter;

    @Test
    public void constructorTest1() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Ala");
        list.add("Ma");
        list.add("Kota");
        adapter = new NotesRecyclerViewAdapter(null, list, "12","11");
    }

    @Test
    public void constructorTest2() {
        adapter = new NotesRecyclerViewAdapter(null, null, "12","11");
    }

    @Test
    public void getItemCount_list() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Ala");
        list.add("Ma");
        list.add("Kota");
        adapter = new NotesRecyclerViewAdapter(null, list, "12","11");
        assertEquals(3, adapter.getItemCount());
    }

    @Test
    public void getItemCount_listRemove() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Ala");
        list.add("Ma");
        list.add("Kota");
        adapter = new NotesRecyclerViewAdapter(null, list, "12","11");
        list.remove(1);
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void getItemCount_listAdd() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Ala");
        list.add("Ma");
        list.add("Kota");
        adapter = new NotesRecyclerViewAdapter(null, list, "12","11");
        list.add("temp");
        assertEquals(4, adapter.getItemCount());
    }

    @Test
    public void getItemCount_listEmpty() {
        ArrayList<String> list = new ArrayList<>();
        adapter = new NotesRecyclerViewAdapter(null, list, "12","11");
        assertEquals(0, adapter.getItemCount());
    }
}
