package br.com.alura.alurator;

import br.com.alura.alurator.protocolo.Request;

import java.util.Map;

public class Alurator {



	public Object executa(String url) {
		// TODO - processa a requisicao executando o metodo
		// da classe em questao

		Request request = new Request(url);
		String nomeController = request.getNomeController();
		String nomeMetodo = request.getNomeMetodo();
		Map<String, Object> params = request.getQueryParams();


		return null;
	}
}
