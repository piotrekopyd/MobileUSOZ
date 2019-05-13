package com.mobile.usoz.LecturersActivities;

import java.io.Serializable;

class Lecturer implements Serializable {
    private String firstName;
    private String surname;
    private String university;
    private String[] lectures;

    public Lecturer(String firstName, String surname, String university) {
        this.firstName = firstName;
        this.surname = surname;
        this.university = university;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getSurname() {
        return surname;
    }
    public String getName() {
        return (firstName + " " + surname);
    }
    public String getUniversity() {
        return university;
    }
    public String[] getLectures() {
        return lectures;
    }
    public void setUniversity(String university) {
        this.university = university;
    }
    public void setLectures(String[] lectures) {
        this.lectures = lectures;
    }
}