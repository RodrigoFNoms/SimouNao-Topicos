package com.example.simounao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String URL = "https://yesno.wtf/api/";
    private Retrofit retrofit;
    private EditText editPergunta;
    private Button botao;
    private TextView txtResposta;
    private TextView txtTraducao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editPergunta =findViewById(R.id.pergunta);
        botao = findViewById(R.id.bt);
        txtResposta = findViewById(R.id.txt);
        txtTraducao = findViewById(R.id.txtTraducao);

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        botao.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        consultarResposta();
        editPergunta.setText("");
    }

    private void consultarResposta(){
        String pergunta = editPergunta.getText().toString();
        if (!pergunta.isEmpty()) {
            RestService restService = retrofit.create(RestService.class);

            Call<Resposta> call = restService.consultar(pergunta);

            call.enqueue(new Callback<Resposta>() {
                @Override
                public void onResponse(Call<Resposta> call, Response<Resposta> response) {
                    if (response.isSuccessful()) {
                        Resposta resposta = response.body();
                        txtResposta.setText(resposta.getAnswer());

                        // Adicione a tradução
                        String traducao = traduzirResposta(resposta.getAnswer());
                        txtTraducao.setText("Tradução: " + traducao);
                    }
                }

                @Override
                public void onFailure(Call<Resposta> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Erro " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Digite uma pergunta antes de consultar.", Toast.LENGTH_SHORT).show();
        }
    }
    private String traduzirResposta(String resposta) {
        if ("yes".equalsIgnoreCase(resposta)) {
            return "sim";
        } else if ("no".equalsIgnoreCase(resposta)) {
            return "não";
        }
        return resposta;
    }
}