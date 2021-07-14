package br.com.alura.alurator;

import br.com.alura.alurator.Reflexao.Reflexao;
import br.com.alura.alurator.protocolo.Request;

import java.util.Map;

public class Alurator {

	private String pacoteBase;

	public Alurator(String pacoteBase) {
		this.pacoteBase = pacoteBase;
	}


	public Object executa(String url) {
		Request request = new Request(url);
		String nomeController = request.getNomeController();
		String nomeMetodo = request.getNomeMetodo();
		Map<String, Object> params = request.getQueryParams();

		Object result = new Reflexao().refleteClasse(pacoteBase + nomeController)
				.criaInstancia()
				.getMetodo(nomeMetodo, params)
				.comTratamentoDeExcecao((metodo, ex) -> {
					System.out.println("Erro no método " + metodo.getName() + " da classe "
							+ metodo.getDeclaringClass().getName() + ".\n\n");
					throw new RuntimeException("Erro no método!");
				})
				.invoca();


		return result;
	}
}
