package com.mobile.usoz.DataModel;


import java.text.SimpleDateFormat;
import java.util.Date;

public class UserData {
    String name, lastName, passions;
    Date birthday;
    private SimpleDateFormat birthdayFormat = new SimpleDateFormat("dd-MM-YYYY");
    UserData(String newName, String newrLastName, String newBirthday, String newPassions){
        try{
            birthday=birthdayFormat.parse(newBirthday);
        }catch(java.text.ParseException e){

        }
        name=newName;
        lastName=newrLastName;
        passions=newPassions;
    }
}
