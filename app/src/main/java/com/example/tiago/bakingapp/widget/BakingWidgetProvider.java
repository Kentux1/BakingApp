package com.example.tiago.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.tiago.bakingapp.R;
import com.example.tiago.bakingapp.WidgetActivity;

public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String[] recipe) {
        Intent intent = new Intent(context, WidgetActivity.class);
        intent.putExtra("widget_recipe", recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);

        if (!recipe[0].equals("0")) {
            views.setTextViewText(R.id.appwidget_header, recipe[1]);
            views.setTextViewText(R.id.appwidget_text, recipe[2]);
        } else {
            views.setTextViewText(R.id.appwidget_header, "");
            views.setTextViewText(R.id.appwidget_text, context.getString(R.string.appwidget_text));
        }

        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        String [] recipe = new String[3];
        recipe[0] = "0";
        recipe[1] = "1";
        recipe[2] = context.getString(R.string.appwidget_text);
        updateBakingWidgets(context, appWidgetManager, appWidgetIds, recipe);
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String[] recipe) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
