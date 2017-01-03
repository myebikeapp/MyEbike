package net.asovel.myebike.resultadosebikes;

import com.backendless.BackendlessCollection;

import net.asovel.myebike.backendless.data.EBike;

public interface EBikesCallback {

    public void onQueryCallback(BackendlessCollection<EBike> collection);
}
