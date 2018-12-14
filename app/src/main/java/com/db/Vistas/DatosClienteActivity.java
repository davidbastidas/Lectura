package com.db.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.db.Controlador.VisitasController;
import com.db.Modelos.Constants;
import com.db.Modelos.VisitaSesion;
import com.db.Modelos.Visitas;
import com.db.R;
import com.db.Vistas.Adaptader.AdapterVisitas;

import java.util.ArrayList;

public class DatosClienteActivity extends AppCompatActivity {

    Button b_ir_observacion;
    EditText e_cedula, e_titular_pago, e_atiende, e_telefono, e_email, e_lectura;
    VisitasController vis = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_cliente);
        setTitle("Datos de la Visita");
        e_cedula = findViewById(R.id.e_cedula);
        e_titular_pago = findViewById(R.id.e_titular_pago);
        e_atiende = findViewById(R.id.e_atiende);
        e_telefono = findViewById(R.id.e_telefono);
        e_email = findViewById(R.id.e_email);
        e_lectura = findViewById(R.id.e_lectura);
        b_ir_observacion = findViewById(R.id.b_ir_observacion);

        final VisitaSesion visita = VisitaSesion.getInstance();
        VisitasController vis = new VisitasController();
        ArrayList<Visitas> visitas = vis.consultar(0, 0, "id=" + visita.getId(), this);
        e_titular_pago.setText(visitas.get(0).getCliente());

        b_ir_observacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                if(!e_telefono.getText().toString().equals("")) {
                    if(e_telefono.getText().toString().length() == 7 || e_telefono.getText().toString().length() == 10) {
                        flag = true;
                    } else{
                        flag = false;
                        Toast.makeText(DatosClienteActivity.this, "El telefono debe ser Fijo o Celular.", Toast.LENGTH_LONG).show();
                    }
                }
                if(!e_atiende.getText().toString().equals("")){
                    visita.setCedula(e_cedula.getText().toString());
                    visita.setTitularPago(e_titular_pago.getText().toString());
                    visita.setPersonaContacto(e_atiende.getText().toString());
                    visita.setTelefono(e_telefono.getText().toString());
                    visita.setEmail(e_email.getText().toString());
                    visita.setLectura(e_lectura.getText().toString());
                } else {
                    flag = false;
                    Toast.makeText(DatosClienteActivity.this, "Debe ingresar la persona que atiende", Toast.LENGTH_LONG).show();
                }

                if(flag){
                    if (Constants.isGpsActivo(DatosClienteActivity.this)) {
                        Intent intentar = new Intent(DatosClienteActivity.this, ObservacionActivity.class);
                        startActivityForResult(intentar, Constants.VISITA_REQUEST_CODE);
                    } else {
                        Constants.ActivarGPS(DatosClienteActivity.this);
                    }
                }
            }
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
