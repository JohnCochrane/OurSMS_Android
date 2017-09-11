package ulster.oursms;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.R.layout.simple_list_item_1;

public class Attend extends Activity {

    ConnectionClass connectionClass;
    private CustomAdapter adapter;
    ListView lstpro;
    public ArrayList<Attendance> CustomLIstViewValueArr = new ArrayList<Attendance>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);
        lstpro= (ListView) findViewById(R.id.lstpro);

        lstpro.setAdapter(new ArrayAdapter<Attendance>(this, android.R.layout.simple_list_item_1, CustomLIstViewValueArr));


        FilList fil = new FilList();
        fil.execute();


    }//end of oncreate


    public class FilList extends AsyncTask<String,String ,String > {

        String z = "";
        Boolean isSuccess = false;
        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();

        @Override
        protected void onPostExecute(String s) {
            Toast t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
            t.show();

        }

        //
        @Override
        protected String doInBackground(String... params) {

            try {
                Connection conn = connectionClass.CONN(); // connect to database

                if (conn == null) {
                    z = "Check Your Internet Access!";
                } else {
                    String query = "Select * from [OurSMS].[dbo].[Attendance]'";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        z = "login successful!";
                        CustomLIstViewValueArr.add(new Attendance(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4)));
                        isSuccess = true;



                        conn.close();
                    }
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage();
            } //end of catch
            return z;
        }//end of doInBackGround
//
////        @Override
////        protected String doInBackground(String... params){
////            try{
////                Connection conn = connectionClass.CONN(); // connect to database
////                if (conn == null) {
////                    z = "Check Your Internet Access!";
////                    isSuccess = false;
////                    System.out.println(z.toString());
////                } else {
////                    String query = "Select * from [OurSMS].[dbo].[Attendance]";
////                    Statement stmt = conn.createStatement();
////                    ResultSet rs = stmt.executeQuery(query);
////
////                    while(rs.next()){
////                        CustomLIstViewValueArr.add(new Attendance(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4)));
////                    }
////                    z="Success";
////                    isSuccess = true;
////                }
////            }catch(SQLException ex){
////                z = (ex.getMessage());
////            }
////            return z;
////        }//do in background
//    }//end of fil
    }
}//end of class
