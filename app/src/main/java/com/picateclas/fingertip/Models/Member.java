package com.picateclas.fingertip.Models;

public class Member {
    private String _name;
    private int _age;

    public Member(String name, int age) {
        this._name = name;
        this._age = age;
    }

    public String getName() {
        return _name;
    }

    public int getAge() {
        return _age;
    }
}
