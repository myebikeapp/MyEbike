package net.asovel.myebike.utils;

import android.os.Parcel;
import android.os.Parcelable;

import net.asovel.myebike.backendless.data.EBike;

public class ParcelableEBike implements Parcelable
{
    public static final String PARCELABLEEBIKE = "PARCELABLEEBIKE";

    private String uso;
    private String suspension;
    private int autonomia;
    private String tipo_cuadro;
    private int precio_SORT2;
    private int valoracion_SORT1;
    private String ubicacion_motor;
    private int peso;
    private String foto;
    private String modelo;
    private String descripcion;
    private int tamano_ruedas;
    private ParcelableMarca marca;

    public ParcelableEBike(String uso, String suspension, int autonomia, String tipo_cuadro, int precio_SORT2,
                           int valoracion_SORT1, String ubicacion_motor, int peso, String foto, String modelo,
                           String descripcion, int tamano_ruedas, ParcelableMarca marca)
    {
        this.uso = uso;
        this.suspension = suspension;
        this.autonomia = autonomia;
        this.tipo_cuadro = tipo_cuadro;
        this.precio_SORT2 = precio_SORT2;
        this.valoracion_SORT1 = valoracion_SORT1;
        this.ubicacion_motor = ubicacion_motor;
        this.peso = peso;
        this.foto = foto;
        this.modelo = modelo;
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

    public String getTipo_cuadro()
    {
        return tipo_cuadro;
    }

    public int getPrecio_SORT2()
    {
        return precio_SORT2;
    }

    public int getValoracion_SORT1()
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
        parcel.writeString(tipo_cuadro);
        parcel.writeInt(precio_SORT2);
        parcel.writeInt(valoracion_SORT1);
        parcel.writeString(ubicacion_motor);
        parcel.writeInt(peso);
        parcel.writeString(foto);
        parcel.writeString(modelo);
        parcel.writeString(descripcion);
        parcel.writeInt(tamano_ruedas);
        parcel.writeParcelable(marca, i);
    }

    public ParcelableEBike(Parcel in)
    {
        uso = in.readString();
        suspension = in.readString();
        autonomia = in.readInt();
        tipo_cuadro = in.readString();
        precio_SORT2 = in.readInt();
        valoracion_SORT1 = in.readInt();
        ubicacion_motor = in.readString();
        peso = in.readInt();
        foto = in.readString();
        modelo = in.readString();
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
        String sinDatos = "sin datos";

        String uso = eBike.getUso();
        if (uso == null)
            uso = sinDatos;

        String suspension = eBike.getSuspension();
        if (suspension == null)
            suspension = sinDatos;

        int autonomia = -1;
        if (eBike.getAutonomia() != null)
            autonomia = eBike.getAutonomia().intValue();

        String cuadro = eBike.getTipo_cuadro();
        if (cuadro == null)
            cuadro = sinDatos;

        int precio = -1;
        if (eBike.getPrecio_SORT2() != null)
            precio = eBike.getPrecio_SORT2().intValue();

        int valoracion = -1;
        if (eBike.getValoracion_SORT1() != null)
            valoracion = eBike.getValoracion_SORT1().intValue();

        String motor = eBike.getUbicacion_motor();
        if (motor == null)
            motor = sinDatos;

        int peso = -1;
        if (eBike.getPeso() != null)
            peso = eBike.getPeso().intValue();

        String modelo = eBike.getModelo();
        if (modelo == null)
            modelo = sinDatos;

        String descripcion = eBike.getDescripcion();
        if (descripcion == null)
            descripcion = sinDatos;

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
                cuadro,
                precio,
                valoracion,
                motor,
                peso,
                eBike.getFoto(),
                modelo,
                descripcion,
                ruedas,
                marca);
    }
}
