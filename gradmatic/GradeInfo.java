package gradmatic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GradeInfo {

    /* This method returns all the info given a particular grade ID */
    public static Grade getGrade(Statement s, int studentID, int subjectID, int periodID) {
        Grade tempGrade = new Grade();

        try {
            ResultSet rs = s.executeQuery("select * from Grades where studentID = " + studentID + " and subjectID = " + subjectID + " and periodID = " + periodID + ";");
            while (rs.next()) {
                tempGrade.quizScore = rs.getInt("quizScore");
                tempGrade.examScore = rs.getInt("examScore");
                tempGrade.activityScore = rs.getInt("activityScore");
                tempGrade.otherScore = rs.getInt("otherScore");
                tempGrade.studentPeriodGrade = rs.getFloat("studentPeriodGrade");
            }

            tempGrade.studentID = studentID;
            tempGrade.subjectID = subjectID;
            tempGrade.periodID = periodID;
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }

        return tempGrade;
    }
    
    /* This method creates a new grade to add to the database or updates an existing one */
    public static void createGrade(Statement s, int studentID, int subjectID, int periodID, int quizScore, int examScore, int activityScore, int otherScore, double studentPeriodGrade) {
        try {
            s.executeUpdate("insert or replace into Grades values (" + studentID + ", " + subjectID + ", " + periodID + ", " + quizScore
            + ", " + examScore + ", " + activityScore + ", " + otherScore + ", " + studentPeriodGrade + ")");
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
	}

    /* This method deletes all the info of a particular grade's record */
    public static void removeGrade(Statement s, int studentID, int subjectID, int periodID) {
        try {
            s.executeUpdate("delete from Grades where studentID = " + studentID + " and subjectID = " + subjectID + " and periodID = " + periodID + ";");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}

class Grade {
    int studentID;
    int subjectID;
    int periodID;
    int quizScore;
    int examScore;
    int activityScore;
    int otherScore;
    double studentPeriodGrade;
}
