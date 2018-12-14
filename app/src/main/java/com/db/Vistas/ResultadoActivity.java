package com.db.Vistas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.db.Controlador.ResultadosController;
import com.db.Controlador.VisitasController;
import com.db.Modelos.Constants;
import com.db.Modelos.Resultados;
import com.db.Modelos.VisitaSesion;
import com.db.Modelos.Visitas;
import com.db.R;
import com.db.Vistas.Adaptader.AdapterResultados;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ResultadoActivity extends AppCompatActivity {

    EditText e_buscar_resultado;
    ListView l_resultados;
    AdapterResultados adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        setTitle("Elija un Resultado");

        e_buscar_resultado = findViewById(R.id.e_buscar_resultado);
        l_resultados = findViewById(R.id.l_resultados);

        ResultadosController res = new ResultadosController();
        ArrayList<Resultados> resultados = res.consultar(0, 0, "", this);
        adapter = new AdapterResultados(this, resultados);
        l_resultados.setAdapter(adapter);
        l_resultados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                try {
                    Resultados resultado = (Resultados) parent.getItemAtPosition(position);
                    validateResultado(resultado.getId());
                } catch (Exception ex) {
                    Toast.makeText(ResultadoActivity.this, "Error en resultado: " + ex, Toast.LENGTH_LONG).show();
                }
            }
        });
        e_buscar_resultado.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                adapter.getFilter().filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {}
        });
    }

    private void validateResultado(long id){
        VisitaSesion visita = VisitaSesion.getInstance();
        Class actividad = null;
        switch ((int) id){
            case Constants.RES_PAGO_TOTAL:
                visita.setResultado(Constants.RES_PAGO_TOTAL);
                visita.setAnomalia(10);
                actividad = EntidadesActivity.class;
                break;
            case Constants.RES_ENTREGA_FACTURA:
                visita.setResultado(Constants.RES_ENTREGA_FACTURA);
                visita.setEntidadRecaudo(0);
                visita.setAnomalia(10);
                actividad = DatosClienteActivity.class;
                break;
            case Constants.RES_ACUERDO_PAGO:
                visita.setResultado(Constants.RES_ACUERDO_PAGO);
                visita.setAnomalia(10);
                actividad = EntidadesActivity.class;
                break;
            case Constants.RES_COMPROMISO:
                //la fecha se toma de la fecha precargada
                visita.setResultado(Constants.RES_COMPROMISO);
                visita.setEntidadRecaudo(0);
                visita.setAnomalia(10);
                VisitasController vis = new VisitasController();
                ArrayList<Visitas> visitas = vis.consultar(0, 0, "id=" + visita.getId(), this);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd");
                Date convertedDate = new Date();
                try {
                    convertedDate = dateFormat.parse(visitas.get(0).getFechaLimiteCompromiso());
                    String date = new SimpleDateFormat("dd/MM/yyyy")
                            .format(convertedDate);
                    visita.setFechaCompromiso(date);
                } catch (ParseException e) {
                    visita.setFechaCompromiso("");
                    Toast.makeText(this, "No se pudo convertir la fecha: " + visitas.get(0).getFechaLimiteCompromiso(), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(this, "Fecha de compromiso: " + visita.getFechaCompromiso(), Toast.LENGTH_LONG).show();
                actividad = DatosClienteActivity.class;
                break;
            case Constants.RES_ABONO:
                visita.setResultado(Constants.RES_ABONO);
                visita.setAnomalia(10);
                actividad = EntidadesActivity.class;
                break;
            case Constants.RES_CLIENTE_NO_PUEDE_PAGAR:
                visita.setResultado(Constants.RES_CLIENTE_NO_PUEDE_PAGAR);
                visita.setEntidadRecaudo(0);
                visita.setAnomalia(10);
                actividad = DatosClienteActivity.class;
                break;
            case Constants.RES_CLIENTE_NO_TIENE_VOLUNTAD_PAGO:
                visita.setResultado(Constants.RES_CLIENTE_NO_TIENE_VOLUNTAD_PAGO);
                visita.setEntidadRecaudo(0);
                visita.setAnomalia(10);
                actividad = DatosClienteActivity.class;
                break;
            case Constants.RES_CLIENTE_CONDICIONA_PAGO:
                visita.setResultado(Constants.RES_CLIENTE_CONDICIONA_PAGO);
                visita.setEntidadRecaudo(0);

                visita.setCedula("");
                visita.setTitularPago("");
                visita.setPersonaContacto("");
                visita.setTelefono("");
                visita.setEmail("");
                visita.setLectura("");
                visita.setFechaCompromiso("");
                visita.setFechaPago("");

                actividad = AnomaliaActivity.class;
                break;
            case Constants.RES_CONTACTO_NO_EFECTIVO:
                visita.setResultado(Constants.RES_CONTACTO_NO_EFECTIVO);
                visita.setEntidadRecaudo(0);

                visita.setCedula("");
                visita.setTitularPago("");
                visita.setPersonaContacto("");
                visita.setTelefono("");
                visita.setEmail("");
                visita.setLectura("");
                visita.setFechaCompromiso("");
                visita.setFechaPago("");

                actividad = AnomaliaActivity.class;
                break;
            case Constants.RES_CLIENTE_CANCELO:
                visita.setResultado(Constants.RES_CLIENTE_CANCELO);
                visita.setAnomalia(10);
                actividad = EntidadesActivity.class;
                break;
            case Constants.RES_REGISTRO_NO_VALIDO:
                visita.setResultado(Constants.RES_REGISTRO_NO_VALIDO);
                visita.setEntidadRecaudo(0);

                visita.setCedula("");
                visita.setTitularPago("");
                visita.setPersonaContacto("");
                visita.setTelefono("");
                visita.setEmail("");
                visita.setLectura("");
                visita.setFechaCompromiso("");
                visita.setFechaPago("");

                actividad = AnomaliaActivity.class;
                break;
            case Constants.RES_DEUDA_CERO:
                visita.setResultado(Constants.RES_DEUDA_CERO);
                visita.setEntidadRecaudo(0);
                visita.setAnomalia(10);
                actividad = DatosClienteActivity.class;
                break;
            case Constants.RES_EVALUAR_CAPACIDAD_OPERATIVA:
                visita.setResultado(Constants.RES_EVALUAR_CAPACIDAD_OPERATIVA);
                visita.setEntidadRecaudo(0);

                visita.setCedula("");
                visita.setTitularPago("");
                visita.setPersonaContacto("");
                visita.setTelefono("");
                visita.setEmail("");
                visita.setLectura("");
                visita.setFechaCompromiso("");
                visita.setFechaPago("");

                actividad = AnomaliaActivity.class;
                break;
            case Constants.RES_GESTION_NO_PROCEDE:
                visita.setResultado(Constants.RES_GESTION_NO_PROCEDE);
                visita.setEntidadRecaudo(0);

                visita.setCedula("");
                visita.setTitularPago("");
                visita.setPersonaContacto("");
                visita.setTelefono("");
                visita.setEmail("");
                visita.setLectura("");
                visita.setFechaCompromiso("");
                visita.setFechaPago("");

                actividad = AnomaliaActivity.class;
                break;
            case Constants.RES_EN_CURSO:
                visita.setResultado(Constants.RES_EN_CURSO);
                visita.setEntidadRecaudo(0);
                visita.setAnomalia(10);

                visita.setCedula("");
                visita.setTitularPago("");
                visita.setPersonaContacto("");
                visita.setTelefono("");
                visita.setEmail("");
                visita.setLectura("");
                visita.setFechaCompromiso("");
                visita.setFechaPago("");

                actividad = ObservacionActivity.class;
                break;
            case Constants.RES_REPROGRAMACION:
                visita.setResultado(Constants.RES_REPROGRAMACION);
                visita.setEntidadRecaudo(0);

                visita.setCedula("");
                visita.setTitularPago("");
                visita.setPersonaContacto("");
                visita.setTelefono("");
                visita.setEmail("");
                visita.setLectura("");
                visita.setFechaCompromiso("");
                visita.setFechaPago("");

                actividad = AnomaliaActivity.class;
                break;
        }
        if (Constants.isGpsActivo(this)) {
            Intent intentar = new Intent(this, actividad);
            startActivityForResult(intentar, Constants.VISITA_REQUEST_CODE);
        } else {
            Constants.ActivarGPS(this);
        }
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
