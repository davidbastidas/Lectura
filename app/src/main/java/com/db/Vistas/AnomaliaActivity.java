package com.db.Vistas;

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

import com.db.Controlador.AnomaliasController;
import com.db.Modelos.Anomalias;
import com.db.Modelos.Constants;
import com.db.Modelos.Resultados;
import com.db.Modelos.VisitaSesion;
import com.db.R;
import com.db.Vistas.Adaptader.AdapterAnomalias;
import com.db.Vistas.Adaptader.AdapterResultados;

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
        String condicion = "";
        if(VisitaSesion.getInstance().getResultado() == Constants.RES_CONTACTO_NO_EFECTIVO){
            condicion = "id IN(2,3,4,5,1)";
        }
        if(VisitaSesion.getInstance().getResultado() == Constants.RES_REGISTRO_NO_VALIDO){
            condicion = "id IN(6,7,8,9)";
        }
        if(VisitaSesion.getInstance().getResultado() == Constants.RES_EVALUAR_CAPACIDAD_OPERATIVA){
            condicion = "id IN(11,12,13,14)";
        }
        if(VisitaSesion.getInstance().getResultado() == Constants.RES_GESTION_NO_PROCEDE){
            condicion = "id IN(12,14,24)";
        }
        if(VisitaSesion.getInstance().getResultado() == Constants.RES_CLIENTE_CONDICIONA_PAGO){
            condicion = "id IN(11,16,17,18,19,20,21,22,23)";
        }
        if(VisitaSesion.getInstance().getResultado() == Constants.RES_REPROGRAMACION){
            condicion = "id IN(1)";
        }

        ArrayList<Anomalias> anomalias = anom.consultar(0, 0, condicion, this);
        adapter = new AdapterAnomalias(this, anomalias);
        l_anomalias.setAdapter(adapter);
        l_anomalias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                try {
                    Anomalias anomalia = (Anomalias) parent.getItemAtPosition(position);
                    VisitaSesion.getInstance().setAnomalia(anomalia.getId());
                    if(anomalia.getId() == Constants.AN_OTROS){
                        VisitaSesion.getInstance().setObservacionObligatoria(true);
                    } else {
                        VisitaSesion.getInstance().setObservacionObligatoria(false);
                    }
                    if (Constants.isGpsActivo(AnomaliaActivity.this)) {
                        if(VisitaSesion.getInstance().getResultado() == Constants.RES_CLIENTE_CONDICIONA_PAGO){
                            Intent intentar = new Intent(AnomaliaActivity.this, DatosClienteActivity.class);
                            startActivityForResult(intentar, Constants.VISITA_REQUEST_CODE);
                        } else {
                            Intent intentar = new Intent(AnomaliaActivity.this, ObservacionActivity.class);
                            startActivityForResult(intentar, Constants.VISITA_REQUEST_CODE);
                        }
                    } else {
                        Constants.ActivarGPS(AnomaliaActivity.this);
                    }

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
        if (requestCode == Constants.VISITA_REQUEST_CODE) {
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
