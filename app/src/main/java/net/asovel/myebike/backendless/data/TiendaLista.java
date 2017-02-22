package net.asovel.myebike.backendless.data;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessDataQuery;

public class TiendaLista
{
    private String registro;
    private String objectId;
    private java.util.Date created;
    private java.util.Date updated;
    private String ownerId;
    private java.util.List<Tienda> lista;

    public String getRegistro()
    {
        return registro;
    }

    public void setRegistro(String registro)
    {
        this.registro = registro;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public java.util.Date getCreated()
    {
        return created;
    }

    public java.util.Date getUpdated()
    {
        return updated;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public java.util.List<Tienda> getLista()
    {
        return lista;
    }

    public void setLista(java.util.List<Tienda> lista)
    {
        this.lista = lista;
    }


    public TiendaLista save()
    {
        return Backendless.Data.of(TiendaLista.class).save(this);
    }

    public Future<TiendaLista> saveAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<TiendaLista> future = new Future<TiendaLista>();
            Backendless.Data.of(TiendaLista.class).save(this, future);

            return future;
        }
    }

    public void saveAsync(AsyncCallback<TiendaLista> callback)
    {
        Backendless.Data.of(TiendaLista.class).save(this, callback);
    }

    public Long remove()
    {
        return Backendless.Data.of(TiendaLista.class).remove(this);
    }

    public Future<Long> removeAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<Long> future = new Future<Long>();
            Backendless.Data.of(TiendaLista.class).remove(this, future);

            return future;
        }
    }

    public void removeAsync(AsyncCallback<Long> callback)
    {
        Backendless.Data.of(TiendaLista.class).remove(this, callback);
    }

    public static TiendaLista findById(String id)
    {
        return Backendless.Data.of(TiendaLista.class).findById(id);
    }

    public static Future<TiendaLista> findByIdAsync(String id)
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<TiendaLista> future = new Future<TiendaLista>();
            Backendless.Data.of(TiendaLista.class).findById(id, future);

            return future;
        }
    }

    public static void findByIdAsync(String id, AsyncCallback<TiendaLista> callback)
    {
        Backendless.Data.of(TiendaLista.class).findById(id, callback);
    }

    public static TiendaLista findFirst()
    {
        return Backendless.Data.of(TiendaLista.class).findFirst();
    }

    public static Future<TiendaLista> findFirstAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<TiendaLista> future = new Future<TiendaLista>();
            Backendless.Data.of(TiendaLista.class).findFirst(future);

            return future;
        }
    }

    public static void findFirstAsync(AsyncCallback<TiendaLista> callback)
    {
        Backendless.Data.of(TiendaLista.class).findFirst(callback);
    }

    public static TiendaLista findLast()
    {
        return Backendless.Data.of(TiendaLista.class).findLast();
    }

    public static Future<TiendaLista> findLastAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<TiendaLista> future = new Future<TiendaLista>();
            Backendless.Data.of(TiendaLista.class).findLast(future);

            return future;
        }
    }

    public static void findLastAsync(AsyncCallback<TiendaLista> callback)
    {
        Backendless.Data.of(TiendaLista.class).findLast(callback);
    }

    public static BackendlessCollection<TiendaLista> find(BackendlessDataQuery query)
    {
        return Backendless.Data.of(TiendaLista.class).find(query);
    }

    public static Future<BackendlessCollection<TiendaLista>> findAsync(BackendlessDataQuery query)
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<BackendlessCollection<TiendaLista>> future = new Future<BackendlessCollection<TiendaLista>>();
            Backendless.Data.of(TiendaLista.class).find(query, future);

            return future;
        }
    }

    public static void findAsync(BackendlessDataQuery query, AsyncCallback<BackendlessCollection<TiendaLista>> callback)
    {
        Backendless.Data.of(TiendaLista.class).find(query, callback);
    }
}