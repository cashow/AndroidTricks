package com.cashow.qrcodescan.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyExecutor {
	private static Executor exec = null;

	public static Executor getExecutor() {
		if (exec == null) {
			synchronized (MyExecutor.class) {
				if (exec == null) {
					exec = new ThreadPoolExecutor(15, 200, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
				}
			}
		}
		return exec;
	}
}
