package com.db.Vistas;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.db.Controlador.EntidadesPagoController;
import com.db.Modelos.Constants;
import com.db.Modelos.EntidadesPago;
import com.db.Modelos.VisitaSesion;
import com.db.R;
import com.db.Vistas.Adaptader.AdapterEntidades;

import java.util.ArrayList;
import java.util.Calendar;

public class EntidadesActivity extends AppCompatActivity {

    EditText e_buscar_entidad;
    ListView l_entidades;
    AdapterEntidades adapter = null;

    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entidades);
        setTitle("Elija un Punto de Pago");
        e_buscar_entidad = findViewById(R.id.e_buscar_entidad);
        l_entidades = findViewById(R.id.l_entidades);

        EntidadesPagoController enti = new EntidadesPagoController();
        ArrayList<EntidadesPago> entidades = enti.consultar(0, 0, "", this);
        adapter = new AdapterEntidades(this, entidades);
        l_entidades.setAdapter(adapter);
        l_entidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                try {
                    EntidadesPago entidad = (EntidadesPago) parent.getItemAtPosition(position);
                    VisitaSesion.getInstance().setEntidadRecaudo(entidad.getId());
                    obtenerFecha();
                } catch (Exception ex) {
                    Toast.makeText(EntidadesActivity.this, "Error en la entidad: " + ex, Toast.LENGTH_LONG).show();
                }
            }
        });
        e_buscar_entidad.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                EntidadesActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {}
        });
    }

    private void irDatosCliente(){
        if (Constants.isGpsActivo(this)) {
            Intent intentar = new Intent(EntidadesActivity.this, DatosClienteActivity.class);
            startActivityForResult(intentar, Constants.VISITA_REQUEST_CODE);
        } else {
            Constants.ActivarGPS(this);
        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                VisitaSesion.getInstance().setFechaPago(diaFormateado + BARRA + mesFormateado + BARRA + year);
                irDatosCliente();
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
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
