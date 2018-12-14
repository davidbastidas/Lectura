package com.db.Vistas.Adaptader;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.db.Modelos.Resultados;
import com.db.Modelos.Visitas;
import com.db.R;

import java.util.ArrayList;

public class AdapterVisitas extends BaseAdapter implements Filterable {
	private final Activity actividad;
	private ArrayList<Visitas> visitas;

	View view;
	LayoutInflater inflater;
	TextView titulo, barrio;

	public AdapterVisitas(Activity actividad, ArrayList<Visitas> visitas){
		super();
		this.actividad = actividad;
		this.visitas = visitas;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = actividad.getLayoutInflater();
		view = inflater.inflate(R.layout.adapter_visitas, null, true);

		titulo = view.findViewById(R.id.t_titulo);
		barrio = view.findViewById(R.id.t_subtitulo);
		titulo.setText(visitas.get(position).getDireccion().toUpperCase());
		barrio.setText(visitas.get(position).getBarrio().toUpperCase() + " - NIC: " + visitas.get(position).getNic());
		return view;
	}

	public int getCount() {
		return visitas.size();
	}

	public Object getItem(int arg0) {
		return visitas.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public Filter getFilter() {

		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {

				visitas = (ArrayList<Visitas>) results.values;
				if (results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();
				ArrayList<Visitas> FilteredArrayNames = new ArrayList<Visitas>();

				// perform your search here using the searchConstraint String.

				constraint = constraint.toString().toLowerCase();
				for (int i = 0; i < visitas.size(); i++) {
					Visitas visita = visitas.get(i);
					if (visita.getDireccion().toLowerCase().startsWith(constraint.toString()))  {
						FilteredArrayNames.add(visita);
					}
				}

				results.count = FilteredArrayNames.size();
				results.values = FilteredArrayNames;

				return results;
			}
		};

		return filter;
	}
}
