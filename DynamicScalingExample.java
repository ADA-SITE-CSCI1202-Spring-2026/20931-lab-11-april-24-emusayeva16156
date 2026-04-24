public class DynamicScalingExample {

    static class MathTask implements Runnable {
        private int start;
        private int end;

        public MathTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            long sum = 0;
            for (int i = start; i < end; i++) {
                for (int j = 0; j < 100; j++) {
                    sum += (long) i * i * i + i * j;
                }
            }
            // System.out.println("Partial sum: " + sum);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Available cores: " + cores);

        int totalWork = 10_000_000;

        // 🔹 1 Thread
        long startTime1 = System.currentTimeMillis();

        Thread singleThread = new Thread(new MathTask(0, totalWork));
        singleThread.start();
        singleThread.join();

        long endTime1 = System.currentTimeMillis();
        System.out.println("Time with 1 thread: " + (endTime1 - startTime1) + " ms");

        // 🔹 Multiple Threads
        long startTime2 = System.currentTimeMillis();

        Thread[] threads = new Thread[cores];
        int chunk = totalWork / cores;

        for (int i = 0; i < cores; i++) {
            int start = i * chunk;
            int end = (i == cores - 1) ? totalWork : start + chunk;

            threads[i] = new Thread(new MathTask(start, end));
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        long endTime2 = System.currentTimeMillis();
        System.out.println("Time with " + cores + " threads: " + (endTime2 - startTime2) + " ms");
    }
}
