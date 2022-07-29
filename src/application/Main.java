package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {

	/********************************
	 * Linked Lists
	 ******************************/
	static LinkedList<Patient> patientList = new LinkedList<Patient>();
	static LinkedList<Nurse> nurseList = new LinkedList<Nurse>();
	static LinkedList<Doctor> doctorList = new LinkedList<Doctor>();

	static LinkedList<Appointment> upcomingList = new LinkedList<Appointment>();
	static LinkedList<Appointment> waitingList = new LinkedList<Appointment>();
	static LinkedList<Appointment> recordList = new LinkedList<Appointment>();

	static LinkedList<Appointment> readyList = new LinkedList<Appointment>();
	static LinkedList<Appointment> currAppointmentList = new LinkedList<Appointment>();

	/*********************************************************************
	 * Global variables - They will be used throughout the entire program
	 *********************************************************************/
	static Appointment currAppointment = null; // To store the current appointment being accessed
	static Appointment currRecord = null; // To store the current record being accessed
	static Patient currInfoPatient = null; // To store the current patient to edit info for

	static Patient currPatientUser = null; // To store the current patient using the program
	static Nurse currNurseUser = null; // To store the current nurse using the program
	static Doctor currDoctorUser = null; // To store the current doctor using the program

	static String currentApptoEdit = null; // To store what appointment to edit info for
	static String currentPatienttoEdit = null; // To store what patient to edit info for
	static boolean infoPrinted = false; // Reports if the information was already printed
	static boolean isUsernameAvailable = true; // To detect what user name to reset
	static boolean foundUsernameToReset = false; // To detect what user name to reset

	static String currsignUpUsername = null; // To store what user name to reset
	static String currsignUpPassword = null; // To store what user name to reset

	/********************************
	 * Java FX injections
	 ******************************/
	// Textfields
	@FXML
	private TextField usernameTextfield, passwordTextfield;
	@FXML
	private TextField bodyTemp, pulseRate, respRate, bloodPressure;
	@FXML
	private TextField examText, prescriptionText, notesText;
	@FXML
	private TextField newPhone, newPharmacy, newContact, newInsurance, message;
	@FXML
	private TextField profileName, profileDOB, profilePhone, permissionCode;

	// Buttons
	@FXML
	private Button signUpButton, resetButton;

	// Labels
	@FXML
	private Label nameLabel;
	@FXML
	private Label LoginMessage;
	@FXML
	private Label appointmentLabel, vitalsLabel, createAppLabel;
	@FXML
	private Label waitingLabel, examLabel;
	@FXML
	private Label dateLabel, examsLabel, notesLabel, participantsLabel;
	@FXML
	private Label prescriptionsLabel, timeLabel, vitalzLabel, noAppFound;
	@FXML
	private Label CurrnameLabel, DOBLabel, phoneLabel, pharmacyLabel;
	@FXML
	private Label messageLabel, messageWarning, noMessages, messageSuccess;
	@FXML
	private Label contactLabel, insuranceLabel, editInfoMessage, currDoctor;
	@FXML
	private Label availableUsername, signUpMessage;

	// Choice Boxes
	@FXML
	private ChoiceBox<String> appointmentTime, currDoctors, profileDoctor1, profileDoctor2;
	@FXML
	private ChoiceBox<String> profileNurses, profilePatients1, profilePatients2;

	// Date Picker
	@FXML
	private DatePicker appointmentDate;

	// List view boxes
	@FXML
	private ListView<String> listOfAppointments, listOfWaitingAppointments, listOfRecordsN, listOfRecordsD;
	@FXML
	private ListView<String> listOfUpAppointments, listOfEditAppointments, listOfEditAppointmentsN;
	@FXML
	private ListView<String> listOfRecordsP, listOfPatientsToEdit;

	// Scenes to control Screens
	private Stage stage;
	private Scene scene;
	private Parent root;

	// Java FX Injections to use when the program is closing
	@FXML
	private Button exitButton;
	@FXML
	private AnchorPane LogInPane;
	Stage closeStage;

	/******************************************
	 * Main Starts - Initializes the program
	 *****************************************/

	public static void main(String[] args) {

		launch(args); // Starts the application

	}// End of main method

	@Override
	public void start(Stage stage) {

		/***************************************
		 * Build Linked Lists from Text files
		 *************************************/

		// Patients
		try {
			File myObj = new File("src/application/Patients.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				patientList.add(new Patient(myReader.nextLine(), myReader.nextLine(), myReader.nextLine(),
						myReader.nextLine(), myReader.nextLine(), myReader.nextLine(), myReader.nextLine(),
						myReader.nextLine(), myReader.nextLine(), myReader.nextLine()));
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		// Nurses
		try {
			File myObj = new File("src/application/Nurses.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				nurseList.add(
						new Nurse(myReader.nextLine(), myReader.nextLine(), myReader.nextLine(), myReader.nextLine()));
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		// Doctors
		try {
			File myObj = new File("src/application/Doctors.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				doctorList.add(new Doctor(myReader.nextLine(), myReader.nextLine(), myReader.nextLine(),
						myReader.nextLine(), myReader.nextLine()));
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		// Upcoming Appointments
		try {
			File myObj = new File("src/application/Appointments.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				upcomingList.add(new Appointment(myReader.nextLine(), myReader.nextLine(), myReader.nextLine(),
						myReader.nextLine(), myReader.nextLine(), myReader.nextLine(), myReader.nextLine()));
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		// waiting appointments
		try {
			File myObj = new File("src/application/Waiting.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				waitingList.add(new Appointment(myReader.nextLine(), myReader.nextLine(), myReader.nextLine(),
						myReader.nextLine(), myReader.nextLine(), myReader.nextLine(), myReader.nextLine()));
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		// Recorded Appointments
		try {
			File myObj = new File("src/application/Records.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				recordList.add(new Appointment(myReader.nextLine(), myReader.nextLine(), myReader.nextLine(),
						myReader.nextLine(), myReader.nextLine(), myReader.nextLine(), myReader.nextLine()));
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		/********************************
		 * The Application starts
		 ******************************/
		try {
			// Load the Log In scene
			Parent root = FXMLLoader.load(getClass().getResource("LogInScene2.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

			// if X is pressed to close the program -> call closeProgram(); closeProgram
			stage.setOnCloseRequest(event -> {
				event.consume();
				closeProgramX(stage);
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}// End of start()

	/*********************************************
	 * Display Labels -> these display functions control the labels in java FX
	 ********************************************/
	public void displayOnLabel(String message) {
		nameLabel.setText(message);
	}

	public void displayLoginMessage(String message) {
		LoginMessage.setText(message);
	}

	public void displayAppointmentMessage(String message) {
		appointmentLabel.setText(message);
	}

	public void displayVitalsLabelMessage(String message) {
		vitalsLabel.setText(message);
	}

	public void displayExamLabelMessage(String message) {
		examLabel.setText(message);
	}

	public void displayCreateAppMessage(String message) {
		createAppLabel.setText(message);
	}

	public void displayWaitingAppMessage(String message) {
		waitingLabel.setText(message);
	}

	public void displayDateLabel(String message) {
		dateLabel.setText(message);
	}

	public void displayExamsLabel(String message) {
		examsLabel.setText(message);
	}

	public void displayNotesLabel(String message) {
		notesLabel.setText(message);
	}

	public void displayParticipantsLabel(String message) {
		participantsLabel.setText(message);
	}

	public void displayPrescriptionsLabel(String message) {
		prescriptionsLabel.setText(message);
	}

	public void displayTimeLabel(String message) {
		timeLabel.setText(message);
	}

	public void displayVitalzLabel(String message) {
		vitalzLabel.setText(message);
	}

	public void displayNoAppFount(String message) {
		noAppFound.setText(message);
	}

	public void displayPName(String message) {
		CurrnameLabel.setText(message);
	}

	public void displayPDOB(String message) {
		DOBLabel.setText(message);
	}

	public void displayPPhone(String message) {
		phoneLabel.setText(message);
	}

	public void displayPPharmacy(String message) {
		pharmacyLabel.setText(message);
	}

	public void displayPContact(String message) {
		contactLabel.setText(message);
	}

	public void displayPInsurance(String message) {
		insuranceLabel.setText(message);
	}

	public void displayEditInfoLabel(String message) {
		editInfoMessage.setText(message);
	}

	public void displayEditCurrDoctorLabel(String message) {
		currDoctor.setText(message);
	}

	public void displayAvailableUsername(String message) {
		availableUsername.setText(message);
	}

	public void displaySignUpMessage(String message) {
		signUpMessage.setText(message);
	}

	/********************************
	 * Log in - Scene Controller
	 ******************************/
	public void login(ActionEvent event) throws IOException {

		// Assign the values of the Text fields on screen
		String loginUsername = usernameTextfield.getText();
		String loginPassword = passwordTextfield.getText();

		// If they are empty, Report to the screen
		if (loginUsername.trim().isEmpty() && loginPassword.trim().isEmpty())
			displayLoginMessage("Please Enter a Username and Password");

		else if (loginUsername.trim().isEmpty())
			displayLoginMessage("Please Enter a Username");

		else if (loginPassword.trim().isEmpty())
			displayLoginMessage("Please Enter a Password");

		// If they are not empty, continue with the program
		else {
			// PATIENT LOG IN
			for (int i = 0; i < patientList.size(); i++) { // Check if the username is inside the Patient Linked list
				if (loginUsername.equals(patientList.get(i).getUsername())) {
					if (loginPassword.equals(patientList.get(i).getPassword())) { // If it is, Check for the Password
						currPatientUser = patientList.get(i); // If password matches too, assign current user as Patient
						break; // End Loop
					}
				}

			}

			// NURSE LOGIN
			for (int i = 0; i < nurseList.size(); i++) { // Check if the username is inside the Nurse Linked list
				if (loginUsername.equals(nurseList.get(i).getUsername())) {
					if (loginPassword.equals(nurseList.get(i).getPassword())) { // If it is, Check for the Password
						currNurseUser = nurseList.get(i); // If password matches too, assign current user as Nurse
						break; // End Loop
					}
				}
			}

			// DOCTOR LOGIN
			for (int i = 0; i < doctorList.size(); i++) { // Check if the username is inside the Doctor Linked list
				if (loginUsername.equals(doctorList.get(i).getUsername())) {
					if (loginPassword.equals(doctorList.get(i).getPassword())) { // If it is, Check for the Password
						currDoctorUser = doctorList.get(i); // If password matches too, assign current user as Doctor
						break; // End Loop
					}
				}
			}

			// If the User is a Patient
			if (currPatientUser != null) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("PatientScene.fxml"));
				root = loader.load();

				// Add Welcome Message to the Label
				Main patientSceneController = loader.getController();
				patientSceneController.displayOnLabel("Welcome " + currPatientUser.getName());

				// Load the Patient home screen from "PatientScene.fxml"
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();

			}

			// If the User is a Nurse
			else if (currNurseUser != null) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("NurseScene.fxml"));
				root = loader.load();

				// Add Welcome Message to the Label
				Main nurseSceneController = loader.getController();
				nurseSceneController.displayOnLabel("Welcome " + currNurseUser.getName());

				// Load the Nurse home screen from "NurseScene.fxml"
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();

			}

			// If the User is a Doctor
			else if (currDoctorUser != null) {
				doctorList.get(0).setName("Angel FLores");
				FXMLLoader loader = new FXMLLoader(getClass().getResource("DoctorScene.fxml"));
				root = loader.load();

				// Add Welcome Message to the Label
				Main doctorSceneController = loader.getController();
				doctorSceneController.displayOnLabel("Welcome " + currDoctorUser.getName());

				// Load the Doctor home screen from "DoctorScene.fxml"
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();

			}

			// If the username provided was not found
			else {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("ErrorScene.fxml"));
				root = loader.load();

				// Load Error Screen
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
		}
	}// End of Log in

	/****************************************
	 * logout - Button Controller Go to Log in Scene if logout button is pressed
	 ***************************************/
	public void logout(ActionEvent event) throws IOException {

		currNurseUser = null;
		currDoctorUser = null;
		currPatientUser = null;

		Parent root = FXMLLoader.load(getClass().getResource("LogInScene2.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	/********************************
	 * Nurse Main - Scene Controller
	 ******************************/

	// Loads the "Upcoming Appointments" scene
	public void goToUpcomingAppointments(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("UpcomingAppointmentsScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// Loads the "Appointment Records" scene
	public void goToAppointmentRecords(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("AppointmentRecordsScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// Loads the "Edit Patient Info" scene
	public void editPatientInfo(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("EditPatientInfoScene2.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// Loads the "Appointment Editing" scene
	public void editPatientAppointments(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("EditAppointmentScene2.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// Loads the Main scene for Nurses again
	public void backToNurseMain(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("NurseScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}
	
	

	/************************************************************
	 * Nurse Upcoming Appointment - Scene Controller
	 **********************************************************/
	public void takeVitals(ActionEvent event) throws IOException {

		// Determines that only one member of the list can be selected
		listOfAppointments.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// Store the selected appointment on a String variable
		String selectedAppointment = null;
		selectedAppointment = listOfAppointments.getSelectionModel().getSelectedItem();

		// display message if the person haven't clicked on the Find Appointments buttom
		if (listOfAppointments.getItems().isEmpty()) {
			displayAppointmentMessage("Click on 'Find Appointments' and select one");
		}

		// If nothing was selected from the list, report to the screen
		else if (selectedAppointment == null) {
			displayAppointmentMessage("Please select an appointment");
		}

		else {

			// Convert the selected appointment into an array of String
			String[] participantsStr = selectedAppointment.split(" ");

			// FInd the selected appointment on the upcoming appointments
			for (int j = 0; j < upcomingList.size(); j++) {
				if (upcomingList.get(j).getDate().equals(participantsStr[2])
						&& upcomingList.get(j).getTime().equals(participantsStr[3]) && upcomingList.get(j)
								.getParticipants().equals(participantsStr[0] + " " + participantsStr[1])) {
					currAppointment = upcomingList.get(j);
					upcomingList.remove(j);
					System.out.println(currAppointment.getDate());
					break;
				}
			}

			// If everything worked properly, Load the Vitals Scene
			Parent root = FXMLLoader.load(getClass().getResource("VitalsScene.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}// End of take Vitals

	// Function to record virals in the system
	public void recordVitals(ActionEvent event) throws IOException {

		// Define all textfields on the screen
		String temp = bodyTemp.getText();
		String pRate = pulseRate.getText();
		String rRate = respRate.getText();
		String pressure = bloodPressure.getText();

		// Check in any are empty, if not, then update vitals and get back to upcoming
		// Appointments
		if (temp.trim().isEmpty() && pRate.trim().isEmpty() && rRate.trim().isEmpty() && pressure.trim().isEmpty())
			displayVitalsLabelMessage("All vitals are missing");

		else if (temp.trim().isEmpty() || pRate.trim().isEmpty() || rRate.trim().isEmpty() || pressure.trim().isEmpty())
			displayVitalsLabelMessage("Some vitals are missing");

		// If all vitals were written, record them on the system
		else {
			currAppointment.setVitals("Temp: " + temp + "   " + "P-Rate: " + pRate + "   " + "R-Rate: " + rRate + "   "
					+ "B-Press: " + pressure);

			// Pass the appointment to the Waiting List for doctors to finish
			waitingList.add(currAppointment);
			currAppointment = null;

			// Go back one more scene
			Parent root = FXMLLoader.load(getClass().getResource("UpcomingAppointmentsScene.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}

	}// End of Record Vitals

	// This method finds appointments in the upcoming list and adds them to the List
	// View
	@FXML
	void findAppointments(ActionEvent event) {

		for (int j = 0; j < upcomingList.size(); j++) {

			String appointment = upcomingList.get(j).getParticipants() + " " + upcomingList.get(j).getDate() + " "
					+ upcomingList.get(j).getTime();

			listOfAppointments.getItems().add(appointment);
		}
	}

	// This method goes back one more scene
	public void backToUpcomingApps(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("UpcomingAppointmentsScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	/************************************************************
	 * Nurse Appointment Records - Scene Controller
	 **********************************************************/
	public void printRecords(ActionEvent event) throws IOException {

		// Allow one record to be selected at a time
		listOfRecordsN.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// Record selected one
		String selectedRecord = null;
		selectedRecord = listOfRecordsN.getSelectionModel().getSelectedItem();

		// If list is empty, Ask the user to solve it
		if (listOfRecordsN.getItems().isEmpty()) {
			displayAppointmentMessage("Click on 'Find Records' and select one");
		}

		// If they haven't selected one, ask them to
		else if (selectedRecord == null) {
			displayAppointmentMessage("Please select a Record");

		}

		// If everything was properly selected, go to Next Screen
		else {

			// Convert the selected appointment into an array of String
			String[] participantsStr = selectedRecord.split(" ");

			// Find the selected appointment on the records
			for (int j = 0; j < recordList.size(); j++) {

				if (recordList.get(j).getDate().equals(participantsStr[2])
						&& recordList.get(j).getTime().equals(participantsStr[3])
						&& recordList.get(j).getParticipants().equals(participantsStr[0] + " " + participantsStr[1])) {

					System.out.println("Record was found");
					currRecord = recordList.get(j);
					System.out.println(currRecord.getDate());
					break;
				}
			}

			// If everything worked properly, Load the Scene
			Parent root = FXMLLoader.load(getClass().getResource("RecordForNurses.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}

	}// End of printRecords

	// This method finds appointments in the records list and adds them to the List
	// View
	@FXML
	void findRecordsNurse(ActionEvent event) {

		for (int j = 0; j < recordList.size(); j++) {

			String appointment = recordList.get(j).getParticipants() + " " + recordList.get(j).getDate() + " "
					+ recordList.get(j).getTime();

			listOfRecordsN.getItems().add(appointment);

		}
	}

	// This method displays all the Record info on the Screen
	public void seeAppointment(ActionEvent event) {

		displayDateLabel(currRecord.getDate());
		displayExamsLabel(currRecord.getExams());
		displayNotesLabel(currRecord.getNotes());
		displayParticipantsLabel(currRecord.getParticipants());
		displayPrescriptionsLabel(currRecord.getPrescriptions());
		displayTimeLabel(currRecord.getTime());
		displayVitalzLabel(currRecord.getVitals());
	}

	// Go back one scene
	public void backToAppointmentRecords(ActionEvent event) throws IOException {

		currRecord = null;

		Parent root = FXMLLoader.load(getClass().getResource("AppointmentRecordsScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	/************************************************************
	 * Nurse Edit Patient Appointment - Scene Controller
	 **********************************************************/
	// This method finds appointments in the upcoming list and adds them to the List
	// View
	@FXML
	void findEditAppointmentsforNurses(ActionEvent event) {

		for (int j = 0; j < upcomingList.size(); j++) {

			String appointment = upcomingList.get(j).getParticipants() + " " + upcomingList.get(j).getDate() + " "
					+ upcomingList.get(j).getTime();

			listOfEditAppointmentsN.getItems().add(appointment);

		}
	} // End of findEditAppointmentsforNurses

	// This method controls the next scene for nurses
	public void goToEditAppointmentForNurses(ActionEvent event) throws IOException {

		// Allow one record to be selected at a time
		listOfEditAppointmentsN.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// Record selected one
		String selectedRecord = null;
		selectedRecord = listOfEditAppointmentsN.getSelectionModel().getSelectedItem();

		// If list is empty, Ask the user to solve it
		if (listOfEditAppointmentsN.getItems().isEmpty()) {

			displayAppointmentMessage("Click on 'Find Appointments' and select one");
		}

		// If they haven't selected one, ask them to
		else if (selectedRecord == null) {

			displayAppointmentMessage("Please select an Appointment");
		}

		// If everything was properly selected, go to Next Screen
		else {

			// Convert the selected appointment into an array of String
			String[] participantsStr = selectedRecord.split(" ");
			currentApptoEdit = participantsStr[0] + " " + participantsStr[1];

			// Find the selected appointment on the upcoming appointments
			for (int j = 0; j < upcomingList.size(); j++) {

				if (upcomingList.get(j).getDate().equals(participantsStr[2])
						&& upcomingList.get(j).getTime().equals(participantsStr[3]) && upcomingList.get(j)
								.getParticipants().equals(participantsStr[0] + " " + participantsStr[1])) {

					System.out.println("Record was found");
					currAppointment = upcomingList.get(j);
					upcomingList.remove(j);
					System.out.println(currAppointment.getDate());
					break;
				}
			}

			// If everything was properly selected, go to Next Screen
			Parent root = FXMLLoader.load(getClass().getResource("FinishEditApp2.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}

	}// End of Go to Edit Appointment for Nurses

	// This method records the nurse changes to the appointment
	public void pushEditedAppointmentForNurses(ActionEvent event) throws IOException {

		// If a date is not picked, ask user to do it
		if (appointmentDate.getValue() == null) {

			displayCreateAppMessage("Please select Date ");

		}

		// If an appointment is not picked, ask user to do it
		else if (appointmentTime.getValue() == null) {
			displayCreateAppMessage("Please select an available time");
		}

		// If everything was picked, add appointment to the Linked List
		else {

			// get values from the textfields
			LocalDate myDate = appointmentDate.getValue();
			String date = myDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")); // Define format for Date
			String time = appointmentTime.getValue();
			String vitals = "";
			String exams = "";
			String notes = "";
			String prescriptions = "";
			String participants = currentApptoEdit;

			// Add appointment
			upcomingList.add(new Appointment(date, time, vitals, exams, notes, prescriptions, participants));

			// Load patient Main Scene
			Parent root = FXMLLoader.load(getClass().getResource("NurseScene.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}

	}// End of Push Edited Appointment for nurses

	/************************************************************
	 * Nurse Edit Patient Info - Scene Controller
	 **********************************************************/
	// This method finds patients and adds them to the List View
	@FXML
	void findPatientsToEdit(ActionEvent event) {

		for (int j = 0; j < patientList.size(); j++) {

			String patient = patientList.get(j).getDateOfBirth() + " " + patientList.get(j).getName();

			listOfPatientsToEdit.getItems().add(patient);

		}
	} // End of findPatientsToEdit

	// This method helps on editing patient info
	public void goToEditPatientInfo(ActionEvent event) throws IOException {

		// Allow one record to be selected at a time
		listOfPatientsToEdit.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// Record selected one
		String selectedPatient = null;
		selectedPatient = listOfPatientsToEdit.getSelectionModel().getSelectedItem();

		// If list is empty, Ask the user to solve it
		if (listOfPatientsToEdit.getItems().isEmpty()) {

			displayAppointmentMessage("Click on 'Find Appointments' and select one");
		}

		// If they haven't selected one, ask them to
		else if (selectedPatient == null) {

			displayAppointmentMessage("Please select an Appointment");

		}

		// If everything was properly selected, go to Next Screen
		else {

			// Get current Patient to Edit
			currentPatienttoEdit = selectedPatient;

			// Find the selected patient in the Patient List
			for (int j = 0; j < patientList.size(); j++) {

				String findPatient = patientList.get(j).getDateOfBirth() + " " + patientList.get(j).getName();

				if (findPatient.equals(currentPatienttoEdit)) {

					currInfoPatient = patientList.get(j);

					System.out.println(currInfoPatient.getDateOfBirth() + " " + currInfoPatient.getName());
					break;
				}
			}

			// If everything worked properly, Load the Scene
			Parent root = FXMLLoader.load(getClass().getResource("EditPatientInfoScene3.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}

	}// End of goToEditPatientInfo

	// This method updates all the new patient info
	public void updateInfoPatientsForNurses(ActionEvent event) throws IOException {

		// Make sure they see the info first
		if (infoPrinted == false) {
			displayEditInfoLabel("See current info first");
		} else {

			// Get all data from the text fields
			String phonetoUptade = newPhone.getText();
			String pharmacytoUptade = newPharmacy.getText();
			String contactToUpdate = newContact.getText();
			String insuranceToUpdate = newInsurance.getText();
			String currDoctorToUpdate = currDoctors.getValue();

			// Save only the spots that have input in them
			if (!phonetoUptade.trim().isEmpty())
				currInfoPatient.setPhone(phonetoUptade);

			if (!pharmacytoUptade.trim().isEmpty())
				currInfoPatient.setPharmacy(pharmacytoUptade);

			if (!contactToUpdate.trim().isEmpty())
				currInfoPatient.setContactInfo(contactToUpdate);

			if (!insuranceToUpdate.trim().isEmpty())
				currInfoPatient.setInsurance(insuranceToUpdate);

			if (currDoctorToUpdate != null)
				currInfoPatient.setCurrDoctor(currDoctorToUpdate);

			// If everything worked properly, Load the Scene
			Parent root = FXMLLoader.load(getClass().getResource("NurseScene.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}
	}// end of updateInfoPatientsForNurses

	// This Method Displays Patient Info on the screen
	public void seePatientInfoForNurses(ActionEvent event) {

		// Determine all Doctors Available
		for (int i = 0; i < doctorList.size(); i++) {
			currDoctors.getItems().add(doctorList.get(i).getUsername());
		}

		// Add info to the labels on Screen
		displayPName(currInfoPatient.getName());
		displayPDOB(currInfoPatient.getDateOfBirth());
		displayPPhone(currInfoPatient.getPhone());
		displayPPharmacy(currInfoPatient.getPharmacy());
		displayPContact(currInfoPatient.getContactInfo());
		displayPInsurance(currInfoPatient.getInsurance());
		displayEditCurrDoctorLabel(currInfoPatient.getCurrDoctor());

		infoPrinted = true;

	} // End of seePatientInfoForNurses

	/***********************************
	 * Doctor Main - Scene Controller
	 **********************************/
	// Loads the Appoinments waiting Scene
	public void goToAppointmentsWaiting(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("AppointmentsWaitingScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// Loads the Appointment Records Scene
	public void goToAppointmentRecordsD(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("AppointmentRecordsScene2.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// Goes back to main scene for doctors
	public void backToDoctorMain(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("DoctorScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	/************************************************************
	 * Doctor Appointments Waiting - Scene Controller
	 **********************************************************/
	public void finishAppointment(ActionEvent event) throws IOException {

		// Allow one record to be selected at a time
		listOfWaitingAppointments.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		String selectedAppointment = null;

		// Record selected one
		selectedAppointment = listOfWaitingAppointments.getSelectionModel().getSelectedItem();

		// If list is empty, Ask the user to solve it
		if (listOfWaitingAppointments.getItems().isEmpty()) {
			displayWaitingAppMessage("Click on 'Find Appointments' and select one");
		}

		// If they haven't selected one, ask them to
		else if (selectedAppointment == null) {

			displayWaitingAppMessage("Please select an appointment");
		}

		// If everything was properly selected, go to Next Screen
		else {

			// Convert the selected participant into an array of String
			String[] participantsStr = selectedAppointment.split(" ");

			// Find the selected appointment in the Waiting Appointment List
			for (int j = 0; j < waitingList.size(); j++) {
				if (waitingList.get(j).getDate().equals(participantsStr[2])
						&& waitingList.get(j).getTime().equals(participantsStr[3])
						&& waitingList.get(j).getParticipants().equals(participantsStr[0] + " " + participantsStr[1])) {

					currAppointment = waitingList.get(j);
					waitingList.remove(j);
					System.out.println(currAppointment.getDate());
					break;
				}
			}

			// If everything worked properly, Load the Scene
			Parent root = FXMLLoader.load(getClass().getResource("ExamsScene.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}

	}// End of finish Appointment

	// Records the Exam the Doctor just did
	public void recordExam(ActionEvent event) throws IOException {

		// Get data from Text fields
		String exams = examText.getText();
		String prescriptions = prescriptionText.getText();
		String notes = notesText.getText();

		// Check in any are empty report it, if not then update vitals and get back to
		// upcoming Appointments
		if (exams.trim().isEmpty() && prescriptions.trim().isEmpty() && notes.trim().isEmpty()) {

			displayExamLabelMessage("All items are missing");
		}

		else if (exams.trim().isEmpty() || prescriptions.trim().isEmpty() || notes.trim().isEmpty()) {
			displayExamLabelMessage("Some items are missing");
		}

		// If everything worked properly, Load the Scene
		else {

			// Set the exams on the appropriate patient
			currAppointment.setExams(exams);
			currAppointment.setPrescriptions(prescriptions);
			currAppointment.setNotes(notes);

			// Add appointment to records
			recordList.add(currAppointment);
			currAppointment = null;

			// Load Scene
			Parent root = FXMLLoader.load(getClass().getResource("AppointmentsWaitingScene.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}

	}// End of Record Exam

	// This method finds appointments in the ready list and adds them to the List
	// View
	@FXML
	void findWaitingAppointments(ActionEvent event) {

		for (int j = 0; j < waitingList.size(); j++) {

			String appointment = waitingList.get(j).getParticipants() + " " + waitingList.get(j).getDate() + " "
					+ waitingList.get(j).getTime();

			listOfWaitingAppointments.getItems().add(appointment);
		}
	}// ENd of findWaitingAppointments

	// Return back one scene
	public void backToWaitingApps(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("AppointmentsWaitingScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	/************************************************************
	 * Doctor Appointment Records - Scene Controller
	 **********************************************************/
	public void printRecordsN(ActionEvent event) throws IOException {

		// Allow one record to be selected at a time
		listOfRecordsD.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// Record selected one
		String selectedRecord = null;
		selectedRecord = listOfRecordsD.getSelectionModel().getSelectedItem();

		// If list is empty, Ask the user to solve it
		if (listOfRecordsD.getItems().isEmpty()) {

			displayAppointmentMessage("Click on 'Find Records' and select one");
		}

		// If they haven't selected one, ask them to
		else if (selectedRecord == null) {

			displayAppointmentMessage("Please select a Record");
		}

		// If everything was properly selected, go to Next Screen
		else {

			// Convert the selected appointment into an array of String
			String[] participantsStr = selectedRecord.split(" ");

			// Find the selected appointment in the Records List
			for (int j = 0; j < recordList.size(); j++) {

				if (recordList.get(j).getDate().equals(participantsStr[2])
						&& recordList.get(j).getTime().equals(participantsStr[3])
						&& recordList.get(j).getParticipants().equals(participantsStr[0] + " " + participantsStr[1])) {

					System.out.println("Record was found");
					currRecord = recordList.get(j);

					System.out.println(currRecord.getDate());
					break;
				}
			}

			// Load Scene
			Parent root = FXMLLoader.load(getClass().getResource("RecordForDoctors.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}

	}// End of printRecordsN

	// Goes back one scene
	public void backToAppointmentRecordsD(ActionEvent event) throws IOException {

		currRecord = null;

		Parent root = FXMLLoader.load(getClass().getResource("AppointmentRecordsScene2.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// This method finds appointments in the records list and adds them to the List
	// View
	@FXML
	void findRecordsDoctor(ActionEvent event) {

		for (int j = 0; j < recordList.size(); j++) {

			String appointment = recordList.get(j).getParticipants() + " " + recordList.get(j).getDate() + " "
					+ recordList.get(j).getTime();

			listOfRecordsD.getItems().add(appointment);
		}
	}
	
	
	/************************************************************
	 * Doctor Messages - Scene Controller
	 **********************************************************/
	public void goToDoctorsMessages(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("DoctorMessages.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}
	@FXML
	void findPatientsToSendMessages(ActionEvent event) {

		for (int j = 0; j < patientList.size(); j++) {

			String patient = patientList.get(j).getDateOfBirth() + " " + patientList.get(j).getName();

			listOfPatientsToEdit.getItems().add(patient);

		}
	} // End of findPatientsToEdit
	
	
	public void goToPatientMessages(ActionEvent event) throws IOException {

		// Allow one record to be selected at a time
		listOfPatientsToEdit.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// Record selected one
		String selectedPatient = null;
		selectedPatient = listOfPatientsToEdit.getSelectionModel().getSelectedItem();

		// If list is empty, Ask the user to solve it
		if (listOfPatientsToEdit.getItems().isEmpty()) {

			displayAppointmentMessage("Click on 'Find Patients' and select one");
		}

		// If they haven't selected one, ask them to
		else if (selectedPatient == null) {

			displayAppointmentMessage("Please select a Patient");

		}

		// If everything was properly selected, go to Next Screen
		else {

			// Get current Patient to Edit
			currentPatienttoEdit = selectedPatient;

			// Find the selected patient in the Patient List
			for (int j = 0; j < patientList.size(); j++) {

				String findPatient = patientList.get(j).getDateOfBirth() + " " + patientList.get(j).getName();

				if (findPatient.equals(currentPatienttoEdit)) {

					currPatientUser = patientList.get(j);

					break;
				}
			}

			// If everything worked properly, Load the Scene
			Parent root = FXMLLoader.load(getClass().getResource("DoctorMessages2.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}

	}// End of goToPatientMessages
	
	
	@FXML
    void sendMessage2(ActionEvent event) {
    	
    	//System.out.println("THanks");
    	
    	String theMessage = message.getText();
    	
    	if(theMessage.trim().isEmpty()) {
    		
    		messageWarning.setText("You need to type a meesage to send");
    	}
    	
    	else {
    		
    		
    		for(int i = 0; i < patientList.size(); i++){
    			if(currPatientUser.getName().equals(patientList.get(i).getName())){
    				patientList.get(i).setMessage("Doctor: " + theMessage);
    				break;
    			}
    		}
    		
    		//messageSuccess
    		messageSuccess.setText("Message was sent - You can go back to Main menu now");
    		messageWarning.setText(" ");
    		
    		
    	}

    }///End of Send Message2
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/***********************************
	 * Patient Main - Scene Controller
	 **********************************/
	public void goToPersonalUpcoming(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("PersonalUpcomingScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// Loads Personal Record Screen
	public void goToPersonalRecord(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("PersonalRecordScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// Loads the Edit Appointment List
	public void editAppointment(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("EditAppointmentScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// Loads the Edit personal info Scene
	public void editPatientInfoP(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("EditPatientInfoScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// Loads the Appointment Records Scene for Patiends
	public void backToAppointmentRecordsP(ActionEvent event) throws IOException {

		currRecord = null;

		Parent root = FXMLLoader.load(getClass().getResource("PersonalRecordScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// Loads the Patient Main Screen
	public void backToPatientMain(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("PatientScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/************************************************************
	 * Patient Upcoming Appointment - Scene Controller
	 **********************************************************/

	// This method finds appointments in the upcoming list and adds them to the List
	// View
	@FXML
	void findUpAppointments(ActionEvent event) {

		for (int j = 0; j < upcomingList.size(); j++) {

			String findApp = currPatientUser.getUsername() + " " + currPatientUser.getCurrDoctor();

			if (upcomingList.get(j).getParticipants().equals(findApp)) {

				String appointment = upcomingList.get(j).getParticipants() + " " + upcomingList.get(j).getDate() + " "
						+ upcomingList.get(j).getTime();

				listOfUpAppointments.getItems().add(appointment);

			}
		}
	} // End of findUpAppointments

	// Loads the Personal Appointment Scene
	public void goToPersonalAppointment(ActionEvent event) throws IOException {

		// Allow one record to be selected at a time
		listOfUpAppointments.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// Record selected one
		String selectedRecord = null;
		selectedRecord = listOfUpAppointments.getSelectionModel().getSelectedItem();

		// If list is empty, Ask the user to solve it
		if (listOfUpAppointments.getItems().isEmpty()) {

			displayAppointmentMessage("Click on 'Find Records' and select one");
		}

		// If they haven't selected one, ask them to
		else if (selectedRecord == null) {

			displayAppointmentMessage("Please select a Record");

		}

		// If everything was properly selected, go to Next Screen
		else {

			// Convert the selected appointment into an array of String
			String[] participantsStr = selectedRecord.split(" ");

			// Find the selected appointment in the Waiting Appointment List and add it if
			// found
			for (int j = 0; j < upcomingList.size(); j++) {

				if (upcomingList.get(j).getDate().equals(participantsStr[2])
						&& upcomingList.get(j).getTime().equals(participantsStr[3]) && upcomingList.get(j)
								.getParticipants().equals(participantsStr[0] + " " + participantsStr[1])) {

					currAppointment = upcomingList.get(j);
					break;
				}
			}

			// Load Scene
			Parent root = FXMLLoader.load(getClass().getResource("PatientUpAppointment.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}

	}// End of Go to Personal Appointment

	// Displays appointment on the Screen
	public void seePatientAppointment(ActionEvent event) {

		// Adds text to the Labels
		displayDateLabel(currAppointment.getDate());
		displayTimeLabel(currAppointment.getTime());
	}

	/************************************************************
	 * Patient Create Appointment - Scene Controller
	 **********************************************************/
	public void createAppointment(ActionEvent event) throws IOException {

		// Load Create Appointment Scene
		Parent root = FXMLLoader.load(getClass().getResource("CreateAppointmentScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// Records the Appointment on the System
	public void pushAppointment(ActionEvent event) throws IOException {

		// Ask user to select a date if he hasn't yet
		if (appointmentDate.getValue() == null) {

			displayCreateAppMessage("Please select Date ");
		}

		// Ask user to select a time if he hasn't yet
		else if (appointmentTime.getValue() == null) {
			displayCreateAppMessage("Please select an available time");
		}

		else {

			// Get values from the Text fields on screen
			LocalDate myDate = appointmentDate.getValue();
			String date = myDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
			String time = appointmentTime.getValue();
			String vitals = "";
			String exams = "";
			String notes = "";
			String prescriptions = "";
			String participants = (currPatientUser.getUsername() + " " + currPatientUser.getCurrDoctor());

			// Add to list, Return to Scene, Print report
			upcomingList.add(new Appointment(date, time, vitals, exams, notes, prescriptions, participants));

			// Load patient Main Scene
			Parent root = FXMLLoader.load(getClass().getResource("PatientScene.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}

	}// End of Push Appointment

	// THis method displays all the available times for appointments
	public void displayTimes(ActionEvent event) {

		// Create array Lists with the Times
		ArrayList<String> times = new ArrayList<String>();
		ArrayList<String> takenTimes = new ArrayList<String>();
		ArrayList<String> availableTimes = new ArrayList<String>();

		// Add all the possible times
		times.add("10:00-AM");
		times.add("11:00-AM");
		times.add("12:00-PM");
		times.add("01:00-PM");
		times.add("02:00-PM");
		times.add("03:00-PM");
		times.add("04:00-PM");

		// Access Array List of strings
		ObservableList<String> TimesList = FXCollections.observableArrayList(times);

		// If there are no appointments, add all times
		if (upcomingList.size() == 0)
			appointmentTime.getItems().addAll(TimesList);

		// Check for appointments which times are already taken
		else {

			for (int i = 0; i < upcomingList.size(); i++) {

				for (int j = 0; j < times.size(); j++) {
					if (upcomingList.get(i).getTime().equals(times.get(j))) {
						takenTimes.add(upcomingList.get(i).getTime());
					} else {
						availableTimes.add(times.get(j));
					}
				}
			}

			// Add all available times to the Srop Menu
			ObservableList<String> AvTimesList = FXCollections.observableArrayList(availableTimes);
			appointmentTime.getItems().addAll(AvTimesList);
		}

	}// End of displayTimes

	/************************************************************
	 * Patient Edit Appointment - Scene Controller
	 **********************************************************/
	// This method finds appointments in the upcoming list and adds them to the List
	// View
	@FXML
	void findEditAppointments(ActionEvent event) {

		for (int j = 0; j < upcomingList.size(); j++) {

			String findApp = currPatientUser.getUsername() + " " + currPatientUser.getCurrDoctor();

			// Check if the appointment already exists
			if (upcomingList.get(j).getParticipants().equals(findApp)) {
				String appointment = upcomingList.get(j).getParticipants() + " " + upcomingList.get(j).getDate() + " "
						+ upcomingList.get(j).getTime();
				listOfEditAppointments.getItems().add(appointment);

			}


		}
	} // End of find Edit Appointments

	// Loads the Edit Appointments Scene
	public void goToEditAppointment(ActionEvent event) throws IOException {

		// Allow one record to be selected at a time
		listOfEditAppointments.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// Record selected one
		String selectedRecord = null;
		selectedRecord = listOfEditAppointments.getSelectionModel().getSelectedItem();

		// If list is empty, Ask the user to solve it
		if (listOfEditAppointments.getItems().isEmpty()) {

			displayAppointmentMessage("Click on 'Find Appointments' and select one");
		}

		// If they haven't selected one, ask them to
		else if (selectedRecord == null) {
			displayAppointmentMessage("Please select an Appointment");
		}

		// If everything was properly selected, go to Next Screen
		else {

			// Convert the selected appointment into an array of String
			String[] participantsStr = selectedRecord.split(" ");

			// Find the selected appointment in the Waiting Appointment List
			for (int j = 0; j < upcomingList.size(); j++) {

				if (upcomingList.get(j).getDate().equals(participantsStr[2])
						&& upcomingList.get(j).getTime().equals(participantsStr[3]) && upcomingList.get(j)
								.getParticipants().equals(participantsStr[0] + " " + participantsStr[1])) {

					// Add new appointment to upcoming List
					currAppointment = upcomingList.get(j);
					// Rmove the previous one
					upcomingList.remove(j);
					break;
				}
			}

			// Load Scene
			Parent root = FXMLLoader.load(getClass().getResource("FinishEditApp.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}

	}// End of Go to Edit Appointment

	// Records edited appointments
	public void pushEditedAppointment(ActionEvent event) throws IOException {

		// Ask to select a date if they haven't
		if (appointmentDate.getValue() == null) {

			displayCreateAppMessage("Please select Date ");
		}

		// Ask to select a time if they haven't
		else if (appointmentTime.getValue() == null) {
			displayCreateAppMessage("Please select an available time");
		}

		// Record Screen if all required inputs were typed
		else {

			// Store data from the Text fields
			LocalDate myDate = appointmentDate.getValue();
			String date = myDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
			String time = appointmentTime.getValue();
			String vitals = "";
			String exams = "";
			String notes = "";
			String prescriptions = "";
			String participants = (currPatientUser.getUsername() + " " + currPatientUser.getCurrDoctor());

			// Add to list, Return to Scene
			upcomingList.add(new Appointment(date, time, vitals, exams, notes, prescriptions, participants));

			// Load patient Main Scene
			Parent root = FXMLLoader.load(getClass().getResource("PatientScene.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}

	}// End of Push Edited Appointment

	/************************************************************
	 * Patient Record - Scene Controller
	 **********************************************************/
	public void printRecordsP(ActionEvent event) throws IOException {

		// Allow one record to be selected at a time
		listOfRecordsP.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// Record selected one
		String selectedRecord = null;
		selectedRecord = listOfRecordsP.getSelectionModel().getSelectedItem();

		// If list is empty, Ask the user to solve it
		if (listOfRecordsP.getItems().isEmpty()) {

			displayAppointmentMessage("Click on 'Find Records' and select one");
		}

		// If they haven't selected one, ask them to
		else if (selectedRecord == null) {

			displayAppointmentMessage("Please select a Record");

		}

		// If everything was properly selected, go to Next Screen
		else {

			// Convert the selected appointment into an array of String
			String[] participantsStr = selectedRecord.split(" ");

			// Find the selected record in the Records List
			for (int j = 0; j < recordList.size(); j++) {

				if (recordList.get(j).getDate().equals(participantsStr[2])
						&& recordList.get(j).getTime().equals(participantsStr[3])
						&& recordList.get(j).getParticipants().equals(participantsStr[0] + " " + participantsStr[1])) {

					// Retrieve Record if found
					currRecord = recordList.get(j);
					System.out.println(currRecord.getDate());
					break;
				}
			}

			// Load Scene
			Parent root = FXMLLoader.load(getClass().getResource("RecordForPatients.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}

	}// End of printRecords

	// This method finds Records in the list and adds them to the List View
	@FXML
	public void findRecordsPatient(ActionEvent event) {

		for (int j = 0; j < recordList.size(); j++) {

			String findRecord = currPatientUser.getUsername() + " " + currPatientUser.getCurrDoctor();

			// Check if Patient has any appointments under his/her name
			if (recordList.get(j).getParticipants().equals(findRecord)) {

				String appointment = recordList.get(j).getParticipants() + " " + recordList.get(j).getDate() + " "
						+ recordList.get(j).getTime();

				listOfRecordsP.getItems().add(appointment);

			}
		}
	}// End of findRecordsPatient()

	/************************************************************
	 * Patient Edit Personal Info - Scene Controller
	 **********************************************************/
	public void seePatientInfo(ActionEvent event) {

		// Adds all Patient info to the labels
		displayPName(currPatientUser.getName());
		displayPDOB(currPatientUser.getDateOfBirth());
		displayPPhone(currPatientUser.getPhone());
		displayPPharmacy(currPatientUser.getPharmacy());
		displayPContact(currPatientUser.getContactInfo());
		displayPInsurance(currPatientUser.getInsurance());

		// Report if info was printed
		infoPrinted = true;

	}

	// Updates the info of the Patient
	public void updateInfoPatients(ActionEvent event) throws IOException {

		// Patient has to see his/her info before updating
		if (infoPrinted == false) {
			displayEditInfoLabel("See current info first");
		} else {

			// Define all textfields
			String phonetoUptade = newPhone.getText();
			String pharmacytoUptade = newPharmacy.getText();
			String contactToUpdate = newContact.getText();
			String insuranceToUpdate = newInsurance.getText();

			// Update only text fields with info in them
			if (!phonetoUptade.trim().isEmpty())
				currPatientUser.setPhone(phonetoUptade);

			if (!pharmacytoUptade.trim().isEmpty())
				currPatientUser.setPharmacy(pharmacytoUptade);

			if (!contactToUpdate.trim().isEmpty())
				currPatientUser.setContactInfo(contactToUpdate);

			if (!insuranceToUpdate.trim().isEmpty())
				currPatientUser.setInsurance(insuranceToUpdate);

			// Load Scene
			Parent root = FXMLLoader.load(getClass().getResource("PatientScene.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}
	}// end of pdateInfoPatients

	
	/************************************************************
	 * Patient Messages - Scene Controller
	 **********************************************************/
		public void patientMessages(ActionEvent event) throws IOException {

			Parent root = FXMLLoader.load(getClass().getResource("PatientMessages.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}
		
		
	    @FXML
	    void displayMessage(ActionEvent event) {
	    	
	    	
	    	if (currPatientUser.getMessage().isEmpty())
	    	{
	    		noMessages.setText("There is no messages");
	    	}
	    	
	    	else{
	    		messageLabel.setText(currPatientUser.getMessage());
	    	}
	    	

	    }//End of displayMessage

	    @FXML
	    void sendMessage(ActionEvent event) {
	    	
	    	//System.out.println("THanks");
	    	
	    	String theMessage = message.getText();
	    	
	    	if(theMessage.trim().isEmpty()) {
	    		
	    		messageWarning.setText("You need to type a meesage to send");
	    	}
	    	
	    	else {
	    		
	    		
	    		for(int i = 0; i < patientList.size(); i++){
	    			if(currPatientUser.getName().equals(patientList.get(i).getName())){
	    				patientList.get(i).setMessage("Patient: " + theMessage);
	    				break;
	    			}
	    		}
	    		
	    		//messageSuccess
	    		messageSuccess.setText("Message was sent - You can go back to Main menu now");
	    		messageWarning.setText(" ");
	    		
	    		
	    	}

	    }///End of Send Message

	
	
	
	
	
	
	
	
	/********************************
	 * Sign Up - Scene Controller
	 ******************************/
	public void signUp(ActionEvent event) throws IOException {

		// Load Sign up scene
		FXMLLoader loader = new FXMLLoader(getClass().getResource("signUpScene.fxml"));
		root = loader.load();

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}// End of Sign Up

	// Checks if a username is avaiable
	public void checkAvailableUsername(ActionEvent event) throws IOException {

		// boolean to detect id a username was found
		isUsernameAvailable = true;
		boolean availableUsername = true;

		// take username from text field
		String lookUsername = usernameTextfield.getText();

		// Check on Patient List
		for (int i = 0; i < patientList.size(); i++) {
			if (lookUsername.equals(patientList.get(i).getUsername())) {
				availableUsername = false;
				isUsernameAvailable = false;
				break;
			}
		}

		// Check on Nurse List
		for (int i = 0; i < nurseList.size(); i++) {
			if (lookUsername.equals(nurseList.get(i).getUsername())) {
				availableUsername = false;
				isUsernameAvailable = false;
				break;
			}
		}

		// Check on Doctor List
		for (int i = 0; i < doctorList.size(); i++) {
			if (lookUsername.equals(doctorList.get(i).getUsername())) {
				availableUsername = false;
				isUsernameAvailable = false;
				break;
			}
		}

		// Ask them to enter empty text fields
		if (lookUsername.trim().isEmpty()) {
			displayAvailableUsername("Please enter a username");
		} else if (availableUsername == false) {
			displayAvailableUsername("Username is not Available");
		} else {
			displayAvailableUsername("Username is available :)");
		}

	}// End of checkAvailableUsername

	// Creates an account if filters were passed
	public void createAccount(ActionEvent event) throws IOException {

		// Get data from text fields
		String createUsername = usernameTextfield.getText();
		String createPassword = passwordTextfield.getText();

		// Ask to fill any empty text fields
		if (createUsername.trim().isEmpty()) {
			displaySignUpMessage("Please complete steps above");
		} else if (isUsernameAvailable == false) {
			displaySignUpMessage("Cannot create a username that already exist");
		} else {

			if (createPassword.trim().isEmpty()) {
				displaySignUpMessage("Please Enter a Password");
			} else {
				System.out.println("Creating account");

				currsignUpUsername = createUsername;// Add to global currsignUpUsername
				currsignUpPassword = createPassword;// Add to global currsignUpPassword

				// Load scene
				Parent root = FXMLLoader.load(getClass().getResource("signUpTypeScene.fxml"));
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();

			}

		}

	}// End of createAccount

	// Creates Account for Patients
	public void pushAccountPatient(ActionEvent event) throws IOException {

		// Get data from text fields
		String name = profileName.getText();
		String dob = profileDOB.getText();
		String phone = profilePhone.getText();

		// Check if any are empty
		if (name.trim().isEmpty() && dob.trim().isEmpty() && phone.trim().isEmpty())// && pressure.trim().isEmpty())
			displayEditInfoLabel("All fields are missing");

		else if (name.trim().isEmpty() || dob.trim().isEmpty() || phone.trim().isEmpty())
			displayEditInfoLabel("Some fields are missing");

		// else -> create profile
		else {

			patientList.add(new Patient(currsignUpUsername, currsignUpPassword, name, dob, "", "", phone, "", "", ""));

			// Load Scene
			Parent root = FXMLLoader.load(getClass().getResource("LogInScene2.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}
	}// end of pushAccountPatient

	// Creates account for nurses
	public void pushAccountNurse(ActionEvent event) throws IOException {

		// Get data from text fields
		String name = profileName.getText();
		String doctor1 = profileDoctor1.getValue();
		String doctor2 = profileDoctor2.getValue();

		// Check if any are empty
		if (name.trim().isEmpty() && doctor1 == null && doctor2 == null)// && pressure.trim().isEmpty())
			displayEditInfoLabel("All fields are missing");

		else if (name.trim().isEmpty() || doctor1 == null || doctor2 == null)
			displayEditInfoLabel("Some fields are missing");

		// Make sure doctors are not duplicated
		else if (doctor1.equals(doctor2))
			displayEditInfoLabel("Doctors are duplicated - Please correct");

		// else -> create profile
		else {

			String doctorList = doctor1 + " " + doctor2;

			nurseList.add(new Nurse(currsignUpUsername, currsignUpPassword, name, doctorList));

			// Load Scene
			Parent root = FXMLLoader.load(getClass().getResource("LogInScene2.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}
	}// end of pushAccountNurse

	// Creates account for Doctor
	public void pushAccountDoctor(ActionEvent event) throws IOException {

		// Get data from text fields
		String name = profileName.getText();
		String nurse = profileNurses.getValue();
		String patient1 = profilePatients1.getValue();
		String patient2 = profilePatients2.getValue();

		// Check if any are empty
		if (name.trim().isEmpty() && nurse == null && patient1 == null && patient2 == null)// &&
																							// pressure.trim().isEmpty())
			displayEditInfoLabel("All fields are missing");

		else if (name.trim().isEmpty() || nurse == null || patient1 == null || patient2 == null)
			displayEditInfoLabel("Some fields are missing");

		// Chck if patients are duplicated
		else if (patient1.equals(patient2))
			displayEditInfoLabel("Patients are duplicated - Please correct");

		// else -> create profile
		else {

			String patients = patient1 + " " + patient2;

			System.out.println("New account for Doctor created succesfully");

			// Load Scene
			Parent root = FXMLLoader.load(getClass().getResource("LogInScene2.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}
	}// end of pushAccountDoctor

	// Step 2 for sign up -> Nurse
	public void goToStep2Nurse(ActionEvent event) throws IOException {

		// Access Code
		String code = "1234";

		// Load pass code entered on text fields
		String passCode = permissionCode.getText();

		// See if pass code text field is empty
		if (passCode.trim().isEmpty()) {

			displaySignUpMessage("Please Enter a Passcode");
		}

		// Report if pass codes do not match
		else if (!passCode.equals(code)) {

			displaySignUpMessage("The Passcode you entered is wrong");
		}

		else {

			// Load Scene
			Parent root = FXMLLoader.load(getClass().getResource("SignUpSet2Nurse.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}

	}// End of goToStep2Nurse

	// Step 2 for sign up -> Doctor
	public void goToStep2Doctor(ActionEvent event) throws IOException {

		// Access Code
		String code = "1234";

		// Load pass code entered on text fields
		String passCode = permissionCode.getText();

		// See if pass code text field is empty
		if (passCode.trim().isEmpty()) {

			displaySignUpMessage("Please Enter a Passcode");
		}

		// Report if pass codes do not match
		else if (!passCode.equals(code)) {

			displaySignUpMessage("The Passcode you entered is wrong");
		}

		else {

			// Load Scene
			Parent root = FXMLLoader.load(getClass().getResource("SignUpSet2Doctor.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}

	}// End of goToStep2Doctor

	// Step 2 -> Patient
	public void goToStep2Patient(ActionEvent event) throws IOException {

		// Load Scene
		Parent root = FXMLLoader.load(getClass().getResource("signUpSet2Patient.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}// End of goToStep2Patient

	// This method finds doctors and adds them to the Drop down menus
	public void displayDoctors(ActionEvent event) throws IOException {

		for (int i = 0; i < doctorList.size(); i++) {
			profileDoctor1.getItems().add(doctorList.get(i).getUsername());
		}

		for (int i = 0; i < doctorList.size(); i++) {
			profileDoctor2.getItems().add(doctorList.get(i).getUsername());
		}

	}

	// This method finds nurses and adds them to the Drop down menus
	public void displayNurses(ActionEvent event) throws IOException {

		for (int i = 0; i < nurseList.size(); i++) {
			profileNurses.getItems().add(nurseList.get(i).getUsername());
		}

	}

	// This method finds patients and adds them to the Drop down menus
	public void displayPatients(ActionEvent event) throws IOException {

		for (int i = 0; i < patientList.size(); i++) {
			profilePatients1.getItems().add(patientList.get(i).getUsername());
		}

		for (int i = 0; i < patientList.size(); i++) {
			profilePatients2.getItems().add(patientList.get(i).getUsername());
		}

	}

	/*************************************
	 * Reset Password - Scene Controller
	 ************************************/
	public void resetPassword(ActionEvent event) throws IOException {

		// Load Scene
		Parent root = FXMLLoader.load(getClass().getResource("ResetPasswordScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void findUsername(ActionEvent event) throws IOException {

		// to detect if user name was found
		foundUsernameToReset = false;

		// get user name from text field
		String lookUsername = usernameTextfield.getText();

		// Check on Patient List
		for (int i = 0; i < patientList.size(); i++) {
			if (lookUsername.equals(patientList.get(i).getUsername())) {
				foundUsernameToReset = true;
				break;
			}
		}

		// Check on Nurse List
		for (int i = 0; i < nurseList.size(); i++) {
			if (lookUsername.equals(nurseList.get(i).getUsername()))
				foundUsernameToReset = true;
			break;
		}

		// Check on Doctor List
		for (int i = 0; i < doctorList.size(); i++) {
			if (lookUsername.equals(doctorList.get(i).getUsername()))
				foundUsernameToReset = true;
			break;
		}

		// Detect if the user missed any trext fields
		if (lookUsername.trim().isEmpty()) {
			displayAvailableUsername("Please enter a username");
		} else if (foundUsernameToReset == false) {
			displayAvailableUsername("Username was not found");
		} else {
			displayAvailableUsername("Username was found, you can reset password now");
		}

	}// End of findUsername

	// Complete password change
	public void updatePassword(ActionEvent event) throws IOException {

		// Get data from textfields
		String currentUsername = usernameTextfield.getText();
		String newPassword = passwordTextfield.getText();

		// Check if any spots are empry
		if (currentUsername.trim().isEmpty()) {
			displaySignUpMessage("Please complete steps above");
		} else if (foundUsernameToReset == false) {
			displaySignUpMessage("Cannot Reset a Password for an acccount that hasn't been found");
		} else {

			if (newPassword.trim().isEmpty()) {
				displaySignUpMessage("Please Enter a Password");
			} else {

				// Look in Patient list
				for (int i = 0; i < patientList.size(); i++) {

					// Change Password
					if (currentUsername.equals(patientList.get(i).getUsername())) {
						patientList.get(i).setPassword(newPassword);
					}

				}

				// Look in Nurse list
				for (int i = 0; i < nurseList.size(); i++) {

					// Change Password
					if (currentUsername.equals(nurseList.get(i).getUsername())) {
						nurseList.get(i).setPassword(newPassword);
					}

				}

				// Look in Doctor list
				for (int i = 0; i < doctorList.size(); i++) {

					// Change Password
					if (currentUsername.equals(doctorList.get(i).getUsername())) {
						doctorList.get(i).setPassword(newPassword);
					}

				}

				// Load Scene
				Parent root = FXMLLoader.load(getClass().getResource("LogInScene2.fxml"));
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();

			}

		}

	}// End of createAccount

	// Closes the program if X is pressed
	public void closeProgramX(Stage closeStage) {

		// Create alert screen
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("You are about to logout!");
		alert.setContentText("Do you want to 'Save' before Exiting?");

		// If ok button is pressed
		if (alert.showAndWait().get() == ButtonType.OK) {

			// ****************************************************
			// write Linked Lists back to text files
			// ****************************************************

			// patient
			try {
				FileWriter myWriter = new FileWriter("src/application/Patients.txt");
				Patient patout;

				while (patientList.size() != 0) {
					patout = patientList.removeFirst();

					myWriter.write(patout.getUsername() + "\n");
					myWriter.write(patout.getPassword() + "\n");
					myWriter.write(patout.getName() + "\n");
					myWriter.write(patout.getDateOfBirth() + "\n");
					myWriter.write(patout.getCurrDoctor() + "\n");
					myWriter.write(patout.getMessage() + "\n");
					myWriter.write(patout.getPhone() + "\n");
					myWriter.write(patout.getPharmacy() + "\n");
					myWriter.write(patout.getContactInfo() + "\n");
					myWriter.write(patout.getInsurance() + "\n");
				}
				myWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			// nurse
			try {
				FileWriter myWriter = new FileWriter("src/application/Nurses.txt");
				Nurse nurout;

				while (nurseList.size() != 0) {
					nurout = nurseList.removeFirst();

					myWriter.write(nurout.getUsername() + "\n");
					myWriter.write(nurout.getPassword() + "\n");
					myWriter.write(nurout.getName() + "\n");
					myWriter.write(nurout.getDoctorList() + "\n");
				}
				myWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			// doctor
			try {
				FileWriter myWriter = new FileWriter("src/application/Doctors.txt");
				Doctor docout;

				while (doctorList.size() != 0) {
					docout = doctorList.removeFirst();

					myWriter.write(docout.getUsername() + "\n");
					myWriter.write(docout.getPassword() + "\n");
					myWriter.write(docout.getName() + "\n");
					myWriter.write(docout.getCurrNurse() + "\n");
					myWriter.write(docout.getPatientList() + "\n");
				}
				myWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			// upcoming appointments
			try {
				FileWriter myWriter = new FileWriter("src/application/Appointments.txt");
				Appointment upout;

				while (upcomingList.size() != 0) {
					//System.out.println("Size of list is " + upcomingList.size());
					upout = upcomingList.removeLast();

					myWriter.write(upout.getDate() + "\n");
					myWriter.write(upout.getTime() + "\n");
					myWriter.write(upout.getVitals() + "\n");
					myWriter.write(upout.getExams() + "\n");
					myWriter.write(upout.getNotes() + "\n");
					myWriter.write(upout.getPrescriptions() + "\n");
					myWriter.write(upout.getParticipants() + "\n");
				}
				myWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			// recorded appointments
			try {
				FileWriter myWriter = new FileWriter("src/application/Records.txt");
				Appointment rdout;

				while (recordList.size() != 0) {
					rdout = recordList.removeFirst();

					myWriter.write(rdout.getDate() + "\n");
					myWriter.write(rdout.getTime() + "\n");
					myWriter.write(rdout.getVitals() + "\n");
					myWriter.write(rdout.getExams() + "\n");
					myWriter.write(rdout.getNotes() + "\n");
					myWriter.write(rdout.getPrescriptions() + "\n");
					myWriter.write(rdout.getParticipants() + "\n");
				}
				myWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			// waiting appointments
			try {
				FileWriter myWriter = new FileWriter("src/application/Waiting.txt");
				Appointment waout;

				while (waitingList.size() != 0) {
					waout = waitingList.removeFirst();

					myWriter.write(waout.getDate() + "\n");
					myWriter.write(waout.getTime() + "\n");
					myWriter.write(waout.getVitals() + "\n");
					myWriter.write(waout.getExams() + "\n");
					myWriter.write(waout.getNotes() + "\n");
					myWriter.write(waout.getPrescriptions() + "\n");
					myWriter.write(waout.getParticipants() + "\n");
				}
				myWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			// Closing Window
			System.out.println("You successfully closed the program");
			closeStage.close();
		}

	}// End of closeProgram

	// Close Program if exit button is pressed
	public void closeProgram(ActionEvent event) {

		// Create Alert Screen
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("You are about to logout!");
		alert.setContentText("Do you want to 'Save' before Exiting?");

		// If ok button is pressed
		if (alert.showAndWait().get() == ButtonType.OK) {

			// ****************************************************
			// write Linked Lists back to text files
			// ****************************************************

			// patient
			try {
				FileWriter myWriter = new FileWriter("src/application/Patients.txt");
				Patient patout;

				while (patientList.size() != 0) {
					patout = patientList.removeFirst();

					myWriter.write(patout.getUsername() + "\n");
					myWriter.write(patout.getPassword() + "\n");
					myWriter.write(patout.getName() + "\n");
					myWriter.write(patout.getDateOfBirth() + "\n");
					myWriter.write(patout.getCurrDoctor() + "\n");
					myWriter.write(patout.getMessage() + "\n");
					myWriter.write(patout.getPhone() + "\n");
					myWriter.write(patout.getPharmacy() + "\n");
					myWriter.write(patout.getContactInfo() + "\n");
					myWriter.write(patout.getInsurance() + "\n");
				}
				myWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			// nurse
			try {
				FileWriter myWriter = new FileWriter("src/application/Nurses.txt");
				Nurse nurout;

				while (nurseList.size() != 0) {
					nurout = nurseList.removeFirst();

					myWriter.write(nurout.getUsername() + "\n");
					myWriter.write(nurout.getPassword() + "\n");
					myWriter.write(nurout.getName() + "\n");
					myWriter.write(nurout.getDoctorList() + "\n");
				}
				myWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			// doctor
			try {
				FileWriter myWriter = new FileWriter("src/application/Doctors.txt");
				Doctor docout;

				while (doctorList.size() != 0) {
					docout = doctorList.removeFirst();

					myWriter.write(docout.getUsername() + "\n");
					myWriter.write(docout.getPassword() + "\n");
					myWriter.write(docout.getName() + "\n");
					myWriter.write(docout.getCurrNurse() + "\n");
					myWriter.write(docout.getPatientList() + "\n");
				}
				myWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			// upcoming appointments
			try {
				FileWriter myWriter = new FileWriter("src/application/Appointments.txt");
				Appointment upout;

				while (upcomingList.size() != 0) {
					//System.out.println("Size of list is " + upcomingList.size());
					upout = upcomingList.removeLast();

					myWriter.write(upout.getDate() + "\n");
					myWriter.write(upout.getTime() + "\n");
					myWriter.write(upout.getVitals() + "\n");
					myWriter.write(upout.getExams() + "\n");
					myWriter.write(upout.getNotes() + "\n");
					myWriter.write(upout.getPrescriptions() + "\n");
					myWriter.write(upout.getParticipants() + "\n");
				}
				myWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			// recorded appointments
			try {
				FileWriter myWriter = new FileWriter("src/application/Records.txt");
				Appointment rdout;

				while (recordList.size() != 0) {
					rdout = recordList.removeFirst();

					myWriter.write(rdout.getDate() + "\n");
					myWriter.write(rdout.getTime() + "\n");
					myWriter.write(rdout.getVitals() + "\n");
					myWriter.write(rdout.getExams() + "\n");
					myWriter.write(rdout.getNotes() + "\n");
					myWriter.write(rdout.getPrescriptions() + "\n");
					myWriter.write(rdout.getParticipants() + "\n");
				}
				myWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			// waiting appointments
			try {
				FileWriter myWriter = new FileWriter("src/application/Waiting.txt");
				Appointment waout;

				while (waitingList.size() != 0) {
					waout = waitingList.removeFirst();

					myWriter.write(waout.getDate() + "\n");
					myWriter.write(waout.getTime() + "\n");
					myWriter.write(waout.getVitals() + "\n");
					myWriter.write(waout.getExams() + "\n");
					myWriter.write(waout.getNotes() + "\n");
					myWriter.write(waout.getPrescriptions() + "\n");
					myWriter.write(waout.getParticipants() + "\n");
				}
				myWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			// Closing Window
			closeStage = (Stage) LogInPane.getScene().getWindow();
			System.out.println("You successfully closed the program");
			closeStage.close();
		}

	}// End of closeProgram

} // End of main class