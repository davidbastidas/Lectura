package com.dbl.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.dbl.Controlador.AnomaliasController;
import com.dbl.Controlador.EntidadesPagoController;
import com.dbl.Controlador.ObservacionRapidaController;
import com.dbl.Controlador.ResultadosController;
import com.dbl.Controlador.AuditoriaController;
import com.dbl.Modelos.Anomalias;
import com.dbl.Modelos.Auditorias;
import com.dbl.Modelos.Constants;
import com.dbl.Modelos.EntidadesPago;
import com.dbl.Modelos.ObservacionRapida;
import com.dbl.Modelos.Resultados;
import com.dbl.R;
import com.dbl.Vistas.Adaptader.AdapterDetalleVisita;

import java.util.ArrayList;

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

        Auditorias visita = null;
        AuditoriaController vis = new AuditoriaController();
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            visitaId= 0;
        } else {
            visitaId = extras.getLong(Constants.EXTRA_SERVICIO_ID);
            ArrayList<Auditorias> visitas = vis.consultar(0, 0, "id = " + visitaId, this);
            visita = visitas.get(0);
        }

        ArrayList<Auditorias> data = new ArrayList<Auditorias>();
        Auditorias datum = null;

        datum = new Auditorias();
        datum.setCliente("NIC");
        datum.setPersonaContacto("" + visita.getNic());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Cliente");
        datum.setPersonaContacto(visita.getCliente());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Direccion");
        datum.setPersonaContacto(visita.getDireccion());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Deuda");
        datum.setPersonaContacto("" + visita.getDeuda());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Facturas");
        datum.setPersonaContacto("" + visita.getFacturas());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Medidor");
        datum.setPersonaContacto(visita.getMedidor());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Fecha Limite para Acuerdo de Pago");
        datum.setPersonaContacto(visita.getFechaLimiteCompromiso());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Municipio");
        datum.setPersonaContacto(visita.getMunicipio());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Localidad");
        datum.setPersonaContacto(visita.getLocalidad());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Barrio");
        datum.setPersonaContacto(visita.getBarrio());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Tipo de Gestion");
        datum.setPersonaContacto(visita.getTipoVisita());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Tarifa");
        datum.setPersonaContacto(visita.getTarifa());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Nis");
        datum.setPersonaContacto("" + visita.getNis());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Cedula");
        datum.setPersonaContacto(visita.getCedula());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Titular de Pago");
        datum.setPersonaContacto(visita.getTitularPago());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Atiende");
        datum.setPersonaContacto(visita.getPersonaContacto());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Telefono");
        datum.setPersonaContacto(visita.getTelefono());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("E-mail");
        datum.setPersonaContacto(visita.getEmail());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Fecha de Pago");
        datum.setPersonaContacto(visita.getFechaPago());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Fecha de Compromiso");
        datum.setPersonaContacto(visita.getFechaCompromiso());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Observaciones");
        String obsRapida = "";
        if(visita.getObservacionRapida() != 0){
            ObservacionRapidaController oc = new ObservacionRapidaController();
            ArrayList<ObservacionRapida> consultar = oc.consultar(0, 0, "id = " + visita.getObservacionRapida(), this);
            obsRapida = consultar.get(0).getNombre();
        }
        datum.setPersonaContacto("" + obsRapida);
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Otras Observaciones");
        datum.setPersonaContacto(visita.getObservacionAnalisis());
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Anomalia");
        String anomalia = "";
        if(visita.getAnomalia() != 0){
            AnomaliasController an = new AnomaliasController();
            ArrayList<Anomalias> consultar = an.consultar(0, 0, "id = " + visita.getAnomalia(), this);
            anomalia = consultar.get(0).getNombre();
        }
        datum.setPersonaContacto("" + anomalia);
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Resultado");
        String resultado = "";
        if(visita.getResultado() != 0){
            ResultadosController rs = new ResultadosController();
            ArrayList<Resultados> consultar = rs.consultar(0, 0, "id = " + visita.getResultado(), this);
            resultado = consultar.get(0).getNombre();
        }
        datum.setPersonaContacto("" + resultado);
        data.add(datum);

        datum = new Auditorias();
        datum.setCliente("Entidad de Recaudo");
        String entidad = "";
        if(visita.getEntidadRecaudo() != 0){
            EntidadesPagoController rs = new EntidadesPagoController();
            ArrayList<EntidadesPago> consultar = rs.consultar(0, 0, "id = " + visita.getEntidadRecaudo(), this);
            entidad = consultar.get(0).getNombre();
        }
        datum.setPersonaContacto("" + entidad);
        data.add(datum);

        datum = new Auditorias();
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
                    startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
                } else {
                    Constants.ActivarGPS(DetalleActivity.this);
                }
            }
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
}
