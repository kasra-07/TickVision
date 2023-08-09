package ir.mahchegroup.tickvision.rv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.mahchegroup.tickvision.R;

public class RvSelectAdapter extends RecyclerView.Adapter<RvSelectAdapter.RvViewHolder> {
    private final Context context;
    private final List<SelectRvModel> list;

    public RvSelectAdapter(Context context, List<SelectRvModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.select_vision_rv_item, null);
        return new RvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvViewHolder holder, int position) {
        SelectRvModel model = list.get(position);

        holder.img.setImageResource(model.getIsTick().equals("0") ? R.drawable.no_rv : R.drawable.ok_rv);
        holder.tv.setText(model.getTitle());
        holder.tv.setTextColor(model.getIsTick().equals("0") ? context.getColor(R.color.primary_color) : context.getColor(R.color.accent_color));
        holder.root.setBackgroundResource(model.getIsTick().equals("0") ? R.drawable.red_edt_background : R.drawable.blue_edt_background);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tv;
        LinearLayout root;
        public RvViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_is_tick_vision_select_vision);
            tv = itemView.findViewById(R.id.tv_vision_name_select_vision);
            root = itemView.findViewById(R.id.rv_item_root);
        }
    }
}
