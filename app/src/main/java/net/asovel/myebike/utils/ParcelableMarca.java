package net.asovel.myebike.utils;

import android.os.Parcel;
import android.os.Parcelable;

import net.asovel.myebike.backendless.data.Marca;

public class ParcelableMarca implements Parcelable
{
    private String pagina_web;
    private String nombre;
    private String logo;
    private boolean experto;

    public ParcelableMarca(String pagina_web, String nombre, String logo, boolean experto)
    {
        this.pagina_web = pagina_web;
        this.nombre = nombre;
        this.logo = logo;
        this.experto = experto;
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

    public boolean getExperto()
    {
        return experto;
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
        parcel.writeByte((byte) (experto ? 1 : 0));
    }

    protected ParcelableMarca(Parcel in)
    {
        pagina_web = in.readString();
        nombre = in.readString();
        logo = in.readString();
        experto = in.readByte() != 0;
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
        boolean experto = false;
        if (marca.getExperto() != null)
            experto = marca.getExperto().booleanValue();

        return new ParcelableMarca(marca.getPagina_web(), marca.getNombre(), marca.getLogo(), experto);
    }
}
