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
						store.notifyAll();//������notifyAll��Ȼ��wait��������ܻ������̶߳��ڵȴ�
						store.wait();
					}
					store.notifyAll();//�����߳�֮ǰҲҪnotifyAll,֪ͨ�����߳���Դ�Ѿ����ͷ���
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
				} finally {
					System.out.println(name + ": " + String.valueOf(goodsCount));
				}
			}
		}
	}
}
