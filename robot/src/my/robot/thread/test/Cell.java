package my.robot.thread.test;


/**<pre>
 * 线程死锁的测试类
 * Cell中的value有三个同步方法
 * getValue()---取值
 * setValue()---赋值
 * swapValue()---交换值
 * 		每个方法都是原子性的，因此加有同步锁(synchronized)
 * 测试方法main中创建两个线程t1、t2他们不断执行对Cell的两个实例c1,c2交换值的工作
 * t1内执行c1.swap(c2)
 * t2内执行c2.swap(c1)
 * 因此两个线程会有如下执行过程
 *               1.                2.
 * t1------->c1.swap(c2)------>获得c1的锁
 *             |         3.                4.
 * 			   |--->t=getValue()--->获得c1的锁(因为已经持有)
 * 			   |--->o=other.getValue()--->获得c2的锁 6.
 * 						5.					|     7.
 * 											|由于需要c2的锁而等待
 * 										____|_____
 * 									   |		  |
 * 							  如果c2此时也执行完    如果c2此时恰好执行完swap()释放掉锁			
 * 							（4.）步骤开始执行第(5)   或者还每执行swap()申请到锁则不会出现问题
 * 							步，
 * 							那么t1拥有c1的锁，等待c2的锁
 * 							   t2拥有c2的锁，等待c1的锁
 * 							此时就发生死锁
 * </pre>
 * @author journey
 *
 */
public class Cell {

	private long value;

	public Cell(long value) {
		this.value = value;
	}

	public synchronized long getValue() {
		return value;
	}

	public synchronized void setValue(long value) {
		this.value = value;
	}

	public synchronized void swap(Cell other) {
		long t = getValue();
		long o = other.getValue();
		setValue(o);
		other.setValue(t);

	}

	public static void main(String[] args) throws InterruptedException {
		final Cell c1 = new Cell(100);
		final Cell c2 = new Cell(200);
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				long count = 0;
				try {
					while (true) {
						count++;
						c1.swap(c2);
						//if (count % 100 == 0)
							System.out.println("t1 progress: " + count);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		t1.setName("thead1");
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				long count = 0;
				try {
					while (true) {
						count++;
						c2.swap(c1);
						//if (count % 100 == 0)
							System.out.println("t2 progress: " + count);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t2.setName("thread2");
		t1.start();
		t2.start();
		t1.join();
		t2.join();
	}

}
