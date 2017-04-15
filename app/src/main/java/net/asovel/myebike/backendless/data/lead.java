package net.asovel.myebike.backendless.data;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessDataQuery;

public class lead
{
    private java.util.Date created;
    private java.util.Date updated;
    private Integer codigo_postal;
    private String email;
    private String nombre;
    private String referencia;
    private String ownerId;
    private Integer telefono;
    private String objectId;
    private String mensaje;

    public java.util.Date getCreated()
    {
        return created;
    }

    public java.util.Date getUpdated()
    {
        return updated;
    }

    public Integer getCodigo_postal()
    {
        return codigo_postal;
    }

    public void setCodigo_postal(Integer codigo_postal)
    {
        this.codigo_postal = codigo_postal;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getReferencia()
    {
        return referencia;
    }

    public void setReferencia(String referencia)
    {
        this.referencia = referencia;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public Integer getTelefono()
    {
        return telefono;
    }

    public void setTelefono(Integer telefono)
    {
        this.telefono = telefono;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public String getMensaje()
    {
        return mensaje;
    }

    public void setMensaje(String mensaje)
    {
        this.mensaje = mensaje;
    }


    public lead save()
    {
        return Backendless.Data.of(lead.class).save(this);
    }

    public Future<lead> saveAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<lead> future = new Future<lead>();
            Backendless.Data.of(lead.class).save(this, future);

            return future;
        }
    }

    public void saveAsync(AsyncCallback<lead> callback)
    {
        Backendless.Data.of(lead.class).save(this, callback);
    }

    public Long remove()
    {
        return Backendless.Data.of(lead.class).remove(this);
    }

    public Future<Long> removeAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<Long> future = new Future<Long>();
            Backendless.Data.of(lead.class).remove(this, future);

            return future;
        }
    }

    public void removeAsync(AsyncCallback<Long> callback)
    {
        Backendless.Data.of(lead.class).remove(this, callback);
    }

    public static lead findById(String id)
    {
        return Backendless.Data.of(lead.class).findById(id);
    }

    public static Future<lead> findByIdAsync(String id)
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<lead> future = new Future<lead>();
            Backendless.Data.of(lead.class).findById(id, future);

            return future;
        }
    }

    public static void findByIdAsync(String id, AsyncCallback<lead> callback)
    {
        Backendless.Data.of(lead.class).findById(id, callback);
    }

    public static lead findFirst()
    {
        return Backendless.Data.of(lead.class).findFirst();
    }

    public static Future<lead> findFirstAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<lead> future = new Future<lead>();
            Backendless.Data.of(lead.class).findFirst(future);

            return future;
        }
    }

    public static void findFirstAsync(AsyncCallback<lead> callback)
    {
        Backendless.Data.of(lead.class).findFirst(callback);
    }

    public static lead findLast()
    {
        return Backendless.Data.of(lead.class).findLast();
    }

    public static Future<lead> findLastAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<lead> future = new Future<lead>();
            Backendless.Data.of(lead.class).findLast(future);

            return future;
        }
    }

    public static void findLastAsync(AsyncCallback<lead> callback)
    {
        Backendless.Data.of(lead.class).findLast(callback);
    }

    public static BackendlessCollection<lead> find(BackendlessDataQuery query)
    {
        return Backendless.Data.of(lead.class).find(query);
    }

    public static Future<BackendlessCollection<lead>> findAsync(BackendlessDataQuery query)
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<BackendlessCollection<lead>> future = new Future<BackendlessCollection<lead>>();
            Backendless.Data.of(lead.class).find(query, future);

            return future;
        }
    }

    public static void findAsync(BackendlessDataQuery query, AsyncCallback<BackendlessCollection<lead>> callback)
    {
        Backendless.Data.of(lead.class).find(query, callback);
    }
}