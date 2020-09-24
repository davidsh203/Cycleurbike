package com.example.cycleurbike.fragments;

import androidx.fragment.app.Fragment;


public class UserHelperClass extends Fragment {

    String firstName, lastName,email,password,birthDay,city;
    String id;
    public UserHelperClass() {
        // Required empty public constructor
    }
    public UserHelperClass(String firstName, String lastName, String email, String password, String birthDay, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.birthDay = birthDay;
        this.city = city;
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId1() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
