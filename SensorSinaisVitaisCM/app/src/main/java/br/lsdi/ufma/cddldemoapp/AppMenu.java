package br.lsdi.ufma.cddldemoapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class AppMenu {
    private static final int SUBSCRIBE_SENSOR_MENU_ID = Menu.FIRST;
    private static final int PUB_SUB_MESSAGE_MENU_ID = Menu.FIRST + 1;
    private static final int SINAIS_VITAIS_MENU_ID = Menu.FIRST + 2;

    private static AppMenu appMenu;

    // Adicionais Menus no SPINNER(android)=COMBOBOX(html)
    public void setMenu(Menu menu) {
        menu.add(android.view.Menu.NONE, SUBSCRIBE_SENSOR_MENU_ID, android.view.Menu.NONE, R.string.subscrever_sensores);
        menu.add(android.view.Menu.NONE, PUB_SUB_MESSAGE_MENU_ID, android.view.Menu.NONE, R.string.pub_sub_mensagem);
        menu.add(android.view.Menu.NONE, SINAIS_VITAIS_MENU_ID, android.view.Menu.NONE, R.string.sinais_vitais);
    }

    public static AppMenu getInstance() {
        if (appMenu == null) {
            appMenu = new AppMenu();
        }
        return appMenu;
    }

    // Ativa o SPNNIER que e o menus
    public void setMenuItem(Context ctx, MenuItem item) {
        switch (item.getItemId()) {
            case SUBSCRIBE_SENSOR_MENU_ID:
                ctx.startActivity(new Intent(ctx, MainActivity.class));
                return;
            case PUB_SUB_MESSAGE_MENU_ID:
                ctx.startActivity(new Intent(ctx, PubSubActivity.class));
                return;
            case SINAIS_VITAIS_MENU_ID:
                ctx.startActivity(new Intent(ctx, Sinais_vitais.class));
                return;
        }
    }
}
