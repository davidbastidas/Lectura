package com.db.Controlador;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.db.Database.SQLite;
import com.db.Modelos.Anomalias;
import com.db.Modelos.Constants;
import com.db.Modelos.EntidadesPago;

import java.util.ArrayList;

public class EntidadesPagoController {

	int tamanoConsulta = 0;
	long lastInsert;
	public long getLastInsert() {
		return lastInsert;
	}

	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(EntidadesPago entidadesPago, Activity activity) {
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			ContentValues registro = new ContentValues();
			registro.put("id", entidadesPago.getId());
			registro.put("nombre", entidadesPago.getNombre());
			lastInsert = db.insert(Constants.TABLA_ENTIDADES_PAGO, null, registro);
		}
	}
	public synchronized int actualizar(ContentValues registro, String where, Activity activity){
		int actualizados = 0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
	        actualizados = db.update(Constants.TABLA_ENTIDADES_PAGO, registro, where, null);
		}
		return actualizados;
	}
	public synchronized int eliminar(String where, Activity activity){
		int registros = 0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();

		if (db != null) {
			registros = db.delete(Constants.TABLA_ENTIDADES_PAGO, where, null);
		}
		return registros;
	}
	public synchronized ArrayList<EntidadesPago> consultar(int pagina, int limite, String condicion, Activity activity){
		EntidadesPago dataSet;
		ArrayList<EntidadesPago> entidadesPagos = new ArrayList<EntidadesPago>();
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
		c = db.rawQuery("SELECT * FROM " + Constants.TABLA_ENTIDADES_PAGO + " " + where+" ORDER BY nombre "+limit, null);
		countCursor = db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_ENTIDADES_PAGO + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		if (c.moveToFirst()) {
			do {
				dataSet = new EntidadesPago();
				dataSet.setId(c.getLong(0));
				dataSet.setNombre(c.getString(1));
				entidadesPagos.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		return entidadesPagos;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where = "";
		if(!condicion.equals("")){
			where = " WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_ENTIDADES_PAGO + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		countCursor.close();
		return tamanoConsulta;
	}
}
