package net.asovel.myebike.backendless.data;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessDataQuery;

public class MarcaLista
{
    private String ownerId;
    private String objectId;
    private java.util.Date created;
    private String registro;
    private java.util.Date updated;
    private java.util.List<Marca> lista;

    public String getOwnerId()
    {
        return ownerId;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public java.util.Date getCreated()
    {
        return created;
    }

    public String getRegistro()
    {
        return registro;
    }

    public void setRegistro(String registro)
    {
        this.registro = registro;
    }

    public java.util.Date getUpdated()
    {
        return updated;
    }

    public java.util.List<Marca> getLista()
    {
        return lista;
    }

    public void setLista(java.util.List<Marca> lista)
    {
        this.lista = lista;
    }


    public MarcaLista save()
    {
        return Backendless.Data.of(MarcaLista.class).save(this);
    }

    public Future<MarcaLista> saveAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<MarcaLista> future = new Future<MarcaLista>();
            Backendless.Data.of(MarcaLista.class).save(this, future);

            return future;
        }
    }

    public void saveAsync(AsyncCallback<MarcaLista> callback)
    {
        Backendless.Data.of(MarcaLista.class).save(this, callback);
    }

    public Long remove()
    {
        return Backendless.Data.of(MarcaLista.class).remove(this);
    }

    public Future<Long> removeAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<Long> future = new Future<Long>();
            Backendless.Data.of(MarcaLista.class).remove(this, future);

            return future;
        }
    }

    public void removeAsync(AsyncCallback<Long> callback)
    {
        Backendless.Data.of(MarcaLista.class).remove(this, callback);
    }

    public static MarcaLista findById(String id)
    {
        return Backendless.Data.of(MarcaLista.class).findById(id);
    }

    public static Future<MarcaLista> findByIdAsync(String id)
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<MarcaLista> future = new Future<MarcaLista>();
            Backendless.Data.of(MarcaLista.class).findById(id, future);

            return future;
        }
    }

    public static void findByIdAsync(String id, AsyncCallback<MarcaLista> callback)
    {
        Backendless.Data.of(MarcaLista.class).findById(id, callback);
    }

    public static MarcaLista findFirst()
    {
        return Backendless.Data.of(MarcaLista.class).findFirst();
    }

    public static Future<MarcaLista> findFirstAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<MarcaLista> future = new Future<MarcaLista>();
            Backendless.Data.of(MarcaLista.class).findFirst(future);

            return future;
        }
    }

    public static void findFirstAsync(AsyncCallback<MarcaLista> callback)
    {
        Backendless.Data.of(MarcaLista.class).findFirst(callback);
    }

    public static MarcaLista findLast()
    {
        return Backendless.Data.of(MarcaLista.class).findLast();
    }

    public static Future<MarcaLista> findLastAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<MarcaLista> future = new Future<MarcaLista>();
            Backendless.Data.of(MarcaLista.class).findLast(future);

            return future;
        }
    }

    public static void findLastAsync(AsyncCallback<MarcaLista> callback)
    {
        Backendless.Data.of(MarcaLista.class).findLast(callback);
    }

    public static BackendlessCollection<MarcaLista> find(BackendlessDataQuery query)
    {
        return Backendless.Data.of(MarcaLista.class).find(query);
    }

    public static Future<BackendlessCollection<MarcaLista>> findAsync(BackendlessDataQuery query)
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<BackendlessCollection<MarcaLista>> future = new Future<BackendlessCollection<MarcaLista>>();
            Backendless.Data.of(MarcaLista.class).find(query, future);

            return future;
        }
    }

    public static void findAsync(BackendlessDataQuery query, AsyncCallback<BackendlessCollection<MarcaLista>> callback)
    {
        Backendless.Data.of(MarcaLista.class).find(query, callback);
    }
}