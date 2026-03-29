package com.example.lab9;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lab9.beans.ApiResponse;
import com.example.lab9.beans.Etudiant;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AddEtudiant extends AppCompatActivity implements View.OnClickListener {

    private EditText nom, prenom;
    private Spinner ville;
    private RadioButton m, f;
    private Button add;
    private RequestQueue requestQueue;

    private static final String insertUrl =
            "http://10.0.2.2/webservicephp8/ws/createEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_etudiant);

        // Initialisation des vues
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        ville = findViewById(R.id.ville);
        m = findViewById(R.id.homme);
        f = findViewById(R.id.femme);
        add = findViewById(R.id.ajouter);

        // Volley
        requestQueue = Volley.newRequestQueue(this);

        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == add) {
            envoyerEtudiant();
        }
    }

    private void envoyerEtudiant() {

        StringRequest request = new StringRequest(Request.Method.POST, insertUrl,
                response -> {

                    Log.d("RESPONSE", response);

                    try {
                        Gson gson = new Gson();
                        ApiResponse apiResponse = gson.fromJson(response, ApiResponse.class);

                        if (apiResponse != null) {

                            Toast.makeText(AddEtudiant.this,
                                    apiResponse.message,
                                    Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception e) {
                        Log.e("JSON_ERROR", e.getMessage());
                    }

                },
                error -> Log.e("VOLLEY_ERROR", error.toString())
        ) {

            @Override
            protected Map<String, String> getParams() {

                String sexe = "";

                if (m.isChecked()) {
                    sexe = "homme";
                } else if (f.isChecked()) {
                    sexe = "femme";
                }

                Map<String, String> params = new HashMap<>();
                params.put("nom", nom.getText().toString().trim());
                params.put("prenom", prenom.getText().toString().trim());
                params.put("ville", ville.getSelectedItem().toString());
                params.put("sexe", sexe);

                return params;
            }
        };

        requestQueue.add(request);
    }
}