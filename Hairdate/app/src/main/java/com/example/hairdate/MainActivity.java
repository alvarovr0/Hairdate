package com.example.hairdate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Query query = db.collection("Peluquero").whereEqualTo("UID", FirebaseAuth.getInstance().getUid());
            if(query.get().isSuccessful()){
                fragment = new menu_Peluquero();
            }else{
                Query query2 = db.collection("Cliente").whereEqualTo("UID", FirebaseAuth.getInstance().getUid());
                if(query2.get().isSuccessful()){
                    //fragment = new menu_();
                }
            }
        } else {
            fragment = new principal();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();*/
        /*db = FirebaseFirestore.getInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment[] fragment = {null};

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Query query = db.collection("Peluquero").whereEqualTo("UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    if (querySnapshot.size() > 0) {
                        Bundle result = new Bundle();
                        result.putString("bundleKey",mAuth.getUid());
                        getParentFragmentManager().setFragmentResult("requestKey", result);
                        fragment[0] = new menu_Peluquero();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainerView, fragment[0]);
                        fragmentTransaction.commit();
                    } else {
                        Query query2 = db.collection("Cliente").whereEqualTo("UID", FirebaseAuth.getInstance().getUid());
                        query2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                if (querySnapshot.size() > 0) {
                                    Bundle result = new Bundle();
                                    result.putString("bundleKey",mAuth.getUid());
                                    Log.d("UID", String.valueOf(result));
                                    getParentFragmentManager().setFragmentResult("requestKey", result);
                                    //fragment = new menu_Cliente(); Esta línea la tengo comentada remarcado porque no tengo el menú cliente XD
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragmentContainerView, fragment[0]);
                                    fragmentTransaction.commit();
                                } else {
                                    fragment[0] = new principal();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragmentContainerView, fragment[0]);
                                    fragmentTransaction.commit();
                                }
                            }
                        });
                    }
                }
            });
        } else {
            fragment[0] = new principal();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment[0]);
            fragmentTransaction.commitAllowingStateLoss();
        }*/
        /*db = FirebaseFirestore.getInstance();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            db.collection("Peluquero").whereEqualTo("UID", uid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    // El usuario es un peluquero
                    fragmentTransaction.replace(R.id.fragmentContainerView, new menu_Peluquero());
                    fragmentTransaction.commitAllowingStateLoss();
                } else {
                    db.collection("Cliente").whereEqualTo("UID", uid).get().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful() && !task2.getResult().isEmpty()) {
                            // El usuario es un cliente
                            //fragmentTransaction.replace(R.id.fragmentContainerView, new MenuClienteFragment());
                            //fragmentTransaction.commitAllowingStateLoss();
                        } else {
                            // No se encontró el usuario
                            fragmentTransaction.replace(R.id.fragmentContainerView, new principal());
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    });
                }
            });
        } else {
            // El usuario no ha iniciado sesión
            fragmentTransaction.replace(R.id.fragmentContainerView, new principal());
            fragmentTransaction.commitAllowingStateLoss();
        }*/
    }
}
