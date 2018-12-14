package com.db.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class VisitasActivity extends AppCompatActivity {

    TextView t_pagina;
    ListView l_visitas;
    VisitasController vis = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitas);
        setTitle("Lista de Visitas");

        t_pagina = findViewById(R.id.t_pagina);
        l_visitas = findViewById(R.id.l_visitas);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            cargarLista();
        } else {
            if (extras.containsKey(Constants.EXTRA_BARRIO)) {
                String barrio = extras.getString(Constants.EXTRA_BARRIO);
                barrio = " and barrio like '%" + barrio + "%'";
                vis = new VisitasController();
                System.err.println("barrio: " + barrio);
                ArrayList<Visitas> visitas = vis.consultar(
                        0,
                        0,
                        "estado = 0 " + barrio,
                        this);
                AdapterVisitas adapter = new AdapterVisitas(this, visitas);
                l_visitas.setAdapter(adapter);
                t_pagina.setText(visitas.size() + " Visitas por Barrio");
            }else if (extras.containsKey(Constants.EXTRA_NIC)) {
                String nic = extras.getString(Constants.EXTRA_NIC);
                String medidor = extras.getString(Constants.EXTRA_MEDIDOR);
                String direccion = extras.getString(Constants.EXTRA_DIRECCION);
                boolean realizados = extras.getBoolean(Constants.EXTRA_REALIZADO);

                String sql = "";
                if(!direccion.equals("")){
                    sql = " and direccion like '%" + direccion + "%'";
                }
                if(!medidor.equals("")){
                    sql = " and medidor like '%" + medidor + "%'";
                }
                if(!nic.equals("")){
                    sql = " and nic like '%" + nic + "%'";
                }

                int rel = 0;
                if(realizados){
                    rel = 1;
                }
                vis = new VisitasController();
                System.err.println("buscar: " + sql + rel);
                ArrayList<Visitas> visitas = vis.consultar(
                        0,
                        0,
                        "estado = " + rel + sql,
                        this);
                AdapterVisitas adapter = new AdapterVisitas(this, visitas);
                l_visitas.setAdapter(adapter);
                t_pagina.setText(visitas.size() + " Visita(s) Encontrada(s)");
            }
        }


        l_visitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                try {
                    Visitas visita = (Visitas) parent.getItemAtPosition(position);
                    VisitaSesion.getInstance().setId(visita.getId());
                    Intent intentar = new Intent(VisitasActivity.this, DetalleActivity.class);
                    intentar.putExtra(Constants.EXTRA_VISITA_ID, visita.getId());
                    startActivityForResult(intentar, Constants.VISITA_REQUEST_CODE);
                } catch (Exception ex) {
                    Toast.makeText(VisitasActivity.this, "Error en la visita: " + ex, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cargarLista(){
        vis = new VisitasController();
        ArrayList<Visitas> visitas = vis.consultar(0, 0, "estado=0", this);
        AdapterVisitas adapter = new AdapterVisitas(this, visitas);
        l_visitas.setAdapter(adapter);
        t_pagina.setText(visitas.size() + " Visitas");
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
}
