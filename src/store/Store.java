package store;

public class Store {
	public int goodsCount = 10;

	public void open() {
		StoreOpenThread storeOpenThread = new StoreOpenThread();
		storeOpenThread.start();
	}

	//check goods has been sell
	class StoreOpenThread extends Thread {
		@Override
		public void run() {
			try {
				while (goodsCount > 0) {
					Thread.sleep(1000);//睡一会避免浪费资源
				}
				System.out.println("ok");
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			} finally {
			}

		}
	}
}