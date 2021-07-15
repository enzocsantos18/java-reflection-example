package br.com.alura.alurator.containerIOC;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ContainerIOC {
	
	private Map<Class<?>, Class<?>> mapaDeTipos = new HashMap<>();
	
	public Object getInstancia(Class<?> tipoFonte ) {
		Class<?> tipoDestino =mapaDeTipos.get(tipoFonte); 
		if( tipoDestino!= null) {
			return getInstancia(tipoDestino);
		}
		
		Stream<Constructor<?>> construtores = Stream.of(tipoFonte.getDeclaredConstructors());
		
		Optional<Constructor<?>> construtorPadrao = 
				construtores.filter(construtor -> construtor.getParameterCount() == 0).findFirst();
		
		try {
			
			if(construtorPadrao.isPresent()) {
				Object instancia = construtorPadrao.get().newInstance();
				
				return instancia;
			}
			else {
				Constructor<?> construtor = tipoFonte.getDeclaredConstructors()[0];
				
				List<Object> params = new ArrayList<>();
				for (Parameter campo : construtor.getParameters()) {
					Class<?> tipoParametro = campo.getType();
					
					params.add(getInstancia(tipoParametro));
				}
				
				return construtor.newInstance(params.toArray());
				
			}

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

	public void registra(Class<?> tipoOrigem, Class<?> tipoDestino) {
		
		boolean compativel = verificaCompatibilidade(tipoOrigem, tipoDestino);
		
		if (!compativel) throw new ClassCastException("Não é possível resolver " + tipoOrigem.getName() + "para o tipo destino " + tipoDestino.getName());
		
		mapaDeTipos.put(tipoOrigem, tipoDestino);
	}

	private boolean verificaCompatibilidade(Class<?> tipoOrigem, Class<?> tipoDestino) {
//		boolean compativel;
//		
//		if(tipoOrigem.isInterface()) {
//			compativel = Stream.of(tipoDestino.getInterfaces()).anyMatch(interfaceImplementada -> interfaceImplementada.equals(tipoOrigem));
//		}
//		else {
//			compativel = tipoDestino.getSuperclass().equals(tipoOrigem) || tipoDestino.equals(tipoOrigem);
//		}

		
		return tipoOrigem.isAssignableFrom(tipoDestino);
	}
}
