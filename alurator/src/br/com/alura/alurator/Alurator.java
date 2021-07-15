package br.com.alura.alurator;

import br.com.alura.alurator.Reflexao.ManipuladorObjeto;
import br.com.alura.alurator.Reflexao.Reflexao;
import br.com.alura.alurator.containerIOC.ContainerIOC;
import br.com.alura.alurator.converter.XmlConverter;
import br.com.alura.alurator.protocolo.Request;

import java.util.Map;

public class Alurator {

	private String pacoteBase;
	private ContainerIOC containerIOC;

	public Alurator(String pacoteBase) {
		this.pacoteBase = pacoteBase;
		this.containerIOC = new ContainerIOC();
	}


	public Object executa(String url) {
		Request request = new Request(url);
		String nomeController = request.getNomeController();
		String nomeMetodo = request.getNomeMetodo();
		Map<String, Object> params = request.getQueryParams();

		Class<?> classeControle = new Reflexao().getClasse(pacoteBase + nomeController);
		
		Object instanciaControle = containerIOC.getInstancia(classeControle);
		
		Object result = new ManipuladorObjeto(instanciaControle)
				.getMetodo(nomeMetodo, params)
				.comTratamentoDeExcecao((metodo, ex) -> {
					System.out.println("Erro no método " + metodo.getName() + " da classe "
							+ metodo.getDeclaringClass().getName() + ".\n\n");
					throw new RuntimeException("Erro no método!");
				})
				.invoca();
		
		result = new XmlConverter().converte(result);


		return result;
	}
	
	public <T, K extends T> void registra(Class<T> tipoOrigem, Class<K> tipoDestino) {
		containerIOC.registra(tipoOrigem, tipoDestino);
	}
}
