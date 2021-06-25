package gradmatic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SubjectInfo {

    /* This method returns all the info given a particular subject ID */
    public static Subject getSubject(Statement s, int subjectID) {
        Subject tempSubject = new Subject();

        try {
            ResultSet rs = s.executeQuery("select * from Subjects where subjectID ='" + subjectID + "';");
            while (rs.next()) {
                tempSubject.subjectName = rs.getString("subjectName");
                tempSubject.quizWeight = rs.getFloat("quizWeight");
                tempSubject.examWeight = rs.getFloat("examWeight");
                tempSubject.activityWeight = rs.getFloat("activityWeight");
                tempSubject.otherWeight = rs.getFloat("otherWeight");
            }

            tempSubject.subjectID = subjectID;
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }

        return tempSubject;
    }
    
    /* This method creates a new subject to add to the database or updates an existing one */
    public static void createSubject(Statement s, int subjectID, String subjectName, double quizWeight, double examWeight, double activityWeight, double otherWeight) {
        try {
            s.executeUpdate("insert or replace into Subjects values (" + subjectID + ", '" + subjectName + "', " + quizWeight + ", " + examWeight + ", " + activityWeight + ", " + otherWeight + ")");
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
	}

    /* This method deletes all the info of a particular subject ID's record */
    public static void removeSubject(Statement s, int subjectID) {
        try {
            s.executeUpdate("delete from Subjects where subjectID ='" + subjectID + "';");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static Subject getSubjectTotals(Statement s, int subjectID, int periodID) {
        Subject tempSubject = new Subject();

        try {
            ResultSet rs = s.executeQuery("select * from Subject_Totals where subjectID = " + subjectID + " and periodID = " + periodID + ";");
            while (rs.next()) {
                tempSubject.quizTotal = rs.getInt("quizTotal");
                tempSubject.examTotal = rs.getInt("examTotal");
                tempSubject.activityTotal = rs.getInt("activityTotal");
                tempSubject.otherTotal = rs.getInt("otherTotal");
            }

            tempSubject.subjectID = subjectID;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }

        return tempSubject;
    }
    
    public static void setSubjectTotals(Statement s, int subjectID, int periodID, int quizTotal, int examTotal, int activityTotal, int otherTotal) {
        try {
            s.executeUpdate("insert or replace into Subject_Totals values (" + subjectID + ", " + periodID + ", " + quizTotal + ", " + examTotal + ", " + activityTotal + ", " + otherTotal + ")");
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}

class Subject {
    int subjectID;
    String subjectName;
    double quizWeight;
    double examWeight;
    double activityWeight;
    double otherWeight;
    int quizTotal;
    int examTotal;
    int activityTotal;
    int otherTotal;
}
