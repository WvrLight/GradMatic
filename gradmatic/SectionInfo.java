package gradmatic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SectionInfo {

    /* This method returns all the info given a particular section ID */
    public static Section getSection(Statement s, int sectionID) {
        Section tempSection = new Section();

        try {
            ResultSet rs = s.executeQuery("select * from Sections where sectionID = " + sectionID + ";");
            while (rs.next()) {
                tempSection.sectionName = rs.getString("sectionName");
            }

            tempSection.sectionID = sectionID;
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }

        return tempSection;
    }
    
    /* This method creates a new section to add to the database or updates an existing one */
    public static void createSection(Statement s, int sectionID, String sectionName) {
        try {
            s.executeUpdate("insert or replace into Sections (sectionID, sectionName) values (" + sectionID + ", '" + sectionName + "')");
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
	}

    /* This method deletes all the info of a particular section ID's record */
    public static void removeSection(Statement s, int sectionID) {
        try {
            s.executeUpdate("delete from Sections where sectionID ='" + sectionID + "';");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}

class Section {
    int sectionID;
    String sectionName;
}
