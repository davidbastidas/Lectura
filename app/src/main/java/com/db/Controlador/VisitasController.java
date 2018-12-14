package com.db.Controlador;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.db.Database.SQLite;
import com.db.Modelos.Constants;
import com.db.Modelos.Visitas;

import java.util.ArrayList;

public class VisitasController {

	int tamanoConsulta = 0;
	long lastInsert;
	public long getLastInsert() {
		return lastInsert;
	}

	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(Visitas visita, Activity activity) {
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			ContentValues registro = new ContentValues();
			registro.put("id", visita.getId());
			registro.put("tipo_visita", visita.getTipoVisita());
			registro.put("municipio", visita.getMunicipio());
			registro.put("localidad", visita.getLocalidad());
			registro.put("barrio", visita.getBarrio());
			registro.put("direccion", visita.getDireccion());
			registro.put("cliente", visita.getCliente());
			registro.put("deuda", visita.getDeuda());
			registro.put("facturas", visita.getFacturas());
			registro.put("nic", visita.getNic());
			registro.put("nis", visita.getNis());
			registro.put("medidor", visita.getMedidor());
			registro.put("tarifa", visita.getTarifa());
			registro.put("fecha_limite_compromiso", visita.getFechaLimiteCompromiso());
			registro.put("resultado", visita.getResultado());
			registro.put("anomalia", visita.getAnomalia());
			registro.put("entidad_recaudo", visita.getEntidadRecaudo());
			registro.put("fecha_pago", visita.getFechaPago());
			registro.put("fecha_compromiso", visita.getFechaCompromiso());
			registro.put("persona_contacto", visita.getPersonaContacto());
			registro.put("cedula", visita.getCedula());
			registro.put("titular_pago", visita.getTitularPago());
			registro.put("telefono", visita.getTelefono());
			registro.put("email", visita.getEmail());
			registro.put("observacion_rapida", visita.getObservacionRapida());
			registro.put("observacion_analisis", visita.getObservacionAnalisis());
			registro.put("lectura", visita.getLectura());
			registro.put("latitud", visita.getLatitud());
			registro.put("longitud", visita.getLongitud());
			registro.put("orden", 0);
			registro.put("foto", visita.getFoto());
			registro.put("fecha_realizado", visita.getFechaRealizado());
			registro.put("gestor_asignado_id", visita.getGestorAsignadoId());
			registro.put("gestor_realiza_id", 0);
			registro.put("estado", 0);
			registro.put("last_insert", 0);
			lastInsert = db.insert(Constants.TABLA_VISITAS, null, registro);
		}
	}
	public synchronized int actualizar(ContentValues registro, String where, Activity activity){
		int actualizados = 0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
	        actualizados = db.update(Constants.TABLA_VISITAS, registro, where, null);
		}
		return actualizados;
	}
	public synchronized int eliminar(String where, Activity activity){
		int registros=0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();

		if (db != null) {
			registros=db.delete(Constants.TABLA_VISITAS, where, null);
		}
		return registros;
	}
	public synchronized void eliminarTodo(Activity activity){
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			db.execSQL("DELETE FROM " + Constants.TABLA_VISITAS);
		}
	}
	public synchronized ArrayList<Visitas> consultar(int pagina, int limite, String condicion, Activity activity){
		Visitas dataSet;
		ArrayList<Visitas> visitas = new ArrayList<Visitas>();
		Cursor c = null, countCursor = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String limit = "";
		if(limite != 0){
			limit = " LIMIT " + pagina + "," + limite;
		}
		String where = "";
		if(!condicion.equals("")){
			where = " WHERE " + condicion;
		}
		c = db.rawQuery("SELECT * FROM " + Constants.TABLA_VISITAS + " " + where+" ORDER BY id "+limit, null);
		countCursor = db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_VISITAS + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		if (c.moveToFirst()) {
			do {
				dataSet = new Visitas();
				dataSet.setId(c.getLong(0));
				dataSet.setTipoVisita(c.getString(1));
				dataSet.setMunicipio(c.getString(2));
				dataSet.setLocalidad(c.getString(3));
				dataSet.setBarrio(c.getString(4));
				dataSet.setDireccion(c.getString(5));
				dataSet.setCliente(c.getString(6));
				dataSet.setDeuda(c.getLong(7));
				dataSet.setFacturas(c.getLong(8));
				dataSet.setNic(c.getLong(9));
				dataSet.setNis(c.getLong(10));
				dataSet.setMedidor(c.getString(11));
				dataSet.setTarifa(c.getString(12));
				dataSet.setFechaLimiteCompromiso(c.getString(13));
				dataSet.setResultado(c.getLong(14));
				dataSet.setAnomalia(c.getLong(15));
				dataSet.setEntidadRecaudo(c.getLong(16));
				dataSet.setFechaPago(c.getString(17));
				dataSet.setFechaCompromiso(c.getString(18));
				dataSet.setPersonaContacto(c.getString(19));
				dataSet.setCedula(c.getString(20));
				dataSet.setTitularPago(c.getString(21));
				dataSet.setTelefono(c.getString(22));
				dataSet.setEmail(c.getString(23));
				dataSet.setObservacionRapida(c.getLong(24));
				dataSet.setObservacionAnalisis(c.getString(25));
				dataSet.setLectura(c.getString(26));
				dataSet.setLatitud(c.getString(27));
				dataSet.setLongitud(c.getString(28));
				dataSet.setOrden(c.getLong(29));
				dataSet.setFoto(c.getString(30));
				dataSet.setFechaRealizado(c.getString(31));
				dataSet.setGestorAsignadoId(c.getLong(32));
				dataSet.setGestorRealizaId(c.getLong(33));
				dataSet.setEstado(c.getLong(34));
				dataSet.setLastInsert(c.getLong(35));
				visitas.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		return visitas;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where = "";
		if(!condicion.equals("")){
			where = " WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_VISITAS + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		countCursor.close();
		return tamanoConsulta;
	}

	public int ultimoOrden(Activity activity){
		Cursor countCursor = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		countCursor = db.rawQuery("SELECT max(orden) FROM " + Constants.TABLA_VISITAS, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		countCursor.close();
		return tamanoConsulta;
	}

	public synchronized ArrayList<Visitas> consultaBarrios(Activity activity){
		Visitas dataSet;
		ArrayList<Visitas> visitas = new ArrayList<Visitas>();
		Cursor c = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String limit = "";
		c = db.rawQuery("SELECT barrio FROM " + Constants.TABLA_VISITAS + " WHERE estado = 0 GROUP BY barrio ORDER BY barrio", null);
		if (c.moveToFirst()) {
			do {
				dataSet = new Visitas();
				dataSet.setBarrio(c.getString(0));
				visitas.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		return visitas;
	}
}
