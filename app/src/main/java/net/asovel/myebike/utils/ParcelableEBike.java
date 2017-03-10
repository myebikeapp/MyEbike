package net.asovel.myebike.utils;

import android.os.Parcel;
import android.os.Parcelable;

import net.asovel.myebike.backendless.data.EBike;
import net.asovel.myebike.main.FragmentMyEBike;

public class ParcelableEBike implements Parcelable
{
    public static final String PARCELABLEEBIKE = "PARCELABLEEBIKE";

    private String uso;
    private String suspension;
    private int autonomia;
    private int precio_SORT2;
    private float valoracion_SORT1;
    private String ubicacion_motor;
    private int peso;
    private String foto;
    private String modelo;
    private String link;
    private String descripcion;
    private int tamano_ruedas;
    private ParcelableMarca marca;

    public ParcelableEBike(String uso, String suspension, int autonomia, int precio_SORT2,
                           float valoracion_SORT1, String ubicacion_motor, int peso, String foto, String modelo,
                           String link, String descripcion, int tamano_ruedas, ParcelableMarca marca)
    {
        this.uso = uso;
        this.suspension = suspension;
        this.autonomia = autonomia;
        this.precio_SORT2 = precio_SORT2;
        this.valoracion_SORT1 = valoracion_SORT1;
        this.ubicacion_motor = ubicacion_motor;
        this.peso = peso;
        this.foto = foto;
        this.modelo = modelo;
        this.link = link;
        this.descripcion = descripcion;
        this.tamano_ruedas = tamano_ruedas;
        this.marca = marca;
    }

    public String getUso()
    {
        return uso;
    }

    public String getSuspension()
    {
        return suspension;
    }

    public int getAutonomia()
    {
        return autonomia;
    }

    public int getPrecio_SORT2()
    {
        return precio_SORT2;
    }

    public float getValoracion_SORT1()
    {
        return valoracion_SORT1;
    }

    public String getUbicacion_motor()
    {
        return ubicacion_motor;
    }

    public int getPeso()
    {
        return peso;
    }

    public String getFoto()
    {
        return foto;
    }

    public String getModelo()
    {
        return modelo;
    }

    public String getLink()
    {
        return link;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public int getTamano_ruedas()
    {
        return tamano_ruedas;
    }

    public ParcelableMarca getMarca()
    {
        return marca;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(uso);
        parcel.writeString(suspension);
        parcel.writeInt(autonomia);
        parcel.writeInt(precio_SORT2);
        parcel.writeFloat(valoracion_SORT1);
        parcel.writeString(ubicacion_motor);
        parcel.writeInt(peso);
        parcel.writeString(foto);
        parcel.writeString(modelo);
        parcel.writeString(link);
        parcel.writeString(descripcion);
        parcel.writeInt(tamano_ruedas);
        parcel.writeParcelable(marca, i);
    }

    public ParcelableEBike(Parcel in)
    {
        uso = in.readString();
        suspension = in.readString();
        autonomia = in.readInt();
        precio_SORT2 = in.readInt();
        valoracion_SORT1 = in.readFloat();
        ubicacion_motor = in.readString();
        peso = in.readInt();
        foto = in.readString();
        modelo = in.readString();
        link = in.readString();
        descripcion = in.readString();
        tamano_ruedas = in.readInt();
        marca = in.readParcelable(ParcelableMarca.class.getClassLoader());
    }

    public static final Creator<ParcelableEBike> CREATOR = new Creator<ParcelableEBike>()
    {
        @Override
        public ParcelableEBike createFromParcel(Parcel in)
        {
            return new ParcelableEBike(in);
        }

        @Override
        public ParcelableEBike[] newArray(int size)
        {
            return new ParcelableEBike[size];
        }
    };

    public static ParcelableEBike fromEBike(EBike eBike)
    {
        String sinDatos = "";

        String uso = eBike.getUso();
        if (uso == null)
            uso = sinDatos;
        else if (uso.equals(FragmentMyEBike.USO[2]))
            uso = "plegable";
        else if (uso.equals("montana"))
            uso = "montaña";
        else if (uso.equals(FragmentMyEBike.USO[6]))
            uso = "e-scooter";
        else if (uso.equals(FragmentMyEBike.USO[7]))
            uso = "otros";


        String suspension = eBike.getSuspension();
        if (suspension == null)
            suspension = sinDatos;
        else if (suspension.equals(FragmentMyEBike.SUSPENSION[1]))
            suspension = "sin suspensión";
        else if (suspension.equals(FragmentMyEBike.SUSPENSION[3]))
            suspension = "delantera y trasera";

        int autonomia = -1;
        if (eBike.getAutonomia() != null)
            autonomia = eBike.getAutonomia().intValue();

        int precio = -1;
        if (eBike.getPrecio_SORT2() != null)
            precio = eBike.getPrecio_SORT2().intValue();

        float valoracion = -1;
        if (eBike.getValoracion_SORT1() != null)
            valoracion = eBike.getValoracion_SORT1().floatValue();

        String motor = eBike.getUbicacion_motor();
        if (motor == null)
            motor = sinDatos;
        else if (motor.equals(FragmentMyEBike.MOTOR[2]))
            motor = "central o pedalier";

        int peso = -1;
        if (eBike.getPeso() != null)
            peso = eBike.getPeso().intValue();

        String link = eBike.getLink();
        String descripcion = eBike.getDescripcion();

        int ruedas = -1;
        if (eBike.getTamano_ruedas() != null)
            ruedas = eBike.getTamano_ruedas().intValue();

        ParcelableMarca marca = null;
        if (eBike.getMarca() != null)
            marca = ParcelableMarca.fromMarca(eBike.getMarca());

        return new ParcelableEBike(
                uso,
                suspension,
                autonomia,
                precio,
                valoracion,
                motor,
                peso,
                eBike.getFoto(),
                eBike.getModelo(),
                link,
                descripcion,
                ruedas,
                marca);
    }
}
