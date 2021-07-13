package br.com.alura.alurator.protocolo;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private String nomeController;
    private String nomeMetodo;
    private Map<String, Object> queryParams;


    public Request(String url) {
        String[] partesUrl = url.replaceFirst("/", "").split("[?]");

        String[] controleEMetodo = partesUrl[0].split("/");

        this.nomeController = Character.toUpperCase(controleEMetodo[0].charAt(0))
				+ controleEMetodo[0].substring(1) + "Controller";

        this.nomeController = controleEMetodo[1];

        this.queryParams = partesUrl.length > 1
				? new QueryParamsBuilder().withParams(partesUrl[1]).build()
				: new HashMap<String, Object>();
    }

    public String getNomeController() {
        return nomeController;
    }

    public String getNomeMetodo() {
        return nomeMetodo;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }
}
