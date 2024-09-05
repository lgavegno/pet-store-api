package com.mycompany.peluqueriacanina.model;  //Persistencia

import com.mycompany.peluqueriacanina.controller.Duenio;
import com.mycompany.peluqueriacanina.controller.Mascota;
import java.util.List;

public class ControladoraPersistencia {

    DuenioJpaController duenioJpa = new DuenioJpaController();
    MascotaJpaController mascoJpa = new MascotaJpaController();

    public void guardar(Duenio duenio, Mascota masco) {

        //crea en la BDD el Duenio
        duenioJpa.create(duenio);

        //crea en la BDD la Mascota
        mascoJpa.create(masco);

    }

    public List<Mascota> traerMascotas() {

        return mascoJpa.findMascotaEntities();
    }

}
