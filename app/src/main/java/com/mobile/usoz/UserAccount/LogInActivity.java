package com.mobile.usoz.UserAccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mobile.usoz.R;
import com.mobile.usoz.UserActivities.UserProfileAcitivity;

public class LogInActivity extends AppCompatActivity  implements View.OnClickListener {
    private Button createAccountButton, logInButton;
    private static final int RC_SIGN_IN = 123;
    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 1;
    private CallbackManager mCallbackManager;
    private static final String TAG = "FacebookLogin";
    private FirebaseAuth mAuth;
    private TextView loginTextView, passwordTextView;
    private ProgressBar progressBar;
    private LoginButton facebookLoginButton;
    private SignInButton googleLogInButton;

    private Toolbar toolbar;
    // Configure Google Sign In

    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        createAccountButton = findViewById(R.id.createAccountButton);
        logInButton = findViewById(R.id.logInButton);
        loginTextView = findViewById(R.id.loginTextView);
        passwordTextView = findViewById(R.id.PswdTextView) ;
        progressBar = findViewById(R.id.progressBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createAccountButton.setOnClickListener(this);
        logInButton.setOnClickListener(this);



        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton = findViewById(R.id.fbLogin_button);
        prepareFacebookLogin();

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleLogInButton = findViewById(R.id.googleSignInButton);
        googleLogInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v!=null) {
            switch (v.getId()) {
                case R.id.logInButton:
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (NullPointerException e) {}
                    signInEmailPassword(loginTextView.getText().toString(), passwordTextView.getText().toString());
                    break;
                case R.id.createAccountButton:
                    openCreateAccountActivity();
                    break;
                case R.id.googleSignInButton:
                    googleSignIn();
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI(currentUser);

        }
    }

    private void updateUI(FirebaseUser currentUser) {
        Toast.makeText(LogInActivity.this, "Zalogowano!", Toast.LENGTH_LONG).show();

        Intent mainIntent = new Intent(LogInActivity.this, UserProfileAcitivity.class);
        startActivity(mainIntent);
        finish();
    }


    private void openCreateAccountActivity(){
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

//    private void createSignInIntent(){
//        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build(),
//                new AuthUI.IdpConfig.FacebookBuilder().build());
//
//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .build(),
//                RC_SIGN_IN);
//    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);


        //email/psswd login
//        if (requestCode == RC_SIGN_IN) {
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//
//            if (resultCode == RESULT_OK) {
//                // Successfully signed in
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                // ...
//            } else {
//                // Sign in failed. If response is null the user canceled the
//                // sign-in flow using the back button. Otherwise check
//                // response.getError().getErrorCode() and handle the error.
//                // ...
//            }
//        }
        //google login
        if(requestCode == GOOGLE_SIGN_IN_REQUEST_CODE){
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }



    // ------------------------------------------- FACEBOOK LOGIN ----------------------------------------------------

    private void prepareFacebookLogin(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        facebookLoginButton.setReadPermissions("email", "public_profile");
        facebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
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
                            // Logowanie powiodlo sie
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // Jesli logowanie nie uda sie
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LogInActivity.this, "Błąd podczas logowania!",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }




    // ------------------------------------------- GOOGLE LOGIN ----------------------------------------------------

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
    }



    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Logowanie do Google service powiodło się, pokaż UI
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {

            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Logowanie powiodło się, przejdz do profilu
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // Jeśli logowanie nie działa
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }

    // ------------------------------------------- EMAIL/PASSWORD LOGIN ----------------------------------------------------

    private void signInEmailPassword(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInActivity.this, "Błąd podczas logowania!",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
//                        if (!task.isSuccessful()) {
//                            mStatusTextView.setText(R.string.auth_failed);
//                        }
                        progressBar.setVisibility(View.GONE);
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


    // ------------------------------------------- SUPPORTING FUNC ----------------------------------------------------

    private boolean validateForm() {
        boolean valid = true;

        String email = loginTextView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            loginTextView.setError("Pole wymagane");
            valid = false;
        } else {
            loginTextView.setError(null);
        }

        String password = passwordTextView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordTextView.setError("Pole wymagane");
            valid = false;
        } else {
            passwordTextView.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {}
    }
}
