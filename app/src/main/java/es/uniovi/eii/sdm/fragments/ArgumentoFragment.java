package es.uniovi.eii.sdm.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import es.uniovi.eii.sdm.R;

public class ArgumentoFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARGUMENTO = "argumento";


    public ArgumentoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_argumento, container, false);

        // Referencias a componentes.
        TextView argumento = (TextView)root.findViewById(R.id.txtArgumento);

        Bundle args = getArguments();
        if(args != null){
            argumento.setText(args.getString(ARGUMENTO));
        }

        return root;
    }
}