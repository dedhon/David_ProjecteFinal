package com.example.david_projectefinal.ui.maquina;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.david_projectefinal.BuidemDataSource;
import com.example.david_projectefinal.MaquinaaddClass;
import com.example.david_projectefinal.R;
import com.example.david_projectefinal.filtratge;
import com.example.david_projectefinal.ui.maps.maps;
import com.example.david_projectefinal.ui.zona.ZonaFragment;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class MaquinaFragment extends Fragment {
    Context context;
    int pos = 0;
    static String nom, numSerie;

    private filtratge filtreAplicat;

    MediaPlayer mediaPlayer;
    //Contingut adapter per listview
    String[] columnesMaquina = new String[]{
            BuidemDataSource.iD,
            BuidemDataSource.nomM,
            BuidemDataSource.adreçaM,
            BuidemDataSource.codiPostalM,
            BuidemDataSource.poblacioM,
            BuidemDataSource.tlfM,
            BuidemDataSource.emailM,
            BuidemDataSource.numM,
            BuidemDataSource.dataM,
            BuidemDataSource.tipusForeign,
            BuidemDataSource.zonaForeign
    };
    int[] toMaquina = new int[]{
            R.id.lblId,
            R.id.lblNomCli,
            R.id.lblAdreça,
            R.id.lblCodiPostal,
            R.id.lblPobla,
            R.id.lblTlf,
            R.id.lblEmail,
            R.id.lblNumSer,
            R.id.lblDataUR,
            R.id.lblTipus,
            R.id.lblZona
    };
    private static int ADD_MAQUINA = 1;
    private static int UPDATA_MAQUINA = 2;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_MAQUINA) {
            if (resultCode == RESULT_OK) {
                filtreAplicat = filtratge.FILTRE_TOT;
                actualitzarMaquines();
            }
        }
        if (requestCode == UPDATA_MAQUINA) {
            if (resultCode == RESULT_OK) {
                filtreAplicat = filtratge.FILTRE_TOT;
                actualitzarMaquines();
            }
        }
    }
    ////////////////

    //Image views
    ImageView addMaquina, deleteMaquina, imageTrucada;
    public static BuidemDataSource bd;
    public static adapterTodoIcon dataAdapter;

    ListView listView;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ordenar: {
                ordenarMaquines();
                return true;
            }
            case R.id.filtrar: {
                buscarMaquinaFiltre();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_actionbar_opcions, menu);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Instanciem la base de dades amb aquest contexte
        bd = new BuidemDataSource(getContext());
        //Definim la vista i la inflem amb el fragment de maquina
        View myview = inflater.inflate(R.layout.fragment_maquina, container, false);
        demanarPermisos();

        deleteMaquina = (ImageView) myview.findViewById(R.id.imgdelete123);
        implementacioListView(myview);
        addMaquinaButon(myview);
        setHasOptionsMenu(true);
        return myview;
    }

    public void buscarMaquinaFiltre() {
        AlertDialog.Builder buscador = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();

        View v2 = inflater.inflate(R.layout.buscar_numserie, null);

        final EditText etNumser = v2.findViewById(R.id.etnumserieAbuscar);
        buscador.setView(v2).setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String aux = etNumser.getText().toString();
                Cursor curAux = bd.filtrarNumSerie(aux);
                dataAdapter = new adapterTodoIcon(getContext(),
                        R.layout.row_estructuramaquines,
                        curAux,
                        columnesMaquina,
                        toMaquina,
                        1, MaquinaFragment.this);
                listView.setAdapter(dataAdapter);
            }
        });
        buscador.setNegativeButton("Cancelar", null);
        AlertDialog dialog = buscador.create();
        dialog.show();
    }

    public void demanarPermisos() {
        final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.CALL_PHONE)) {

                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
            }
        }
    }

    public void ordenarMaquines() {
        List<CharSequence> list = new ArrayList<CharSequence>();
        list.add("Nom Client");
        list.add("Zona, Població i Adreça");
        list.add("Zona");
        list.add("Població");
        list.add("Adreça");
        list.add("Data última revisió");
        final CharSequence[] dialogList = list.toArray(new CharSequence[list.size()]);
        final AlertDialog.Builder builderDialog = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();
        View viewTitle = inflater.inflate(R.layout.seleccio_ordenacio, null);
        builderDialog.setCustomTitle(viewTitle);

        builderDialog.setIcon(R.mipmap.ic_launcher);

        int count = dialogList.length;
        //  boolean[] is_checked = new boolean[count];

        builderDialog.setSingleChoiceItems(dialogList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pos = which;
            }
        });
        builderDialog.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogList[pos].equals("Nom Client")) {
                            filtreAplicat = filtratge.FILTRE_NOM;
                            actualitzarMaquines();
                        }
                        if (dialogList[pos].equals("Zona, Població i Adreça")) {
                            filtreAplicat = filtratge.FILTRE_MIX;
                            actualitzarMaquines();
                        }
                        if (dialogList[pos].equals("Zona")) {
                            filtreAplicat = filtratge.FILTRE_ZONA;
                            actualitzarMaquines();
                        }
                        if (dialogList[pos].equals("Població")) {
                            filtreAplicat = filtratge.FILTRE_POBLACIO;
                            actualitzarMaquines();
                        }
                        if (dialogList[pos].equals("Adreça")) {
                            filtreAplicat = filtratge.FILTRE_ADREÇA;
                            actualitzarMaquines();
                        }
                        if (dialogList[pos].equals("Data última revisió")) {
                            filtreAplicat = filtratge.FILTRE_DATA;
                            actualitzarMaquines();
                        }

                    }
                });

        builderDialog.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alert = builderDialog.create();
        alert.show();
    }

    public void deleteMaquina(long idF, ViewGroup parent) {
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.suspdef);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        context = parent.getContext();
        bdCursorDelete(idF);
        String nomF, numF;
        nomF = nom;
        numF = numSerie;

        AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view = factory.inflate(R.layout.sample, null);
        ImageView segur = (ImageView) view.findViewById(R.id.dialog_imageview);
        Glide.with(getContext()).load(R.drawable.segurgif).into(segur);
        alertadd.setView(view);
        String missatgeAenviar = "¿Estas segur que vols eliminar la màquina amb les següents dades:?" + "\n" +
                "-ID Màquina: " + idF + "\n" +
                "-Nom client: " + nomF + "\n" +
                "-Número serie: " + numF;
        alertadd.setMessage(missatgeAenviar);

        alertadd.setNeutralButton("Si!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                MaquinaFragment.bdEliminar(idF);
                filtreAplicat = filtratge.FILTRE_TOT;
                actualitzarMaquines();
                mediaPlayer.stop();
            }
        });
        alertadd.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                mediaPlayer.stop();
            }
        });
        alertadd.show();

    }

    private static void bdCursorDelete(long idF) {
        Cursor cur = bd.agafarMaquinaUna(idF);
        cur.moveToFirst();
        nom = cur.getString(cur.getColumnIndex(BuidemDataSource.nomM));
        numSerie = cur.getString(cur.getColumnIndex(BuidemDataSource.numM));

    }

    private static void bdEliminar(long idF) {
        bd.eliminarMaquina(idF);
    }


    public void addMaquinaButon(View v) {
        addMaquina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddMaquina();
            }
        });
    }

    public Cursor primeraCarregaCursor() {
        Cursor aux = null;
        if (filtreAplicat == null) {
            aux = bd.mostrarAllMaquines();
        } else {
            if (filtreAplicat.equals(filtratge.FILTRE_NOM)) {
                aux = bd.ordenarNom();
            } else {
                if (filtreAplicat.equals(filtratge.FILTRE_ZONA)) {
                    aux = bd.ordenarZona();
                } else {
                    if (filtreAplicat.equals(filtratge.FILTRE_POBLACIO)) {
                        aux = bd.ordenarPoblacio();
                    } else {
                        if (filtreAplicat.equals(filtratge.FILTRE_ADREÇA)) {
                            aux = bd.ordenarAdreça();
                        } else {
                            if (filtreAplicat.equals(filtratge.FILTRE_DATA)) {
                                aux = bd.ordenarData();
                            } else {
                                if (filtreAplicat.equals(filtratge.FILTRE_MIX)) {
                                    aux = bd.ordenarMix();
                                }
                            }
                        }
                    }
                }
            }
        }
        return aux;
    }

    public void implementacioListView(View myview) {
        //Definim una imatge per aplicarli amb el GLIDE un GIF a la imatge
        addMaquina = (ImageView) myview.findViewById(R.id.imageAddMaquina);
        Glide.with(getContext()).load(R.drawable.addmac).into(addMaquina);
        //Instanciem el listView
        listView = (ListView) myview.findViewById(R.id.list1);

        Cursor cursor = primeraCarregaCursor();

        dataAdapter = new adapterTodoIcon(getContext(),
                R.layout.row_estructuramaquines,
                cursor,
                columnesMaquina,
                toMaquina,
                1, MaquinaFragment.this);

        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                editarAddMaquina(id);

            }
        });
    }

    public void dialogAddMaquina() {

        Bundle bundle = new Bundle();
        long[] idActual = new long[2];
        idActual[0] = 1;
        idActual[1] = 0;
        bundle.putLongArray("FILTRE", idActual);
        Intent intent = new Intent(getActivity(), MaquinaaddClass.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, ADD_MAQUINA);
    }

    public void editarAddMaquina(long id) {

        Bundle bundle = new Bundle();
        long[] idActual = new long[2];
        idActual[0] = 2;
        idActual[1] = id;
        bundle.putLongArray("FILTRE", idActual);
        Intent intent = new Intent(getActivity(), MaquinaaddClass.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, UPDATA_MAQUINA);


    }

    private void actualitzarMaquines() {

        // Demanem totes les tasques
        Cursor cursorMaquines = bd.mostrarAllMaquines();

        // Demanem les tasques depenen del filtre que s'estigui aplicant
        switch (filtreAplicat) {
            case FILTRE_TOT:
                cursorMaquines = bd.mostrarAllMaquines();
                break;
            case FILTRE_MIX:
                cursorMaquines = bd.ordenarMix();
                break;
            case FILTRE_NOM:
                cursorMaquines = bd.ordenarNom();
                break;
            case FILTRE_ADREÇA:
                cursorMaquines = bd.ordenarAdreça();
                break;
            case FILTRE_ZONA:
                cursorMaquines = bd.ordenarZona();
                break;
            case FILTRE_POBLACIO:
                cursorMaquines = bd.ordenarPoblacio();
                break;
            case FILTRE_DATA:
                cursorMaquines = bd.ordenarData();
                break;
        }
        dataAdapter.changeCursor(cursorMaquines);
        dataAdapter.notifyDataSetChanged();
    }

}

class adapterTodoIcon extends android.widget.SimpleCursorAdapter {

    private static final String colorTaskPending = "#F04C4C";
    private static final String colorTaskCompleted = "#FFFFFF";

    private MaquinaFragment aTiconMaquina;

    public adapterTodoIcon(Context context, int layout, Cursor c, String[] from, int[] to, int flags, MaquinaFragment frag) {
        super(context, layout, c, from, to, flags);
        aTiconMaquina = frag;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        ImageView imageEmail = (ImageView) view.findViewById(R.id.imgEmail);
        ImageView imageTrucada = (ImageView) view.findViewById(R.id.imgTlf);
        ImageView botoEliminarProducte = (ImageView) view.findViewById(R.id.imgdelete123);
        ImageView botoEnviarMaps = (ImageView) view.findViewById(R.id.imgMaps);
        botoEliminarProducte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);
                aTiconMaquina.deleteMaquina(linia.getInt(linia.getColumnIndexOrThrow(BuidemDataSource.iD)), parent);

            }
        });
        botoEnviarMaps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
/*
                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);


                maps fragmento = new maps();
                Bundle b = new Bundle();
                b.putString("nom", "madrid");

                fragmento.setArguments(b);
                FragmentTransaction tr = aTiconMaquina.getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.nav_host_fragment, fragmento);
                tr.addToBackStack(null).commit();
                Bundle result = new Bundle();
                result.putString("bundleKey", "result");
                aTiconMaquina.getParentFragmentManager().setFragmentResult("requestKey", result);*/
            }
        });
        imageTrucada.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Carrego la linia del cursor de la posició.
                ferTrucada(view, position, aTiconMaquina.getContext());
            }
        });
        imageEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Carrego la linia del cursor de la posició.
                enviarEmail(view, position, aTiconMaquina.getContext());
            }
        });


        return view;
    }

    public void enviarEmail(View v, int position, Context context) {
        Cursor linia = (Cursor) getItem(position);
        Cursor updateMaquina = aTiconMaquina.bd.agafarMaquinaUna(linia.getInt(linia.getColumnIndexOrThrow(BuidemDataSource.iD)));
        updateMaquina.moveToFirst();
        String sEmail = updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.emailM));
        if (!sEmail.trim().equals("")) {
            String sNums = updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.numM));
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{sEmail});
            email.putExtra(Intent.EXTRA_SUBJECT, "“Propera revisió màquina nº " + sNums);
            email.setType("message/rfc822");
            aTiconMaquina.startActivity(Intent.createChooser(email, "Choose an Email client :"));
        } else {
            Toast.makeText(context, "No hi ha e-mail al que enviar un correu!!", Toast.LENGTH_SHORT).show();

        }
    }

    public void ferTrucada(View v, int position, Context context) {
        Cursor linia = (Cursor) getItem(position);
        Cursor updateMaquina = aTiconMaquina.bd.agafarMaquinaUna(linia.getInt(linia.getColumnIndexOrThrow(BuidemDataSource.iD)));
        updateMaquina.moveToFirst();
        String tlfon = updateMaquina.getString(updateMaquina.getColumnIndex(BuidemDataSource.tlfM));
        if (!tlfon.trim().equals("")) {
            if (tlfon.length() == 9) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + tlfon));
                aTiconMaquina.startActivity(i);
            } else {
                Toast.makeText(context, "La longitud del telefon ha de ser de 9 digits!!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "No hi ha telèfon al que trucar!!", Toast.LENGTH_SHORT).show();

        }

    }
}