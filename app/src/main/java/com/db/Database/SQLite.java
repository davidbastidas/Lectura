package com.db.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.db.Modelos.Constants;

public class SQLite extends SQLiteOpenHelper {

	private static SQLite mInstance;
	private static SQLiteDatabase myWritableDb;

	private SQLite(Context context) {
		super(context, Constants.DB_NAME, null, 1);
	}

	/**
     * Get default instance of the class to keep it a singleton
     *
     * @param context
     * the application context
     */
	public static SQLite getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new SQLite(context);
		}
		return mInstance;
	}

	/**
     * Returns a writable database instance in order not to open and close many
     * SQLiteDatabase objects simultaneously
     *
     * @return a writable instance to SQLiteDatabase
     */
	public SQLiteDatabase getMyWritableDatabase() {
		if ((myWritableDb == null) || (!myWritableDb.isOpen())) {
			myWritableDb = this.getWritableDatabase();
		}

		return myWritableDb;
	}

	@Override
    public void close() {
        super.close();
        if (myWritableDb != null) {
            myWritableDb.close();
            myWritableDb = null;
        }
    }
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + Constants.TABLA_USUARIOS + "(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT," +
				" nickname TEXT," +
				" tipo int," +
				" fk_delegacion int," +
				" fk_id int" +
				")");

		db.execSQL("create table " + Constants.TABLA_VISITAS + "(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" tipo_visita TEXT," +
				" municipio TEXT," +
				" localidad TEXT," +
				" barrio TEXT," +
				" direccion TEXT," +
				" cliente TEXT," +
				" deuda INTEGER," +
				" facturas INTEGER," +
				" nic INTEGER," +
				" nis INTEGER," +
				" medidor TEXT," +
				" tarifa TEXT," +
				" fecha_limite_compromiso DATE," +
				" resultado INTEGER," +
				" anomalia INTEGER," +
				" entidad_recaudo INTEGER," +
				" fecha_pago DATE," +
				" fecha_compromiso DATE," +
				" persona_contacto TEXT," +
				" cedula TEXT," +
				" titular_pago TEXT," +
				" telefono TEXT," +
				" email TEXT," +
				" observacion_rapida INTEGER," +
				" observacion_analisis TEXT," +
				" lectura TEXT," +
				" latitud TEXT," +
				" longitud TEXT," +
				" orden INTEGER," +
				" foto TEXT," +
				" fecha_realizado DATETIME," +
				" gestor_asignado_id INTEGER," +
				" gestor_realiza_id INTEGER," +
				" estado INTEGER," +
				" last_insert INTEGER" +
				")");

		db.execSQL("create table " + Constants.TABLA_RESULTADOS + "(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT" +
				")");

		db.execSQL("create table " + Constants.TABLA_ANOMALIAS + "(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT" +
				")");

		db.execSQL("create table " + Constants.TABLA_ENTIDADES_PAGO + "(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT" +
				")");

		db.execSQL("create table " + Constants.TABLA_OBSERVACION_RAPIDA + "(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT" +
				")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,
                          int versionNueva) {
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLA_USUARIOS);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLA_VISITAS);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLA_RESULTADOS);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLA_ANOMALIAS);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLA_ENTIDADES_PAGO);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLA_OBSERVACION_RAPIDA);
		onCreate(db);
	}
}
