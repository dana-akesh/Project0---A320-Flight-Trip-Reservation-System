package com.example.project0;

public class FirstClass extends Seat {
    protected FirstClass(String seatNum) {
        super(seatNum);
    }

    @Override
    public String toString() {
        return "FirstClass" + super.toString();
    }
}
