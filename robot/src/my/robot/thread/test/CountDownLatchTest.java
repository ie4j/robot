package my.robot.thread.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchTest {

	public static void main(String args[]) throws InterruptedException {
		class Job implements Runnable {
			private final CountDownLatch start;
			private final CountDownLatch end;

			Job(CountDownLatch start, CountDownLatch end) {
				this.start = start;
				this.end = end;
			}

			public void run() {
				try {
					start.await();
					Thread.sleep(4000);
					end.countDown();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		CountDownLatch start = new CountDownLatch(1);
		CountDownLatch end = new CountDownLatch(5);
		ExecutorService ex1 = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 5; i++) {
			ex1.execute((new Job(start, end)));
		}
		//do something ……此处线程还未开始工作
		start.countDown();//执行后线程开始工作
		//do something …… 此处代码和线程工作一起执行，互补影响
		System.out.println("wait……");
		try {
			end.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			ex1.shutdown();
		}

	}
}
