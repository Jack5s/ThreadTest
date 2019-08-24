package store;
import store.Customer;
import store.Store;

public class Applicaiton {
	public static Store store = new Store();
	public static void main(String[] args) {
		store.open();
		int i;
		for (i = 1; i <= 2; i++) {
			new Customer(String.valueOf(i)).buyGoods(store);
		}
	}
}