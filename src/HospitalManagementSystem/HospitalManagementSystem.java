package HospitalManagementSystem;
import java.util.logging.Logger;
import java.util.logging.Level;


import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final Logger logger = Logger.getLogger(HospitalManagementSystem.class.getName());
    private static final String url= "jdbc:mysql://localhost:3306/hospital";
    private static final String username= "root";
    private static final String password= "Mysqlpass123@";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            logger.log(Level.SEVERE, "An error occurred while connecting to the database", e);
        }
        Scanner scanner= new Scanner(System.in);
        try{
            Connection connection= DriverManager.getConnection(url, username,password);
            Patient patient= new Patient(connection, scanner);
            Doctor doctor= new Doctor(connection);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice: ");
                int choice= scanner.nextInt();

                switch (choice){
                    case 1:
                        // Add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        // View Patient
                        patient.viewPatients();
                        System.out.println();
                        break;

                    case 3:
                        // View Doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;

                    case 4:
                        //Book Appointments
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;

                    case 5:
                        try {
                            connection.close();
                            logger.info("Database connection closed. Exiting system...");
                        } catch (SQLException e) {
                            logger.log(Level.SEVERE, "Error while closing the database connection", e);
                        }
                        return;
                    default:
                        System.out.println("Enter valid Choice.");
                }

            }

        }catch (SQLException e){
            logger.log(Level.SEVERE, "An error occurred while connecting to the database", e);
        }
    }
    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.print("Enter Patient Id: ");
        int patientId= scanner.nextInt();
        System.out.print("Enter Doctor Id: ");
        int doctorId= scanner.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate= scanner.next();
        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if (checkDoctorAvailability(doctorId,appointmentDate,connection)){
                String appointmentQuery= "INSERT INTO appointments(patient_id, doctor_id, appointment_date) Values(?,?,?)";
                try{
                    PreparedStatement preparedStatement= connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected= preparedStatement.executeUpdate();
                    if (rowsAffected>0){
                        System.out.println("Appointment Booked");
                    }else{
                        System.out.println("Failed to Book Appointment.");
                    }

                }catch (SQLException e){
                    logger.log(Level.SEVERE, "An error occurred while connecting to the database", e);
                }

            }else {
                System.out.println("Doctor not available on this date");
            }

        }else {
            System.out.println("Either doctor or patient doesn't exist.");

        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){

        String query= "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date= ?";
        try{

            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if (count==0){
                    return true;
                }else{
                    return false;
                }
            }

        }catch (SQLException e){
            logger.log(Level.SEVERE, "An error occurred while connecting to the database", e);
        }
        return false;
    }

}
