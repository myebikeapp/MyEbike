package net.asovel.myebike.backendless.data;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessDataQuery;

public class EBike
{
    private String suspension;
    private Integer autonomia;
    private java.util.Date created;
    private Integer precio_SORT2;
    private Double valoracion_SORT1;
    private String ubicacion_motor;
    private String uso;
    private Integer peso;
    private Integer any;
    private String foto;
    private String ownerId;
    private Integer patrocinado_SORT0;
    private String modelo;
    private String link;
    private String objectId;
    private String descripcion;
    private Integer tamano_ruedas;
    private java.util.Date updated;
    private Marca marca;

    public String getSuspension()
    {
        return suspension;
    }

    public void setSuspension(String suspension)
    {
        this.suspension = suspension;
    }

    public Integer getAutonomia()
    {
        return autonomia;
    }

    public void setAutonomia(Integer autonomia)
    {
        this.autonomia = autonomia;
    }

    public java.util.Date getCreated()
    {
        return created;
    }

    public Integer getPrecio_SORT2()
    {
        return precio_SORT2;
    }

    public void setPrecio_SORT2(Integer precio_SORT2)
    {
        this.precio_SORT2 = precio_SORT2;
    }

    public Double getValoracion_SORT1()
    {
        return valoracion_SORT1;
    }

    public void setValoracion_SORT1(Double valoracion_SORT1)
    {
        this.valoracion_SORT1 = valoracion_SORT1;
    }

    public String getUbicacion_motor()
    {
        return ubicacion_motor;
    }

    public void setUbicacion_motor(String ubicacion_motor)
    {
        this.ubicacion_motor = ubicacion_motor;
    }

    public String getUso()
    {
        return uso;
    }

    public void setUso(String uso)
    {
        this.uso = uso;
    }

    public Integer getPeso()
    {
        return peso;
    }

    public void setPeso(Integer peso)
    {
        this.peso = peso;
    }

    public Integer getAny()
    {
        return any;
    }

    public void setAny(Integer any)
    {
        this.any = any;
    }

    public String getFoto()
    {
        return foto;
    }

    public void setFoto(String foto)
    {
        this.foto = foto;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public Integer getPatrocinado_SORT0()
    {
        return patrocinado_SORT0;
    }

    public void setPatrocinado_SORT0(Integer patrocinado_SORT0)
    {
        this.patrocinado_SORT0 = patrocinado_SORT0;
    }

    public String getModelo()
    {
        return modelo;
    }

    public void setModelo(String modelo)
    {
        this.modelo = modelo;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public Integer getTamano_ruedas()
    {
        return tamano_ruedas;
    }

    public void setTamano_ruedas(Integer tamano_ruedas)
    {
        this.tamano_ruedas = tamano_ruedas;
    }

    public java.util.Date getUpdated()
    {
        return updated;
    }

    public Marca getMarca()
    {
        return marca;
    }

    public void setMarca(Marca marca)
    {
        this.marca = marca;
    }


    public EBike save()
    {
        return Backendless.Data.of(EBike.class).save(this);
    }

    public Future<EBike> saveAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<EBike> future = new Future<EBike>();
            Backendless.Data.of(EBike.class).save(this, future);

            return future;
        }
    }

    public void saveAsync(AsyncCallback<EBike> callback)
    {
        Backendless.Data.of(EBike.class).save(this, callback);
    }

    public Long remove()
    {
        return Backendless.Data.of(EBike.class).remove(this);
    }

    public Future<Long> removeAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<Long> future = new Future<Long>();
            Backendless.Data.of(EBike.class).remove(this, future);

            return future;
        }
    }

    public void removeAsync(AsyncCallback<Long> callback)
    {
        Backendless.Data.of(EBike.class).remove(this, callback);
    }

    public static EBike findById(String id)
    {
        return Backendless.Data.of(EBike.class).findById(id);
    }

    public static Future<EBike> findByIdAsync(String id)
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<EBike> future = new Future<EBike>();
            Backendless.Data.of(EBike.class).findById(id, future);

            return future;
        }
    }

    public static void findByIdAsync(String id, AsyncCallback<EBike> callback)
    {
        Backendless.Data.of(EBike.class).findById(id, callback);
    }

    public static EBike findFirst()
    {
        return Backendless.Data.of(EBike.class).findFirst();
    }

    public static Future<EBike> findFirstAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<EBike> future = new Future<EBike>();
            Backendless.Data.of(EBike.class).findFirst(future);

            return future;
        }
    }

    public static void findFirstAsync(AsyncCallback<EBike> callback)
    {
        Backendless.Data.of(EBike.class).findFirst(callback);
    }

    public static EBike findLast()
    {
        return Backendless.Data.of(EBike.class).findLast();
    }

    public static Future<EBike> findLastAsync()
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<EBike> future = new Future<EBike>();
            Backendless.Data.of(EBike.class).findLast(future);

            return future;
        }
    }

    public static void findLastAsync(AsyncCallback<EBike> callback)
    {
        Backendless.Data.of(EBike.class).findLast(callback);
    }

    public static BackendlessCollection<EBike> find(BackendlessDataQuery query)
    {
        return Backendless.Data.of(EBike.class).find(query);
    }

    public static Future<BackendlessCollection<EBike>> findAsync(BackendlessDataQuery query)
    {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<BackendlessCollection<EBike>> future = new Future<BackendlessCollection<EBike>>();
            Backendless.Data.of(EBike.class).find(query, future);

            return future;
        }
    }

    public static void findAsync(BackendlessDataQuery query, AsyncCallback<BackendlessCollection<EBike>> callback)
    {
        Backendless.Data.of(EBike.class).find(query, callback);
    }
}