package com.safo.safoandroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.safo.safoandroid.model.Agendamento;
import com.safo.safoandroid.model.Quadra;
import com.safo.safoandroid.network.RetrofitClient;
import com.safo.safoandroid.network.SafoApiService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroAgendamentoActivity extends AppCompatActivity {

    private Spinner spinnerQuadras;
    private EditText editNomeCliente, editDataHoraInicio, editDataHoraFim, editStatus;
    private Button btnSalvar;
    private List<Quadra> listaQuadras = new ArrayList<>();
    private String perfilUsuario = "CLIENTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_agendamento);

        spinnerQuadras = findViewById(R.id.spinnerQuadras);
        editNomeCliente = findViewById(R.id.editNomeCliente);
        editDataHoraInicio = findViewById(R.id.editDataHoraInicio);
        editDataHoraFim = findViewById(R.id.editDataHoraFim);
        editStatus = findViewById(R.id.editStatusAgendamento);
        btnSalvar = findViewById(R.id.btnSalvarAgendamento);

        // Recebe os dados passados pela Intent
        String nomeCliente = getIntent().getStringExtra("nomeCliente");
        if (getIntent().hasExtra("perfilUsuario")) {
            perfilUsuario = getIntent().getStringExtra("perfilUsuario");
        }

        // Auto-preenche e bloqueia o nome do cliente se estiver presente
        if (nomeCliente != null && !nomeCliente.isEmpty()) {
            editNomeCliente.setText(nomeCliente);
            editNomeCliente.setEnabled(false);
        }

        // Se for perfil de cliente, fixa o status como PENDENTE e bloqueia
        if ("CLIENTE".equalsIgnoreCase(perfilUsuario) || (nomeCliente != null && !nomeCliente.isEmpty())) {
            editStatus.setText("PENDENTE");
            editStatus.setEnabled(false);
        }

        // Seletores virtuais de data e hora
        editDataHoraInicio.setOnClickListener(v -> abrirSeletorDataHora(editDataHoraInicio));
        editDataHoraFim.setOnClickListener(v -> abrirSeletorDataHora(editDataHoraFim));

        // Carrega as quadras cadastradas no servidor
        carregarQuadras();

        btnSalvar.setOnClickListener(v -> salvarAgendamento());
    }

    private void abrirSeletorDataHora(EditText campoTarget) {
        Calendar c = Calendar.getInstance();

        if (campoTarget == editDataHoraFim && !editDataHoraInicio.getText().toString().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                Date inicioDate = sdf.parse(editDataHoraInicio.getText().toString());
                if (inicioDate != null) {
                    c.setTime(inicioDate);
                    c.add(Calendar.HOUR_OF_DAY, 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int anoInicial = c.get(Calendar.YEAR);
        int mesInicial = c.get(Calendar.MONTH);
        int diaInicial = c.get(Calendar.DAY_OF_MONTH);
        int horaInicial = c.get(Calendar.HOUR_OF_DAY);
        int minutoInicial = c.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timeView, hourOfDay, minuteSelection) -> {
                int minutoAjustado = (minuteSelection < 15 || minuteSelection >= 45) ? 0 : 30;
                int horaAjustada = hourOfDay;
                if (minuteSelection >= 45) {
                    horaAjustada = (hourOfDay + 1) % 24;
                }

                // Criamos variáveis locais mutáveis para armazenar o resultado final
                int anoFinal = year;
                int mesFinal = month;
                int diaFinal = dayOfMonth;
                int horaFinal = horaAjustada;
                int minutoFinal = minutoAjustado;

                if (campoTarget == editDataHoraFim && !editDataHoraInicio.getText().toString().isEmpty()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                        Date inicioDate = sdf.parse(editDataHoraInicio.getText().toString());

                        Calendar calFim = Calendar.getInstance();
                        calFim.set(year, month, dayOfMonth, horaAjustada, minutoAjustado, 0);

                        if (inicioDate != null && calFim.getTime().before(new Date(inicioDate.getTime() + 3600000L))) {
                            calFim.setTime(inicioDate);
                            calFim.add(Calendar.HOUR_OF_DAY, 1);

                            anoFinal = calFim.get(Calendar.YEAR);
                            mesFinal = calFim.get(Calendar.MONTH);
                            diaFinal = calFim.get(Calendar.DAY_OF_MONTH);
                            horaFinal = calFim.get(Calendar.HOUR_OF_DAY);
                            minutoFinal = calFim.get(Calendar.MINUTE);

                            Toast.makeText(this, "Ajustado para respeitar o mínimo de 1h de duração.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String dataFormatada = String.format(Locale.getDefault(), "%04d-%02d-%02dT%02d:%02d:00",
                        anoFinal, mesFinal + 1, diaFinal, horaFinal, minutoFinal);
                campoTarget.setText(dataFormatada);
            }, horaInicial, minutoInicial, true);
            timePickerDialog.show();

        }, anoInicial, mesInicial, diaInicial);
        datePickerDialog.show();
    }

    private void carregarQuadras() {
        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);
        apiService.getQuadras().enqueue(new Callback<List<Quadra>>() {
            @Override
            public void onResponse(Call<List<Quadra>> call, Response<List<Quadra>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    listaQuadras = response.body();
                    List<String> exibicaoQuadras = new ArrayList<>();

                    // Formata a exibição incluindo o preço por hora da quadra
                    for (Quadra q : listaQuadras) {
                        if (q.getPrecoHora() != null) {
                            exibicaoQuadras.add(q.getNome() + " - R$ " + q.getPrecoHora() + "/h");
                        } else {
                            exibicaoQuadras.add(q.getNome());
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            CadastroAgendamentoActivity.this,
                            android.R.layout.simple_spinner_item,
                            exibicaoQuadras
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerQuadras.setAdapter(adapter);
                } else {
                    Toast.makeText(CadastroAgendamentoActivity.this, "Nenhuma quadra encontrada no banco de dados!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Quadra>> call, Throwable t) {
                Toast.makeText(CadastroAgendamentoActivity.this, "Erro de conexão ao buscar quadras", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void salvarAgendamento() {
        if (spinnerQuadras.getSelectedItemPosition() < 0 || listaQuadras.isEmpty()) {
            Toast.makeText(this, "Selecione uma quadra!", Toast.LENGTH_SHORT).show();
            return;
        }

        Quadra quadraSelecionada = listaQuadras.get(spinnerQuadras.getSelectedItemPosition());
        String nomeCliente = editNomeCliente.getText().toString().trim();
        String inicio = editDataHoraInicio.getText().toString().trim();
        String fim = editDataHoraFim.getText().toString().trim();
        String status = editStatus.getText().toString().trim();

        if (nomeCliente.isEmpty() || inicio.isEmpty() || fim.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação básica: início deve ser anterior ao fim
        if (inicio.compareTo(fim) >= 0) {
            Toast.makeText(this, "O horário de fim deve ser posterior ao horário de início!", Toast.LENGTH_LONG).show();
            return;
        }

        // REGRA 2: Valida se a duração do agendamento é de no mínimo 1 hora (60 minutos)
        if (!validarDuracaoMinima(inicio, fim)) {
            Toast.makeText(this, "O agendamento deve ter no mínimo 1 hora de duração!", Toast.LENGTH_LONG).show();
            return;
        }

        Agendamento ag = new Agendamento();
        ag.setQuadra(quadraSelecionada);
        ag.setNomeCliente(nomeCliente);
        ag.setDataHoraInicio(inicio);
        ag.setDataHoraFim(fim);
        ag.setStatus(status.toUpperCase());

        SafoApiService apiService = RetrofitClient.getClient().create(SafoApiService.class);
        apiService.salvarAgendamento(ag).enqueue(new Callback<Agendamento>() {
            @Override
            public void onResponse(Call<Agendamento> call, Response<Agendamento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroAgendamentoActivity.this, "Solicitação de reserva enviada!", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.code() == 409 || response.code() == 400) {
                    Toast.makeText(CadastroAgendamentoActivity.this, "Horário indisponível para esta quadra!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CadastroAgendamentoActivity.this, "Erro ao solicitar reserva", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Agendamento> call, Throwable t) {
                Toast.makeText(CadastroAgendamentoActivity.this, "Falha na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validarDuracaoMinima(String dataHoraInicioStr, String dataHoraFimStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date inicio = sdf.parse(dataHoraInicioStr);
            Date fim = sdf.parse(dataHoraFimStr);

            if (inicio != null && fim != null) {
                long diferencaMilis = fim.getTime() - inicio.getTime();
                long minutosDiferenca = diferencaMilis / (1000 * 60);

                return minutosDiferenca >= 60;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}