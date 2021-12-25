package com.example.drassit.ui.gas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.drassit.R;
import com.example.drassit.ui.model.MyLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FillGas extends AppCompatActivity {

    FirebaseAuth firebaseAuth;




    private Spinner spFillGasLocation;
    private FillGasAdapter fillGasAdapter;
    private  Calendar myCalendar;
    private EditText edFillGasTime;
    private EditText edFillGasVolume, edFillGasPrice;
    private Button btnFillGas;

    boolean skipOnChange = false;
    private float GAS_PRICE = 22000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_gas);


        DecimalFormat formatter = new DecimalFormat("#,###,###");

        btnFillGas = findViewById(R.id.btnFillGas);
        edFillGasVolume = findViewById(R.id.edFillGasVolume);
        edFillGasPrice = findViewById(R.id.edFillGasPrice);
        edFillGasTime = findViewById(R.id.edFillGasTime);
        spFillGasLocation = findViewById(R.id.spFillGasLocation);
        edFillGasPrice.setText(""+Float.parseFloat(edFillGasVolume.getText().toString())*GAS_PRICE);

        myCalendar = Calendar.getInstance();
        edFillGasTime.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(myCalendar.getTime()));
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                updateLabel();
            }
        };

        edFillGasTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FillGas.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        fillGasAdapter = new FillGasAdapter(this, R.layout.fill_gas_location_selected, getListLocation());
        spFillGasLocation.setAdapter(fillGasAdapter);
        spFillGasLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(FillGas.this, fillGasAdapter.getItem(i).getLocation(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edFillGasVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(charSequence.length()!=0){
//                    edFillGasPrice.setText(formatter.format(Double.parseDouble(charSequence.toString())*GAS_PRICE));
//                }
//                else {
//                    edFillGasPrice.setText("0");
//                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (skipOnChange)
                    return;

                skipOnChange = true;
                try {
                    if(edFillGasVolume.getText().toString().length()==0){
                        edFillGasPrice.setText("0");
                    }
                    else{
                        edFillGasPrice.setText(formatter.format(Double.parseDouble(edFillGasVolume.getText().toString())*GAS_PRICE));
                    }

                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    skipOnChange = false;
                }
            }
        });

        edFillGasPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (skipOnChange)
                    return;

                skipOnChange = true;
                try {
                    if(edFillGasPrice.getText().toString().length()==0){
                        edFillGasVolume.setText("0");
                    }
                    else{
                        edFillGasVolume.setText(Float.parseFloat(edFillGasPrice.getText().toString())/GAS_PRICE + "");
                    }


                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    skipOnChange = false;
                }
            }
        });


        btnFillGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillGas();
            }
        });
    }

    private void init(){


    }

    private List<MyLocation> getListLocation(){
        List<MyLocation> list =  new ArrayList<>();
        list.add(new MyLocation("1","Tram xang 1"));
        list.add(new MyLocation("2","Tram xang 2"));
        list.add(new MyLocation("3","Tram xang 3"));
        list.add(new MyLocation("4","Tram xang 4"));
        list.add(new MyLocation("5","Tram xang 5"));
        return list;
    }

    private void updateLabel(){
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.getDefault());

        edFillGasTime.setText(simpleDateFormat.format(myCalendar.getTime()));
    }

    private void fillGas(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.getIdToken(true);
    }
}