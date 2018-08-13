package com.ewind.hl.ui;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.net.Uri;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.area.AreaFactory;

import java.util.ArrayList;
import java.util.List;

public class RecentAreasProvider extends SearchRecentSuggestionsProvider {
    private static final String TAG = RecentAreasProvider.class.getName();

    public final static String AUTHORITY = RecentAreasProvider.class.getName();
    public final static int MODE = DATABASE_MODE_QUERIES;

    public static void remember(Area area, Context context) {
        SearchRecentSuggestions suggestions = getSuggestions(context);
        suggestions.saveRecentQuery(area.getName(), null);
    }

    @NonNull
    protected static SearchRecentSuggestions getSuggestions(Context context) {
        return new SearchRecentSuggestions(context, AUTHORITY, MODE);
    }

    public static List<Area> getRecent(int limit, Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        String contentUri = "content://" + AUTHORITY + '/' + SearchManager.SUGGEST_URI_PATH_QUERY;
        Uri uri = Uri.parse(contentUri);

        List<Area> result = new ArrayList<>(limit);

        try (Cursor cursor = contentResolver.query(uri, null, null, new String[]{""}, null)) {
            if (cursor != null) {
                while (cursor.moveToNext() && result.size() < limit) {
                    String areaName = cursor.getString(2);
                    Area area = AreaFactory.getArea(areaName);

                    if (area != null) {
                        result.add(area);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Failed to get event types history", e);
            SearchRecentSuggestions suggestions = getSuggestions(context);
            suggestions.clearHistory();
        }

        return result;
    }

    public RecentAreasProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}