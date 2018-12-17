package com.dbl.Vistas;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dbl.Controlador.AnomaliasController;
import com.dbl.Controlador.AuditoriaController;
import com.dbl.Controlador.GestorConexion;
import com.dbl.Controlador.ObservacionRapidaController;
import com.dbl.Controlador.PciController;
import com.dbl.Modelos.Anomalias;
import com.dbl.Modelos.AuditoriaSesion;
import com.dbl.Modelos.Auditorias;
import com.dbl.Modelos.Constants;
import com.dbl.Modelos.ObservacionRapida;
import com.dbl.Modelos.Pci;
import com.dbl.Modelos.SesionSingleton;
import com.dbl.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OperarioActivity extends AppCompatActivity {

    Button b_buscar, b_barrios, b_visitas;
    TextView t_reporte, t_acerca, t_perfil;
    ProgressDialog progressDialog = null;
    Auditorias auditoriaEnviar = null;
    Pci pciEnviar = null;
    private int sizeVisitas = 0, sizePci = 0;
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
                startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
            }
        });

        b_barrios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentar = new Intent(OperarioActivity.this, BarriosActivity.class);
                startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
            }
        });

        b_visitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentar = new Intent(OperarioActivity.this, ServiciosActivity.class);
                startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
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
        AuditoriaSesion.resetSesion();
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
                progressDialog = Constants.dialogIndeterminate(this, "Descargando Auditorias...");
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
        AuditoriaController audcont = new AuditoriaController();
        int audRealizadas = audcont.count("estado = 1", this);
        int audPendientes = audcont.count("estado = 0", this);
        int audEnviadas = audcont.count("last_insert > 0", this);

        PciController pcicont = new PciController();
        int pciRealizados = pcicont.count("estado = 1", this);
        int pciPendientes = pcicont.count("estado = 0", this);
        int pciEnviados = pcicont.count("last_insert > 0", this);
        t_reporte.setText(
                "Auditorias Realizadas = " + audRealizadas+"\n"+
                "Auditorias Pendientes = " + audPendientes + "\n" +
                "Auditorias Enviadas = " + audEnviadas + "\n" +
                "PCI Realizados = " + pciRealizados+"\n"+
                "PCI Pendientes = " + pciPendientes + "\n" +
                "PCI Enviados = " + pciEnviados
        );
    }

    private void acercaDe(){
        t_acerca.setText(
                "Lectura de Medidores Version 1.1 2018-12-24 08:00:00"
        );
    }

    private void alFinalizarDescargaVisitas(String result) {
        System.out.println("alFinalizarDescargaVisitas= " + result);
        result = "{\"array\":" + result + "}";
        SesionSingleton se = SesionSingleton.getInstance();
        AuditoriaController audcont = new AuditoriaController();
        PciController pcicont = new PciController();
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
                //insertando la tabla anomalias
                JSONArray jArrayAnomalias = json_data.getJSONArray("anomalias");
                int size = jArrayAnomalias.length();
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

                //insertando la tabla auditorias
                JSONArray jArrayVisitas = json_data.getJSONArray("auditorias");
                size = jArrayVisitas.length();
                AuditoriaController audCon = new AuditoriaController();
                Auditorias visita = null;
                for (int i = 0; i < size; ++i) {
                    JSONObject tr = jArrayVisitas.getJSONObject(i);
                    visita = new Auditorias();
                    visita.setId(tr.getLong("id"));
                    visita.setBarrio(tr.getString("barrio"));
                    visita.setLocalidad(tr.getString("localidad"));
                    visita.setCliente(tr.getString("cliente"));
                    visita.setDireccion(tr.getString("direccion"));
                    visita.setNic(tr.getLong("nic"));
                    visita.setRuta(tr.getLong("ruta"));
                    visita.setItin(tr.getLong("itin"));
                    visita.setMedidor(tr.getString("medidor"));
                    visita.setMotivo(tr.getString("motivo"));
                    visita.setNis(tr.getInt("nis"));
                    visita.setLectura("");
                    visita.setAnomalia(0);
                    visita.setObservacionRapida(0);
                    visita.setHabitado(0);
                    visita.setVisible(0);
                    visita.setObservacionAnalisis("");
                    visita.setLatitud("");
                    visita.setLongitud("");
                    visita.setOrden(0);
                    visita.setFoto("");
                    visita.setFechaRealizado("");
                    visita.setLectorAsignadoId(SesionSingleton.getInstance().getFkId());
                    visita.setLectorRealizaId(0);
                    visita.setEstado(0);
                    visita.setLastInsert(0);
                    audCon.insertar(visita, this);
                }

                //insertando la tabla pci
                JSONArray jArrayPci = json_data.getJSONArray("pci");
                size = jArrayPci.length();
                PciController pciCon = new PciController();
                Pci pci = null;
                for (int i = 0; i < size; ++i) {
                    JSONObject tr = jArrayPci.getJSONObject(i);
                    pci = new Pci();
                    pci.setId(tr.getLong("id"));
                    pci.setCt(tr.getString("ct"));
                    pci.setMt(tr.getString("mt"));
                    pci.setDireccion(tr.getString("direccion"));
                    pci.setMedidor(tr.getString("medidor"));
                    pci.setMedidorAnterior(tr.getString("medidor_anterior"));
                    pci.setMedidorPosterior(tr.getString("medidor_posterior"));
                    pci.setBarrio(tr.getString("barrio"));
                    pci.setLectura("");
                    pci.setAnomalia(0);
                    pci.setObservacionAnalisis("");
                    pci.setMunicipio(tr.getString("municipio"));
                    pci.setCodigo(tr.getString("codigo"));
                    pci.setAnAnterior(tr.getString("an_anterior"));
                    pci.setLecturaAnterior(tr.getString("lectura_anterior"));
                    pci.setUnicom(tr.getLong("unicom"));
                    pci.setRuta(tr.getLong("ruta"));
                    pci.setItin(tr.getLong("itin"));
                    pci.setLatitud("");
                    pci.setLongitud("");
                    pci.setOrden(0);
                    pci.setFoto("");
                    pci.setFechaRealizado("");
                    pci.setLectorAsignadoId(SesionSingleton.getInstance().getFkId());
                    pci.setLectorRealizaId(0);
                    pci.setEstado(0);
                    pci.setLastInsert(0);
                    pciCon.insertar(pci, this);
                }

                Toast.makeText(this, "Datos Descargados con exito.", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        } catch (Exception e) {
            System.out.println("Exception: "+e);
            Toast.makeText(this, Constants.MSG_LEYENDO_DATOS + "de las visitas. " + e, Toast.LENGTH_LONG).show();
        } finally {
            progressDialog.dismiss();
            mostrarReporte();
        }
    }

    private void enviarVisitas() {
        AuditoriaController audCont = new AuditoriaController();
        ArrayList<Auditorias> arrayAuditorias = audCont.consultar(0, 0, "estado = 1 and last_insert = 0", this);
        sizeVisitas = arrayAuditorias.size();

        PciController pciCont = new PciController();
        ArrayList<Pci> arrayPci = pciCont.consultar(0, 0, "estado = 1 and last_insert = 0", this);
        sizePci = arrayPci.size();
        if(sizeVisitas > 0 || sizePci > 0){
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
        AuditoriaController audCont = new AuditoriaController();
        ArrayList<Auditorias> arrayAuditorias = audCont.consultar(0, 0, "estado = 1 and last_insert = 0", this);
        sizeVisitas = arrayAuditorias.size();
        for (int i = 0; i < sizeVisitas; i++){
            auditoriaEnviar = new Auditorias();
            auditoriaEnviar.setId(arrayAuditorias.get(i).getId());
            auditoriaEnviar.setLectura(arrayAuditorias.get(i).getLectura());
            auditoriaEnviar.setAnomalia(arrayAuditorias.get(i).getAnomalia());
            auditoriaEnviar.setObservacionRapida(arrayAuditorias.get(i).getObservacionRapida());
            auditoriaEnviar.setObservacionAnalisis(arrayAuditorias.get(i).getObservacionAnalisis());
            auditoriaEnviar.setLatitud(arrayAuditorias.get(i).getLatitud());
            auditoriaEnviar.setLongitud(arrayAuditorias.get(i).getLongitud());
            auditoriaEnviar.setOrden(arrayAuditorias.get(i).getOrden());
            auditoriaEnviar.setFechaRealizado(arrayAuditorias.get(i).getFechaRealizado());
            auditoriaEnviar.setFoto(arrayAuditorias.get(i).getFoto());
            String response = conexionGestor.enviarAuditoria(auditoriaEnviar, SesionSingleton.getInstance().getFkId());
            System.err.println("response: " + response);
            try {
                JSONObject json_data = new JSONObject(response);
                ContentValues registro = new ContentValues();
                registro.put("last_insert", 1);
                if (json_data.getBoolean("estado")) {
                    audCont.actualizar(registro, "id = " + auditoriaEnviar.getId(), OperarioActivity.this);
                }
            } catch (final Exception e){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(
                                OperarioActivity.this,
                                Constants.MSG_SINCRONIZACION + ". Auditoria.\n" + e,
                                Toast.LENGTH_LONG).show();
                    }
                });
            } finally {

            }
        }

        PciController pciCont = new PciController();
        ArrayList<Pci> arrayPci = pciCont.consultar(0, 0, "estado = 1 and last_insert = 0", this);
        sizePci = arrayPci.size();
        for (int i = 0; i < sizePci; i++){
            pciEnviar = new Pci();
            pciEnviar.setId(arrayPci.get(i).getId());
            pciEnviar.setLectura(arrayPci.get(i).getLectura());
            pciEnviar.setAnomalia(arrayPci.get(i).getAnomalia());
            pciEnviar.setObservacionAnalisis(arrayPci.get(i).getObservacionAnalisis());
            pciEnviar.setLatitud(arrayPci.get(i).getLatitud());
            pciEnviar.setLongitud(arrayPci.get(i).getLongitud());
            pciEnviar.setOrden(arrayPci.get(i).getOrden());
            pciEnviar.setFechaRealizado(arrayPci.get(i).getFechaRealizado());
            pciEnviar.setFoto(arrayPci.get(i).getFoto());
            String response = conexionGestor.enviarPci(pciEnviar, SesionSingleton.getInstance().getFkId());
            System.err.println("response: " + response);
            try {
                JSONObject json_data = new JSONObject(response);
                ContentValues registro = new ContentValues();
                registro.put("last_insert", 1);
                if (json_data.getBoolean("estado")) {
                    pciCont.actualizar(registro, "id = " + pciEnviar.getId(), OperarioActivity.this);
                }
            } catch (final Exception e){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(
                                OperarioActivity.this,
                                Constants.MSG_SINCRONIZACION + ". PCI.\n" + e,
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