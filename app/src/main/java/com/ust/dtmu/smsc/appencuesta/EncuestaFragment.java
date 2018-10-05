package com.ust.dtmu.smsc.appencuesta;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EncuestaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EncuestaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EncuestaFragment extends Fragment implements OnMapReadyCallback {
    //MAP CODE
    int REQUEST_IMAGE_CAPTURE = 0;
    private final String CARPETA_RAIZ="encuestasApp/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"fotos";
    String path;

    SupportMapFragment mapFragment;
    GoogleMap map;
    View view;
    ImageView btn_delete1, btn_delete2, img1, img2;
    Bitmap imageBitmap1, imageBitmap2;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EncuestaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EncuestaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EncuestaFragment newInstance(String param1, String param2) {
        EncuestaFragment fragment = new EncuestaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_encuesta, container, false);

        final ScrollView mainScrollView = (ScrollView) view.findViewById(R.id.main_scrollview);
        ImageView transparentImageView = (ImageView) view.findViewById(R.id.transparent_image);
        validaPermisos();

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
        btn_delete1 = (ImageView) view.findViewById(R.id.btn_delte1);
        btn_delete2 = (ImageView) view.findViewById(R.id.btn_delte2);
        img1 = (ImageView) view.findViewById(R.id.img1);
        img2 = (ImageView) view.findViewById(R.id.img2);
        btn_delete1.setVisibility(View.INVISIBLE);
        btn_delete2.setVisibility(View.INVISIBLE);
        btn_delete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBitmap1 = null;
                img1.setImageResource(R.drawable.camera);
                btn_delete1.setVisibility(View.INVISIBLE);
            }
        });
        btn_delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBitmap2 = null;
                img2.setImageResource(R.drawable.camera);
                btn_delete2.setVisibility(View.INVISIBLE);
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFoto(1);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFoto(2);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        LatLng laPaz = new LatLng(-16.5326454, -68.1436549);
        map.moveCamera(CameraUpdateFactory.newLatLng(laPaz));


        CameraPosition claPaz = CameraPosition.builder().target(new LatLng(-16.5326454, -68.1436549)).zoom(16).bearing(0).tilt(45).build();


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng).title("Domicilio ciudadano");
                map.clear();
                map.addMarker(markerOptions);
            }
        });
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(claPaz));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMyLocationEnabled(true);
    }

    private boolean tomarFoto(int numero) {
        REQUEST_IMAGE_CAPTURE = numero;
        String nombreImg ="";
        //ESCRITURA EN EL DISPOSITIVO
        /*File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();
        if (isCreada == false){
            isCreada = fileImagen.mkdirs();
        }

        if (isCreada == true){
            nombreImg = (System.currentTimeMillis()/100)+"jpg";
        }
        path = Environment.getExternalStorageDirectory()+File.separator+RUTA_IMAGEN+nombreImg;
        File imagen = new File(path);
        */
        //INICIAR CAMARA
        if (ActivityCompat.checkSelfPermission(getContext(), CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhoto.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(takePhoto, REQUEST_IMAGE_CAPTURE);
            return true;
        }else{
            pedirPermisos();
        }
        return false;
    }

    private boolean validaPermisos() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ActivityCompat.checkSelfPermission(getContext(), CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), CAMERA)) {
            pedirPermisos();
        } else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA}, 100);
        }


        return false;
    }

    private void pedirPermisos() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe Aceptar Todos Los Permisos para el correcto funcionamiento de la App");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA}, 100);
            }
        });
        dialogo.show();
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"si", "no"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getActivity());
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                map.setMyLocationEnabled(true);
            }else{
                solicitarPermisosManual();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imageBitmap1 = (Bitmap)extras.get("data");
            PicassoReziser picassoReziser = new PicassoReziser();

            img1.setImageBitmap(picassoReziser.transform(imageBitmap1));
            btn_delete1.setVisibility(View.VISIBLE);
        }

        if (requestCode == 2 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imageBitmap2 = (Bitmap)extras.get("data");
            img2.setImageBitmap(imageBitmap2);
            btn_delete2.setVisibility(View.VISIBLE);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
