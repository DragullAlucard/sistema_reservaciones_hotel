/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reservacioneshotel;

/**
 *
 * @author alexander
 */
public class Habitacion {
    
    //Atributos
    private String idHabitacion;
    private String numeroHabitacion;
    private String tipoHabitacion; //Habitacion individual, doble, Premium....
    private double precioHabitacion;
    private String estadoHabitacion; //Disponible, Ocupada, Limpieza...
    
    //Constructor vacio
    public Habitacion(){        
    }
    
    //Constructor
    public Habitacion(String idHabitacion, String numeroHabitacion, String tipoHabitacion, double precioHabitacion, String estadoHabitacion){
        this.idHabitacion = idHabitacion;
        this.numeroHabitacion = numeroHabitacion;
        this.tipoHabitacion = tipoHabitacion;
        this.precioHabitacion = precioHabitacion;
        this.estadoHabitacion = estadoHabitacion;
    }
    
    //Setters y Getters
    public void setIdHabitacion(String idHabitacion){
        this.idHabitacion = idHabitacion;
    }
    public String getIdHabitacion(){
        return idHabitacion;
    }
    public void setNumeroHabitacion(String numeroHabitacion){
        this.numeroHabitacion = numeroHabitacion;
    }
    public String getNumeroHabitacion(){
        return numeroHabitacion;
    }
    public void setTipoHabitacion(String tipoHabitacion){
        this.tipoHabitacion = tipoHabitacion;
    }
    public String getTipoHabitacion(){
        return tipoHabitacion;
    }
    public void setPrecioHabitacion(double precioHabitacion){
        this.precioHabitacion = precioHabitacion;
    }
    public double getPrecioHabitacion(){
        return precioHabitacion;
    }
    public void setEstadoHabitacion(String estadoHabitacion){
        this.estadoHabitacion = estadoHabitacion;
    }
    public String getEstadoHabitacion (){
        return estadoHabitacion;
    }
    
    @Override
    public String toString(){
        return "Habitacion " + numeroHabitacion + " (" + tipoHabitacion + ") - Q" + precioHabitacion;
    }
}
