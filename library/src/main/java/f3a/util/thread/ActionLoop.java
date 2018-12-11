package f3a.util.thread;

/**
 * 循环执行操作
 * @author Bob
 *
 */
public abstract class ActionLoop implements Runnable {
	
	protected boolean isRunning = false;
	
	protected long cd;
    
    public ActionLoop() {
    }
    
    public ActionLoop(long cd) {
        this.cd = cd;
    }
    
    public ActionLoop cd(long cd) {
        this.cd = cd;
        return this;
    }
    
    public long getCd() {
        return cd;
    }
	
	public void cancel() {
		isRunning = false;
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public void run() {
		isRunning = true;
		preAction();
		while (isRunning) {
			action();
			try {
				Thread.sleep(getCd());
			} catch (InterruptedException e) {
			}
		}
		endAction();
	}
	
	protected abstract void action();
	
	protected void preAction() {}
	
	protected void endAction() {}
}
