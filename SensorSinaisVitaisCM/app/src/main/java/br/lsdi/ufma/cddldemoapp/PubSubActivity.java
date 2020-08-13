package br.lsdi.ufma.cddldemoapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import br.ufma.lsdi.cddl.CDDL;
import br.ufma.lsdi.cddl.listeners.IMonitorListener;
import br.ufma.lsdi.cddl.message.Message;
import br.ufma.lsdi.cddl.pubsub.Monitor;
import br.ufma.lsdi.cddl.pubsub.MonitorImpl;
import br.ufma.lsdi.cddl.pubsub.Publisher;
import br.ufma.lsdi.cddl.pubsub.PublisherFactory;
import br.ufma.lsdi.cddl.pubsub.Subscriber;
import br.ufma.lsdi.cddl.pubsub.SubscriberFactory;

public class PubSubActivity extends AppCompatActivity {

    private static final String MY_SERVICE = "my-service";
    private EditText mensagemEditText;
    private TextView mensagensTextView;
    private CDDL cddl;
    private Publisher pub;
    private Subscriber sub;
    private EventBus eb;
    MonitorPrincipal monit = new MonitorPrincipal();
    Controle_Alerta controle_alerta = new Controle_Alerta(this);
    TextView t1, t2, t3;

    private String caminho = "/storage/emulated/0/Download/pacientes/055/05500001.csv";
    InputStream is;
    BufferedReader reader;
    Message msgem = new Message();
    String ms;
    public Handler handler = new Handler();
    public AlertDialog alerta;
    String monitorCode;

    private ListView listView;
    private List<String> listViewMessages;
    private ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_sub);

        mensagemEditText = findViewById(R.id.mensagemEditText);

        // Mostra total de alertas gerados por nível
        t1= findViewById(R.id.idA1);
        t2 = findViewById(R.id.idA2);
        t3 = findViewById(R.id.idA3);

        mensagensTextView = findViewById(R.id.mensagensTextView);

        Button publicarButton = findViewById(R.id.publicarButton);
        publicarButton.setOnClickListener(this::onClick);

        eb = EventBus.builder().build();
        eb.register(this);


        configCDDL();
        configPublisher();
        configSubscriber();
        configListView();
    }

    private void configArquivo() {
        try {
            is = new FileInputStream(caminho);
            reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        }
        catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void atualizaTotalAlerta(TextView tex, int i){
        if( i == 1 ){
            int[] total = controle_alerta.getTotalAlertaNivel();
            int t = total[0] + 1;
            total[0] = t;
            t1.setText(Integer.toString(t));
            controle_alerta.setTotalAlertaNivel(total);
        }
        else if( i== 2 ){
            int[] total = controle_alerta.getTotalAlertaNivel();
            int t = total[1] + 1;
            total[1] = t;
            t2.setText(Integer.toString(t));
            controle_alerta.setTotalAlertaNivel(total);
        }
        else{
            int[] total = controle_alerta.getTotalAlertaNivel();
            int t = total[2] + 1;
            total[2] = t;
            t3.setText(Integer.toString(t));
            controle_alerta.setTotalAlertaNivel(total);
        }
    }

    private void configPublisher() {
        pub = PublisherFactory.createPublisher();
        pub.addConnection(cddl.getConnection());
    }

    private void configSubscriber() {
        sub = SubscriberFactory.createSubscriber();
        sub.addConnection(cddl.getConnection());

        sub.subscribeServiceByName(MY_SERVICE);
        sub.setSubscriberListener(this::onMessage);

        // EWS  Frequência  respiratória
        // Sinal:RESP Valor: <=9
        monitorCode = pub.getMonitor().addRule("select * from Message " +
                "where cast(serviceValue[0], string)='RESP' and cast(serviceValue[1], double)<=9", message -> {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            atualizaTotalAlerta(t2,2);
                            geraAlerta(2, msgAlerta(message));
                        }
                    });
                }
            }.start();});

        // EWS  Frequência  respiratória
        // Sinal:RESP Valor: 15 >= x <= 20
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='RESP' and cast(serviceValue[1], double)>=16 and cast(serviceValue[1], double)<=20", message -> {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            atualizaTotalAlerta(t1,1);
                            if(true) {
                                geraAlerta(1, msgAlerta(message));
                            }
                        }
                    });
                }
            }.start();});

        // EWS  Frequência  respiratória
        // Sinal:RESP Valor: 21 >= x <= 29
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='RESP' and cast(serviceValue[1], double)>=21 and cast(serviceValue[1], double)<=29", message -> {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            atualizaTotalAlerta(t2,2);
                            geraAlerta(2, msgAlerta(message));
                        }
                    });
                }
            }.start();});

        // EWS  Frequência  respiratória
        // Sinal:RESP Valor: >=30
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='RESP' and cast(serviceValue[1], double)>=30", message -> {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            atualizaTotalAlerta(t3,3);
                            geraAlerta(3, msgAlerta(message));
                        }
                    });
                }
            }.start();});

        // EWS Saturação O2
        // Sinal:SPO2 Valor: <85
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='SPO2' and cast(serviceValue[1], double)<85", message -> {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            atualizaTotalAlerta(t3,3);
                            geraAlerta(3, msgAlerta(message));
                        }
                    });
                }
            }.start();});

        // EWS Saturação O2
        // Sinal:SPO2 Valor: 85 >= x <= 89
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='SPO2' and cast(serviceValue[1], double)>=85 and cast(serviceValue[1], double)<=89", message -> {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            atualizaTotalAlerta(t2,2);
                            geraAlerta(2, msgAlerta(message));
                        }
                    });
                }
            }.start();});

        // EWS Saturação O2
        // Sinal:SPO2 Valor: 90 >= x <= 92
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='SPO2' and cast(serviceValue[1], double)>=90 and cast(serviceValue[1], double)<=92", message -> {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            atualizaTotalAlerta(t1,1);
                            geraAlerta(1, msgAlerta(message));
                        }
                    });
                }
            }.start();});

        // EWS Frequência cardíaca
        // Sinal:HR Valor: <= 40
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='HR' and cast(serviceValue[1], double)<=40", message -> {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            atualizaTotalAlerta(t2,2);
                            geraAlerta(2, msgAlerta(message));
                        }
                    });
                }
            }.start();});

        // EWS Frequência cardíaca
        // Sinal:HR Valor: 41>= x <= 50
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='HR' and cast(serviceValue[1], double)>=41 and cast(serviceValue[1], double)<=50", message -> {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            atualizaTotalAlerta(t1,1);
                            geraAlerta(1, msgAlerta(message));
                        }
                    });
                }
            }.start();});

        // EWS Frequência cardíaca
        // Sinal:HR Valor: 101>= x <= 110
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='HR' and cast(serviceValue[1], double)>=102 and cast(serviceValue[1], double)<=110", message -> {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            atualizaTotalAlerta(t1,1);
                            geraAlerta(1, msgAlerta(message));
                        }
                    });
                }
            }.start();});

        // EWS Frequência cardíaca
        // Sinal:HR Valor: 111>= x <= 129
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='HR' and cast(serviceValue[1], double)>=111 and cast(serviceValue[1], double)<=129", message -> {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            atualizaTotalAlerta(t2,2);
                            geraAlerta(2, msgAlerta(message));
                        }
                    });
                }
            }.start();});

        // EWS Frequência cardíaca
        // Sinal:HR Valor: >=130
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='HR' and cast(serviceValue[1], double)>=130", message -> {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            atualizaTotalAlerta(t3,3);
                            geraAlerta(3, msgAlerta(message));
                        }
                    });
                }
            }.start();});

        // EWS Pulso
        // Sinal:PULSE Valor: <=40>
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='PULSE' and cast(serviceValue[1], double)<=40", message -> {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            atualizaTotalAlerta(t1,1);
                            geraAlerta(1, msgAlerta(message));
                        }
                    });
                }
            }.start();});

        // EWS Pulso
        // Sinal:PULSE Valor: >=101
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='PULSE' and cast(serviceValue[1], double)>=102", message -> {
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            atualizaTotalAlerta(t1,1);
                            geraAlerta(1, msgAlerta(message));
                        }
                    });
                }
            }.start();});
    }
    public String msgAlerta(Message msg){
        Object[] valor = msg.getServiceValue();
        String mensagemRecebida = StringUtils.join(valor, ", ");
        String ms = "";
        ms = mensagemRecebida;
        String[] sin = ms.split(";|;\\s");
        //String ss = sin[0];
        //double vv = Double.parseDouble(sin[1]);
        return sin[0];
    }

    public void configMonitor(Message msn){
        monit.metodo(sub, pub, this, msn);
    }

    private void onMessage(Message message) {
        //eb.post(new MessageEvent(message));

        //Tentando mandar a mensagem
        //Object[] valor = message.getServiceValue();
        //listViewMessages.add(0, StringUtils.join(valor, ", "));
        //listViewAdapter.notifyDataSetChanged();

        //new Thread() {
            //public void run() {
               // runOnUiThread(new Runnable() {
                    //@Override
                    //public void run() {
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    //}
                //});
            //}
        //}.start();

        handler.post(() -> {
            Object[] valor = message.getServiceValue();
            listViewMessages.add(StringUtils.join(valor, ", "));
            listViewAdapter.notifyDataSetChanged();

            String str = (String) valor[0];
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on(MessageEvent event) {
        Object[] valor = event.getMessage().getServiceValue();
        mensagensTextView.setText((String) valor[0]);
        listViewMessages.add(StringUtils.join((String)valor[0], ", "));
    }


    private void configCDDL() {
        cddl = CDDL.getInstance();
    }

    @Override
    protected void onDestroy() {
        eb.unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        AppMenu appMenu = AppMenu.getInstance();
        appMenu.setMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AppMenu appMenu = AppMenu.getInstance();
        appMenu.setMenuItem(PubSubActivity.this, item);
        return super.onOptionsItemSelected(item);
    }

    private void onClick(View view) {
        //Message msg = new Message();
        //msg.setServiceName(MY_SERVICE);
        //msg.setServiceValue(mensagemEditText.getText().toString());
        //pub.publish(msg);

        //msgem.setServiceName(MY_SERVICE);
        //readData(caminho);

        //try{
            //InputStream is = new FileInputStream(caminho);
            //BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            limpaListView();
            configArquivo();
            String line = "";
            try {
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    ms = "";
                    ms = line;
                    Message msn = new Message();
                    msn.setServiceName(MY_SERVICE);

                    String[] sinal = line.split(";|;\\s");
                    //String[] sinal = line.split(",");
                    String s = sinal[0];
                    double d = Double.parseDouble(sinal[1]);

                    Object[] o = {s,d};
                    msn.setServiceValue(o);

                    pub.publish(msn);

                    //sub.subscribeServiceByName(MY_SERVICE);
                    //sub.setSubscriberListener(this::onMessage);
                }
                is.close();
                reader.close();
            } catch (IOException e) {
                Log.wtf("Sinais_vitais", "Erro ao ler arquivo" + line, e);
                e.printStackTrace();
            }
        //}
        //catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
        //}
    }

    // Método ler o arquivo coluna por coluna
    public void readDataByColumn(String caminho) {
        try{
            InputStream is = new BufferedInputStream(new FileInputStream(caminho));
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line = "";
            try {
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] cols = line.split(",");
                    System.out.println("Sinais vitais: " + cols[0] );

                    // Passa a leitura do sensor para
                    //subscriber.setSubscriberListener(this::onMessage);
                    //Object obj = cols[0];
                    ms = "";
                    ms = cols[0];
                    //onMessageTopic(ms);
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    // Método ler arquivo linha por linha
    public void readData( String caminho) {
        try{
            InputStream is = new FileInputStream(caminho);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8)
            );
            String line = "";
            try {
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    //Log.d("SinaisVitais", line);

                    // envia os dados para tela celular
                    ms = "";
                    ms = line;

                    msgem.setServiceValue(ms);
                    pub.publish(msgem);
                }
                is.close();
                reader.close();
            } catch (IOException e) {
                Log.wtf("Sinais_vitais", "Erro ao ler arquivo" + line, e);
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private void configListView() {
        listView = findViewById(R.id.listview);
        listViewMessages = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(this, listViewMessages);
        listView.setAdapter(listViewAdapter);
    }

    public void limpaListView(){
        listViewMessages.clear();
        listViewAdapter.notifyDataSetChanged();
    }

    // Cria uma janela com alerta, informando o nível de degradação do paciente
    public void geraAlerta(int nivelAlerta, String str) {
        if ( nivelAlerta != 0 ) {
            //LayoutInflater é utilizado para inflar nosso layout em uma view.
            //-pegamos nossa instancia da classe
            LayoutInflater li = getLayoutInflater();

            //inflamos o layout alerta.xml na view
            //View view = li.inflate(R.layout.alerta, null);
            View view = li.inflate(R.layout.alerta1, null);
            if (nivelAlerta == 1) {
                view = li.inflate(R.layout.alerta1, null);
            } else if (nivelAlerta == 2) {
                view = li.inflate(R.layout.alerta2, null);
            } else if (nivelAlerta == 3) {
                view = li.inflate(R.layout.alerta3, null);
            }

            //definimos para o botão do layout um clickListener
            view.findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    //exibe um Toast informativo.
                    Toast.makeText(PubSubActivity.this, "alerta", Toast.LENGTH_SHORT).show();
                    //desfaz o alerta.
                    //alerta.dismiss();
                    alerta.cancel();
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setTitle("Alerta de paciente");
            View viewText = view;
            TextView textView = viewText.findViewById(R.id.sinal);
            textView.setText(str);
            builder.setView(view);
            alerta = builder.create();
            alerta.show();
        }
    }
}
