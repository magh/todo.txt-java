package com.todotxt.todotxtjava;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.dropbox.client.DropboxClient;
import com.dropbox.client.DropboxClientHelper;

public class DropboxUtil {

	private final static String TAG = DropboxUtil.class.getSimpleName();
	
	public static class DropboxProvider {
		private String consumerKey;
		private String consumerSecret;
		private String username;
		private String password;
		private DropboxClient client;
		public DropboxProvider(String consumerKey,
				String consumerSecret, String username, String password){
			this.consumerSecret = consumerSecret;
			this.consumerKey = consumerKey;
			this.username = username;
			this.password = password;
		}
		public DropboxClient get() throws Exception{
			synchronized (DropboxClient.class) {
				if(client == null){
					client = DropboxClientHelper.newClient(consumerKey,
							consumerSecret, username, password);
				}
			}
			return client;
		}
	}
	
	public static boolean addTask(DropboxClient client, String input, File tmp, File output) {
		if (client != null) {
			ArrayList<Task> tasks = null;
			try {
				tasks = fetchTasks(client);
				Task task = TaskHelper.createTask(tasks.size(), input);
				tasks.add(task);
				TodoUtil.writeToFile(tasks, tmp);
				DropboxClientHelper.putFile(client, "/", tmp);
				TodoUtil.writeToFile(tasks, output);
				return true;
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
		return false;
	}

	public static List<Task> addTask(DropboxClient client, String input) {
		if (client != null) {
			ArrayList<Task> tasks = null;
			try {
				tasks = fetchTasks(client);
				Task task = TaskHelper.createTask(tasks.size(), input);
				tasks.add(task);
				AtomicLong length = new AtomicLong();
				InputStream is = TodoUtil.toInputStream(tasks, length);
				DropboxClientHelper.putFile(client, "/", "todo.txt", is, length.get());
				return tasks;
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
		return null;
	}

	public static boolean updateTask(DropboxClient client, 
			char prio, String input, Task backup, File tmp, File output) {
		if (client != null) {
			Task t = TaskHelper.createTask(backup.id, input);
			t.prio = prio;
			try {
				ArrayList<Task> tasks = fetchTasks(client);
				Task found = TaskHelper.find(tasks, backup);
				if(found != null){
					t.id = found.id;
					TaskHelper.updateById(tasks, t);
					TodoUtil.writeToFile(tasks, tmp);
					DropboxClientHelper.putFile(client, "/", tmp);
					TodoUtil.writeToFile(tasks, output);
					return true;
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
		return false;
	}

	public static List<Task> updateTask(DropboxClient client, 
			char prio, String input, Task backup) {
		if (client != null) {
			Task t = TaskHelper.createTask(backup.id, input);
			t.prio = prio;
			try {
				ArrayList<Task> tasks = fetchTasks(client);
				Task found = TaskHelper.find(tasks, backup);
				if(found != null){
					t.id = found.id;
					TaskHelper.updateById(tasks, t);
					AtomicLong length = new AtomicLong();
					InputStream is = TodoUtil.toInputStream(tasks, length);
					DropboxClientHelper.putFile(client, "/", "todo.txt", is, length.get());
					return tasks;
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
		return null;
	}

	public static ArrayList<Task> fetchTasks(DropboxClient client) throws Exception {
		InputStream is = DropboxClientHelper.getFileStream(client, Constants.REMOTE_FILE);
		return TodoUtil.loadTasksFromStream(is);
	}

}
