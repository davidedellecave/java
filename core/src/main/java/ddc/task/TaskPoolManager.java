package ddc.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import ddc.task.impl.ArgsValue;

public class TaskPoolManager {
	private final static Logger logger = Logger.getLogger(TaskPoolManager.class);

	public void run(TaskPoolFactory factory, ArgsValue args) throws InterruptedException {
		logger.info("\n------------------------- JobManager - Task List -------------------------");
		TaskPool pool = factory.create(args);		
		while (pool != null && pool.size() > 0) {
			run(pool);
			pool = factory.onNext(pool);
		}
	}

	private void run(TaskPool pool) throws InterruptedException {
		if (pool.getPoolType()==TaskPool.ExecutionType.Concurrent) {
			runConcurrent(pool);
		} else {
			runSequence(pool);
		}
	}

	private void runSequence(TaskPool pool) throws InterruptedException {
		for (TaskExecutor je : pool) {
			je.run();
		}
	}
	
	private void runConcurrent(TaskPool pool) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(pool.getMaxThreads());
		for (TaskExecutor ex : pool) {
			executor.execute(ex);
		}
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
	}
}
