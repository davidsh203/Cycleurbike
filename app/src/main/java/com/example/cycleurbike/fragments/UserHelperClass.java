package com.example.cycleurbike.fragments;

import androidx.fragment.app.Fragment;

import com.example.cycleurbike.ClassHelpers.Route;

import java.io.Serializable;
import java.util.ArrayList;


public class UserHelperClass implements Serializable {

    String firstName, lastName,email,password,birthDay,city,id;
    ArrayList<Route> routes, sharedRoutes;


    public UserHelperClass() {
        // Required empty public constructor
    }
    public UserHelperClass(String firstName, String lastName, String email, String birthDay, String city) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setBirthDay(birthDay);
        setCity(city);
        routes = new ArrayList<>();
        sharedRoutes = new ArrayList<>();

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

    public ArrayList<Route> getRoutes() {

        if (routes == null){
            routes = new ArrayList<>();
        }

        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    public ArrayList<Route> getSharedRoutes() {

        if (sharedRoutes == null){
            sharedRoutes = new ArrayList<>();
        }

        return sharedRoutes;
    }

    public void setSharedRoutes(ArrayList<Route> sharedRoutes) {
        this.sharedRoutes = sharedRoutes;
    }
}
