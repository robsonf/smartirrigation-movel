package br.ifce.crato;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ManipulaWebService {
	private static final String url = "http://192.168.0.103:8080/smartirrigation/rest/agendas/";
	public List<AgendaWS> agendas = new ArrayList<AgendaWS>();
	public static Context c;
	

	public String getRESTFileContent(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);

		try {
			HttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = toString(instream);

				instream.close();
				return result;
			}
		} catch (Exception e) {
			Log.e("NGVL", "Falha ao acessar Web service", e);
		}
		return null;
	}

	public boolean apagarTodasAgendaWSs(){
		boolean retorno = false;
		List<AgendaWS> agendas = recuperarAgendaWSs();
		if(agendas != null){
			retorno = true;
			for (AgendaWS a : agendas) {
				if(a!=null){
					apagarAgendaWS(a);
				}
			}
		}
		return retorno;
	}
	
	public List<AgendaWS> recuperarAgendaWSs() {
		String result = getRESTFileContent(url);
		if (result == null) {
			Log.e("NGVL", "Falha ao acessar WS");
			return null;
		}
		agendas = new ArrayList<AgendaWS>();
		try {
			JSONArray ja = new JSONArray(result);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject agenda = ja.getJSONObject(i);
				AgendaWS a = new AgendaWS();
				a.setId(agenda.getInt("id"));
				a.setHoraInicial(agenda.getString("horaInicial"));
				a.setHoraFinal(agenda.getString("horaFinal"));
				a.setEstado(agenda.getBoolean("estado"));
				a.setSeg(agenda.getBoolean("seg"));
				a.setTer(agenda.getBoolean("ter"));
				a.setQua(agenda.getBoolean("qua"));
				a.setQui(agenda.getBoolean("qui"));
				a.setSex(agenda.getBoolean("sex"));
				a.setSab(agenda.getBoolean("sab"));
				a.setDom(agenda.getBoolean("dom"));
				JSONArray setores = agenda.getJSONArray("setores");
				List<SetorWS> s = new ArrayList<SetorWS>();
				for (int j = 0; j < setores.length(); j++) {
					JSONObject setor = setores.getJSONObject(j);
					s.add(new SetorWS(setor.getString("descricao"), setor
							.getInt("id")));
				}
				a.setSetores(s);
				agendas.add(a);
			}
		} catch (JSONException e) {
			Log.e("NGVL", "Erro no parsing do JSON", e);
		}

		return agendas;
	}

	public void recuperarAgendaWS(int id) {
		// Observe o ID da pessoa no final da URL
		String result = getRESTFileContent(url + id);
		if (result == null) {
			Log.e("NGVL", "Falha ao acessar WS");
			return;
		}
		try {
			JSONObject agenda = new JSONObject(result);
			AgendaWS a = new AgendaWS();
			a.setId(agenda.getInt("id"));
			a.setHoraInicial(agenda.getString("horaInicial"));
			a.setHoraFinal(agenda.getString("horaFinal"));
			a.setEstado(agenda.getBoolean("estado"));
			a.setSeg(agenda.getBoolean("seg"));
			a.setTer(agenda.getBoolean("ter"));
			a.setQua(agenda.getBoolean("qua"));
			a.setQui(agenda.getBoolean("qui"));
			a.setSex(agenda.getBoolean("sex"));
			a.setSab(agenda.getBoolean("sab"));
			a.setDom(agenda.getBoolean("dom"));
			JSONArray setores = agenda.getJSONArray("setores");
			List<SetorWS> s = new ArrayList<SetorWS>();
			for (int j = 0; j < setores.length(); j++) {
				JSONObject setor = setores.getJSONObject(j);
				s.add(new SetorWS(setor.getString("descricao"), setor
						.getInt("id")));
			}
			a.setSetores(s);
		} catch (JSONException e) {
			Log.e("NGVL", "Erro no parsing do JSON", e);
		}
	}


	public void apagarAgendaWS(AgendaWS a) {
		try {
			StringBuffer sbAgendaWS = new StringBuffer();
			executaHTTP(sbAgendaWS, String.valueOf(a.getId()), "DELETE");
		} catch (MalformedURLException e) {
			Log.e("NGVL", "Erro no parsing do JSON", e);
		} catch (IOException e) {
			Log.e("NGVL", "Erro no parsing do JSON", e);
		} catch (Exception e) {
			Log.e("NGVL", "Erro no parsing do JSON", e);
		}
	}

	public void adicionarAgendaWS(AgendaWS a) {
		try {
			/*
			 * {\
			 * "id\":1,\"horaInicial\":\"21:00\",\"horaFinal\":\"23:30\",\"estado\":true,\"seg\":true,\"ter\":false,\"qua\":true,\"qui\":false,\"sex\":true,\"sab\":false,\"dom\":true,
			 * \
			 * "setores\":[{\"id\":1,\"descricao\":\"setor1\"},{\"id\":4,\"descricao\":\"setor4\"}]}
			 */
			StringBuffer sbAgendaWS = new StringBuffer();

			sbAgendaWS.append("{\"id\":");
			sbAgendaWS.append(a.getId());
			sbAgendaWS.append(",\"horaInicial\":\"");
			sbAgendaWS.append(a.getHoraInicial());
			sbAgendaWS.append("\",\"horaFinal\":\"");
			sbAgendaWS.append(a.getHoraFinal());
			sbAgendaWS.append("\",\"estado\":");
			sbAgendaWS.append(a.isEstado());
			sbAgendaWS.append(",\"seg\":");
			sbAgendaWS.append(a.isSeg());
			sbAgendaWS.append(",\"ter\":");
			sbAgendaWS.append(a.isTer());
			sbAgendaWS.append(",\"qua\":");
			sbAgendaWS.append(a.isQua());
			sbAgendaWS.append(",\"qui\":");
			sbAgendaWS.append(a.isQui());
			sbAgendaWS.append(",\"sex\":");
			sbAgendaWS.append(a.isSex());
			sbAgendaWS.append(",\"sab\":");
			sbAgendaWS.append(a.isSab());
			sbAgendaWS.append(",\"dom\":");
			sbAgendaWS.append(a.isDom());
			sbAgendaWS.append(",\"setores\":[");
			List<SetorWS> setores = a.getSetores();
			for (int i = 0; i < setores.size(); i++) {
				SetorWS setor = setores.get(i);
				if (setor != null) {
					sbAgendaWS.append("{\"id\":" + setor.getId()
							+ ",\"descricao\":\"" + setor.getDescricao()
							+ "\"}");
					if (setores.size() > 1 && i != (setores.size() - 1))
						sbAgendaWS.append(",");
				}
			}
			sbAgendaWS.append("]}");

			executaHTTP(sbAgendaWS, "", "POST");

		} catch (MalformedURLException e) {
			Log.e("NGVL", "Erro no parsing do JSON", e);
		} catch (IOException e) {
			Log.e("NGVL", "Erro no parsing do JSON", e);
		} catch (Exception e) {
			Log.e("NGVL", "Erro no parsing do JSON", e);
		}
	}

	private void executaHTTP(StringBuffer sbAgendaWS, String parametro,
			String tipo) throws MalformedURLException, IOException,
			ProtocolException {

		HttpURLConnection httpcon = (HttpURLConnection) ((new URL(
				ManipulaWebService.url + parametro).openConnection()));
		if (!tipo.equalsIgnoreCase("DELETE")) {
			httpcon.setDoOutput(true);
		}
		httpcon.setDoInput(true);
		httpcon.setRequestProperty("Content-Type", "application/json");
		httpcon.setRequestProperty("Accept", "application/json");
		httpcon.setRequestMethod(tipo);
		httpcon.connect();

		if (!tipo.equalsIgnoreCase("DELETE")) {
			byte[] outputBytes = (sbAgendaWS.toString()).getBytes("UTF-8");
			OutputStream os = httpcon.getOutputStream();
			os.write(outputBytes);
			os.flush();
			os.close();
		}
		InputStream is = httpcon.getInputStream();
		String saida = is.toString();
	}

	private String toString(InputStream is) throws IOException {

		byte[] bytes = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int lidos;
		while ((lidos = is.read(bytes)) > 0) {
			baos.write(bytes, 0, lidos);
		}
		return new String(baos.toByteArray());
	}

}
