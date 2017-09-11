package ulster.oursms;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity {

    private NfcAdapter nfcAdapter;

    ConnectionClass connectionClass;
    //Declaring connection variables


    EditText edtuserid, edtpass;
    Button btnlogin;
    ProgressBar pbbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        connectionClass = new ConnectionClass(); //the class file
        edtuserid = (EditText) findViewById(R.id.edtuserid);//names of the layout controls
        edtpass = (EditText) findViewById(R.id.edtpass);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        //setting up the function when button btnWrite is clicked
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();// this is the Asynctask
                doLogin.execute("");

            }
        });


    }//end of onCreate

    public class DoLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;


        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();


        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(Login.this, r, Toast.LENGTH_SHORT).show();

            if (isSuccess) {
                Intent i = new Intent(Login.this, StudentDash.class);
                startActivity(i);
                finish();
            }

        }//end of doLogin

        @Override
        protected String doInBackground(String... params) {


            if (userid.trim().equals("") || password.trim().equals("")) {
                z = "Please check Username and Password";
            } else {
                try {
                    Connection conn = connectionClass.CONN(); // connect to database
                    if (conn == null) {
                        z = "Check Your Internet Access!";
                    } else {
                        String query = "Select * from [OurSMS].[dbo].[User] where username='" + userid + "' and password='" + password + "'";
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            z = "login successful!";
                            isSuccess = true;
                            conn.close();
                        } else {
                            z = "Invalid credentials!";
                            isSuccess = false;
                        }//end of elseif
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = ex.getMessage();
                } //end of catch
            }//end of nested if
            return z;
        }//end of doInBackGround
    }

}//end of class
