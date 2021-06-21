package gradmatic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentInfo {

    /* This method returns all the info given a particular student ID */
    public static Student getStudent(Statement s, int studentID) {
        Student tempStudent = new Student();

        try {
            ResultSet rs = s.executeQuery("select * from Students where studentID = " + studentID + ";");
            while (rs.next()) {
                tempStudent.studentLN = rs.getString("studentLN");
                tempStudent.studentFN = rs.getString("studentFN");
                tempStudent.studentMI = rs.getString("studentMI");
                tempStudent.studentGWA = rs.getFloat("studentGWA");
                tempStudent.sectionID = rs.getInt("sectionID");
            }

            tempStudent.studentID = studentID;
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }

        return tempStudent;
    }
    
    /* This method creates a new student to add to the database or updates an existing one */
    public static void createStudent(Statement s, int studentID, String studentLN, String studentFN, String studentMI, int sectionID) {
        try {
            s.executeUpdate("insert or replace into Students (studentID, studentLN, studentFN, studentMI, studentGWA, sectionID) values (" + studentID + ", '" + studentLN + "', '" + studentFN + "', '" + studentMI + "', 0, " + sectionID + ")");
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
	}

    /* This method deletes all the info of a particular student ID's record */
    public static void removeStudent(Statement s, int studentID) {
        try {
            s.executeUpdate("delete from Students where studentID = " + studentID + ";");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    
    /** IMPORTANT! THIS IS AN EXAMPLE ON HOW TO USE THESE METHODS IN THE UI 
    *   Most of this stuff is for establishing a connection to the database
    *   There is an editable area located below where methods that retrieve and store data from the database can be put
    */    
	public static void main(String[] args) {
		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:gm.db");
			Statement s = connection.createStatement();
			s.setQueryTimeout(5);

			// EDITABLE AREA

            // sample method of creating a student
            createStudent(s, 122, "asd", "isaac", "s", 1);

            // sample method of updating a student
            // updateStudent(s, 122, 121, "a", "kevin", "m", 0, 2);

            // sample use of getting created student
            // Student test = getStudent(s, 122);
            // System.out.println(test.studentID);
            // System.out.println(test.studentLN);
            // System.out.println(test.studentFN);
            // System.out.println(test.studentMI);

            // sample use of removing a student
            // removeStudent(s, 122);

            // END OF EDITABLE AREA
		}
		catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
		}
		finally {
			try {
			    if (connection != null)
				    connection.close();
			}
			catch (SQLException e) {
			    // connection close failed.
			    System.err.println(e.getMessage());
			}
		}
	}
}

class Student {
    int studentID;
    String studentLN;
    String studentFN;
    String studentMI;
    float studentGWA;
    int sectionID;
}
