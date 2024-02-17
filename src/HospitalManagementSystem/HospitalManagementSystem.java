package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password="12345";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner=new Scanner(System.in);
        try{
            Connection connection= DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(connection,scanner);
            Doctor doctor=new Doctor(connection);
            while (true){
                System.out.println("----HOSPITAL MANAGEMENT SYSTEM----");
                System.out.println("Select from the options below");
                System.out.println("1 add patient");
                System.out.println("2 view patients");
                System.out.println("3 view doctors");
                System.out.println("4 book appointment");
                System.out.println("5 exit");
                int choice=scanner.nextInt();

                switch (choice){
                    case 1:
                        //add patient
                        patient.addPatient();
                        System.out.println();
                    case 2:
                        //view patient
                        patient.viewPatients();
                        System.out.println();
                    case 3:
                        //view doctors
                        doctor.viewDoctors();
                        System.out.println();
                    case 4:
                        //book appointment
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println();
                    case 5:
                        //exit
                        return;
                    default:
                        System.out.println("**ENTER VALID CHOICE**");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public static void bookAppointment(Patient patient ,Doctor doctor ,Connection connection,Scanner scanner){
        System.out.print("ENTER Patient ID");
        int patient_id=scanner.nextInt();
        System.out.println("ENTER Doctor ID");
        int doctor_id=scanner.nextInt();
        System.out.print("Enter Appointment Date (YYYY-MM-DD)");
        String appointmentDate=scanner.next();
        if(patient.getPatientById(patient_id) && doctor.getDoctorById(doctor_id)){
            if(checkDoctorAvailability(doctor_id,appointmentDate,connection)){
                String appointmentQuery="INSERT INTO appointments(patient_id,doctor_id,appointment_date) VALUES(?,?,?)";
                try {
                    PreparedStatement preparedStatement= connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patient_id);
                    preparedStatement.setInt(2,doctor_id);
                    preparedStatement.setString(3,appointmentDate);
                    int effectedRows=preparedStatement.executeUpdate();
                    if(effectedRows>0){
                        System.out.println("appointment booked👍");
                    }
                    else {
                        System.out.println("failed to book appointment");
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("doctor is not available on this date!!");
            }
        }
        else {
            System.out.println("Either doctor or patient doesn't exist!!!");
        }
    }

    private static boolean checkDoctorAvailability(int doctorId, String appointmentDate , Connection connection) {
        String query="SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appointment_date=?";
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                return count == 0;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
