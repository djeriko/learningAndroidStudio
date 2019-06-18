package com.google.firebase.quickstart.auth.kotlin

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.quickstart.auth.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_google.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
class GoogleSignInActivity : BaseActivity(), View.OnClickListener {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //Конструкотор родительского класса
        setContentView(R.layout.activity_google) //Подключает содержимое из файла разметки

        (findViewById(R.id.testButton) as Button)!!.setOnClickListener {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)

            val rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
            val token = FirebaseAuth.getInstance().currentUser!!.getIdToken(false).result!!.token
/*          val body = ScoreFunLoot()
            body.team_1_name = "1"
            body.team_1_score = "2"
            body.team_2_name = "3"
            body.team_2_score = "4"*/

            Log.d("idtoken",token);
            val subscribe = Retrofit.Builder()
                    //.baseUrl("https://funloot.herokuapp.com/")
                    .baseUrl("http://192.168.1.56:3000/") //Базовая часть адреса
                    .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                    .addCallAdapterFactory(rxAdapter)
                    .client(client.build())
                    .build()
                    .create(FunlootApi::class.java)
                    /*.postScore(token!!,body)*/
                    .getNews(token!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        Log.d("TEST", result.toString())
                        //textOnePiece.text = result[0].news_body.toString()
                        for ( item in result){
                            textOnePiece.text=textOnePiece.text.toString() + item.news_body.toString()
                        }

                    }, { throwable -> {} })

        }



        // Button listeners
        signInButton.setOnClickListener(this)
        signOutButton.setOnClickListener(this)
        disconnectButton.setOnClickListener(this)

        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]
    }

    //Всплывающее сообщение
    fun changeText(view: View){
        val myChangeText = Toast.makeText(this, "Wow wow!", Toast.LENGTH_LONG)

        myChangeText.show()
    }
    //

    //Show news
    fun showNews(view: View){
        val showString = textOnePiece.text.toString()

        var count: Int = Integer.parseInt(showString)
        count++

        textOnePiece.text = count.toString()
    }
    //

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        // [START_EXCLUDE silent]
        showProgressDialog()
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog()
                    // [END_EXCLUDE]
                }
    }
    // [END auth_with_google]

    // [START signin]
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

    private fun signOut() {
        // Firebase sign out
        auth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun revokeAccess() {
        // Firebase sign out
        auth.signOut()

        // Google revoke access
        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()
        if (user != null) {
            status.text = getString(R.string.google_status_fmt, user.email)
            detail.text = getString(R.string.firebase_status_fmt, user.uid)

            signInButton.visibility = View.GONE
            signOutAndDisconnect.visibility = View.VISIBLE
        } else {
            status.setText(R.string.signed_out)
            detail.text = null



            signInButton.visibility = View.VISIBLE
            signOutAndDisconnect.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.signInButton -> signIn()
            R.id.signOutButton -> signOut()
            R.id.disconnectButton -> revokeAccess()
        }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    //Переход на другое окно (активити)
    fun newsMe(view: View){ //Метод вызова новостей
        val newsIntent = Intent (this, NewsActivity::class.java)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)

        val rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
        val token = FirebaseAuth.getInstance().currentUser!!.getIdToken(false).result!!.token
        Log.d("idtoken",token);
        val subscribe = Retrofit.Builder()
                //.baseUrl("https://funloot.herokuapp.com/")
                .baseUrl("http://192.168.1.56:3000/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .addCallAdapterFactory(rxAdapter)
                .client(client.build())
                .build()
                .create(FunlootApi::class.java)
                .getNews(token!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    Log.d("TEST", result.toString())
                    newsIntent.putExtra(NewsActivity.NEWS_TEXT, result[0].news_body.toString())
                    startActivity(newsIntent)

                }, { throwable -> {} })

    }


}
