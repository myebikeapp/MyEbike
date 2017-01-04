package net.asovel.myebike.backendless.data;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessDataQuery;

public class Tienda {

    private String nombre_tienda;
    private String direccion;
    private String email;
    private String codigo_postal;
    private String ciudad;
    private Integer patrocinada_SORT0;
    private String objectId;
    private java.util.Date updated;
    private Integer numero;
    private java.util.Date created;
    private String ownerId;
    private Double longitud;
    private Integer telefono;
    private String pagina_web;
    private Double latitud;

    public String getNombre_tienda() {
        return nombre_tienda;
    }

    public void setNombre_tienda(String nombre_tienda) {
        this.nombre_tienda = nombre_tienda;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Integer getPatrocinada_SORT0() {
        return patrocinada_SORT0;
    }

    public void setPatrocinada_SORT0(Integer patrocinada_SORT0) {
        this.patrocinada_SORT0 = patrocinada_SORT0;
    }

    public String getObjectId() {
        return objectId;
    }

    public java.util.Date getUpdated() {
        return updated;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public java.util.Date getCreated() {
        return created;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getPagina_web() {
        return pagina_web;
    }

    public void setPagina_web(String pagina_web) {
        this.pagina_web = pagina_web;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }


    public Tienda save() {
        return Backendless.Data.of(Tienda.class).save(this);
    }

    public Future<Tienda> saveAsync() {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<Tienda> future = new Future<Tienda>();
            Backendless.Data.of(Tienda.class).save(this, future);

            return future;
        }
    }

    public void saveAsync(AsyncCallback<Tienda> callback) {
        Backendless.Data.of(Tienda.class).save(this, callback);
    }

    public Long remove() {
        return Backendless.Data.of(Tienda.class).remove(this);
    }

    public Future<Long> removeAsync() {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<Long> future = new Future<Long>();
            Backendless.Data.of(Tienda.class).remove(this, future);

            return future;
        }
    }

    public void removeAsync(AsyncCallback<Long> callback) {
        Backendless.Data.of(Tienda.class).remove(this, callback);
    }

    public static Tienda findById(String id) {
        return Backendless.Data.of(Tienda.class).findById(id);
    }

    public static Future<Tienda> findByIdAsync(String id) {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<Tienda> future = new Future<Tienda>();
            Backendless.Data.of(Tienda.class).findById(id, future);

            return future;
        }
    }

    public static void findByIdAsync(String id, AsyncCallback<Tienda> callback) {
        Backendless.Data.of(Tienda.class).findById(id, callback);
    }

    public static Tienda findFirst() {
        return Backendless.Data.of(Tienda.class).findFirst();
    }

    public static Future<Tienda> findFirstAsync() {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<Tienda> future = new Future<Tienda>();
            Backendless.Data.of(Tienda.class).findFirst(future);

            return future;
        }
    }

    public static void findFirstAsync(AsyncCallback<Tienda> callback) {
        Backendless.Data.of(Tienda.class).findFirst(callback);
    }

    public static Tienda findLast() {
        return Backendless.Data.of(Tienda.class).findLast();
    }

    public static Future<Tienda> findLastAsync() {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<Tienda> future = new Future<Tienda>();
            Backendless.Data.of(Tienda.class).findLast(future);

            return future;
        }
    }

    public static void findLastAsync(AsyncCallback<Tienda> callback) {
        Backendless.Data.of(Tienda.class).findLast(callback);
    }

    public static BackendlessCollection<Tienda> find(BackendlessDataQuery query) {
        return Backendless.Data.of(Tienda.class).find(query);
    }

    public static Future<BackendlessCollection<Tienda>> findAsync(BackendlessDataQuery query) {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<BackendlessCollection<Tienda>> future = new Future<BackendlessCollection<Tienda>>();
            Backendless.Data.of(Tienda.class).find(query, future);

            return future;
        }
    }

    public static void findAsync(BackendlessDataQuery query, AsyncCallback<BackendlessCollection<Tienda>> callback) {
        Backendless.Data.of(Tienda.class).find(query, callback);
    }
}