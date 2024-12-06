package HospitalManagementSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.Level;


public class Patient {
    private static final Logger logger = Logger.getLogger(Patient.class.getName());
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        this.connection= connection;
        this.scanner= scanner;
    }

    public void addPatient(){
        System.out.print("Enter patient name:");
        String name= scanner.next();
        System.out.print("Enter patient age:");
        int age= scanner.nextInt();
        System.out.print("Enter patient gender:");
        String gender= scanner.next();


        try{
            String query= "INSERT INTO patients(name, age, gender) Values(?, ?, ?)";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient Added Successfully!");
            }else{
                System.out.println("Failed to add Patient.");
            }


        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An error occurred while connecting to the database", e);
        }

    }
public void viewPatients(){
    String query = "SELECT id, name, age, gender FROM patients";
        try{
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            ResultSet resultSet= preparedStatement.executeQuery();
            // Header
            System.out.println("Patients:");
            System.out.println("+------------+--------------------+------------+------------------+");
            System.out.println("| Patient Id | Name               | Age        | Gender           |");
            System.out.println("+------------+--------------------+------------+------------------+");

            //loop through the result set and print each patient's data
            while(resultSet.next()){
                int id= resultSet.getInt("id");
                String name= resultSet.getString("name");
                int age= resultSet.getInt("age");
                String gender= resultSet.getString("gender");

                System.out.printf("| %-12d | %-18s | %-10d | %-16s |%n", id, name, age, gender);
            }
// Print patient data
            System.out.printf("|%-12s|%-20s|%-12s|%-17s|%n", "Patient Id", "Name", "Age", "Gender");
            System.out.println("+------------+--------------------+------------+------------------+");


        }catch (SQLException e){
            logger.log(Level.SEVERE, "An error occurred while connecting to the database", e);

        }
}
public boolean getPatientById(int id){
        String query="SELECT * FROM patients WHERE id = ?";
        try{
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }

        }catch (SQLException e){
            logger.log(Level.SEVERE, "An error occurred while connecting to the database", e);
        }
        return false;
}


}
