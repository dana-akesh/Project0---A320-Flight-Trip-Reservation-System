package com.example.project0;

public class Passenger extends Person {
    public static int WINDOW = 0;
    public static int AISLE = 1;
    public static int NONE = 2;
    private long passport;
    private int seatPref;


    protected Passenger(String name, long passport, String seatPref) {
        super(name);
        this.passport = passport;
        setSeatPref(seatPref);
    }

    @Override
    public String toString() {
        return super.toString() + "Passenger{" +
                "passport=" + passport +
                ", seatPref=" + seatPref +
                '}';
    }

    public long getPassport() {
        return passport;
    }

    public void setPassport(long passport) {
        this.passport = passport;
    }

    public int getSeatPref() {
        return seatPref;
    }

    public void setSeatPref(String seatPref) {
        if (seatPref.trim().charAt(0) == 'F' || seatPref.trim().charAt(0) == 'A')
            this.seatPref = Passenger.WINDOW;
        else if (seatPref.trim().charAt(0) == 'C' || seatPref.trim().charAt(0) == 'D')
            this.seatPref = Passenger.AISLE;
        else
            this.seatPref = Passenger.NONE;
    }
}
