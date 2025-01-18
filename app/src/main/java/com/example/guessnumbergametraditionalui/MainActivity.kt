package com.example.guessnumbergametraditionalui

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.credentials.CredentialManager
import android.credentials.GetCredentialException
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    private lateinit var navigateClassic: Button
    private lateinit var navigateAdvance: Button
    private lateinit var navigateHumanVsHuman: Button
    private lateinit var loginButton: Button
    private lateinit var logoutButton: Button
    private lateinit var credentialManager: CredentialManager

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        credentialManager = CredentialManager.create(this)

        loginButton = findViewById(R.id.Login)

        loginButton.setOnClickListener {
            loginWithGoogleAndCredentialManager(this)
        }

        logoutButton = findViewById(R.id.logout)


    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun loginWithGoogleAndCredentialManager(context: Context) {
        // Create a request for Google Sign-In
        val googleIdOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(getString(R.string.client_id))
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        Log.e(TAG, "breakpoint 1")
        lifecycleScope.launch {
            try {
                Log.e(TAG, "breakpoint 2")
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                handleFailure(e)
            } catch (e: GetCredentialCancellationException) {
                handleFailure(e)
            }
        }
    }

    private suspend fun handleSignIn(result: androidx.credentials.GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {

                        Log.e(TAG, "breakpoint 3")
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken,null)
                        val user = Firebase.auth.signInWithCredential(authCredential).await().user
                        user?.let {
                            if(it.isAnonymous.not()){
                                prepareMenu()
                            }
                        }

                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                }
            }

            else -> {

                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }


    private fun prepareMenu() {
        navigateClassic = findViewById(R.id.navigate_classic)
        navigateAdvance = findViewById(R.id.navigate_advance)
        navigateHumanVsHuman = findViewById(R.id.navigate_human_vs_human)

        navigateClassic.visibility = View.VISIBLE
        navigateAdvance.visibility = View.VISIBLE
        logoutButton.visibility = View.VISIBLE

        navigateClassic.setOnClickListener {
            val intent = Intent(this, ClassicGame::class.java)
            startActivity(intent)
        }


        navigateAdvance.setOnClickListener{
            val intent2 = Intent(this, HumanVsComputer::class.java)
            startActivity(intent2)
        }

        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        Firebase.auth.signOut()
        navigateClassic.visibility = View.GONE
        navigateAdvance.visibility = View.GONE
        logoutButton.visibility = View.GONE
        loginButton.visibility = View.VISIBLE
    }

    private fun handleFailure(error: Exception) {
        Log.d(TAG, "Error: $error")
    }


}