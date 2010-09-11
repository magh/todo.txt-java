package com.todotxt.todotxtjava;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskHelper {
	
	private final static String TAG = TaskHelper.class.getSimpleName();
	
	public final static char NONE = '-';

	public final static String COMPLETED = "x ";

	public final static SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd ");

	private final static Pattern prioPattern = Pattern.compile("\\(([A-Z])\\) (.*)");

	private final static Pattern contextPattern = Pattern.compile("@(\\w+)");

	private final static Pattern projectPattern = Pattern.compile("\\+(\\w+)");

	private final static Pattern duePattern = Pattern.compile("DUE:(\\d{4}-\\d{2}-\\d{2})");

	public static Task createTask(long id, String line){
		//prio and text
		Matcher m = prioPattern.matcher(line);
		char prio = NONE;
		String text;
		if(m.find()){
			prio = m.group(1).charAt(0);
			text = m.group(2);
		}else{
			text = line;
		}
		return new Task(id, prio, text.trim());
	}
	
	public static List<String> getContexts(String text){
		return getItems(contextPattern, text);
	}

	public static List<String> getProjects(String text){
		return getItems(projectPattern, text);
	}

	public static String getDueDate(String text){
		List<String> items = getItems(duePattern, text);
		return items.size() == 1 ? items.get(0) : null;
	}

	private static List<String> getItems(Pattern p, String text){
		Matcher m = p.matcher(text);
		List<String> items = new ArrayList<String>();
		while(m.find()){
			String item = m.group(1);
			items.add(item);
		}
		return items;
	}

	public static Comparator<Task> byId = new Comparator<Task>() {
		@Override
		public int compare(Task arg0, Task arg1) {
			if (arg0 != null && arg1 != null) {
				return Long.valueOf(arg0.id).compareTo(arg1.id);
			}
			return -1;
		}
	};

	public static Comparator<Task> byPrio = new Comparator<Task>() {
		@Override
		public int compare(Task arg0, Task arg1) {
			if (arg0 != null && arg1 != null) {
				if (arg0.prio == TaskHelper.NONE
						|| arg1.prio == TaskHelper.NONE) {
					//Put prio NONE last.
					return new Character(arg1.prio).compareTo(new Character(arg0.prio));
				}else{
					return new Character(arg0.prio).compareTo(new Character(arg1.prio));
				}
			}
			return -1;
		}
	};

	public static Comparator<Task> byText = new Comparator<Task>() {
		@Override
		public int compare(Task arg0, Task arg1) {
			try{
				return arg0.text.compareTo(arg1.text);
			}catch(Exception e){
				Log.e(TAG, e.getMessage(), e);
			}
			return -1;
		}
	};

	public static Comparator<Task> byDueDate = new Comparator<Task>() {
		@Override
		public int compare(Task arg0, Task arg1) {
			try{
				if(arg0.dueDate == null && arg1.dueDate == null){
					return byText.compare(arg0, arg1);
				}
				if(arg0.dueDate == null){
					return 1;
				}
				if(arg1.dueDate == null){
					return -1;
				}
				return arg0.dueDate.compareTo(arg1.dueDate);
			}catch(Exception e){
				Log.e(TAG, e.getMessage(), e);
			}
			return -1;
		}
	};

	public static String toString(char prio) {
		return prio >= 'A' && prio <= 'Z' ? "" + prio : "" + NONE;
	}

	public static Task getById(List<Task> tasks, long id) {
		for (Task item : tasks) {
			if (item.id == id) {
				return item;
			}
		}
		return null;
	}

	public static List<Task> getByPrio(List<Task> items, char prio) {
		List<Task> res = new ArrayList<Task>();
		for (Task item : items) {
			if (item.prio == prio) {
				res.add(item);
			}
		}
		return res;
	}

	public static List<Task> getByPrio(List<Task> items, List<String> prios) {
		List<Task> res = new ArrayList<Task>();
		for (Task item : items) {
			if (prios.contains(""+item.prio)) {
				res.add(item);
			}
		}
		return res;
	}

	public static List<Task> getByContext(List<Task> items, String context) {
		List<Task> res = new ArrayList<Task>();
		for (Task item : items) {
			if (item.contexts.contains(context)) {
				res.add(item);
			}
		}
		return res;
	}

	public static List<Task> getByContext(List<Task> items, List<String> contexts) {
		List<Task> res = new ArrayList<Task>();
		for (Task item : items) {
			for (String cxt : item.contexts) {
				if (contexts.contains(cxt)) {
					res.add(item);
					break;
				}
			}
		}
		return res;
	}

	public static List<Task> getByProject(List<Task> items, List<String> projects) {
		List<Task> res = new ArrayList<Task>();
		for (Task item : items) {
			for (String cxt : item.projects) {
				if (projects.contains(cxt)) {
					res.add(item);
					break;
				}
			}
		}
		return res;
	}

	public static List<Task> getByText(List<Task> items, String text) {
		List<Task> res = new ArrayList<Task>();
		for (Task item : items) {
			if(item.text.contains(text)){
				res.add(item);
			}
		}
		return res;
	}

	public static List<Task> getByTextIgnoreCase(List<Task> items, String text) {
		text = text.toUpperCase();
		List<Task> res = new ArrayList<Task>();
		for (Task item : items) {
			if(item.text.toUpperCase().contains(text)){
				res.add(item);
			}
		}
		return res;
	}

	public static ArrayList<String> getPrios(List<Task> items){
		Set<String> res = new HashSet<String>();
		for (Task item : items) {
			res.add(toString(item.prio));
		}
		ArrayList<String> ret = new ArrayList<String>(res);
		Collections.sort(ret);
		return ret;
	}

	public static ArrayList<String> getContexts(List<Task> items){
		Set<String> res = new HashSet<String>();
		for (Task item : items) {
			res.addAll(item.contexts);
		}
		ArrayList<String> ret = new ArrayList<String>(res);
		Collections.sort(ret);
		return ret;
	}

	public static ArrayList<String> getProjects(List<Task> items){
		Set<String> res = new HashSet<String>();
		for (Task item : items) {
			res.addAll(item.projects);
		}
		ArrayList<String> ret = new ArrayList<String>(res);
		Collections.sort(ret);
		return ret;
	}

	public static String toFileFormat(Task task) {
		StringBuilder sb = new StringBuilder();
		if(!TaskHelper.isDeleted(task)) {
			if(!TaskHelper.isCompleted(task)){
				if (task.prio >= 'A' && task.prio <= 'Z') {
					sb.append("(");
					sb.append(task.prio);
					sb.append(") ");
				}
			}
			sb.append(task.text);
		}
		return sb.toString();
	}

	/**
	 * @param tasks
	 * @param task
	 * @return old task or null
	 */
	public static Task updateById(List<Task> tasks, Task task){
		for (Task task2 : tasks) {
			if(task.id == task2.id){
				Task backup = copy(task2);
				copy(task, task2);
				return backup;
			}
		}
		return null;
	}

	public static Task copy(Task src){
		return new Task(src.id, src.prio, src.text);
	}
	
	public static void copy(Task src, Task dest){
		dest.id = src.id;
		dest.contexts = src.contexts;
		dest.prio = src.prio;
		dest.projects = src.projects;
		dest.text = src.text;
	}

	/**
	 * Find by text and prio
	 * @param tasks
	 * @param task
	 * @return copy of found task
	 */
	public static Task find(List<Task> tasks, Task task){
		for (Task task2 : tasks) {
			if(task2.text.equals(task.text) && task2.prio == task.prio){
				return copy(task2);
			}
		}
		return null;
	}

	public static boolean isDeleted(Task task){
		return Util.isEmpty(task.text);
	}

	public static boolean isCompleted(Task task){
		return task.text.startsWith(COMPLETED);
	}

}
