package com.github.nekdenis.currencylist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.nekdenis.currencylist.R;
import com.github.nekdenis.currencylist.db.provider.exchangevalue.ExchangevalueCursor;
import com.github.nekdenis.currencylist.view.ColumnGraph;

public class RateAdapter extends CursorAdapter {

    private static final int VIEW_TYPES_COUNT = 2;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    private boolean useTodayLayout = true;

    public RateAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.list_item_rate_today;
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.list_item_rate;
                break;
            }
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        ExchangevalueCursor exchangevalueCursor = new ExchangevalueCursor(cursor);
        holder.nameA.setText(exchangevalueCursor.getTitle());
        holder.valueA.setText(exchangevalueCursor.getRate());
        if (holder.graph != null) {
//            holder.graph.setChartData(new float[]{10, 12, 7, 14, 15, 19, 13, 2, 10, 13, 13, 10, 15, 14});
        }
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        this.useTodayLayout = useTodayLayout;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && useTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPES_COUNT;
    }

    private static class ViewHolder {
        private final TextView nameA;
        private final TextView valueA;
        private final ColumnGraph graph;

        public ViewHolder(View view) {
            graph = (ColumnGraph) view.findViewById(R.id.currency_graph);
            nameA = (TextView) view.findViewById(R.id.currency_path);
            valueA = (TextView) view.findViewById(R.id.currency_current_rate);
        }
    }
}