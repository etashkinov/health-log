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

import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.type.EventTypeFactory;
import com.ewind.hl.model.event.detail.EventDetail;

import java.util.ArrayList;
import java.util.List;

public class RecentEventTypesProvider extends SearchRecentSuggestionsProvider {
    private static final String TAG = RecentEventTypesProvider.class.getName();

    public final static String AUTHORITY = RecentEventTypesProvider.class.getName();
    public final static int MODE = DATABASE_MODE_QUERIES;

    public static void remember(EventType<?> type, Context context) {
        SearchRecentSuggestions suggestions = getSuggestions(context);
        suggestions.saveRecentQuery(type.getName(), null);
    }

    @NonNull
    protected static SearchRecentSuggestions getSuggestions(Context context) {
        return new SearchRecentSuggestions(context, AUTHORITY, MODE);
    }

    public static List<EventType<?>> getRecent(int limit, Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        String contentUri = "content://" + AUTHORITY + '/' + SearchManager.SUGGEST_URI_PATH_QUERY;
        Uri uri = Uri.parse(contentUri);

        List<EventType<?>> result = new ArrayList<>(limit);

        try (Cursor cursor = contentResolver.query(uri, null, null, new String[] { "" }, null)) {
            if (cursor != null) {
                while (cursor.moveToNext() && result.size() < limit) {
                        String typeName = cursor.getString(2);
                        EventType<EventDetail> type = EventTypeFactory.get(typeName);
                        result.add(type);
                }
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Failed to get event types history", e);
            SearchRecentSuggestions suggestions = getSuggestions(context);
            suggestions.clearHistory();
        }

        return result;
    }

    public RecentEventTypesProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}