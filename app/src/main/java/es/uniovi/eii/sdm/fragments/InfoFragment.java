package es.uniovi.eii.sdm.fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import es.uniovi.eii.sdm.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ESTRENO = "estreno";
    public static final String DURACION = "duracion";
    public static final String CARATULA = "caratula";


    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_info, container, false);

        //Referencias a componentes
        final TextView txtEstreno = (TextView)root.findViewById(R.id.txtEstreno);
        final TextView txtDuracion = (TextView)root.findViewById(R.id.txtDuracion);
        ImageView caratula = (ImageView)root.findViewById(R.id.imgCaratula);

        Bundle args = getArguments();
        if(args != null){
            txtEstreno.setText(args.getString(ESTRENO));
            txtDuracion.setText(args.getString(DURACION));
            Picasso.get()
                    .load(args.getString(CARATULA)).into(caratula);
        }
        // Inflate the layout for this fragment
        return root;
    }
}