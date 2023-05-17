package com.example.project0;

public abstract class Seat implements Comparable<Seat> {
    private int rowNum;
    private int columnNum;
    private String seatNum;
    private Passenger passenger;

    protected Seat(String seatNum) {
        this.seatNum = seatNum;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "rowNum=" + rowNum +
                ", columnNum=" + columnNum +
                ", seatNum='" + seatNum + '\'' +
                ", passenger=" + passenger +
                '}';
    }

    public String getSeatNum() {
        return seatNum;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    @Override
    public int compareTo(Seat o) {
        if(seatNum.substring(1).equalsIgnoreCase(o.seatNum.substring(1))){
            if(seatNum.charAt(0) > o.seatNum.charAt(0)){
                return 1;
            } else if(seatNum.charAt(0) < o.seatNum.charAt(0)){
                return -1;
            }
            return 0;
        }
        else if(Integer.parseInt(seatNum.substring(1)) > Integer.parseInt(o.seatNum.substring(1)))
            return 1;
        else if(Integer.parseInt(seatNum.substring(1)) < Integer.parseInt(o.seatNum.substring(1)))
            return -1;
        return 0;
    }
}
