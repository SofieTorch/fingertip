package com.picateclas.fingertip.Models;

import androidx.annotation.NonNull;

public class Member {
    private String _id;
    private String _name;
    private int _age;

    public Member(String name, int age, String id) {
        this._name = name;
        this._age = age;
        this._id = id;
    }

    public Member(String name, int age) {
        this._name = name;
        this._age = age;
        this._id = null;
    }

    public String getName() {
        return _name;
    }

    public int getAge() {
        return _age;
    }

    public String getId() { return _id; }

    @Override
    public String toString() {
        return _name + ", " + _age + " años";
    }
}
