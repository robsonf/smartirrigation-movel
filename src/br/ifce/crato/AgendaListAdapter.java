package br.ifce.crato;

import java.util.List;

import br.ifce.crato.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AgendaListAdapter extends BaseAdapter {
	private Context context;
	private List<Agenda> lista;

	public AgendaListAdapter(Context context, List<Agenda> lista) {
		this.context = context;
		this.lista = lista;
	}

	public int getCount() {
		return lista.size();
	}

	public Object getItem(int position) {
		return lista.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// Recupera o Carro da posi��o atual
		Agenda a = lista.get(position);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.agenda_linha_tabela, null);

		// Atualiza o valor do TextView
		TextView linha1 = (TextView) view.findViewById(R.id.linha1);
		String l1 = "";
		l1 += a.horaInicial + " : " + a.horaFinal + " - Estado: " + (a.estado==1?"On":"Off"); 
		linha1.setText(l1);

		TextView linha2 = (TextView) view.findViewById(R.id.linha2);
		String l2 = "";
		l2 += "Repetições: " +(a.seg==1?"Seg ":"") + (a.ter==1?"Ter ":"") + (a.qua==1?"Qua ":"") + (a.qui==1?"Qui ":"") +(a.sex==1?"Sex ":"")+(a.sab==1?"Sab ":"")+(a.dom==1?"Dom ":""); 
		linha2.setText(l2);

		TextView linha3 = (TextView) view.findViewById(R.id.linha3);
		String l3 = "";
		l3 += "Setores: " +(a.s1==1?"1 ":"")+(a.s2==1?"2 ":"")+(a.s3==1?"3 ":"")+(a.s4==1?"4 ":""); 
		linha3.setText(l3);

		return view;
	}
	
}