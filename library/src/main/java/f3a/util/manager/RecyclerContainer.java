package f3a.util.manager;

import java.util.LinkedList;
import java.util.List;

/**
 * 物体复用容器
 * @author Bob
 * @since 2018-08-03
 */
public abstract class RecyclerContainer<T> {
    
    /** 复用队列 **/
    final List<T> usableList = new LinkedList<>();
    /** 使用队列 **/
    final List<T> usingList = new LinkedList<>();
    
    /**
     * 添加物体
     */
    public T add() {
        synchronized (usingList) {
            T newObj;
            if (usableList.isEmpty()) {// 如果复用队列为空，则新建
                newObj = onCreateObj();
            } else {// 否则从复用队列中取
                newObj = usableList.remove(0);
            }
            // 检查已经显示的礼物是否超过最大数量
            if (maxUsingCount != MAX_USING_COUNT_INFINITE && usingList.size() >= maxUsingCount) {
                switch (recycleStrategy) {
                    case FIFO:
                        removeAsync(usingList.get(0));
                        break;
                    case FILO:
                        removeAsync(usingList.get(usingList.size() - 1));
                        break;
                }
            }
            // 将该礼物加入使用队列
            usingList.add(newObj);
            onAdd(newObj);
            return newObj;
        }
    }
    
    protected void onAdd(T obj) {
    
    }
    
    /**
     * 移除物体
     */
    private void removeAsync(T obj) {
        // 将物体从使用队列移到复用队列
        usingList.remove(obj);
        usableList.add(obj);
        onRemove(obj);
    }
    
    /**
     * 移除物体
     */
    public void remove(T obj) {
        synchronized (usingList) {
            removeAsync(obj);
        }
    }
    
    /**
     * 移除物体
     */
    protected void onRemove(T obj) {
    
    }
    
    /**
     * 新建物体
     */
    public abstract T onCreateObj();
    
    /**
     * 使用物体：关系到物体回收策略，如果回收策略无所谓，可以不调用此方法，否则每次使用物体都应该调用该方法
     */
    public void useObj(T obj) {
        synchronized (usingList) {
            usingList.remove(obj);
            usingList.add(obj);
        }
    }
    
    public int getUsingCount() {
        return usingList.size();
    }
    
    public int getUsableCount() {
        return usableList.size();
    }
    
    //////////////////////////////// 容器最大数量限制 ////////////////////////////////
    
    /** 最大数量：不限制 **/
    public static final int MAX_USING_COUNT_INFINITE = -1;
    
    /** 最大数量：如果当前使用数量触发临界值则会强制回收正在使用的，具体回收策略可以自行设置 **/
    private int maxUsingCount = MAX_USING_COUNT_INFINITE;
    
    public void setMaxUsingCount(int maxUsingCount) {
        this.maxUsingCount = maxUsingCount;
    }
    
    //////////////////////////////// 回收策略 ////////////////////////////////
    
    public enum RecycleStrategy {
        /** 先用先回收 **/
        FIFO,
        /** 先用后回收 **/
        FILO
    }
    
    RecycleStrategy recycleStrategy = RecycleStrategy.FIFO;
    
    public RecycleStrategy getRecycleStrategy() {
        return recycleStrategy;
    }
    
    public void setRecycleStrategy(RecycleStrategy recycleStrategy) {
        this.recycleStrategy = recycleStrategy;
    }
}
