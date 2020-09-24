/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prac_1_aplicacion_estilo_dropbox;

/**
 *
 * @author eddyp
 */
public class Tipo_archivo {
    String Tipo_archivo;
    String Nombre;

    public Tipo_archivo(String Tipo_archivo, String Nombre) {
        this.Tipo_archivo = Tipo_archivo;
        this.Nombre = Nombre;
    }

    public String getTipo_archivo() {
        return Tipo_archivo;
    }

    public void setTipo_archivo(String Tipo_archivo) {
        this.Tipo_archivo = Tipo_archivo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    
    
}
