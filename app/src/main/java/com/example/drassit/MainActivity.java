package com.example.drassit;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.drassit.ui.model.Account;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.FacebookException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    private static final int RC_SIGN_IN = 999;
    private Button btnGoogleLogin;
    private LoginButton btnFacebookLogin;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    List<Account> lstAccount;
    DatabaseReference _myAccountRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAllAccount();
        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(MainActivity.this);
        init();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("15739369722-c38lodi517q3ubcns5m0rt7jg93bk2kr.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        LoginManager.getInstance().logOut();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.signOut();
        }

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoolgle();
            }
        });

        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //signInFacebook();
            }
        });

        btnFacebookLogin.setReadPermissions("email", "public_profile");
        btnFacebookLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!= null){
                    updateUI(user);
                }
                else{
                    updateUI(null);
                }
            }
        };

        accessTokenTracker  = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    mAuth.signOut();
                }
            }
        };


    }
    List<String> accounts = new ArrayList<>();

    private List<String> getAllAccount() {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("Account");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String account =  ds.child("tokenId").getValue(String.class);
                    accounts.add(account);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
            }
        };
        usersRef.addListenerForSingleValueEvent(valueEventListener);

        return accounts;
    }

            private void init(){
        mCallbackManager = CallbackManager.Factory.create();
        btnGoogleLogin = findViewById(R.id.custom_google_signin_button);
        btnFacebookLogin = findViewById(R.id.custom_facebook_signin_button);


    }


    private void signInGoolgle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInGoogleWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            createIdToken(user);
//                            Intent intent = new Intent(MainActivity.this,DriverAssitant.class);
//                            startActivity(intent);
//                            finish();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInGoogleWithCredential:failure", task.getException());
                            updateUI(null);
                            Toast.makeText(MainActivity.this, "Google Loggin failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInFacebookWithCredential: Success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            createIdToken(user);


                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInFacebookWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Facebook Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user){
        if(user!=null){
            Intent intent = new Intent(MainActivity.this,DriverAssitant.class);
            startActivity(intent);
            finish();
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(authStateListener);
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        if(authStateListener!=null){
//            mAuth.removeAuthStateListener(authStateListener);
//        }
//    }

    private void createIdToken(FirebaseUser user){
        user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult result) {
                String idToken = result.getToken();
                getData();
                DatabaseReference userRef = database.getReference("Account");
                DatabaseReference  userRef_1 = userRef.child(UUID.randomUUID().toString());
               //getData();
            if(!CollectionUtils.isEmpty(lstAccount))
                for( Account acc: lstAccount){
                    if(acc.getTokenID().equals(idToken)){
                        updateUI(user);
                        return;
                    }
                }

                if(!CollectionUtils.isEmpty(accounts))
                    for( String id: accounts){
                        if(id.equals(idToken)){
                            updateUI(user);
                            return;
                        }
                    }

//                list id = getdata();
//                for(id in list):
//                if id==idToken;
//                    updateUI();
//                    return;
                //String id = myRef.getKey();
                DatabaseReference  tokenId = userRef_1.child("tokenId");
                tokenId.setValue(idToken);
                tokenId.push();





                //myRef.setValue(idToken);
                //Do whatever
                Log.d(TAG, "GetTokenResult result = " + idToken);
            }
        });
    }

    public void getData() {
        lstAccount = new ArrayList<>();
        _myAccountRef = database.getReference("Account");

        _myAccountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){

                    Account acc= data.getValue(Account.class);
                    lstAccount.add(acc);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }




        });
    }
}