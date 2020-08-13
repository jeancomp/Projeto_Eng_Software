package br.lsdi.ufma.cddldemoapp;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import br.ufma.lsdi.cddl.message.Message;
import br.ufma.lsdi.cddl.pubsub.Publisher;
import br.ufma.lsdi.cddl.pubsub.Subscriber;
import br.ufma.lsdi.cddl.listeners.IMonitorListener;

public class MonitorPrincipal {
    PubSubActivity pubActivity;
    Publisher publisher;
    Subscriber subscriber;
    AlertDialog alerta;
    String monitorCode;
    //public ExampleMonitorListener monitorListener = new ExampleMonitorListener();

    private IMonitorListener monitorListener = new IMonitorListener() {
        @Override
        public void onEvent(final Message message) {
            Object[] valor = message.getServiceValue();
            String mensagemRecebida = StringUtils.join(valor, ", ");
            String[] separated = mensagemRecebida.split(",");
            String atividade = String.valueOf(separated[0]);
            //Double latitude = Double.valueOf(separated[1]);
            System.out.printf("mensagemRecebida: %s \n", mensagemRecebida);
            System.out.printf("separated: %s \n", separated[0]);
            System.out.printf("atividade: %s \n", atividade);
            //System.out.printf("latitude: %.2f \n", latitude);


            String ms = "";
            ms = mensagemRecebida;
            String[] sin = ms.split(";|;\\s");
            String ss = sin[0];
            double vv = Double.parseDouble(sin[1]);
            if( ss.equals("RESP")) {
                System.out.printf("Sinais vitais string: %s \n",ss);
                System.out.printf("Sinais vitais numero: %f \n",vv);
                new Thread() {
                    public void run() {
                        pubActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pubActivity.geraAlerta(1, msgAlerta(message));
                            }
                        });
                    }
                }.start();
            }


            System.out.println("A mensagem vem anexa: " + message);
            String line = message.toString();
            String[] sinal = line.split(";|;\\s");
            String s = sinal[0];
            double v = Double.parseDouble(sinal[1]);
            //if( s.equals("C.O.")) {
            //System.out.printf("Sinais vitais string: %s \n",s);
            //System.out.printf("Sinais vitais numero: %f \n",v);
            //}

            System.out.printf(">>>>>>>>>>>>>>>>>>>>>>>>>>>>Line: %s \n", line);
            System.out.printf(">>>>>>>>>>>>>>>>>>>>>>>>>>>>Sinal[0]: %s \n", sinal[0]);
            System.out.printf(">>>>>>>>>>>>>>>>>>>>>>>>>>>>s: %s \n", s);

            if (line.equals("RESP")) {
                new Thread() {
                    public void run() {
                            pubActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>Gerando alertas");
                                    pubActivity.geraAlerta(1, msgAlerta(message));
                                }
                            });
                    }
                }.start();
            }
        }
    };

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

    public void metodo(Subscriber sub, Publisher pub, PubSubActivity p, Message msg) {
        publisher = pub;
        subscriber = sub;
        pubActivity = p;

        // EWS  Frequência  respiratória
        // Sinal:RESP Valor: <=9
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='RESP' and cast(serviceValue[1], double)<=9", message -> {
            System.out.println("1 - ####################################################################");
            new Thread() {
                public void run() {
                    pubActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubActivity.geraAlerta(2, msgAlerta(msg));
                        }
                    });
                }
            }.start();});

        // EWS  Frequência  respiratória
        // Sinal:RESP Valor: 15 >= x <= 20
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='RESP' and cast(serviceValue[1], double)>=15 and cast(serviceValue[1], double)<=20", message -> {
            System.out.println("2 - ####################################################################");
            new Thread() {
                public void run() {
                    pubActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubActivity.geraAlerta(1, msgAlerta(msg));
                        }
                    });
                }
            }.start();});

        // EWS  Frequência  respiratória
        // Sinal:RESP Valor: 21 >= x <= 29
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='RESP' and cast(serviceValue[1], double)>=21 and cast(serviceValue[1], double)<=29", message -> {
            System.out.println("3 - ####################################################################");
            new Thread() {
                public void run() {
                    pubActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubActivity.geraAlerta(2, msgAlerta(msg));
                        }
                    });
                }
            }.start();});

        // EWS  Frequência  respiratória
        // Sinal:RESP Valor: >=30
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='RESP' and cast(serviceValue[1], double)>=30", message -> {
            System.out.println("4 - ####################################################################");
            new Thread() {
                public void run() {
                    pubActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubActivity.geraAlerta(3, msgAlerta(msg));
                        }
                    });
                }
            }.start();});

        // EWS Saturação O2
        // Sinal:SPO2 Valor: <85
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='SPO2' and cast(serviceValue[1], double)<85", message -> {
            System.out.println("5 - ####################################################################");
            new Thread() {
                public void run() {
                    pubActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubActivity.geraAlerta(3, msgAlerta(msg));
                        }
                    });
                }
            }.start();});

        // EWS Saturação O2
        // Sinal:SPO2 Valor: 85 >= x <= 89
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='SPO2' and cast(serviceValue[1], double)>=85 and cast(serviceValue[1], double)<=89", message -> {
            System.out.println("6 - ####################################################################");
            new Thread() {
                public void run() {
                    pubActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubActivity.geraAlerta(2, msgAlerta(msg));
                        }
                    });
                }
            }.start();});

        // EWS Saturação O2
        // Sinal:SPO2 Valor: 90 >= x <= 92
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='SPO2' and cast(serviceValue[1], double)>=90 and cast(serviceValue[1], double)<=92", message -> {
            System.out.println("7 - ####################################################################");
            new Thread() {
                public void run() {
                    pubActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubActivity.geraAlerta(1, msgAlerta(msg));
                        }
                    });
                }
            }.start();});

        // EWS Frequência cardíaca
        // Sinal:HR Valor: <= 40
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='HR' and cast(serviceValue[1], double)<=40", message -> {
            System.out.println("8 - ####################################################################");
            new Thread() {
                public void run() {
                    pubActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubActivity.geraAlerta(2, msgAlerta(msg));
                        }
                    });
                }
            }.start();});

        // EWS Frequência cardíaca
        // Sinal:HR Valor: 41>= x <= 50
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='HR' and cast(serviceValue[1], double)>=41 and cast(serviceValue[1], double)<=50", message -> {
            System.out.println("9 - ####################################################################");
            new Thread() {
                public void run() {
                    pubActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubActivity.geraAlerta(1, msgAlerta(msg));
                        }
                    });
                }
            }.start();});

        // EWS Frequência cardíaca
        // Sinal:HR Valor: 101>= x <= 110
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='HR' and cast(serviceValue[1], double)>=101 and cast(serviceValue[1], double)<=110", message -> {
            System.out.println("10 - ####################################################################");
            new Thread() {
                public void run() {
                    pubActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubActivity.geraAlerta(1, msgAlerta(msg));
                        }
                    });
                }
            }.start();});

        // EWS Frequência cardíaca
        // Sinal:HR Valor: 111>= x <= 129
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='HR' and cast(serviceValue[1], double)>=111 and cast(serviceValue[1], double)<=129", message -> {
            System.out.println("11 - ####################################################################");
            new Thread() {
                public void run() {
                    pubActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubActivity.geraAlerta(2, msgAlerta(msg));
                        }
                    });
                }
            }.start();});

        // EWS Frequência cardíaca
        // Sinal:HR Valor: >=130
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='HR' and cast(serviceValue[1], double)>=130", message -> {
            System.out.println("12 - ####################################################################");
            new Thread() {
                public void run() {
                    pubActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubActivity.geraAlerta(3, msgAlerta(msg));
                        }
                    });
                }
            }.start();});

        // EWS Pulso
        // Sinal:PULSE Valor: <=40>
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='PULSE' and cast(serviceValue[1], double)<=40", message -> {
            System.out.println("13 - ####################################################################");
            new Thread() {
                public void run() {
                    pubActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubActivity.geraAlerta(1, msgAlerta(msg));
                        }
                    });
                }
            }.start();});

        // EWS Pulso
        // Sinal:PULSE Valor: >=101
        monitorCode = pub.getMonitor().addRule("select * from Message where cast(serviceValue[0], string)='PULSE' and cast(serviceValue[1], double)>=101", message -> {
            System.out.println("14 - ####################################################################");
            new Thread() {
                public void run() {
                    pubActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pubActivity.geraAlerta(1, msgAlerta(msg));
                        }
                    });
                }
            }.start();});
    }

    public void removeMonitor(String monitorCode){
        // remove the monitor added above
        subscriber.getMonitor().removeRule(monitorCode);
    }
}
