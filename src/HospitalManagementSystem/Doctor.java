package HospitalManagementSystem;

import javax.print.Doc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Doctor {
    private static final Logger logger = Logger.getLogger(Doctor.class.getName());
    private Connection connection;

    public Doctor(Connection connection){
        this.connection= connection;

    }


    public void viewDoctors(){
        String query = "SELECT id, name, specialization FROM doctors";
        try{
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            ResultSet resultSet= preparedStatement.executeQuery();
            System.out.println("Doctors:");
            System.out.println("+------------+--------------------+------------------------------+");
            System.out.println("| Doctors Id | Name               | Specialization               |");
            System.out.println("+------------+--------------------+------------------------------+");
            while(resultSet.next()){
                int id= resultSet.getInt("id");
                String name= resultSet.getString("name");
                String specialization = resultSet.getString("specialization");

                System.out.printf("|%-12d|%-20s|%-30s|%n", id, name, specialization);
                System.out.println("+------------+--------------------+------------------------------+");


            }


        }catch (SQLException e){
            logger.log(Level.SEVERE, "An error occurred while connecting to the database", e);

        }
    }
    public boolean getDoctorById(int id){
        String query="SELECT * FROM doctors WHERE id = ?";
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

