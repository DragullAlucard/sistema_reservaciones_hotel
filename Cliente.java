/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reservacioneshotel;

/**
 *
 * @author alexander
 */
public class Cliente {
    
    //Atributos
    private String idCliente;
    private String nombreCliente;
    private String emailCliente;
    private String telefonoCliente;
    
    //Constructor vacio
    public Cliente(){        
    }
   
    //Constructor
    public Cliente(String idCliente, String nombreCliente, String emailCliente, String telefonoCliente){
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.emailCliente = emailCliente;
        this.telefonoCliente = telefonoCliente;
    }
    
    //Setters y Getters
    public void setIdCliente(String idCliente){
        this.idCliente = idCliente;
    }
    public String getIdCliente(){
        return idCliente;
    }
    public void setNombreCliente(String nombreCliente){
        this.nombreCliente = nombreCliente;
    }
    public String getNombreCliente(){
        return nombreCliente;
    }
    public void setEmailCliente(String emailCliente){
        this.emailCliente = emailCliente;
    }
    public String getEmailCliente(){
        return emailCliente;
    }
    public void setTelefonoCliente(String telefonoCliente){
        this.telefonoCliente = telefonoCliente;
    }
    public String getTelefonoCliente(){
        return telefonoCliente;
    }
    
    @Override
    public String toString(){
        return nombreCliente + " (" + emailCliente + ")";
    }    
}
