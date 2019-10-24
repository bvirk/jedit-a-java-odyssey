package home.txtconv;
import static home.txtconv.util.Util.*;

class SpinTest implements Runnable {
	public void run() {
		for (Spinner spnr : Spinner.values()) {
			int loops=20;
			printTimed(9,spnr);
			while (loops-- >0) {
				Spinner.next(spnr);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {}
			}
		}
		Spinner.clr();
	}
}