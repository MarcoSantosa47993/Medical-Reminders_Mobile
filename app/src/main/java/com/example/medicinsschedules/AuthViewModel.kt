package com.example.medicinsschedules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shared.models.MyUser
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = Firebase.firestore

    private val usersCollection = db.collection("users")

    private val _authState = MutableLiveData<AuthState>(AuthState.Init)
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            getUser(auth.currentUser!!.uid) { doc ->
                _authState.value = AuthState.Authenticated(MyUser.fromDocument(doc))
            }
        }
    }

    fun login(email: String, password: String) {

        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        try {
            _authState.value = AuthState.Loading
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        checkAuthStatus()
                    } else {
                        _authState.value =
                            AuthState.Error(task.exception?.message ?: "Something went wrong")
                    }
                }
        } catch (ex: Exception) {
            _authState.value =
                AuthState.Error(ex.message ?: "Something went wrong")
        }
    }

    fun signup(myUser: MyUser, password: String) {
        try {
            _authState.value = AuthState.Loading
            auth.createUserWithEmailAndPassword(myUser.email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        myUser.id = auth.currentUser!!.uid

                        setUser(myUser)
                    } else {
                        _authState.value =
                            AuthState.Error(task.exception?.message ?: "Something went wrong")
                    }
                }
                .addOnFailureListener {
                    _authState.value =
                        AuthState.Error(it.message ?: "Something went wrong")
                }
        } catch (ex: Exception) {
            _authState.value =
                AuthState.Error(ex.message ?: "Something went wrong")
        }
    }

    fun setUser(myUser: MyUser) {
        _authState.value = AuthState.Loading
        try {
            usersCollection.document(myUser.id)
                .set(myUser.toDocument())
                .addOnSuccessListener {
                    _authState.value = AuthState.Authenticated(myUser)


                }
                .addOnFailureListener {
                    _authState.value =
                        AuthState.Error(it.message ?: "Something went wrong")
                }
        } catch (ex: Exception) {
            _authState.value =
                AuthState.Error(ex.message ?: "Something went wrong")
        }
    }

    fun signout() {
        try {
            _authState.value = AuthState.Loading
            auth.signOut()
            _authState.value = AuthState.Unauthenticated
        } catch (ex: Exception) {
            _authState.value =
                AuthState.Error(ex.message ?: "Something went wrong")
        }
    }

    private fun getUser(userId: String, onSuccessListener: OnSuccessListener<DocumentSnapshot>) {
        val docRef = usersCollection.document(userId)
        docRef.get().addOnSuccessListener(onSuccessListener).addOnFailureListener {
            _authState.value =
                AuthState.Error(it.message ?: "Something went wrong")
        }
    }
}

sealed class AuthState {
    data class Authenticated(val myUser: MyUser) : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    object Init : AuthState()
    data class Error(val message: String) : AuthState()
}