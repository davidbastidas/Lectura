package com.dbl.Vistas.Adaptader;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dbl.Modelos.Auditorias;
import com.dbl.R;

import java.util.ArrayList;

public class AdapterDetalleVisita extends BaseAdapter{
	private final Activity actividad;
	private ArrayList<Auditorias> detalle;

	View view;
	LayoutInflater inflater;
	TextView t_titulo, t_subtitulo;

	public AdapterDetalleVisita(Activity actividad, ArrayList<Auditorias> detalle){
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
