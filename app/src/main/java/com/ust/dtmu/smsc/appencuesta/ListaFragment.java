package com.ust.dtmu.smsc.appencuesta;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ust.dtmu.smsc.appencuesta.entities.Person;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaFragment extends Fragment {
    View view;
    ListView listView;
    public ListaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lista, container, false);
        listView = (ListView)view.findViewById(R.id.listview);
        ArrayList<String>listPerson = new ArrayList<>();
        listPerson.add("VICTOR ANAYA");
        listPerson.add("PEDRO PEREZ");
        listPerson.add("ANA MARY");
        listPerson.add("ALAN WALKER");
        listPerson.add("JOSE JOSE");
        listPerson.add("JONAS BRHOTERS");
        listPerson.add("EUGENIO DERBEZ");
        listPerson.add("JOAN SEBASTIAN");
        listPerson.add("MIGUEL ANGEL");
        listPerson.add("BARTOLOMEO J. SIMPSON");
        listPerson.add("ELIAS MARCA");
        listPerson.add("NORA MARISOL");
        listPerson.add("MARISOL ANAYA");
        ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, listPerson);
        listView.setAdapter(adapter);
        return view;
    }

    private EncuestaFragment.OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
