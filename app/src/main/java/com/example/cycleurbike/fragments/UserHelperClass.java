package com.example.cycleurbike.fragments;

import androidx.fragment.app.Fragment;


public class UserHelperClass extends Fragment {

    String firstName, lastName,email,password,birthDay,city,id;
    public UserHelperClass() {
        // Required empty public constructor
    }
    public UserHelperClass(String firstName, String lastName, String email, String password, String birthDay, String city) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPassword(password);
        setBirthDay(birthDay);
        setCity(city);
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId1() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getFirstName() {
        return firstName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getLastName() {
        return lastName;
    }


    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }


    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }


    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }
    public String getBirthDay() {
        return birthDay;
    }


    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }
}
