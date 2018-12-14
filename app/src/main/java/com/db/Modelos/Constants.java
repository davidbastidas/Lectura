package com.db.Modelos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

import java.io.File;

public final class Constants {

    private static SesionSingleton sesion = SesionSingleton.getInstance();

    public static final int PERMISOS_REQUEST_CODE = 100;
    public static final int VISITA_REQUEST_CODE = 999;
    public static final int FOTO_REQUEST_CODE = 998;

    /**resultados*/
    public static final int RES_PAGO_TOTAL = 12;
    public static final int RES_ENTREGA_FACTURA = 13;
    public static final int RES_ACUERDO_PAGO = 3;
    public static final int RES_COMPROMISO = 14;
    public static final int RES_ABONO = 4;
    public static final int RES_CLIENTE_NO_PUEDE_PAGAR = 15;
    public static final int RES_CLIENTE_NO_TIENE_VOLUNTAD_PAGO = 16;
    public static final int RES_CLIENTE_CONDICIONA_PAGO = 17;
    public static final int RES_CONTACTO_NO_EFECTIVO = 1;
    public static final int RES_CLIENTE_CANCELO = 5;

    public static final int RES_REGISTRO_NO_VALIDO = 6;
    public static final int RES_DEUDA_CERO = 7;
    public static final int RES_EVALUAR_CAPACIDAD_OPERATIVA = 8;
    public static final int RES_GESTION_NO_PROCEDE = 9;
    public static final int RES_EN_CURSO = 10;
    public static final int RES_REPROGRAMACION = 11;

    /**anomalias*/
    public static final int AN_OTROS = 22;

    public static final String EXTRA_VISITA_ID = "visita_id";
    public static final String EXTRA_NIC = "nic";
    public static final String EXTRA_MEDIDOR = "medidor";
    public static final String EXTRA_DIRECCION = "direccion";
    public static final String EXTRA_REALIZADO = "realizado";
    public static final String EXTRA_BARRIO = "barrio";

    public static final String DB_NAME = "db_gestion";
    public static final String TABLA_USUARIOS = "usuarios";
    public static final String TABLA_VISITAS = "visitas";
    public static final String TABLA_RESULTADOS = "resultados";
    public static final String TABLA_ANOMALIAS = "anomalias";
    public static final String TABLA_ENTIDADES_PAGO = "entidades_pago";
    public static final String TABLA_OBSERVACION_RAPIDA = "observacion_rapida";

    /** nombre de la configuracion base*/
    public static final String CONFIGURACION = "configuracion";
    public static final String PASSWORDADMIN = "passwordAdmin";
    public static final String PASSWORDSERVICIOS = "passwordServicios";
    public static final String ESTADODATOS = "estado_datos";
    public static final String ESTADOENVIO = "estado_envio";
    public static final String IP = "ip";
    public static final String RUTAWEB = "ruta";

    /** url*/
    //ip/control/api/public/--ruta de el controlador--/
    private static String URL_BASE = "http://" + sesion.getIp() + File.separator + sesion.getRuta() + File.separator +  "api" + File.separator;
    public static final String ROUTE_LOGIN = URL_BASE + "login";
    public static final String ROUTE_VISITAS = URL_BASE + "avisos/getVisitas";
    public static final String ROUTE_ACTUALIZAR_VISITA = URL_BASE + "avisos/actualizarVisita";

    public static ProgressDialog dialogIndeterminate(Context context, String mensaje){
        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Procesando...");
        pd.setMessage(mensaje);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        return pd;
    }

    /** mensajes estandar*/
    public static final String MSG_FORMATO_NO_VALIDO = "La peticion no se realizo. Verifique su conexion o Contacte al administrador.";
    public static final String MSG_SINCRONIZACION = "La visita no se pudo enviar.";
    public static final String MSG_PETICION_RECHAZADA = "Peticion Rechazada.";
    public static final String MSG_LEYENDO_DATOS = "Ocurrio un error leyendo los datos ";
    public static final String MSG_ENVIO_VISITA = "Visita enviada con exito!.";

    public static boolean isGpsActivo(Activity actividad){
        boolean status = false;
        LocationManager locManager = null;
        locManager = (LocationManager) actividad.getSystemService(Context.LOCATION_SERVICE);
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            status = true;
        }
        return status;
    }

    public static void ActivarGPS(Activity actividad) {
        Toast.makeText(actividad, "Por favor active su GPS", Toast.LENGTH_SHORT)
                .show();
        Intent settingsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        actividad.startActivityForResult(settingsIntent, 1);
    }
}
