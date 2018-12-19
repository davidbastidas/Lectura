package com.dbl.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dbl.Controlador.AnomaliasController;
import com.dbl.Modelos.Anomalias;
import com.dbl.Modelos.Constants;
import com.dbl.Modelos.ServicioSesion;
import com.dbl.R;
import com.dbl.Vistas.Adaptader.AdapterAnomalias;

import java.util.ArrayList;

public class AnomaliaActivity extends AppCompatActivity {

    EditText e_buscar_anomalia;
    ListView l_anomalias;
    AdapterAnomalias adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anomalia);
        setTitle("Elegir una Anomalia");
        e_buscar_anomalia = findViewById(R.id.e_buscar_anomalia);
        l_anomalias = findViewById(R.id.l_anomalias);

        AnomaliasController anom = new AnomaliasController();

        ArrayList<Anomalias> anomalias = anom.consultar(0, 0, "", this);
        adapter = new AdapterAnomalias(this, anomalias);
        l_anomalias.setAdapter(adapter);
        l_anomalias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                try {
                    Anomalias anomalia = (Anomalias) parent.getItemAtPosition(position);

                    ServicioSesion.getInstance().setAnomalia(anomalia.getId());
                    ServicioSesion.getInstance().setObservacionObligatoria(false);

                    /*if (Constants.isGpsActivo(AnomaliaActivity.this)) {
                        Intent intentar = new Intent(AnomaliaActivity.this, ObservacionActivity.class);
                        startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
                    } else {
                        Constants.ActivarGPS(AnomaliaActivity.this);
                    }*/

                    Intent intentar = new Intent(AnomaliaActivity.this, ObservacionActivity.class);
                    startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);

                } catch (Exception ex) {
                    Toast.makeText(AnomaliaActivity.this, "Error en anomalia: " + ex, Toast.LENGTH_LONG).show();
                }
            }
        });
        e_buscar_anomalia.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                AnomaliaActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.SERVICIO_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
