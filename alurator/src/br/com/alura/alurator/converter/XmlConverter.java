package br.com.alura.alurator.converter;

import java.lang.reflect.Field;
import java.util.Collection;

import br.com.alura.alurator.annotations.ExportName;

public class XmlConverter {

	public String converte(Object object) {

		Class<?> classe = object.getClass();
		StringBuilder xmlBuilder = new StringBuilder();
		try {
			if (object instanceof Collection) {
				Collection<?> colecao = (Collection<?>) object;
				xmlBuilder.append("<lista>");

				for (Object o : colecao) {
					String xml = converte(o);
					xmlBuilder.append(xml);
				}
				xmlBuilder.append("</lista>");
			} else {

				ExportName anotacaoClasse = classe.getDeclaredAnnotation(ExportName.class);

				String nomeClasse = anotacaoClasse == null ? classe.getName() : anotacaoClasse.value();
				xmlBuilder.append("<" + nomeClasse + ">");

				for (Field campo : classe.getDeclaredFields()) {	
					campo.setAccessible(true);
					
					ExportName anotacao = campo.getDeclaredAnnotation(ExportName.class);
					
					String campoNome = anotacao == null ? campo.getName() : anotacao.value();
					
					Object valor = campo.get(object);

					xmlBuilder.append("<" + campoNome + ">");
					xmlBuilder.append(valor.toString());
					xmlBuilder.append("</" + campoNome + ">");
				}

				xmlBuilder.append("</" + nomeClasse + ">");
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		return xmlBuilder.toString();
	}
}
