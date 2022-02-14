package com.example.birds_of_a_feather_team_20;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GoogleLogin extends AppCompatActivity {
    GoogleSignInClient signInClient;
    static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        //Create sign-in object to request basic account information
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        signInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        //Check to see if there is already an account logged in to the app
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        //TODO: Handling for what happens if the account is already signed in (possibly skip this screen).

        super.onStart();
    }

    public void onLoginClicked(View view){
        Log.e("Click Listener", "Clicked on login button");
        signIn();
    }

    private void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //Get result from the signIn() method
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            //Update greeting
            TextView greeting = (TextView) findViewById(R.id.signin_instructions);
            greeting.setText("Welcome, " + account.getGivenName() + "!");

        } catch(ApiException e){
            Log.e("Sign in failure", "Status code: " + e.getStatusCode());
        }
    }
}