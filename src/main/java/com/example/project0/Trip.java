package com.example.project0;

import java.util.Calendar;

public class Trip {
    private Seat[][] seats = new Seat[31][6];
    private String tripNum;
    private String from;
    private String to;
    private Calendar date;
    private String stringDate;

    public Trip(String tripNum, String from, String to, Calendar date) {
        this.tripNum = tripNum;
        this.from = from;
        this.to = to;
        this.date = date;
        setStringDate(this.date.get(Calendar.DAY_OF_MONTH) + "-" +
                (this.date.get(Calendar.MONTH) + 1) +
                "-" + this.date.get(Calendar.YEAR));
        intizeSeatsArray();
    }

    private void intizeSeatsArray(){
        for (int i = 0; i < 3; i++){
            seats[i][0] = new FirstClass("A"+(i+1));
            seats[i][1] = new FirstClass("C"+(i+1));
            seats[i][2] = new FirstClass("D"+(i+1));
            seats[i][3] = new FirstClass("F"+(i+1));
        }
        for (int i = 5; i < seats.length; i++) {
            if (i == 12)
                continue;
            char c = 'A';
            for (int j = 0; j < seats[i].length; j++,c++) {
                seats[i][j] = new EconomyClass(String.valueOf(c) + (i+1));
            }
        }
    }

    public void reserveSeat(String seatNum, Passenger passenger) {
        if(isSeatEmpty(seatNum)){
            for (int i = 0; i < seats.length; i++){
                for (int j = 0; j < seats[i].length; j++){
                    if(seats[i][j] != null && seats[i][j].getSeatNum().equals(seatNum)){
                        seats[i][j].setPassenger(passenger);
                    }
                }
            }
        }
    }

    public boolean isSeatEmpty(String seatNum) {
        for (int i = 0; i < seats.length; i++){
            for (int j = 0; j < seats[i].length; j++){
                if(seats[i][j] != null && seats[i][j].getSeatNum().equals(seatNum) ){
                    if(seats[i][j].getPassenger() == null)
                        return true;
                }
            }
        }
        return false;
    }

    public Seat getSeat(String seatNum) {
        return null;
    }

    @Override
    public String toString() {
        return tripNum + '/' +
                from + '/' +
                to + '/' +
                date.get(Calendar.DAY_OF_MONTH) + '-' +
                (date.get(Calendar.MONTH) + 1) +
                '-' + date.get(Calendar.YEAR);
    }

    public Seat[][] getSeats() {
        return seats;
    }

    public void setSeats(Seat[][] seats) {
        this.seats = seats;
    }

    public String getTripNum() {
        return tripNum;
    }

    public void setTripNum(String tripNum) {
        this.tripNum = tripNum;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

}
