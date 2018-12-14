package com.db.Modelos;

public class VisitaSesion {

    private static VisitaSesion mInstance;

    public static VisitaSesion getInstance() {
        if (mInstance == null) {
            mInstance = new VisitaSesion();
        }
        return mInstance;
    }

    public static void resetSesion() {
        mInstance = null;
    }

    private long id;
    private String tipoVisita;
    private String municipio;
    private String localidad;
    private String barrio;
    private String direccion;
    private String cliente;
    private long deuda;
    private long facturas;
    private long nic;
    private long nis;
    private String medidor;
    private String tarifa;
    private String fechaLimiteCompromiso;
    private long resultado;
    private long anomalia;
    private long entidadRecaudo;
    private String fechaPago;
    private String fechaCompromiso;
    private String personaContacto;
    private String cedula;
    private String titularPago;
    private String telefono;
    private String email;
    private long observacionRapida;
    private String observacionAnalisis;
    private String lectura;
    private String latitud;
    private String longitud;
    private long orden;
    private String foto;
    private String fechaRealizado;
    private long gestorAsignadoId;
    private long gestorRealizaId;
    private long estado;
    private long lastInsert;

    private boolean observacionObligatoria;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipoVisita() {
        return tipoVisita;
    }

    public void setTipoVisita(String tipoVisita) {
        this.tipoVisita = tipoVisita;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public long getDeuda() {
        return deuda;
    }

    public void setDeuda(long deuda) {
        this.deuda = deuda;
    }

    public long getFacturas() {
        return facturas;
    }

    public void setFacturas(long facturas) {
        this.facturas = facturas;
    }

    public long getNic() {
        return nic;
    }

    public void setNic(long nic) {
        this.nic = nic;
    }

    public long getNis() {
        return nis;
    }

    public void setNis(long nis) {
        this.nis = nis;
    }

    public String getMedidor() {
        return medidor;
    }

    public void setMedidor(String medidor) {
        this.medidor = medidor;
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    public String getFechaLimiteCompromiso() {
        return fechaLimiteCompromiso;
    }

    public void setFechaLimiteCompromiso(String fechaLimiteCompromiso) {
        this.fechaLimiteCompromiso = fechaLimiteCompromiso;
    }

    public long getResultado() {
        return resultado;
    }

    public void setResultado(long resultado) {
        this.resultado = resultado;
    }

    public long getAnomalia() {
        return anomalia;
    }

    public void setAnomalia(long anomalia) {
        this.anomalia = anomalia;
    }

    public long getEntidadRecaudo() {
        return entidadRecaudo;
    }

    public void setEntidadRecaudo(long entidadRecaudo) {
        this.entidadRecaudo = entidadRecaudo;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getFechaCompromiso() {
        return fechaCompromiso;
    }

    public void setFechaCompromiso(String fechaCompromiso) {
        this.fechaCompromiso = fechaCompromiso;
    }

    public String getPersonaContacto() {
        return personaContacto;
    }

    public void setPersonaContacto(String personaContacto) {
        this.personaContacto = personaContacto;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTitularPago() {
        return titularPago;
    }

    public void setTitularPago(String titularPago) {
        this.titularPago = titularPago;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getObservacionRapida() {
        return observacionRapida;
    }

    public void setObservacionRapida(long observacionRapida) {
        this.observacionRapida = observacionRapida;
    }

    public String getObservacionAnalisis() {
        return observacionAnalisis;
    }

    public void setObservacionAnalisis(String observacionAnalisis) {
        this.observacionAnalisis = observacionAnalisis;
    }

    public String getLectura() {
        return lectura;
    }

    public void setLectura(String lectura) {
        this.lectura = lectura;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public long getOrden() {
        return orden;
    }

    public void setOrden(long orden) {
        this.orden = orden;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFechaRealizado() {
        return fechaRealizado;
    }

    public void setFechaRealizado(String fechaRealizado) {
        this.fechaRealizado = fechaRealizado;
    }

    public long getGestorAsignadoId() {
        return gestorAsignadoId;
    }

    public void setGestorAsignadoId(long gestorAsignadoId) {
        this.gestorAsignadoId = gestorAsignadoId;
    }

    public long getGestorRealizaId() {
        return gestorRealizaId;
    }

    public void setGestorRealizaId(long gestorRealizaId) {
        this.gestorRealizaId = gestorRealizaId;
    }

    public long getEstado() {
        return estado;
    }

    public void setEstado(long estado) {
        this.estado = estado;
    }

    public long getLastInsert() {
        return lastInsert;
    }

    public void setLastInsert(long lastInsert) {
        this.lastInsert = lastInsert;
    }

    public boolean isObservacionObligatoria() {
        return observacionObligatoria;
    }

    public void setObservacionObligatoria(boolean observacionObligatoria) {
        this.observacionObligatoria = observacionObligatoria;
    }
}
