package com.db.Vistas.Adaptader;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.db.Modelos.Anomalias;
import com.db.Modelos.Visitas;
import com.db.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterDetalleVisita extends BaseAdapter{
	private final Activity actividad;
	private ArrayList<Visitas> detalle;

	View view;
	LayoutInflater inflater;
	TextView t_titulo, t_subtitulo;

	public AdapterDetalleVisita(Activity actividad, ArrayList<Visitas> detalle){
		super();
		this.actividad = actividad;
		this.detalle = detalle;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = actividad.getLayoutInflater();
		view = inflater.inflate(R.layout.adapter_detalle, null, true);

		t_titulo = view.findViewById(R.id.t_titulo);
		t_subtitulo = view.findViewById(R.id.t_subtitulo);
		t_titulo.setText(detalle.get(position).getCliente().toUpperCase());
		t_subtitulo.setText(detalle.get(position).getPersonaContacto().toUpperCase());
		return view;
	}

	public int getCount() {
		return detalle.size();
	}

	public Object getItem(int arg0) {
		return detalle.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}

}
