package net.asovel.myebike.utils;

import android.os.Parcel;
import android.os.Parcelable;

import net.asovel.myebike.backendless.data.Marca;

public class ParcelableMarca implements Parcelable
{
    private String pagina_web;
    private String nombre;
    private String logo;

    public ParcelableMarca(String pagina_web, String nombre, String logo)
    {
        this.pagina_web = pagina_web;
        this.nombre = nombre;
        this.logo = logo;
    }

    public String getPagina_web()
    {
        return pagina_web;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getLogo()
    {
        return logo;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(pagina_web);
        parcel.writeString(nombre);
        parcel.writeString(logo);
    }

    protected ParcelableMarca(Parcel in)
    {
        pagina_web = in.readString();
        nombre = in.readString();
        logo = in.readString();
    }

    public static final Creator<ParcelableMarca> CREATOR = new Creator<ParcelableMarca>()
    {
        @Override
        public ParcelableMarca createFromParcel(Parcel in)
        {
            return new ParcelableMarca(in);
        }

        @Override
        public ParcelableMarca[] newArray(int size)
        {
            return new ParcelableMarca[size];
        }
    };

    public static ParcelableMarca fromMarca(Marca marca)
    {
        String sinDatos = "sin datos";

        String nombre = marca.getNombre();
        if (nombre == null)
            nombre = sinDatos;

        String web = marca.getPagina_web();
        if (web == null)
            web = sinDatos;

        return new ParcelableMarca(web, nombre, marca.getLogo());
    }
}
