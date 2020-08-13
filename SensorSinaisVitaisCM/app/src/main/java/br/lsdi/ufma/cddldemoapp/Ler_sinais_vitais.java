package br.lsdi.ufma.cddldemoapp;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// caminho="/storage/emulated/0/Download/pacientes/055/05500001.csv"

public class Ler_sinais_vitais {

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
                    Log.d("SinaisVitais", line);
                }
                is.close();
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
}
