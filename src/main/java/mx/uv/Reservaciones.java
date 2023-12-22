package mx.uv;

public class Reservaciones {

    String idR;
    String idU;
    String idH;
    String nombre;
    String precio;
    String checkIn;
    String checkOut;
    String personas;
    public String getIdR() {
        return idR;
    }
    public void setIdR(String idR) {
        this.idR = idR;
    }
    public String getIdU() {
        return idU;
    }
    public void setIdU(String idU) {
        this.idU = idU;
    }
    public String getIdH() {
        return idH;
    }
    public void setIdH(String idH) {
        this.idH = idH;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getPrecio() {
        return precio;
    }
    public void setPrecio(String precio) {
        this.precio = precio;
    }
    public String getCheckIn() {
        return checkIn;
    }
    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }
    public String getCheckOut() {
        return checkOut;
    }
    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }
    public String getPersonas() {
        return personas;
    }
    public void setPersonas(String personas) {
        this.personas = personas;
    }

    public String toString() {
        return "Reservaciones [id=" + idR + ", nombre=" + nombre + ", precio=" + precio + ", checkIn=" + checkIn + ", checkOut=" + checkOut + ", personas=" + personas +"]";
    }

    public Reservaciones(){

    }

    public Reservaciones(String idR, String idU, String idH, String nombre, String precio, String checkIn,
            String checkOut, String personas) {
        this.idR = idR;
        this.idU = idU;
        this.idH = idH;
        this.nombre = nombre;
        this.precio = precio;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.personas = personas;
    }

    
}
