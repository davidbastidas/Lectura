package com.db.Vistas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.db.Controlador.AnomaliasController;
import com.db.Controlador.EntidadesPagoController;
import com.db.Controlador.GestorConexion;
import com.db.Controlador.ObservacionRapidaController;
import com.db.Controlador.ResultadosController;
import com.db.Controlador.VisitasController;
import com.db.Modelos.Anomalias;
import com.db.Modelos.Constants;
import com.db.Modelos.EntidadesPago;
import com.db.Modelos.ObservacionRapida;
import com.db.Modelos.Resultados;
import com.db.Modelos.SesionSingleton;
import com.db.Modelos.VisitaSesion;
import com.db.Modelos.Visitas;
import com.db.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OperarioActivity extends AppCompatActivity {

    Button b_buscar, b_barrios, b_visitas;
    TextView t_reporte, t_acerca, t_perfil;
    ProgressDialog progressDialog = null;
    Visitas visitaEnviar = null;
    private int sizeVisitas = 0;
    GestorConexion conexionGestor = new GestorConexion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operario);
        b_buscar = findViewById(R.id.b_buscar);
        b_barrios = findViewById(R.id.b_barrios);
        b_visitas = findViewById(R.id.b_visitas);
        t_reporte = findViewById(R.id.t_reporte);
        t_acerca = findViewById(R.id.t_acerca);
        t_perfil = findViewById(R.id.t_perfil);
        t_perfil.setText("Bienvenido. " + SesionSingleton.getInstance().getNombreUsuario());
        if(SesionSingleton.getInstance().getNombreUsuario() == null){
            finish();
        }

        b_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentar = new Intent(OperarioActivity.this, BuscarActivity.class);
                startActivityForResult(intentar, Constants.VISITA_REQUEST_CODE);
            }
        });

        b_barrios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentar = new Intent(OperarioActivity.this, BarriosActivity.class);
                startActivityForResult(intentar, Constants.VISITA_REQUEST_CODE);
            }
        });

        b_visitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentar = new Intent(OperarioActivity.this, VisitasActivity.class);
                startActivityForResult(intentar, Constants.VISITA_REQUEST_CODE);
            }
        });

        mostrarReporte();
        acercaDe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enviarVisitas();
        mostrarReporte();
        VisitaSesion.resetSesion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_operario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.servicios:
                progressDialog = Constants.dialogIndeterminate(this, "Descargando Visitas...");
                new AsyncTask<String, Void, String>(){
                    @Override
                    protected String doInBackground(String... params) {
                        GestorConexion con = new GestorConexion();
                        return con.descargarVisitas(SesionSingleton.getInstance().getFkId());
                    }
                    @Override
                    protected void onPostExecute(String result) {
                        alFinalizarDescargaVisitas(result);
                    }
                }.execute();
                return true;
            case R.id.sincronizar:
                enviarVisitas();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void mostrarReporte() {
        VisitasController visitas = new VisitasController();
        int realizadas = visitas.count("estado = 1", this);
        int pendientes = visitas.count("estado = 0", this);
        int enviadas = visitas.count("last_insert > 0", this);
        t_reporte.setText(
                "Visitas Realizadas= " + realizadas+"\n"+
                "Visitas Pendientes= " + pendientes + "\n" +
                "Visitas Enviadas= " + enviadas
        );
    }

    private void acercaDe(){
        t_acerca.setText(
                "Gestion de Avisos Version 1.1 2018-12-01 08:00:00"
        );
    }

    private void alFinalizarDescargaVisitas(String result) {
        System.out.println("alFinalizarDescargaVisitas= " + result);
        result = "{\"array\":" + result + "}";
        SesionSingleton se = SesionSingleton.getInstance();
        VisitasController vis = new VisitasController();
        JSONObject json_data = null;
        boolean flag = true;
        try {
            json_data = new JSONObject(result);
            JSONArray jsonArray = json_data.getJSONArray("array");
            json_data = new JSONObject(jsonArray.get(0).toString());
            if(json_data.getBoolean("estado")){
                flag = true;
            }else{
                Toast.makeText(this, Constants.MSG_PETICION_RECHAZADA, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            Toast.makeText(this, Constants.MSG_FORMATO_NO_VALIDO + e, Toast.LENGTH_LONG).show();
        } finally {

        }

        try {
            if(flag){
                //insertando la tabla resultados
                JSONArray jArrayResultados = json_data.getJSONArray("resultados");
                int size = jArrayResultados.length();
                ResultadosController resCon = new ResultadosController();
                Resultados resultado = null;
                for (int i = 0; i < size; ++i) {
                    if(i == 0){
                        resCon.eliminar("", this);
                    }
                    JSONObject tr = jArrayResultados.getJSONObject(i);
                    resultado = new Resultados();
                    resultado.setId(tr.getLong("id"));
                    resultado.setNombre(tr.getString("nombre"));
                    resCon.insertar(resultado, this);
                }

                //insertando la tabla anomalias
                JSONArray jArrayAnomalias = json_data.getJSONArray("anomalias");
                size = jArrayAnomalias.length();
                AnomaliasController anoCon = new AnomaliasController();
                Anomalias anomalia = null;
                for (int i = 0; i < size; ++i) {
                    if(i == 0){
                        anoCon.eliminar("", this);
                    }
                    JSONObject tr = jArrayAnomalias.getJSONObject(i);
                    anomalia = new Anomalias();
                    anomalia.setId(tr.getLong("id"));
                    anomalia.setNombre(tr.getString("nombre"));
                    anoCon.insertar(anomalia, this);
                }

                //insertando la tabla entidades pago
                JSONArray jArrayEntidades = json_data.getJSONArray("entidades");
                size = jArrayEntidades.length();
                EntidadesPagoController entCon = new EntidadesPagoController();
                EntidadesPago entidad = null;
                for (int i = 0; i < size; ++i) {
                    if(i == 0){
                        entCon.eliminar("", this);
                    }
                    JSONObject tr = jArrayEntidades.getJSONObject(i);
                    entidad = new EntidadesPago();
                    entidad.setId(tr.getLong("id"));
                    entidad.setNombre(tr.getString("nombre"));
                    entCon.insertar(entidad, this);
                }

                //insertando la tabla observaciones rapidas
                JSONArray jArrayObsRapidas = json_data.getJSONArray("observaciones_rapidas");
                size = jArrayObsRapidas.length();
                ObservacionRapidaController obsCon = new ObservacionRapidaController();
                ObservacionRapida observacion = null;
                for (int i = 0; i < size; ++i) {
                    if(i == 0){
                        obsCon.eliminar("", this);
                    }
                    JSONObject tr = jArrayObsRapidas.getJSONObject(i);
                    observacion = new ObservacionRapida();
                    observacion.setId(tr.getLong("id"));
                    observacion.setNombre(tr.getString("nombre"));
                    obsCon.insertar(observacion, this);
                }

                //insertando la tabla visitas
                JSONArray jArrayVisitas = json_data.getJSONArray("visitas");
                size = jArrayVisitas.length();
                VisitasController visCon = new VisitasController();
                Visitas visita = null;
                for (int i = 0; i < size; ++i) {

                    JSONObject tr = jArrayVisitas.getJSONObject(i);
                    visita = new Visitas();
                    visita.setId(tr.getLong("id"));
                    visita.setTipoVisita(tr.getString("tipo_visita"));
                    visita.setMunicipio(tr.getString("municipio"));
                    visita.setLocalidad(tr.getString("localidad"));
                    visita.setBarrio(tr.getString("barrio"));
                    visita.setDireccion(tr.getString("direccion"));
                    visita.setCliente(tr.getString("cliente"));
                    visita.setDeuda(tr.getLong("deuda"));
                    visita.setFacturas(tr.getInt("factura_vencida"));
                    visita.setNic(tr.getInt("nic"));
                    visita.setNis(tr.getInt("nis"));
                    visita.setMedidor(tr.getString("medidor"));
                    visita.setTarifa(tr.getString("tarifa"));
                    visita.setFechaLimiteCompromiso(tr.getString("fecha_limite_compromiso"));
                    visita.setResultado(0);
                    visita.setAnomalia(0);
                    visita.setEntidadRecaudo(0);
                    visita.setFechaPago("");
                    visita.setFechaCompromiso("");
                    visita.setPersonaContacto("");
                    visita.setCedula("");
                    visita.setTitularPago("");
                    visita.setTelefono("");
                    visita.setEmail("");
                    visita.setObservacionRapida(0);
                    visita.setObservacionAnalisis("");
                    visita.setLectura("");
                    visita.setLatitud("");
                    visita.setLongitud("");
                    visita.setOrden(0);
                    visita.setFoto("");
                    visita.setFechaRealizado("");
                    visita.setGestorAsignadoId(SesionSingleton.getInstance().getFkId());
                    visita.setGestorRealizaId(0);
                    visita.setEstado(0);
                    visita.setLastInsert(0);
                    visCon.insertar(visita, this);
                }

                Toast.makeText(this, "Datos Descargados con exito.", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        } catch (Exception e) {
            System.out.println("Exception: "+e);
            Toast.makeText(this, Constants.MSG_LEYENDO_DATOS + "de la visita. " + e, Toast.LENGTH_LONG).show();
        } finally {
            progressDialog.dismiss();
            mostrarReporte();
        }
    }

    private void enviarVisitas() {
        VisitasController vis = new VisitasController();
        ArrayList<Visitas> arrayVisitas = vis.consultar(0, 0, "estado = 1 and last_insert = 0", this);
        sizeVisitas = arrayVisitas.size();
        if(sizeVisitas > 0){
            progressDialog = Constants.dialogIndeterminate(this, "Sincronizando...");
            new AsyncTask<String, Void, String>(){
                @Override
                protected String doInBackground(String... params) {
                    return iniciarSincronizacion();
                }
                @Override
                protected void onPostExecute(String result) {
                    alFinalizarSincronizacion(result);
                }
            }.execute();
        }
    }

    private String iniciarSincronizacion(){
        VisitasController vis = new VisitasController();
        ArrayList<Visitas> arrayVisitas = vis.consultar(0, 0, "estado = 1 and last_insert = 0", this);
        sizeVisitas = arrayVisitas.size();
        for (int i = 0; i < sizeVisitas; i++){
            visitaEnviar = new Visitas();
            visitaEnviar.setId(arrayVisitas.get(i).getId());
            visitaEnviar.setResultado(arrayVisitas.get(i).getResultado());
            visitaEnviar.setAnomalia(arrayVisitas.get(i).getAnomalia());
            visitaEnviar.setEntidadRecaudo(arrayVisitas.get(i).getEntidadRecaudo());
            visitaEnviar.setFechaPago(arrayVisitas.get(i).getFechaPago());
            visitaEnviar.setFechaCompromiso(arrayVisitas.get(i).getFechaCompromiso());
            visitaEnviar.setPersonaContacto(arrayVisitas.get(i).getPersonaContacto());
            visitaEnviar.setCedula(arrayVisitas.get(i).getCedula());
            visitaEnviar.setTitularPago(arrayVisitas.get(i).getTitularPago());
            visitaEnviar.setTelefono(arrayVisitas.get(i).getTelefono());
            visitaEnviar.setEmail(arrayVisitas.get(i).getEmail());
            visitaEnviar.setObservacionRapida(arrayVisitas.get(i).getObservacionRapida());
            visitaEnviar.setLectura(arrayVisitas.get(i).getLectura());
            visitaEnviar.setObservacionAnalisis(arrayVisitas.get(i).getObservacionAnalisis());
            visitaEnviar.setLatitud(arrayVisitas.get(i).getLatitud());
            visitaEnviar.setLongitud(arrayVisitas.get(i).getLongitud());
            visitaEnviar.setOrden(arrayVisitas.get(i).getOrden());
            visitaEnviar.setFechaRealizado(arrayVisitas.get(i).getFechaRealizado());
            visitaEnviar.setFoto(arrayVisitas.get(i).getFoto());
            String response = conexionGestor.enviarVisita(visitaEnviar, SesionSingleton.getInstance().getFkId());
            System.err.println("response: " + response);
            try {
                JSONObject json_data = new JSONObject(response);
                ContentValues registro = new ContentValues();
                registro.put("last_insert", 1);
                if (json_data.getBoolean("estado")) {
                    vis.actualizar(registro, "id = " + visitaEnviar.getId(), OperarioActivity.this);
                }
            } catch (final Exception e){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(
                                OperarioActivity.this,
                                Constants.MSG_SINCRONIZACION + "\n" + e,
                                Toast.LENGTH_LONG).show();
                    }
                });
            } finally {

            }
        }

        return "Sincronizacion finalizada.";
    }

    private void alFinalizarSincronizacion(String result) {
        System.out.println("alFinalizarSincronizacion= " + result);
        mostrarReporte();
        progressDialog.dismiss();
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
}
