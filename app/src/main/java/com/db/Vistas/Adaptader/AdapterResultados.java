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
import com.db.R;

import java.util.ArrayList;

public class AdapterResultados extends BaseAdapter implements Filterable {
	private final Activity actividad;
	private ArrayList<Resultados> resultadosOriginal;
	private ArrayList<Resultados> resultadosFiltrado;

	View view;
	LayoutInflater inflater;
	TextView titulo;

	public AdapterResultados(Activity actividad, ArrayList<Resultados> resultados){
		super();
		this.actividad = actividad;
		this.resultadosOriginal = resultados;
		this.resultadosFiltrado = resultados;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = actividad.getLayoutInflater();
		view = inflater.inflate(R.layout.adapter_resultados, null, true);

		titulo = view.findViewById(R.id.t_titulo);
		titulo.setText(resultadosFiltrado.get(position).getNombre().toUpperCase());
		return view;
	}

	public int getCount() {
		return resultadosFiltrado.size();
	}

	public Object getItem(int arg0) {
		return resultadosFiltrado.get(arg0);
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

				resultadosFiltrado = (ArrayList<Resultados>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();
				ArrayList<Resultados> FilteredArrayNames = new ArrayList<Resultados>();

				// perform your search here using the searchConstraint String.

				constraint = constraint.toString().toLowerCase();
				for (int i = 0; i < resultadosOriginal.size(); i++) {
					Resultados resultado = resultadosOriginal.get(i);
					if (resultado.getNombre().toLowerCase().startsWith(constraint.toString()))  {
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
