package net.asovel.myebike.backendless.data;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.geo.GeoPoint;
import com.backendless.persistence.BackendlessDataQuery;

public class ImageEntity
{
    private Double serialVersionUID;
    private java.util.Date updated;
    private Double uploaded;
    private java.util.Date created;
    private String url;
    private String ownerId;
    private String objectId;

    public Double getSerialVersionUID()
    {
        return serialVersionUID;
    }

    public void setSerialVersionUID(Double serialVersionUID)
    {
        this.serialVersionUID = serialVersionUID;
    }

    public java.util.Date getUpdated()
    {
        return updated;
    }

    public Double getUploaded()
    {
        return uploaded;
    }

    public void setUploaded(Double uploaded)
    {
        this.uploaded = uploaded;
    }

    public java.util.Date getCreated()
    {
        return created;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public String getObjectId()
    {
        return objectId;
    }


    public ImageEntity save()
    {
        return Backendless.Data.of(ImageEntity.class).save(this);
    }

    public Future<ImageEntity> saveAsync()
    {
        if (Backendless.isAndroid())
        {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else
        {
            Future<ImageEntity> future = new Future<ImageEntity>();
            Backendless.Data.of(ImageEntity.class).save(this, future);

            return future;
        }
    }

    public void saveAsync(AsyncCallback<ImageEntity> callback)
    {
        Backendless.Data.of(ImageEntity.class).save(this, callback);
    }

    public Long remove()
    {
        return Backendless.Data.of(ImageEntity.class).remove(this);
    }

    public Future<Long> removeAsync()
    {
        if (Backendless.isAndroid())
        {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else
        {
            Future<Long> future = new Future<Long>();
            Backendless.Data.of(ImageEntity.class).remove(this, future);

            return future;
        }
    }

    public void removeAsync(AsyncCallback<Long> callback)
    {
        Backendless.Data.of(ImageEntity.class).remove(this, callback);
    }

    public static ImageEntity findById(String id)
    {
        return Backendless.Data.of(ImageEntity.class).findById(id);
    }

    public static Future<ImageEntity> findByIdAsync(String id)
    {
        if (Backendless.isAndroid())
        {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else
        {
            Future<ImageEntity> future = new Future<ImageEntity>();
            Backendless.Data.of(ImageEntity.class).findById(id, future);

            return future;
        }
    }

    public static void findByIdAsync(String id, AsyncCallback<ImageEntity> callback)
    {
        Backendless.Data.of(ImageEntity.class).findById(id, callback);
    }

    public static ImageEntity findFirst()
    {
        return Backendless.Data.of(ImageEntity.class).findFirst();
    }

    public static Future<ImageEntity> findFirstAsync()
    {
        if (Backendless.isAndroid())
        {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else
        {
            Future<ImageEntity> future = new Future<ImageEntity>();
            Backendless.Data.of(ImageEntity.class).findFirst(future);

            return future;
        }
    }

    public static void findFirstAsync(AsyncCallback<ImageEntity> callback)
    {
        Backendless.Data.of(ImageEntity.class).findFirst(callback);
    }

    public static ImageEntity findLast()
    {
        return Backendless.Data.of(ImageEntity.class).findLast();
    }

    public static Future<ImageEntity> findLastAsync()
    {
        if (Backendless.isAndroid())
        {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else
        {
            Future<ImageEntity> future = new Future<ImageEntity>();
            Backendless.Data.of(ImageEntity.class).findLast(future);

            return future;
        }
    }

    public static void findLastAsync(AsyncCallback<ImageEntity> callback)
    {
        Backendless.Data.of(ImageEntity.class).findLast(callback);
    }

    public static BackendlessCollection<ImageEntity> find(BackendlessDataQuery query)
    {
        return Backendless.Data.of(ImageEntity.class).find(query);
    }

    public static Future<BackendlessCollection<ImageEntity>> findAsync(BackendlessDataQuery query)
    {
        if (Backendless.isAndroid())
        {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else
        {
            Future<BackendlessCollection<ImageEntity>> future = new Future<BackendlessCollection<ImageEntity>>();
            Backendless.Data.of(ImageEntity.class).find(query, future);

            return future;
        }
    }

    public static void findAsync(BackendlessDataQuery query, AsyncCallback<BackendlessCollection<ImageEntity>> callback)
    {
        Backendless.Data.of(ImageEntity.class).find(query, callback);
    }
}