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

	public <T, K extends T> void registra(Class<T> tipoOrigem, Class<K> tipoDestino) {
				
		mapaDeTipos.put(tipoOrigem, tipoDestino);
	}

}
