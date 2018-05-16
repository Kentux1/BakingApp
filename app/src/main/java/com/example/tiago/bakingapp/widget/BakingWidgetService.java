package com.example.tiago.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class BakingWidgetService extends IntentService {

    private static final String ACTION_UPDATE_WIDGET = "com.example.tiago.bakingapp.action.update_widget";

    public BakingWidgetService() {
        super("BakingWidgetService");
    }

    public static void startActionUpdateWidget(Context context, String[] recipe) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        intent.putExtra("widget_ingredients", recipe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String[] extra = intent.getStringArrayExtra("widget_ingredients");
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                handleActionUpdateWidget(extra);
            }
        }
    }

    private void handleActionUpdateWidget(String[] arg) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));

        BakingWidgetProvider.updateBakingWidgets(this, appWidgetManager, appWidgetIds, arg);
    }
}
