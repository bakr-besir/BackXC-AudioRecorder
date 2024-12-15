package com.bakrxc.utils;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ArrayAdapterUtil<T> extends ArrayAdapter<T> {
    private final Context context;
    private final List<T> items;
    private final int resource;
    private final AdapterCallback<T> callback;

    /**
     * Constructor for the ArrayAdapterUtil.
     *
     * @param context  the current context
     * @param resource the layout resource ID for each item
     * @param items    the data for the adapter
     * @param callback the callback to customize views
     */
    public ArrayAdapterUtil(@NonNull Context context, @LayoutRes int resource, @NonNull List<T> items, @Nullable AdapterCallback<T> callback) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
        this.callback = callback;
    }

    @NonNull
    @Override
    public android.view.View getView(int position, @Nullable android.view.View convertView, @NonNull android.view.ViewGroup parent) {
        // Use a ViewHolder pattern for better performance
        ViewHolder holder;

        if (convertView == null) {
            convertView = android.view.LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Use the callback to populate the view
        if (callback != null) {
            callback.bindView(holder, items.get(position), position);
        }

        return convertView;
    }

    /**
     * Callback interface for binding views.
     */
    public interface AdapterCallback<T> {
        void bindView(ViewHolder holder, T item, int position);
    }

    /**
     * Generic ViewHolder class to cache views.
     */
    public static class ViewHolder {
        private final android.util.SparseArray<android.view.View> views = new android.util.SparseArray<>();
        private final android.view.View rootView;

        public ViewHolder(android.view.View rootView) {
            this.rootView = rootView;
        }

        public <V extends android.view.View> V findView(int id) {
            android.view.View view = views.get(id);
            if (view == null) {
                view = rootView.findViewById(id);
                views.put(id, view);
            }
            return (V) view;
        }
    }
}
