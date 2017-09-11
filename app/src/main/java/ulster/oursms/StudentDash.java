package ulster.oursms;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class StudentDash extends AppCompatActivity {

    ConnectionClass connectionClass = new ConnectionClass();
    Button btnViewAttend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dash);
        btnViewAttend = (Button) findViewById(R.id.btnViewAttend);

        btnViewAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDash.this, Attend.class);
                startActivity(intent);
            }//end of onCLick
        });//end of button

        }//end of onCreate

}//end of class







//    public void displayData()
//    {
//        String z = "";
//        Boolean isSuccess = false;
//        String query = "Select * From [OurSMS].[dbo].[Attendance]";
//
//        List<Attendance> attendanceList = new ArrayList<Attendance>();
//
//        try {
//
//            Connection conn = connectionClass.CONN();
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery(query);
//            ResultSetMetaData rsmd = rs.getMetaData();
//
//            StringBuffer buffer = new StringBuffer();
//            while (rs.next()) {
//
//
////                Attendance att = new Attendance();
////                att.setId(rs.getInt(0));
////                att.setTagID(rs.getInt(1));
////                att.setStudentID(rs.getInt(2));
////                att.setClassroomID(rs.getInt(3));
////                att.setTimestamp(rs.getString(4));
////
////                attendanceList.add(att);
////                //assign stuff to the table
////
////                z = "login successful!";
////                isSuccess = true;
//
//
//            }
//            conn.close();
//
//
//        }
//        catch(SQLException ex)
//        {
//            //error
//        }



