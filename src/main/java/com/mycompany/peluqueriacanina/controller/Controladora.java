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

    public void borrarMascota(int num_cliente) {
        controlPersis.borrarMascota(num_cliente);

    }

    //este metodo se crea solo desde modificar datos, ver por que?
    public Mascota traerMascotas(int num_cliente) {
        return controlPersis.traerMascotas(num_cliente);
    }

    public void modificarMascota(Mascota masco, String nombreMasco, String raza, String color,
            String observaciones, String alergico, String atenEsp, String nombreDuenio, String celDuenio) {
        masco.setNombre(nombreMasco);
        masco.setRaza(raza);
        masco.setColor(color);
        masco.setObservaciones(observaciones);
        masco.setAtencion_especial(atenEsp);
        masco.setAlergico(alergico);
        
        //modificamos mascota
        controlPersis.modificarMascota(masco);
        
        //seteo nuevos valores del dueño
        Duenio dueno = this.buscarDuenio(masco.getUnDuenio().getId_duenio());
        dueno.setCelDuenio(celDuenio);
        dueno.setNombre(nombreDuenio);
        
        //llamar al modificar Dueño
        this.modificarDuenio(dueno);
        
        
    }

    private Duenio buscarDuenio(int id_duenio) {
        return controlPersis.traerDuenio(id_duenio);
    
    }

    private void modificarDuenio(Duenio dueno) {
        controlPersis.modificarDuenio(dueno);
    }



    
}
