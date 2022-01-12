package com.example.drassit.ui.notifications;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.drassit.MainActivity;
import com.example.drassit.databinding.FragmentNotificationsBinding;
import com.example.drassit.ui.model.Account;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;

    Button btn_logout;
    Button btn_EditProfile;
    TextView tv_phone, tv_address, tv_card, tv_NameAccount;
    ImageView iv_Pic;
    String url;


    FirebaseAuth mAuth;
    DatabaseReference myRef;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textNotifications;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        mAuth = FirebaseAuth.getInstance();

        getData();
        btn_logout = binding.btnLogout;
        btn_EditProfile = binding.btnEditProfile;
        tv_phone =  binding.tvPhone;
        tv_address =  binding.tvAddress;
        tv_card =  binding.tvCard;
        tv_NameAccount =  binding.tvNameAccount;
        iv_Pic = binding.ivPic;

        binding.ivPic.setImageBitmap(null);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser account = mAuth.getCurrentUser();
        if (account != null){

            //Objects.requireNonNull() prevents getPhotoUrl() from returning a NullPointerException

            tv_NameAccount.setText("" +account.getDisplayName());
            // Display name from google or facebook account

            String personImage = Objects.requireNonNull(account.getPhotoUrl()).toString();
            ImageView userImage = binding.ivPic;
            Glide.with(this).load(personImage).into(userImage);
            //Display image from Google or Facebook account
        }





        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);


            }
        });

        btn_EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NofiticationsFragment_EditProfile.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getData()
    {
//        Bundle bundle = this.getActivity().getIntent().getExtras();
//        if(bundle == null) return;
//        Account account = (Account) bundle.get("Account");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Account/"+ Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Account account = dataSnapshot.getValue(Account.class);
                    tv_phone.setText(account.getPhone());
                    tv_address.setText(account.getAddress());
                    tv_card.setText(account.getCard());
                }
    catch (Exception e){
                    return;
}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }


    public void setData(Account account){
        if(account == null){
            account = new Account();
        }
        account.setAddress(tv_address.getText().toString());
        account.setPhone(tv_phone.getText().toString());
        account.setCard(tv_card.getText().toString());

    }

}