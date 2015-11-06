package game.server.logic;
/**
 * This is the tick that sends updates to the server
 * @author KieranLewis
 *
 */
public class serverTick extends Thread{
	private ServerLogic sl;
	public static final long tick = 10;

	public serverTick(ServerLogic sl) {
		this.sl = sl;
		this.setDaemon(true);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		sl.updateWorld();
		
		try {
			Thread.sleep(tick);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
