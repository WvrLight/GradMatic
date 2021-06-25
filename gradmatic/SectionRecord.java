package gradmatic;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

/**
 * SECTION RECORD
 * 
 * To use, simply add the line: SectionRecord <name> = new SectionRecord(<studentID>);
 * 
 * This will store all relevant information for the section:
 * - Section name
 * - List of students within the section
 * - All averages per period of each student
 */
public class SectionRecord {
    Section section = new Section();
    ArrayList<StudentGrades> studentgrades = new ArrayList<StudentGrades>();

    // Test
    // public static void main(String[] args) throws Exception {
    //     SectionRecord test = new SectionRecord(10);
    //     System.out.println(test.section.sectionName);
    //     System.out.println(test.studentgrades.get(0).student.studentLN);
    //     System.out.println(test.studentgrades.get(0).averages.get(0));
    //     System.out.println(test.studentgrades.get(0).averages.get(1));
    //     System.out.println(test.studentgrades.get(0).averages.get(2));
    //     System.out.println(test.studentgrades.get(0).averages.get(3));
    // }

    public SectionRecord(int sectionID) {
        Connection connection = null;
        try {
            ResultSet rs;

            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:gm.db");
            Statement s = connection.createStatement();
            s.setQueryTimeout(5);

            // Store all section info
            section = SectionInfo.getSection(s, sectionID);

            // Students
            ArrayList<Integer> ids = new ArrayList<Integer>();
            rs = s.executeQuery("select studentID from Students where sectionID = " + sectionID);
            while (rs.next()) {
                ids.add(rs.getInt("studentID"));
            }
            for (int i = 0; i < ids.size(); i++) {
                StudentGrades temp = new StudentGrades();
                temp.student = StudentInfo.getStudent(s, ids.get(i));
                studentgrades.add(temp);
            }

            // Grade Averages
            PreparedStatement ps = connection.prepareStatement("select studentPeriodGrade from Grades where studentID = ? and periodID = ?");
            for (int i = 0; i < studentgrades.size(); i++) {
                ps.setString(1, "" + studentgrades.get(i).student.studentID);
                for (int j = 1; j <= 4; j++) {
                    ps.setString(2, "" + j);
                    rs = ps.executeQuery();
                    double tempAve = 0;
                    int AveCount = 0;
                    while (rs.next()) {
                        tempAve += rs.getDouble("studentPeriodGrade");
                        AveCount++;
                    }
                    studentgrades.get(i).averages.add(tempAve / AveCount);
                }
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
}

class StudentGrades {
    Student student = new Student();
    ArrayList<Double> averages = new ArrayList<Double>();
}