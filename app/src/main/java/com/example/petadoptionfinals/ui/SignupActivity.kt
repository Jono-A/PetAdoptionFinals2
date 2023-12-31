package com.example.petadoptionfinals.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petadoptionfinals.databinding.ActivitySignupBinding
import com.example.petadoptionfinals.model.user
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.jar.Attributes.Name

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private val authFirebase = Firebase.auth //**


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.signupButton.setOnClickListener {
            val name = binding.signupName.text.toString()
            val phone = binding.signupPhone.text.toString()
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPassword = binding.signupConfirm.text.toString()



            if (name.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                databaseReference =
                                    FirebaseDatabase.getInstance().getReference("user")
                                val user = user(name, phone, email, password)
                                authFirebase.currentUser?.uid?.let { it1 ->
                                    databaseReference.child(it1).setValue(user)
                                        .addOnSuccessListener {

                                            binding.signupName.text.clear()
                                            binding.signupPhone.text.clear()
                                            binding.signupEmail.text.clear()
                                            binding.signupPassword.text.clear()
                                            binding.signupConfirm.text.clear()


                                            Toast.makeText(this, "User Added", Toast.LENGTH_SHORT)
                                                .show()

                                        }.addOnFailureListener {

                                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                }
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Password does not matched", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
            }


        }

    }
}