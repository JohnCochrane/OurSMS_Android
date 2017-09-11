package ulster.oursms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AppSelector extends AppCompatActivity {

    Button btnLogin, btnTagWriter, btnAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_selector);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnTagWriter = (Button) findViewById(R.id.btnTagWriter);
        btnAttendance = (Button) findViewById(R.id.btnAttendance);

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppSelector.this, Login.class);
                startActivity(intent);
            }
        });

        btnTagWriter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppSelector.this, TagWriter.class);
                startActivity(intent);
            }
        });

        btnAttendance.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppSelector.this, AttendanceTracker.class);
                startActivity(intent);
            }
        });

    }//end of onCreate

}//end of class
