package ulster.oursms;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AttendanceTracker extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    ConnectionClass connectionClass;

    EditText txtClassroomID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_tracker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txtClassroomID = (EditText) findViewById(R.id.txtClassroomID);

        connectionClass = new ConnectionClass(); //the class file

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (hasNfc()) {
            Toast.makeText(this, "NFC is available.", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "NFC is not available on this device. This" +
                    "application may not work correctly.", Toast.LENGTH_LONG).show();
        }

    }


    boolean hasNfc() {
        boolean hasFeature =
                getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
        boolean isEnabled = NfcAdapter.getDefaultAdapter(this).isEnabled();
        return hasFeature && isEnabled;


    }//end of on create



    boolean isNfcIntent(Intent intent) {
        return intent.hasExtra(NfcAdapter.EXTRA_TAG);
    }//end of isNfcIntent


    //this reads tag*******************************
    public NdefMessage getNdefMessageFromIntent(Intent intent) {
        NdefMessage ndefMessage = null;
        Parcelable[] extra =
                intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (extra != null && extra.length > 0) {
            ndefMessage = (NdefMessage) extra[0];
        }
        return ndefMessage;
    }//end of getNdefFromIntent

    public NdefRecord getFirstNdefRecord(NdefMessage ndefMessage) {
        NdefRecord ndefRecord = null;
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if (ndefRecords != null && ndefRecords.length > 0) {
            ndefRecord = ndefRecords[0];
        }
        return ndefRecord;
    }//end of NDef

    private static java.sql.Timestamp getCurrentTime() {
        java.util.Date today = new java.util.Date();

        java.util.Date date = new Date();
        Timestamp param = new java.sql.Timestamp(date.getTime());

        return param;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        if (isNfcIntent(intent)) {
            NdefMessage ndefMsg = getNdefMessageFromIntent(intent);
            if (ndefMsg != null) {
                NdefRecord ndefRecord = getFirstNdefRecord(ndefMsg);
                if (ndefRecord != null) {
                    boolean isTextRecord =
                            isNdefRecordOfTnfAndRdt(ndefRecord,
                                    NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT);
                    if (isTextRecord) {
                        String tagContent = getTextFromNdefRecord(ndefRecord);

                        String S = tagContent;

                        String[] temp;
                        String delimiter = " ";

                        temp = S.split(delimiter);


//                        studentID.setText(temp[0]);
//                        tagID.setText(temp[1]);
//                        tagType.setText(temp[2]);

                        //try
                        try {

                            Date date = new Date();
                            int intTagID, intStudentID, intClassroomID;
                            String strTagType;


                            intStudentID = Integer.valueOf(temp[0]);
                            intTagID = Integer.valueOf(temp[1]);
                            intClassroomID = Integer.valueOf(txtClassroomID.getText().toString());


                            Connection conn = connectionClass.CONN(); // connect to database
                            // UPDATES Attendance
                            String query = "INSERT INTO [OurSMS].[dbo].[Attendance] ("
                                    + " [TagId],"
                                    + " [StudentID],"
                                    + " [ClassroomID],"
                                    + " [Timestamp]) VALUES ("
                                    + "?,?,?,?)";


                            java.util.Date dt = new java.util.Date();

                            java.text.SimpleDateFormat sdf =
                                    new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            String currentTime = sdf.format(dt);

                            PreparedStatement st = conn.prepareStatement(query);
                            st.setInt(1, intTagID);
                            st.setInt(2, intStudentID);
                            st.setInt(3, intClassroomID);
                            st.setString(4, currentTime);

                            st.executeUpdate();
                            if(st.executeUpdate() == 1){
                                Toast.makeText(this, String.format("Attendance Successful"), Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(this, String.format("Attendance Failed"), Toast.LENGTH_SHORT).show();
                            }
                            st.close();
                        } catch (SQLException ex) {
                            //isSuccess = false;
                            Toast.makeText(this, String.format(ex.getMessage()), Toast.LENGTH_LONG).show();
                        } //end of catch
                        catch (Exception ex) {
                            //isSuccess = false;
                            Toast.makeText(this, String.format(ex.getMessage()), Toast.LENGTH_LONG).show();
                        } //end of catch

                        //Toast.makeText(this, String.format("Content: %s", tagContent), Toast.LENGTH_LONG).show();


                    } else {
                        Toast.makeText(this, "Record is not Text formatted.",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "No Ndef record found.",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "No Ndef message found.",
                        Toast.LENGTH_LONG).show();
            }//end of if
        }//end of isNfcIntent
    }//end of onNewIntent


    public boolean isNdefRecordOfTnfAndRdt(NdefRecord ndefRecord, short
            tnf, byte[] rdt) {
        return ndefRecord.getTnf() == tnf && Arrays.equals(ndefRecord.getType(), rdt);
    }//end of isNdef

    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1, payload.length -
                    languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }//end of getTextfrom

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, AttendanceTracker.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent,
                intentFilter, null);
    }//end of

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }//end of

}//end of class
