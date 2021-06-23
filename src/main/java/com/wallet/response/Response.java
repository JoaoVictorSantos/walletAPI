package com.wallet.response;

//Padronizando o retorno da chamada com essa classe,
//sendo assim, ele tem que ser genérica para atender todo o projeto.

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Response<T> {

    private T data;
    private List<String> errors;

    public List<String> getErrors() {
        if(this.errors == null){
            this.errors = new ArrayList<String>();
        }
        return this.errors;
    }
}
