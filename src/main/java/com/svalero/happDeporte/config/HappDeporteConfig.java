package com.svalero.happDeporte.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HappDeporteConfig {

    /**
     * @Configuration: Clase de configuración para que tenga acceso desde cuaqluier parte del proyecto.
     * @Bean: Se usa junto con @Configuration -> para los métodos que creamos y queremos usar en el proyecto.
     * ModelMapper: Libreria para mapear campos entre objetos.
     */
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
