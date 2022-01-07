package com.example.drassit.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.drassit.R;
import com.example.drassit.ui.model.Account;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NofiticationsFragment_EditProfile extends AppCompatActivity {


    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    Button btn_Ok;
    EditText et_Phone, et_Address, et_Card;
    Account acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nofitications_fragment_edit_profile);


        init();
//        getData();
        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void init() {
        btn_Ok = findViewById(R.id.btn_ok);
        et_Phone = findViewById(R.id.et_phone);
        et_Phone = findViewById(R.id.et_address);
        et_Phone = findViewById(R.id.et_card);
    }


    public void getData() {

        Bundle bundle = getIntent().getExtras();
        if (acc == null) {
            acc = new Account();
        }
        acc = (Account) bundle.get("Account");
        et_Phone.setText(acc.getPhone());
        et_Address.setText(acc.getAddress());
        et_Card.setText(acc.getCard());
    }


    public void setData(Account acc) {
        if (acc == null) {
            acc = new Account();
            acc.setPhone(et_Phone.getText().toString());
            acc.setAddress(et_Address.getText().toString());
            acc.setCard(et_Card.getText().toString());
            _myRef = mDatabase.getReference("Account");
            _myRef.child(acc.getId()).setValue(acc.getTokenID());
        }


    }
}