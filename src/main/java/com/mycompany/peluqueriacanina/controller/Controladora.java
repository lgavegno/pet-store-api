package com.mycompany.peluqueriacanina.controller;  //Logica

import com.mycompany.peluqueriacanina.model.ControladoraPersistencia;
import java.util.List;

public class Controladora {

    ControladoraPersistencia controlPersis = new ControladoraPersistencia();

    public void guardar(String nombreMasco, String raza, String color, String observaciones, String alergico, String atenEsp,
            String nombreDuenio, String celDuenio) {

        //Se crea el dueño y asignan valores 
        Duenio duenio = new Duenio();
        duenio.setNombre(nombreDuenio);
        duenio.setCelDuenio(celDuenio);

        //Se crea la mascota y asignan valores
        Mascota masco = new Mascota();
        masco.setNombre(nombreMasco);
        masco.setRaza(raza);
        masco.setColor(color);
        masco.setAlergico(alergico);
        masco.setAtencion_especial(atenEsp);
        masco.setObservaciones(observaciones);
        masco.setUnDuenio(duenio);   //asociamos Mascota con Duenio

        controlPersis.guardar(duenio, masco);

    }

    public List<Mascota> traerMascotas() {
        return controlPersis.traerMascotas();

    }
}
