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

public class AdapterBarrios extends BaseAdapter implements Filterable {
	private final Activity actividad;
	private ArrayList<Visitas> barriosOriginal;
	private ArrayList<Visitas> barriosFiltrado;

	View view;
	LayoutInflater inflater;
	TextView titulo;

	public AdapterBarrios(Activity actividad, ArrayList<Visitas> barrios){
		super();
		this.actividad = actividad;
		this.barriosOriginal = barrios;
		this.barriosFiltrado = barrios;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = actividad.getLayoutInflater();
		view = inflater.inflate(R.layout.adapter_barrios, null, true);

		titulo = view.findViewById(R.id.t_titulo);
		titulo.setText(barriosFiltrado.get(position).getBarrio().toUpperCase());
		return view;
	}

	public int getCount() {
		return barriosFiltrado.size();
	}

	public Object getItem(int arg0) {
		return barriosFiltrado.get(arg0);
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

				barriosFiltrado = (ArrayList<Visitas>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();
				ArrayList<Visitas> FilteredArrayNames = new ArrayList<Visitas>();

				// perform your search here using the searchConstraint String.

				constraint = constraint.toString().toLowerCase();
				for (int i = 0; i < barriosOriginal.size(); i++) {
					Visitas resultado = barriosOriginal.get(i);
					if (resultado.getBarrio().toLowerCase().startsWith(constraint.toString()))  {
						FilteredArrayNames.add(resultado);
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
