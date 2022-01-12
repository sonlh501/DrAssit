package com.example.drassit.ui.notifications;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.drassit.R;
import com.example.drassit.ui.model.Account;
import com.google.android.gms.auth.api.credentials.IdToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                setData(null);
                finish();
            }
        });

    }

    public void init() {
        btn_Ok = findViewById(R.id.btn_ok);
        et_Phone = findViewById(R.id.et_phone);
        et_Address = findViewById(R.id.et_address);
        et_Card = findViewById(R.id.et_card);
    }



    public void setData(Account acc) {
        if (acc == null) {
            acc = new Account();
            acc.setPhone(et_Phone.getText().toString());
            acc.setAddress(et_Address.getText().toString());
            acc.setCard(et_Card.getText().toString());


            _myRef = mDatabase.getReference("Account");
            DatabaseReference accountRef = _myRef.push();
            //accountRef.setValue(idToken);

            DatabaseReference _Ref1 = _myRef.child("3f263f11-2317-4052-ac47-eeb604018e36");

            //DatabaseReference _Ref1_Name = _Ref1.child("Name");
            DatabaseReference _Ref1_Phone = _Ref1.child("Phone");
            DatabaseReference _Ref1_Card = _Ref1.child("Identity Card");
            DatabaseReference _Ref1_Address = _Ref1.child("Address");

            updateUser(acc);

            Account accForInsertFirebase = acc;
            _Ref1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()) {
                            _Ref1.setValue(accForInsertFirebase);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "onCancelled", databaseError.toException());
                }
            });

            //_myRef.child(acc.getId()).setValue(acc.getTokenID());
        }


    }

    private void updateUser(Account acc) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference accountRef = mDatabase.getReference("Account").child(user.getUid());

        accountRef.setValue(acc);
        accountRef.push();

    }
}