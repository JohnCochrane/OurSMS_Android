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
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Locale;

public class TagWriter extends AppCompatActivity {

    private NfcAdapter nfcAdapter;

    //declare layout controls
    EditText txtName, txtStudentID, txtTagType, txtTagID;
    Button btnWriteToTag, btnReadTag;


    String strHoldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_writer);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        txtStudentID = (EditText) findViewById(R.id.txtStudentID);
        txtTagID = (EditText) findViewById(R.id.txtTagID);
        txtTagType = (EditText) findViewById(R.id.txtTagType);

        btnWriteToTag = (Button) findViewById(R.id.btnWrite);
        btnReadTag = (Button) findViewById(R.id.btnReadTag);

        btnReadTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(TagWriter.this, TagReader.class);
                startActivity(i);
            }//end of onClick View v
        });

        btnWriteToTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtStudentID == null) {
                    Toast.makeText(TagWriter.this, "Name is empty", Toast.LENGTH_SHORT).show();
                } else {
                    strHoldName = txtStudentID.getText().toString() + " " + txtTagID.getText().toString() + " " + txtTagType.getText().toString();
                    Toast.makeText(TagWriter.this, "Text is saved", Toast.LENGTH_SHORT).show();
                }//end of else if


//                DoLogin doLogin = new DoLogin();// this is the Asynctask
//                doLogin.execute("");

            }
        });

        if (hasNfc()) {
            Toast.makeText(this, "NFC is available.", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "NFC is not available on this device. This" +
                    "application may not work correctly.", Toast.LENGTH_LONG).show();
        }

    }//end of onCreate

    boolean hasNfc() {
        boolean hasFeature =
                getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
        boolean isEnabled = NfcAdapter.getDefaultAdapter(this).isEnabled();
        return hasFeature && isEnabled;


    }//end of on create

    private boolean formatTag(Tag tag, NdefMessage ndefMessage) {
        try {
            NdefFormatable ndefFormat = NdefFormatable.get(tag);
            if (ndefFormat != null) {
                ndefFormat.connect();
                ndefFormat.format(ndefMessage);
                ndefFormat.close();
                return true;
            }
        } catch (Exception e) {
            Log.e("formatTag", e.getMessage());
        }
        return false;
    }

    private boolean writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        try {
            if (tag != null) {
                Ndef ndef = Ndef.get(tag);
                if (ndef == null) {
                    return formatTag(tag, ndefMessage);
                } else {
                    ndef.connect();
                    if (ndef.isWritable()) {
                        ndef.writeNdefMessage(ndefMessage);
                        ndef.close();
                        return true;
                    }
                    ndef.close();
                }
            }
        } catch (Exception e) {
            Log.e("formatTag", e.getMessage());
        }
        return false;
    }//end of write

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
        Intent intent = new Intent(this,
                TagWriter.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);
    }//end of

    public NdefRecord createTextRecord(String content) {
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");
            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 +
                    languageSize + textLength);
            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);
            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                    NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
        } catch (UnsupportedEncodingException e) {
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }//end of create

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }//end of

    protected void onNewIntent(Intent intent) {
        try {
            if (isNfcIntent(intent)) {
                NdefRecord ndefRecord = createTextRecord(strHoldName);
                NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] {
                        ndefRecord });
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                boolean writeResult = writeNdefMessage(tag, ndefMessage);
                if (writeResult) {
                    Toast.makeText(this, "Tag written!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Tag write failed!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("onNewIntent", e.getMessage());
        }
    }//end of

}//end of class
