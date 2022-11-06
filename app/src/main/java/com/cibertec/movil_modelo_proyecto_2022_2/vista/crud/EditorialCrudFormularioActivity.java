package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Editorial;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Pais;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceEditorial;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServicePais;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorialCrudFormularioActivity extends NewAppCompatActivity {

    TextView txtTitulo;
    Button btnEnviar;

    Spinner spnPais;
    ArrayAdapter<String> adaptador;
    ArrayList<String> paises = new ArrayList<String>();

    //Servicio
    ServiceEditorial serviceEditorial;
    ServicePais servicePais;

    //Componentes
    EditText txtRaz, txtDir, txtRuc, txtFec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorial_crud_formulario);

        txtRaz = findViewById(R.id.idCrudEditorialFrmRazonSocial);
        txtDir = findViewById(R.id.idCrudEditorialFrmDireccion);
        txtRuc = findViewById(R.id.idCrudEditorialFrmRuc);
        txtFec = findViewById(R.id.idCrudEditorialFrmFecCreacion);

        //para conectar al servicio rest
        serviceEditorial = ConnectionRest.getConnection().create(ServiceEditorial.class);
        servicePais = ConnectionRest.getConnection().create(ServicePais.class);

        //Para el adapatador
        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnPais = findViewById(R.id.idCrudEditorialFrmPais);
        spnPais.setAdapter(adaptador);

        cargaPais();

        txtTitulo = findViewById(R.id.idCrudEditorialFrmTitulo);
        btnEnviar = findViewById(R.id.idCrudEditorialFrmBtnRegistrar);

        Bundle extras = getIntent().getExtras();
        String tipo = extras.getString("var_tipo");
        if (tipo.equals("REGISTRAR")){
            txtTitulo.setText("Mantenimiento Editorial - REGISTRA");
            btnEnviar.setText("REGISTRA");
        }else   if (tipo.equals("ACTUALIZAR")){
            txtTitulo.setText("Mantenimiento Editorial - ACTUALIZA");
            btnEnviar.setText("ACTUALIZA");

            Editorial obj = (Editorial) extras.get("var_item");

            txtRaz.setText(obj.getRazonSocial());
            txtDir.setText(obj.getDireccion());
            txtRuc.setText(obj.getRuc());
            txtFec.setText(obj.getFechaCreacion());
        }
    }

    public void cargaPais(){
        Call<List<Pais>> call = servicePais.listaPais();
        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if (response.isSuccessful()){
                    List<Pais> lstPaises =  response.body();
                    for(Pais obj: lstPaises){
                        paises.add(obj.getIdPais() +":"+ obj.getNombre());
                    }
                    adaptador.notifyDataSetChanged();
                }else{
                    mensajeToastLong("Error al acceder al Servicio Rest >>> ");
                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                mensajeToastLong("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }


}