package com.db.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.db.Controlador.AnomaliasController;
import com.db.Controlador.EntidadesPagoController;
import com.db.Controlador.ObservacionRapidaController;
import com.db.Controlador.ResultadosController;
import com.db.Controlador.VisitasController;
import com.db.Modelos.Anomalias;
import com.db.Modelos.Constants;
import com.db.Modelos.EntidadesPago;
import com.db.Modelos.ObservacionRapida;
import com.db.Modelos.Resultados;
import com.db.Modelos.Visitas;
import com.db.R;
import com.db.Vistas.Adaptader.AdapterDetalleVisita;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DetalleActivity extends AppCompatActivity {

    Button b_ir_resultado;
    ListView l_detalle;
    long visitaId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        setTitle("Detalle de la Visita");

        b_ir_resultado = findViewById(R.id.b_ir_resultado);
        l_detalle = findViewById(R.id.l_detalle);

        Visitas visita = null;
        VisitasController vis = new VisitasController();
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            visitaId= 0;
        } else {
            visitaId = extras.getLong(Constants.EXTRA_VISITA_ID);
            ArrayList<Visitas> visitas = vis.consultar(0, 0, "id = " + visitaId, this);
            visita = visitas.get(0);
        }

        ArrayList<Visitas> data = new ArrayList<Visitas>();
        Visitas datum = null;

        datum = new Visitas();
        datum.setCliente("NIC");
        datum.setPersonaContacto("" + visita.getNic());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Cliente");
        datum.setPersonaContacto(visita.getCliente());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Direccion");
        datum.setPersonaContacto(visita.getDireccion());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Deuda");
        datum.setPersonaContacto("" + visita.getDeuda());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Facturas");
        datum.setPersonaContacto("" + visita.getFacturas());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Medidor");
        datum.setPersonaContacto(visita.getMedidor());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Fecha Limite para Acuerdo de Pago");
        datum.setPersonaContacto(visita.getFechaLimiteCompromiso());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Municipio");
        datum.setPersonaContacto(visita.getMunicipio());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Localidad");
        datum.setPersonaContacto(visita.getLocalidad());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Barrio");
        datum.setPersonaContacto(visita.getBarrio());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Tipo de Gestion");
        datum.setPersonaContacto(visita.getTipoVisita());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Tarifa");
        datum.setPersonaContacto(visita.getTarifa());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Nis");
        datum.setPersonaContacto("" + visita.getNis());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Cedula");
        datum.setPersonaContacto(visita.getCedula());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Titular de Pago");
        datum.setPersonaContacto(visita.getTitularPago());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Atiende");
        datum.setPersonaContacto(visita.getPersonaContacto());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Telefono");
        datum.setPersonaContacto(visita.getTelefono());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("E-mail");
        datum.setPersonaContacto(visita.getEmail());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Fecha de Pago");
        datum.setPersonaContacto(visita.getFechaPago());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Fecha de Compromiso");
        datum.setPersonaContacto(visita.getFechaCompromiso());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Observaciones");
        String obsRapida = "";
        if(visita.getObservacionRapida() != 0){
            ObservacionRapidaController oc = new ObservacionRapidaController();
            ArrayList<ObservacionRapida> consultar = oc.consultar(0, 0, "id = " + visita.getObservacionRapida(), this);
            obsRapida = consultar.get(0).getNombre();
        }
        datum.setPersonaContacto("" + obsRapida);
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Otras Observaciones");
        datum.setPersonaContacto(visita.getObservacionAnalisis());
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Anomalia");
        String anomalia = "";
        if(visita.getAnomalia() != 0){
            AnomaliasController an = new AnomaliasController();
            ArrayList<Anomalias> consultar = an.consultar(0, 0, "id = " + visita.getAnomalia(), this);
            anomalia = consultar.get(0).getNombre();
        }
        datum.setPersonaContacto("" + anomalia);
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Resultado");
        String resultado = "";
        if(visita.getResultado() != 0){
            ResultadosController rs = new ResultadosController();
            ArrayList<Resultados> consultar = rs.consultar(0, 0, "id = " + visita.getResultado(), this);
            resultado = consultar.get(0).getNombre();
        }
        datum.setPersonaContacto("" + resultado);
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("Entidad de Recaudo");
        String entidad = "";
        if(visita.getEntidadRecaudo() != 0){
            EntidadesPagoController rs = new EntidadesPagoController();
            ArrayList<EntidadesPago> consultar = rs.consultar(0, 0, "id = " + visita.getEntidadRecaudo(), this);
            entidad = consultar.get(0).getNombre();
        }
        datum.setPersonaContacto("" + entidad);
        data.add(datum);

        datum = new Visitas();
        datum.setCliente("IDBD");
        datum.setPersonaContacto("" + visita.getId());
        data.add(datum);

        AdapterDetalleVisita adapter = new AdapterDetalleVisita(this, data);
        l_detalle.setAdapter(adapter);

        if(visita.getEstado() != 0){
            b_ir_resultado.setEnabled(false);
        }

        b_ir_resultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.isGpsActivo(DetalleActivity.this)) {
                    Intent intentar = new Intent(DetalleActivity.this, ResultadoActivity.class);
                    startActivityForResult(intentar, Constants.VISITA_REQUEST_CODE);
                } else {
                    Constants.ActivarGPS(DetalleActivity.this);
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
}
