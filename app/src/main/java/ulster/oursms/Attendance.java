package ulster.oursms;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by John on 07/06/2017.
 */

public class Attendance
{
    private int id;
    private int TagID;
    private int StudentID;
    private int ClassroomID;
    private String Timestamp;


    public Attendance(int tagID, int studentID, int classroomID, String timestamp)
    {
        TagID = tagID;
        StudentID = studentID;
        ClassroomID = classroomID;
        Timestamp = timestamp;
    }

    public int getTagID() {
        return TagID;
    }

    public void setTagID(int tagID) {
        TagID = tagID;
    }


    public int getStudentID() {
        return StudentID;
    }

    public void setStudentID(int studentID) {
        StudentID = studentID;
    }

    public int getClassroomID() {
        return ClassroomID;
    }

    public void setClassroomID(int classroomID) {
        ClassroomID = classroomID;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}
