package br.com.alura.alurator.playground.reflexao;

import java.lang.reflect.Field;
import java.util.Iterator;

import model.Produto;

public class TesteField {

	public static void main(String[] args) {
		
		Object produto = new Produto("Teste", 25, "teste");
		
		Class<?> classe = produto.getClass();
		
		String xml = "<"+ classe.getSimpleName() +">";
		
		for (Field campo : classe.getDeclaredFields()) {
			campo.setAccessible(true);
			String nome = campo.getName();
			String valor;
			try {
				valor = campo.get(produto).toString();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage().toString());
			}
			
			xml += "<"+ nome +">"+ valor +"</"+ nome +">";
		}
		
		xml += "</"+ classe.getSimpleName() +">";
		
		System.out.println(xml);
		
	}
}
