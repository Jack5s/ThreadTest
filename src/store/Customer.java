package store;

public class Customer {
	public String name;
	public int goodsCount;

	public Customer(String name) {
		this.name = name;
	}

	public void buyGoods(Store store) {
		goodsCount = 0;
		CustomerBuyGoodsThread customerThread = new CustomerBuyGoodsThread(store);
		customerThread.start();
	}

	class CustomerBuyGoodsThread extends Thread {
		private Store store;

		CustomerBuyGoodsThread(Store store) {
			this.store = store;
		}

		@Override
		public void run() {
			synchronized (store) {
				try {
					while (store.goodsCount > 0) {
						store.goodsCount--;
						goodsCount++;
						store.notifyAll();//必须先notifyAll，然后wait，否则可能会所有线程都在等待
						store.wait();
					}
					store.notifyAll();//结束线程之前也要notifyAll,通知其它线程资源已经被释放了
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
				} finally {
					System.out.println(name + ": " + String.valueOf(goodsCount));
				}
			}
		}
	}
}
