package com.example.hairdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        fragmentManager = getSupportFragmentManager();

        FirebaseAuth currentUser = FirebaseAuth.getInstance();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("Peluquero").whereEqualTo("UID", uid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    // El usuario es un peluquero
                    Fragment fragment = new menu_Peluquero();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle result = new Bundle();
                    result.putString("bundleKey",uid);
                    Log.d("UID", String.valueOf(result));
                    getSupportFragmentManager().setFragmentResult("requestKey", result);
                    fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
                    //fragmentTransaction.commitAllowingStateLoss();
                } else {
                    db.collection("Cliente").whereEqualTo("UID", uid).get().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful() && !task2.getResult().isEmpty()) {
                            // El usuario es un cliente
                            //fragmentTransaction.replace(R.id.fragmentContainerView, new MenuClienteFragment());
                            //fragmentTransaction.commitAllowingStateLoss();
                        } else {
                            // No se encontró el usuario
                            Fragment fragment = new principal();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
                            //fragmentTransaction.commitAllowingStateLoss();
                        }
                    });
                }
            });
        } else {
            // El usuario no ha iniciado sesión
            Fragment fragment = new principal();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
            //fragmentTransaction.commitAllowingStateLoss();
        }

    }

}
