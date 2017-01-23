package net.asovel.myebike.resultadosebikes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import net.asovel.myebike.backendless.common.DefaultCallback;
import net.asovel.myebike.backendless.data.EBike;

import java.util.ArrayList;
import java.util.List;

public class Query {

    public static final String WHERECLAUSE = "WHERECLAUSE";
    public static final String SORTBY = "SORTBY";

    private Context context;
    private EBikesCallback callback;
    private BackendlessCollection<EBike> collection;

    public Query(Context context, EBikesCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void launchQuery(List<String> clauses, List<String> sortBy, int pagesize) {

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();

        if (clauses != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < clauses.size(); i++) {
                builder.append(" and " + clauses.get(i));
            }

            String whereClause = builder.substring(5);
            dataQuery.setWhereClause(whereClause);
            Log.w(WHERECLAUSE, whereClause);
        }

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.addRelated("marca");

        queryOptions.setPageSize(pagesize);

        if (sortBy == null) {
            sortBy = new ArrayList<>();
            sortBy.add("patrocinado_SORT0 DESC");
            sortBy.add("valoracion_SORT1 DESC");
            sortBy.add("precio_SORT2");
        }
        queryOptions.setSortBy(sortBy);

        dataQuery.setQueryOptions(queryOptions);

        Backendless.Data.of(EBike.class).find(dataQuery, new DefaultCallback<BackendlessCollection<EBike>>(context) {
            @Override
            public void handleResponse(BackendlessCollection<EBike> response) {
                super.handleResponse(response);

                if (response.getCurrentPage().size() == 0) {
                    Toast.makeText(context, "Hem de posar mes bicis al Backendless", Toast.LENGTH_LONG).show();
                    return;
                }
                collection = response;
                callback.onQueryCallback(collection);
            }
        });
    }

    public int getTotalPages(int pageSize) {
        if (collection == null)
            return 0;

        int totalObjects = collection.getTotalObjects();

        if (totalObjects % pageSize == 0)
            return (totalObjects / pageSize) - 1;

        return totalObjects / pageSize;
    }

    public void nextPage(int page, int pagesize) {
        if (collection == null)
            return;

        collection.getPage(pagesize, page * pagesize, new DefaultCallback<BackendlessCollection<EBike>>(context) {
            @Override
            public void handleResponse(BackendlessCollection<EBike> response) {
                super.handleResponse(response);
                collection = response;
                callback.onQueryCallback(collection);
            }
        });
    }
}
