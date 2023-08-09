package ir.mahchegroup.tickvision.rv;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemClick implements RecyclerView.OnItemTouchListener {
    private final OnItemClickListener Listener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    GestureDetector GestureDetector;

    public RecyclerItemClick(Context context, OnItemClickListener listener) {
        Listener = listener;
        GestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && Listener != null && GestureDetector.onTouchEvent(e)) {
            Listener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}