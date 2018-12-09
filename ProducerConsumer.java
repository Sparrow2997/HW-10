import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;
//buffer class containing queue as buffer.
class Buffer{
	Queue<Integer> q;
	int size;

	Buffer(int size){
		q = new LinkedList<>();
		this.size = size;

	}

	//method which producer will call
	synchronized void write(int item) {

		//if buffer is full, wait for consumer to consume.
		while(q.size()==size) {
			try {
				wait();
			}
			catch(InterruptedException e) {
				System.out.println("Interrupted..");
			}
		}
		q.add(item++);
		System.out.println("Producer produced item : " + (item-1));
		System.out.println("Buffer : "+q);

		//if buffer was empty before, then notify consumer that it has entry now.
		if(q.size() == 1)
			notifyAll();

	}

	//method which consumer will call
	synchronized void read()
	{
		//if buffer is empty, wait for producer to produce
		while(q.size()==0)
		{
			try {
				wait();
			}
			catch (InterruptedException e)
			{
				System.out.println("Interrupted..");
			}
		}
		System.out.println("Consumer consumed item " + q.remove());
		System.out.println("Buffer : "+q);

		//if buffer was full before, then notify producer that it can produce now.
		if(q.size()==size-1){
			notifyAll();
		}


	}

}


class Producer implements Runnable{
	Thread t;
	Buffer bf;
	Producer(Buffer b){
		t = new Thread(this);
		bf = b;
		t.start();
	}
	public void run() {
		int item = 0;

		//keep producing
		while(true) {
			bf.write(item++);
			try {
				Thread.sleep(1000);
			}
			catch(Exception e) {
			}
		}
	}
}

class Consumer implements Runnable{
	Thread t;
	Buffer bf;
	Consumer(Buffer b){
		t = new Thread(this);
		bf = b;
		t.start();
	}
	public void run() {
		//keep consuming
		while(true) {
			bf.read();
			try {
				Thread.sleep(2000);
			}
			catch(Exception e) {

			}
		}
	}
}


public class ProdCon{
	public static void main(String [] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter buffer size : ");
		int bufferSize = sc.nextInt();
		Buffer bf = new Buffer(bufferSize);
		sc.close();

		//pass the same buffer reference to producer and consumer.
		new Producer(bf);
		new Consumer(bf);

		System.out.println("Enter Ctrl-C or click on red square in eclipse to stop.");
	}

}
