package com.example.project0;

public class EconomyClass extends Seat{
    protected EconomyClass(String seatNum) {
        super(seatNum);
    }

    @Override
    public String toString() {
        return "EconomyClass" + super.toString();
    }
}
