package br.lsdi.ufma.cddldemoapp;

import java.util.Calendar;
import java.util.Date;

public class Controle_Alerta {
    String id_paciente="";
    String[] tipoAlerta = {"RESP","SPO2","HR","PULSE"};
    int[] totalAlerta = {0,0,0,0};
    Date[] tempoAlerta;
    int[] totalAlertaNivel = {0,0,0};
    PubSubActivity pub_sub;
    MainActivity sub;

    public Controle_Alerta(PubSubActivity ps) {
        this.pub_sub = ps;
        Calendar data;
        data = Calendar.getInstance();
    }

    public Controle_Alerta(MainActivity sub) {
        this.sub = sub;
    }

    public String getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(String id_paciente) {
        this.id_paciente = id_paciente;
    }

    public int[] getTotalAlerta() {
        return totalAlerta;
    }

    public void setTotalAlerta(int[] totalAlerta) {
        this.totalAlerta = totalAlerta;
    }

    public int[] getTotalAlertaNivel() {
        return totalAlertaNivel;
    }

    public void setTotalAlertaNivel(int[] totalAlertaNivel) {
        this.totalAlertaNivel = totalAlertaNivel;
    }

    public Date[] getTempoAlerta() {
        return tempoAlerta;
    }

    public void setTempoAlerta(Date[] tempoAlerta) {
        this.tempoAlerta = tempoAlerta;
    }
}
