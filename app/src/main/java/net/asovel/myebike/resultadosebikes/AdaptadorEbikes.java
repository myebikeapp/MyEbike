package net.asovel.myebike.resultadosebikes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.asovel.myebike.R;
import net.asovel.myebike.backendless.data.EBike;

import java.util.List;

public class AdaptadorEbikes extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_BUTTON = 1;

    public interface OnAdaptadorButtonsListener {
        void onButtonsClick();
    }

    private final Context context;
    private List<EBike> eBikes;
    private View.OnClickListener listener;
    private OnAdaptadorButtonsListener buttonsListener;
    private int totalPages;
    private int page;

    public AdaptadorEbikes(Context context, int totalPages) {
        this.context = context;
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setButtonsListener(OnAdaptadorButtonsListener listener) {
        this.buttonsListener = listener;
    }

    public void setEBikes(List<EBike> eBikes) {
        this.eBikes = eBikes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int numEBikes = eBikes.size();
        if (totalPages == 0)
            return numEBikes;

        return numEBikes + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position < eBikes.size()) ? VIEW_TYPE_ITEM : VIEW_TYPE_BUTTON;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.listado_bicicletas_item, viewGroup, false);
            view.setOnClickListener(this);
            return new BicicletasViewHolder(view);
        }

        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listado_bicicletas_button, viewGroup, false);

        return new ButtonsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (i < eBikes.size()) {
            EBike eBikeActual = eBikes.get(i);
            ((BicicletasViewHolder) viewHolder).bindBicicleta(eBikeActual);
        } else {
            ((ButtonsViewHolder) viewHolder).bindButtons();
        }
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }

    public class BicicletasViewHolder extends RecyclerView.ViewHolder {

        public static final int IMAGE_WIDTH = 384;
        public static final int IMAGE_HEIGHT = 256;

        private ImageView imagen;
        private TextView marcaModelo;
        private TextView precio;
        private RatingBar valoracion;

        private ImageView aux;

        private Target target;

        public BicicletasViewHolder(View view) {
            super(view);

            imagen = (ImageView) view.findViewById(R.id.list_imagen);
            marcaModelo = (TextView) view.findViewById(R.id.list_marca_modelo);
            precio = (TextView) view.findViewById(R.id.list_precio);
            valoracion = (RatingBar) view.findViewById(R.id.list_valoracion);

            aux= (ImageView) view.findViewById(R.id.a);

            target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

                    /*int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    Matrix matrix = new Matrix();
                    matrix.postScale(0.7f, 0.9f);
                    Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                    imagen.setImageBitmap(bitmap);*/
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {

                }

                @Override
                public void onPrepareLoad(Drawable drawable) {

                }
            };
        }

        public void bindBicicleta(EBike eBike) {
            if (eBike.getFoto() != null) {
                Picasso.with(context)
                        .load(eBike.getFoto())
                        .placeholder(R.drawable.ebike)
                        .fit().centerInside()
                        //.resize(IMAGE_WIDTH, IMAGE_HEIGHT)
                        .into(imagen);
            } else
                imagen.setImageResource(R.drawable.ebike);

            if (eBike.getMarca() != null)
                marcaModelo.setText(eBike.getMarca().getNombre());
            else
                marcaModelo.setText("");


            if (eBike.getModelo() != null)
                marcaModelo.setText(marcaModelo.getText() + " " + eBike.getModelo());
            else
                marcaModelo.setText(marcaModelo.getText());


            if (eBike.getPrecio_SORT2() != null)
                precio.setText("" + eBike.getPrecio_SORT2() + " €");
            else
                precio.setText("");

            if (eBike.getValoracion_SORT1() != null)
                valoracion.setRating(eBike.getValoracion_SORT1());
            else
                valoracion.setRating(0f);
        }
    }

    public class ButtonsViewHolder extends RecyclerView.ViewHolder {

        private Button btnNextPage;
        private Button btnPreviousPage;
        private RadioGroup radioGroup;
        private RadioButton radioButton0;
        private RadioButton radioButton1;
        private RadioButton radioButton2;
        private RadioButton radioButton3;
        private RadioButton radioButton4;
        private View.OnClickListener listener;
        private int lastPosition;
        private int lastID;

        public ButtonsViewHolder(View view) {
            super(view);

            listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId() == lastID)
                        return;
                    setUpRadioGroup(view.getId());
                    buttonsListener.onButtonsClick();
                }
            };

            btnNextPage = (Button) view.findViewById(R.id.btn_next_page);
            btnNextPage.setOnClickListener(listener);

            btnPreviousPage = (Button) view.findViewById(R.id.btn_previous_page);
            btnPreviousPage.setOnClickListener(listener);

            iniRadioButtons(view);
        }

        private void iniRadioButtons(View view) {
            if (totalPages > 1) {
                radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_numbers);
                lastID = R.id.btn_num_page_0;

                radioButton0 = (RadioButton) view.findViewById(R.id.btn_num_page_0);
                radioButton0.setVisibility(View.VISIBLE);
                radioButton0.setOnClickListener(listener);

                radioButton1 = (RadioButton) view.findViewById(R.id.btn_num_page_1);
                radioButton1.setVisibility(View.VISIBLE);
                radioButton1.setOnClickListener(listener);

                radioButton2 = (RadioButton) view.findViewById(R.id.btn_num_page_2);
                radioButton2.setVisibility(View.VISIBLE);
                radioButton2.setOnClickListener(listener);
            }
            if (totalPages > 2) {
                radioButton3 = (RadioButton) view.findViewById(R.id.btn_num_page_3);
                radioButton3.setVisibility(View.VISIBLE);
                radioButton3.setOnClickListener(listener);
            }
            if (totalPages > 3) {
                radioButton4 = (RadioButton) view.findViewById(R.id.btn_num_page_4);
                radioButton4.setVisibility(View.VISIBLE);
                radioButton4.setOnClickListener(listener);
            }
        }

        private void setUpRadioGroup(int id) {
            if (totalPages < 2) {
                if (id == R.id.btn_next_page)
                    page += 1;
                else
                    page -= 1;
            } else if (totalPages < 5) {
                switch (id) {
                    case R.id.btn_next_page:
                        lastID = getRadioPosition1(1);
                        radioGroup.check(lastID);
                        page += 1;
                        break;
                    case R.id.btn_previous_page:
                        lastID = getRadioPosition1(-1);
                        radioGroup.check(lastID);
                        page -= 1;
                        break;
                    case R.id.btn_num_page_0:
                        lastID = R.id.btn_num_page_0;
                        page = 0;
                        break;
                    case R.id.btn_num_page_1:
                        lastID = R.id.btn_num_page_1;
                        page = 1;
                        break;
                    case R.id.btn_num_page_2:
                        lastID = R.id.btn_num_page_2;
                        page = 2;
                        break;
                    case R.id.btn_num_page_3:
                        lastID = R.id.btn_num_page_3;
                        page = 3;
                        break;
                    case R.id.btn_num_page_4:
                        lastID = R.id.btn_num_page_4;
                        page = 4;
                        break;
                }
            } else {
                switch (id) {
                    case R.id.btn_next_page:
                        lastID = getRadioPosition2(1);
                        break;
                    case R.id.btn_previous_page:
                        lastID = getRadioPosition2(-1);
                        break;
                    case R.id.btn_num_page_0:
                        lastID = getRadioPosition2(-lastPosition);
                        break;
                    case R.id.btn_num_page_1:
                        lastID = getRadioPosition2(1 - lastPosition);
                        break;
                    case R.id.btn_num_page_2:
                        lastID = getRadioPosition2(2 - lastPosition);
                        break;
                    case R.id.btn_num_page_3:
                        lastID = getRadioPosition2(3 - lastPosition);
                        break;
                    case R.id.btn_num_page_4:
                        lastID = getRadioPosition2(4 - lastPosition);
                        break;
                }
                radioGroup.check(lastID);
            }
        }

        private int getRadioPosition1(int step) {
            if (step < 0) {
                switch (page) {
                    case 4:
                        return R.id.btn_num_page_3;
                    case 3:
                        return R.id.btn_num_page_2;
                    case 2:
                        return R.id.btn_num_page_1;
                    case 1:
                        return R.id.btn_num_page_0;
                }
            }
            switch (page) {
                case 0:
                    return R.id.btn_num_page_1;
                case 1:
                    return R.id.btn_num_page_2;
                case 2:
                    return R.id.btn_num_page_3;
                case 3:
                    return R.id.btn_num_page_4;
            }
            return 0;
        }

        private int getRadioPosition2(int step) {
            int id;
            if (step > 0) {
                if (totalPages >= page + step + 2)
                    if (lastPosition == 0 && step == 1) {
                        lastPosition = 1;
                        id = R.id.btn_num_page_1;
                    } else {
                        lastPosition = 2;
                        setUpRadioButtonsText(page + step);
                        id = R.id.btn_num_page_2;
                    }
                else if (totalPages == page + step + 1) {
                    lastPosition = 3;
                    setUpRadioButtonsText(totalPages - 2);
                    id = R.id.btn_num_page_3;
                } else {
                    lastPosition = 4;
                    id = R.id.btn_num_page_4;
                }
            } else {
                if (0 <= page + step - 2)
                    if (lastPosition == 4 && step == -1) {
                        lastPosition = 3;
                        id = R.id.btn_num_page_3;
                    } else {
                        lastPosition = 2;
                        setUpRadioButtonsText(page + step);
                        id = R.id.btn_num_page_2;
                    }
                else if (0 == page + step - 1) {
                    lastPosition = 1;
                    setUpRadioButtonsText(2);
                    id = R.id.btn_num_page_1;
                } else {
                    lastPosition = 0;
                    id = R.id.btn_num_page_0;
                }
            }
            page += step;
            return id;
        }

        private void setUpRadioButtonsText(int number) {
            radioButton0.setText("" + (number - 1));
            radioButton1.setText("" + number);
            radioButton2.setText("" + (number + 1));
            radioButton3.setText("" + (number + 2));
            radioButton4.setText("" + (number + 3));
        }

        public void bindButtons() {
            if (page < totalPages)
                btnNextPage.setVisibility(View.VISIBLE);
            else
                btnNextPage.setVisibility(View.GONE);

            if (page > 0)
                btnPreviousPage.setVisibility(View.VISIBLE);
            else
                btnPreviousPage.setVisibility(View.GONE);
        }
    }
}

