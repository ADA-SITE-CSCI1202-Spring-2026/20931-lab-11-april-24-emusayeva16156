class SharedResource {
    private int value;
    private boolean bChanged = false;

    public synchronized void set(int value) {
        try {
            while (bChanged) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.value = value;
        bChanged = true;
        notify();
    }

    public synchronized int get() {
        try {
            while (!bChanged) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bChanged = false;
        notify();
        return value;
    }
}

class Producer implements Runnable {
    private SharedResource resource;

    public Producer(SharedResource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            resource.set(i);
            System.out.println("Produced: " + i);
        }
    }
}

class Consumer implements Runnable {
    private SharedResource resource;

    public Consumer(SharedResource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            int val = resource.get();
            System.out.println("Consumed: " + val);
        }
    }
}

public class WaitNotifyExample {
    public static void main(String[] args) {

        SharedResource resource = new SharedResource();

        Thread producer = new Thread(new Producer(resource));
        Thread consumer = new Thread(new Consumer(resource));

        producer.start();
        consumer.start();
    }
}
