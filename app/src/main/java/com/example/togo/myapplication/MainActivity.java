package com.example.togo.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static String ip;
    InputFilter[] filters = new InputFilter[1];
    private TextView textView;
    private EditText editText;
    private SmartM3 smartM3;
    private ProgressDialog progressDialog;

    //TODO popup message; test wordinsert; options activity; test pogressdialog and etc.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.textView);
        editText=(EditText)findViewById(R.id.ip_address);

        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart)
                            + source.subSequence(start, end)
                            + destTxt.substring(dend);
                    if (!resultingTxt
                            .matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i = 0; i < splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }

        };
        editText.setFilters(filters);
    }

    public void onClick(View view) {
        if (IPAddressValidator.validate(editText.getText().toString())) {
            smartM3 = new SmartM3();
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Connecting...");
            progressDialog.show();
            smartM3.execute();
            try {
                if(smartM3.get()){
                    progressDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, WordActivity.class);
                    startActivity(intent);
                }
                else {
                    progressDialog.dismiss();
                    textView.setText("Error with connection");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            textView.setText("IP address is incorrect");
        }
        /*textView.setText(editText.getText());
        try {
            smartM3=new SmartM3(editText.getText().toString());
        } catch (SmartSpaceException e) {
            //e.printStackTrace();
            //Log.e("SmartM3", e.toString());
        }
        finally {
            smartM3.leaveSmart();
        }*/
    }


    public void setTextView(String s){
        textView.setText(s);
    }

    public void exit(View view) {
        finish();
    }
}
