package HospitalManagementSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

    public class Doctor {
    private Connection connection;
    private Scanner scanner;

    public Doctor(Connection connection){
        this.connection=connection;
        this.scanner=scanner;
    }
        public void viewDoctors(){
            String query="select* from doctors";
            try{
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                ResultSet resultSet=preparedStatement.executeQuery();
                System.out.println("doctors data🗃️:");
                System.out.println("+-----------+------------------+------------------+");
                System.out.println("|doctor id  | Name             |Specialisation    |");
                System.out.println("+-----------+------------------+------------------+");
                while (resultSet.next()){
                    int id=resultSet.getInt("id");
                    String name=resultSet.getString("name");
                    String Specialisation=resultSet.getString("Specialisation");
                    System.out.printf("|%-11s|%-18s|%-18s|\n",id,name,Specialisation);
                    System.out.println("+-----------+------------------+------------------+");
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        public boolean getDoctorById(int id){
            String query = "SELECT * FROM doctors WHERE id= ?";
            try {
                PreparedStatement preparedStatement= connection.prepareStatement(query);
                preparedStatement.setInt(1,id);
                ResultSet resultSet=preparedStatement.executeQuery();
                return resultSet.next();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            return false;
        }

    }
