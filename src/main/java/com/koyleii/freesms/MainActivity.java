package com.koyleii.freesms;

import  androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import ClickSend.Api.SmsApi;
import ClickSend.ApiClient;
import ClickSend.ApiException;
import ClickSend.Model.SmsMessage;
import ClickSend.Model.SmsMessageCollection;

public class MainActivity extends AppCompatActivity {

    private Button btnSend;
    private EditText txtFrom, txtTo, txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //IGNORE POLICY
        if (Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        txtFrom = findViewById(R.id.txtFrom);
        txtTo = findViewById(R.id.txtTo);
        txtMessage = findViewById(R.id.txtMessage);
        btnSend = findViewById(R.id.btnSend);
        checkNetwork();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendSMS();
            }
        });
    }
    @SuppressLint("ResourceType")
    protected void checkNetwork(){

        if(haveNetworkConnection()){

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("You need an internet connection to run this program");
            builder.setTitle("INTERNET REQUIRED");
            builder.setCancelable(false);
            builder.setIcon(R.raw.icon);
            builder.setPositiveButton(
                    "RETRY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checkNetwork();
                        }
                    });
            builder.setNegativeButton(
                    "EXIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            System.exit(0);
                        }
                    });
            builder.show();
        }
    }
    private boolean haveNetworkConnection(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for(NetworkInfo ini : netInfo){
            if (ini.isConnected())
                haveConnectedWifi = true;
            if (ini.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ini.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedMobile || haveConnectedWifi;
    }
    protected void SendSMS(){
        String from = txtFrom.getText().toString();
        String to = txtTo.getText().toString();
        String message = txtMessage.getText().toString();

        ApiClient defaultClient = new ApiClient();
        defaultClient.setUsername("leiiqtqt16@gmail.com");
        defaultClient.setPassword("Tecno_0116");

        SmsApi smsApi = new SmsApi(defaultClient);

        SmsMessage smsMessage = new SmsMessage();
        smsMessage.to("+63"+to);
        smsMessage.body(message);
        smsMessage.setFrom(from);

        List<SmsMessage> smsMessageList = Arrays.asList(smsMessage);
        SmsMessageCollection smsMessageCollection = new SmsMessageCollection();
        smsMessageCollection.messages(smsMessageList);

        try{
            String res = smsApi.smsSendPost(smsMessageCollection);
            txtMessage.setText("");
            txtTo.setText("");
            txtFrom.setText("");
            res = res.toUpperCase();
            Toast.makeText(getApplicationContext(), "SENT SUCCESSFULLY", Toast.LENGTH_SHORT);
            if(res.contains("INSUFFICIENT")){
                Toast.makeText(getApplicationContext(), "INSUFFICIENT BALANCE", Toast.LENGTH_SHORT);
            }else{
                Toast.makeText(getApplicationContext(), "SENT SUCCESSFULLY", Toast.LENGTH_SHORT);
            }
        }catch (ApiException err){
            Toast.makeText(getApplicationContext(), "ERROR"+err, Toast.LENGTH_SHORT);
        }
    }
}