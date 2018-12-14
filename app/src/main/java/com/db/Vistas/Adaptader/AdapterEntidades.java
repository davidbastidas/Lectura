package com.db.Vistas.Adaptader;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.db.Modelos.EntidadesPago;
import com.db.R;

import java.util.ArrayList;

public class AdapterEntidades extends BaseAdapter implements Filterable {
	private final Activity actividad;
	private ArrayList<EntidadesPago> entidadesOriginal;
	private ArrayList<EntidadesPago> entidadesFiltrado;

	View view;
	LayoutInflater inflater;
	TextView titulo;

	public AdapterEntidades(Activity actividad, ArrayList<EntidadesPago> entidades){
		super();
		this.actividad = actividad;
		this.entidadesOriginal = entidades;
		this.entidadesFiltrado = entidades;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = actividad.getLayoutInflater();
		view = inflater.inflate(R.layout.adapter_entidad, null, true);

		titulo = view.findViewById(R.id.t_titulo);
		titulo.setText(entidadesFiltrado.get(position).getNombre().toUpperCase());
		return view;
	}

	public int getCount() {
		return entidadesFiltrado.size();
	}

	public Object getItem(int arg0) {
		return entidadesFiltrado.get(arg0);
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

				entidadesFiltrado = (ArrayList<EntidadesPago>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();
				ArrayList<EntidadesPago> FilteredArrayNames = new ArrayList<EntidadesPago>();

				// perform your search here using the searchConstraint String.

				constraint = constraint.toString().toLowerCase();
				for (int i = 0; i < entidadesOriginal.size(); i++) {
					EntidadesPago entidades = AdapterEntidades.this.entidadesOriginal.get(i);
					if (entidades.getNombre().toLowerCase().startsWith(constraint.toString()))  {
						FilteredArrayNames.add(entidades);
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
