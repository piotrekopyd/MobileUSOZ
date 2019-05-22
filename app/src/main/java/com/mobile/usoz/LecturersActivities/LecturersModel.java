package com.mobile.usoz.LecturersActivities;

import java.util.LinkedList;

public class LecturersModel {
    public LinkedList<Lecturer> lectutersCollection;
    public double grade;
    public double gradeUID;
    public int gradesMapSize;
    public LecturersModel() {
        lectutersCollection = new LinkedList<Lecturer>();
        grade = 0;
        gradeUID = 0;
        gradesMapSize = 0;
    }
}
