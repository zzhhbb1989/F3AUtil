package f3a.util.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * author zhengbo
 * date 2018-01-07
 */
public abstract class MyBaseViewHolder extends RecyclerView.ViewHolder {
    
    public MyBaseViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(clickListener);
    }
    
    public MyBaseViewHolder(ViewGroup parent, int layoutId) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }
    
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onItemClick(getAdapterPosition());
        }
    };
    
    /**
     * 设置数据
     */
    public void setData(){
    }
    
    /**
     * 设置数据
     */
    public void setData(int position){
    }
    
    /**
     * item点击事件
     * @param position 列表位置
     */
    public void onItemClick(int position) {
    }
}
