package model;

import lombok.Data;

@Data
public class Cifrado {

    private String iv;
    private String salt;
    private String claveSimetrica;
    private String mensaje;
    private String firma;
    private int iteraciones;
}
