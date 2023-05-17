package com.example.project0;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Long.parseLong;

public class Driver extends Application {
    static ArrayList<Trip> trips = new ArrayList();
    TableView<Trip> tripsTable;

    public static void main(String[] args) {
        try {
            readTripFile();
            readPassengerFile();
            Application.launch();
            writeTripFile();
            writePassengerFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readTripFile() throws FileNotFoundException, ParseException {
        File file = new File("trips.txt");
        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String[] tokens = line.split("/");
                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(tokens[3].trim());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                Trip trip = new Trip(tokens[0].toUpperCase(), tokens[1], tokens[2], calendar);
                trips.add(trip);
            }
        }
    }

    public static void writeTripFile() throws FileNotFoundException {
        File file = new File("trips.txt");
        PrintWriter print = new PrintWriter(file);
        for (int i = 0; i < trips.size(); i++) {
            print.println(trips.get(i).toString());
        }
        print.close();
    }

    public static void readPassengerFile() throws FileNotFoundException {
        File file = new File("passengers.txt");
        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String[] tokens = line.split("/");
                String seatPref = tokens[3].trim();
                Passenger passenger = new Passenger(tokens[1].trim(), parseLong(tokens[2].trim()), seatPref);
                for (int i = 0; i < trips.size(); i++) {
                    if (trips.get(i).getTripNum().equals(tokens[0].trim())) {
                        trips.get(i).reserveSeat(tokens[3].trim(), passenger);
                    }
                }
            }
        }
    }

    private static void writePassengerFile() throws FileNotFoundException {
        File file = new File("passengers.txt");
        PrintWriter print = new PrintWriter(file);
        for (int i = 0; i < trips.size(); i++) {
            for (int j = 0; j < trips.get(i).getSeats().length; j++) {
                for (int k = 0; k < trips.get(i).getSeats()[j].length; k++) {
                    if (!(trips.get(i).getSeats()[j][k] == null || trips.get(i).getSeats()[j][k].getPassenger() == null)) {
                        print.println(trips.get(i).getTripNum().trim() + '/'
                                + trips.get(i).getSeats()[j][k].getPassenger().getName().trim() + '/'
                                + trips.get(i).getSeats()[j][k].getPassenger().getPassport() + '/'
                                + trips.get(i).getSeats()[j][k].getSeatNum()
                        );
                    }
                }
            }
        }
        print.close();
    }

    public static ObservableList<Trip> getTripList() {
        ObservableList<Trip> tripsObsrvableList = FXCollections.observableArrayList();
        for (int i = 0; i < trips.size(); i++) {
            tripsObsrvableList.add(trips.get(i));
        }
        return tripsObsrvableList;
    }

    public void refreshPage() {

        // Pane that contains both the picture and the functionalities.
        HBox mainPageHB = new HBox();
        mainPageHB.setBackground(new Background(new BackgroundFill(Color.web("#f8f4ff"), CornerRadii.EMPTY, Insets.EMPTY)));
        ImageView mainImage = new ImageView("https://img.icons8.com/external-flaticons-flat-flat-icons/344/external-airport-hotel-management-flaticons-flat-flat-icons.png");
        Label label = new Label("", mainImage);
        mainImage.setFitWidth(350);
        mainImage.setFitHeight(350);
        label.setOnMouseClicked(e -> {
            System.out.println(e.getSource());
        });

        // VBox to put the picture and the label together.
        VBox mainMenuVB = new VBox(15);
        mainMenuVB.setAlignment(Pos.CENTER_LEFT);
        mainMenuVB.setPadding(new Insets(100, 10, 200, 90));
        Label mainL = new Label("Flights Trip Reservation System");
        mainL.setTextFill(Color.NAVY);
        mainL.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        mainMenuVB.getChildren().addAll(label, mainL);

        // VBox to add the table and add passengers and trips
        VBox functionalityVB = new VBox(20);

        // table for flights.
        // trip number column
        TableColumn<Trip, String> numCol = new TableColumn<>("Number");
        numCol.setMinWidth(250);
        numCol.setCellValueFactory(new PropertyValueFactory<>("tripNum"));

        // from column
        TableColumn<Trip, String> fromCol = new TableColumn<>("From");
        fromCol.setMinWidth(250);
        fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));

        // to place column
        TableColumn<Trip, String> toCol = new TableColumn<>("To");
        toCol.setMinWidth(250);
        toCol.setCellValueFactory(new PropertyValueFactory<>("to"));

        // calendar column
        TableColumn<Trip, String> caleCol = new TableColumn<>("Date");
        caleCol.setMinWidth(250);
        caleCol.setCellValueFactory(new PropertyValueFactory<>("stringDate"));

        tripsTable = new TableView<>();
        tripsTable.setItems(getTripList());
        tripsTable.getColumns().addAll(numCol, fromCol, toCol, caleCol);

        // Add a trip
        HBox addTripHB = new HBox(10);
        TextField tripNumTF = new TextField();
        tripNumTF.setPromptText("Trip number"); //to set the hint text
        TextField fromTF = new TextField();
        fromTF.setPromptText("From"); //to set the hint text
        TextField toTF = new TextField();
        toTF.setPromptText("to"); //to set the hint text
        DatePicker datePicker = new DatePicker();
        Button addTripB = new Button("Add Trip");

        addTripB.setOnAction(e -> {
            try {
                for (int i = 0; i < trips.size(); i++) {
                    if (trips.get(i).getTripNum().trim().equals(tripNumTF.getText().trim())) {
                        failAlert("Trip number already exists");
                        return;
                    }
                }
                if (tripNumTF.getText().trim().isEmpty()) {
                    failAlert("Trip number is empty");
                    return;
                } else if (fromTF.getText().trim().isEmpty()) {
                    failAlert("The boarding airport can't be empty");
                    return;
                } else if (toTF.getText().trim().isEmpty()) {
                    failAlert("The destination can't be empty");
                    return;
                } else if (datePicker.getEditor().getText().trim().isEmpty()) {
                    failAlert("Trip number is empty");
                    return;
                }

                Date dIn = new SimpleDateFormat("MM/dd/yyyy").parse(datePicker.getEditor().getText().trim());
                Calendar cIn = Calendar.getInstance();
                cIn.setTime(dIn);
                Trip trip = new Trip(tripNumTF.getText(), fromTF.getText(), toTF.getText(), cIn);
                trips.add(trip);
                tripsTable.refresh();
                refreshPage();
                successAlert("Trip has been added successfully");
                ((Node) e.getSource()).getScene().getWindow().hide();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        });
        addTripHB.getChildren().addAll(tripNumTF, fromTF, toTF, datePicker, addTripB);

        // Reserve a seat
        HBox reserveSeatHB = new HBox(10);

        ComboBox<Trip> tripsCB1 = new ComboBox();
        tripsCB1.getItems().addAll(getTripList());
        tripsCB1.getSelectionModel().select(0);

        TextField passportInTF = new TextField();
        passportInTF.setPromptText("Passport number"); //to set the hint text

        TextField nameTF = new TextField();
        nameTF.setPromptText("Passenger name"); //to set the hint text

        RadioButton economyRB = new RadioButton("Economy");
        RadioButton firstRB = new RadioButton("First");
        firstRB.setSelected(true);
        ToggleGroup classTG = new ToggleGroup();

        ComboBox<String> seatsLetterCB1 = new ComboBox();
        seatsLetterCB1.getItems().addAll("A", "C", "D", "F");
        seatsLetterCB1.getSelectionModel().select(0);

        ComboBox<String> seatsNumberCB1 = new ComboBox();
        seatsNumberCB1.getItems().addAll("1", "2", "3");
        seatsNumberCB1.getSelectionModel().select(0);

        firstRB.setOnAction(e -> {
            seatsLetterCB1.getItems().clear();
            seatsNumberCB1.getItems().clear();
            seatsLetterCB1.getItems().addAll("A", "C", "D", "F");
            seatsLetterCB1.getSelectionModel().select(0);
            seatsNumberCB1.getItems().addAll("1", "2", "3");
            seatsNumberCB1.getSelectionModel().select(0);
        });
        economyRB.setOnAction(e -> {
            seatsLetterCB1.getItems().clear();
            seatsNumberCB1.getItems().clear();
            seatsLetterCB1.getItems().addAll("A", "B", "C", "D", "E", "F");
            seatsLetterCB1.getSelectionModel().select(0);
            seatsNumberCB1.getItems().addAll(
                    "6", "7", "8", "9",
                    "10", "11", "12", "14",
                    "15", "16", "17", "18",
                    "19", "20", "21", "22",
                    "23", "24", "25", "26",
                    "27", "28", "29", "30", "31"
            );
            seatsNumberCB1.getSelectionModel().select(0);
        });

        economyRB.setToggleGroup(classTG);
        firstRB.setToggleGroup(classTG);

        Button reserveSeatB = new Button("Reserve ");

        reserveSeatB.setOnAction(e -> {
            String seatSelected = seatsLetterCB1.getValue().trim() + seatsNumberCB1.getValue().trim();
            String tripNumber = tripsCB1.getSelectionModel().getSelectedItem().getTripNum();
            if (passportInTF.getText().trim().isEmpty()) {
                failAlert("Passport can't be null");
            } else if (nameTF.getText().trim().isEmpty()) {
                failAlert("Name can't be null");
            } else
                for (int i = 0; i < trips.size(); i++) {
                    if (trips.get(i).getTripNum().equalsIgnoreCase(tripNumber)) {
                        if (trips.get(i).isSeatEmpty(seatSelected)) {
                            Passenger passenger = new Passenger(nameTF.getText().trim(), Long.parseLong(passportInTF.getText().trim()), seatSelected.trim());
                            trips.get(i).reserveSeat(seatSelected, passenger);
                            successAlert("Passenger added successfully");
                        }
                    }
                }
        });

        GridPane fetchPassengerHB = new GridPane();
        fetchPassengerHB.setHgap(10);
        fetchPassengerHB.setVgap(15);

        ComboBox<Trip> tripsCB2 = new ComboBox();
        tripsCB2.getItems().addAll(getTripList());
        fetchPassengerHB.add(tripsCB2, 0, 0);
        tripsCB2.getSelectionModel().select(0);

        ComboBox<String> seatsLetterCB2 = new ComboBox();
        seatsLetterCB2.getItems().addAll("A", "B", "C", "D", "E", "F");
        seatsLetterCB2.getSelectionModel().select(0);
        fetchPassengerHB.add(seatsLetterCB2, 1, 0);

        ComboBox<String> seatsNumberCB2 = new ComboBox();
        seatsNumberCB2.getItems().addAll(
                "1", "2", "3", "6",
                "7", "8", "9", "10",
                "11", "12", "14", "15",
                "16", "17", "18", "19",
                "20", "21", "22", "23",
                "24", "25", "26", "27",
                "28", "29", "30", "31"
        );
        seatsNumberCB2.getSelectionModel().select(0);
        fetchPassengerHB.add(seatsNumberCB2, 2, 0);

        Label emptyL = new Label("   ");
        fetchPassengerHB.add(emptyL, 3, 0);

        Label nameL = new Label("Name: ");
        fetchPassengerHB.add(nameL, 0, 1);
        nameL.setFont((Font.font("Georgia", 15)));

        Label nameResultL = new Label();
        fetchPassengerHB.add(nameResultL, 1, 1);
        nameResultL.setFont((Font.font("Georgia", 15)));

        Label PassportL = new Label("Passport: ");
        fetchPassengerHB.add(PassportL, 0, 2);
        PassportL.setFont((Font.font("Georgia", 15)));

        Label passportResultL = new Label();
        fetchPassengerHB.add(passportResultL, 1, 2);
        passportResultL.setFont((Font.font("Georgia", 15)));

        Label seatPrefL = new Label("Seat Preference: ");
        fetchPassengerHB.add(seatPrefL, 0, 3);
        seatPrefL.setFont((Font.font("Georgia", 15)));


        Label seatPrefResultL = new Label();
        fetchPassengerHB.add(seatPrefResultL, 1, 3);
        seatPrefResultL.setFont((Font.font("Georgia", 15)));


        Button fetchPassengerB = new Button("Fetch Passenger");
        fetchPassengerHB.add(fetchPassengerB, 4, 0);
        fetchPassengerB.setOnAction(e -> {
            String seatSelected = seatsLetterCB2.getSelectionModel().getSelectedItem().trim() + seatsNumberCB2.getSelectionModel().getSelectedItem().trim();

            for (int i = 0; i < trips.size(); i++) {
                if (trips.get(i).equals(tripsCB2.getValue())) {
                    for (int j = 0; j < trips.get(i).getSeats().length; j++) {
                        for (int s = 0; s < trips.get(i).getSeats()[j].length; s++) {
                            if (!(trips.get(i).getSeats()[j][s] == null || trips.get(i).getSeats()[j][s].getPassenger() == null)) {
                                if (trips.get(i).getSeats()[j][s].getSeatNum().equals(seatSelected)) {
                                    nameResultL.setText(trips.get(i).getSeats()[j][s].getPassenger().getName());
                                    passportResultL.setText(String.valueOf(trips.get(i).getSeats()[j][s].getPassenger().getPassport()));
                                    if (trips.get(i).getSeats()[j][s].getPassenger().getSeatPref() == 0){
                                        seatPrefResultL.setText("WINDOW");
                                    }else if (trips.get(i).getSeats()[j][s].getPassenger().getSeatPref() == 1){
                                        seatPrefResultL.setText("AISLE");
                                    }else{
                                        seatPrefResultL.setText("NONE");
                                    }
                                    return;
                                }else{
                                    nameResultL.setText("None");
                                    passportResultL.setText("None");
                                }
                            }
                        }
                    }
                }
            }
        });

        reserveSeatHB.getChildren().addAll(tripsCB1, passportInTF, nameTF, firstRB, economyRB, seatsLetterCB1, seatsNumberCB1, reserveSeatB);

        functionalityVB.setPadding(new Insets(80, 0, 0, 50));
        functionalityVB.getChildren().addAll(tripsTable, addTripHB, reserveSeatHB, fetchPassengerHB);

        mainPageHB.getChildren().addAll(mainMenuVB, functionalityVB);
        Scene scene = new Scene(mainPageHB);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Flights Trip Reservation System");
        stage.setMaximized(true);
        stage.show();
    }


    @Override
    public void start(Stage stage) {
        refreshPage();
    }

    private void successAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    private void failAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

}