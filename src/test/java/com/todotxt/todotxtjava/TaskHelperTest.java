package com.todotxt.todotxtjava;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TaskHelperTest {

	@Test
	public void testCreateTask() {
		long id = 1;
		String line = "Test +project @context";
		Task task = TaskHelper.createTask(id, line);
		assertEquals(1, task.id);
		assertEquals(TaskHelper.NONE, task.prio);
		assertEquals(line, task.text);
		assertEquals(1, task.projects.size());
		assertEquals("project", task.projects.get(0));
		assertEquals(1, task.contexts.size());
		assertEquals("context", task.contexts.get(0));
	}

	@Test
	public void testGetContextsString() {
		List<String> contexts = TaskHelper.getContexts("@context");
		assertEquals(1, contexts.size());
		assertEquals("context", contexts.get(0));
	}

	@Test
	public void testGetProjectsString() {
		List<String> projects = TaskHelper.getProjects("+project");
		assertEquals(1, projects.size());
		assertEquals("project", projects.get(0));
	}

	@Test
	public void testGetDueDate() {
		String dueDate = TaskHelper.getDueDate("(A) T1 DUE:2010-02-03");
		assertEquals("2010-02-03", dueDate);
		dueDate = TaskHelper.getDueDate("(A) T1 DUE:2010-02-03 Appended text.");
		assertEquals("2010-02-03", dueDate);
	}

	@Test
	public void testToStringChar() {
		char prio = 'A';
		String prioStr = TaskHelper.toString(prio);
		assertEquals("A", prioStr);
	}
	
	@Test
	public void testGetByIdLong() {
		String text = "T2";
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1"));
		tasks.add(TaskHelper.createTask(1, "(A) "+text));
		tasks.add(TaskHelper.createTask(2, "T3"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		Task byId = TaskHelper.getById(tasks, 1);
		assertEquals(1, byId.id);
		assertEquals(text, byId.text);
	}

	@Test
	public void testGetByPrioListOfTaskChar() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1"));
		tasks.add(TaskHelper.createTask(1, "(A) T2"));
		tasks.add(TaskHelper.createTask(2, "T3"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		List<Task> byPrio = TaskHelper.getByPrio(tasks, 'A');
		assertEquals(1, byPrio.size());
		assertEquals("T2", byPrio.get(0).text);
	}

	@Test
	public void testGetByPrioListOfTaskListOfString() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1"));
		tasks.add(TaskHelper.createTask(1, "(A) T2"));
		tasks.add(TaskHelper.createTask(2, "T3"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		List<String> prios = Arrays.asList(new String[]{"A", "B"});
		List<Task> byPrio = TaskHelper.getByPrio(tasks, prios);
		assertEquals(2, byPrio.size());
	}

	@Test
	public void testGetByContextListOfTaskString() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1"));
		tasks.add(TaskHelper.createTask(1, "(A) T2"));
		tasks.add(TaskHelper.createTask(2, "T3 @context"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		List<Task> byContext = TaskHelper.getByContext(tasks, "context");
		assertEquals(1, byContext.size());
		assertEquals(2, byContext.get(0).id);
	}

	@Test
	public void testGetByContextListOfTaskListOfString() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1"));
		tasks.add(TaskHelper.createTask(1, "(A) T2 @context2"));
		tasks.add(TaskHelper.createTask(2, "T3 @context"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		List<String> contexts = Arrays.asList(new String[]{"context1", "context2"});
		List<Task> byContext = TaskHelper.getByContext(tasks, contexts);
		assertEquals(1, byContext.size());
		assertEquals(1, byContext.get(0).id);
	}

	@Test
	public void testGetByProject() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1 +proj1"));
		tasks.add(TaskHelper.createTask(1, "(A) T2 +proj1 @context2"));
		tasks.add(TaskHelper.createTask(2, "T3 @context +proj2"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		List<String> projects = Arrays.asList(new String[]{"proj1", "proj2"});
		List<Task> byProject = TaskHelper.getByProject(tasks, projects);
		assertEquals(3, byProject.size());
	}

	@Test
	public void testGetByText() {
		String text = "T2 +proj1 @context2";
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1 +proj1"));
		tasks.add(TaskHelper.createTask(1, "(A) "+text));
		tasks.add(TaskHelper.createTask(2, "T3 @context +proj2"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		List<Task> byProject = TaskHelper.getByText(tasks, text);
		assertEquals(1, byProject.size());
		assertEquals(text, byProject.get(0).text);
	}

	@Test
	public void testGetByTextIgnoreCase() {
		String text = "T2 +proj1 @context2";
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1 +proj1"));
		tasks.add(TaskHelper.createTask(1, "(A) "+text));
		tasks.add(TaskHelper.createTask(2, "T3 @context +proj2"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		List<Task> byProject = TaskHelper.getByTextIgnoreCase(tasks, text.toLowerCase());
		assertEquals(1, byProject.size());
		assertEquals(text, byProject.get(0).text);
	}

	@Test
	public void testGetPrios() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1 +proj1"));
		tasks.add(TaskHelper.createTask(1, "(A) T2 prios test case."));
		tasks.add(TaskHelper.createTask(2, "T3 @context +proj2"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		List<String> prios = TaskHelper.getPrios(tasks);
		assertEquals(4, prios.size());
		assertEquals(true, prios.contains("A"));
		assertEquals(true, prios.contains("B"));
		assertEquals(true, prios.contains("C"));
		assertEquals(false, prios.contains("D"));
		assertEquals(false, prios.contains(TaskHelper.NONE));
	}

	@Test
	public void testGetContextsListOfTask() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1 +proj1"));
		tasks.add(TaskHelper.createTask(1, "(A) T2 +proj1 @context2"));
		tasks.add(TaskHelper.createTask(2, "T3 @context +proj2"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		List<String> contexts = TaskHelper.getContexts(tasks);
		assertEquals(2, contexts.size());
		assertEquals(false, contexts.contains("contex"));
		assertEquals(true, contexts.contains("context"));
		assertEquals(true, contexts.contains("context2"));
	}

	@Test
	public void testGetProjectsListOfTask() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1 +proj1"));
		tasks.add(TaskHelper.createTask(1, "(A) T2 +proj1 @context2"));
		tasks.add(TaskHelper.createTask(2, "T3 @context +proj2"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		List<String> projects = TaskHelper.getProjects(tasks);
		assertEquals(2, projects.size());
		assertEquals(false, projects.contains("proj"));
		assertEquals(true, projects.contains("proj1"));
		assertEquals(true, projects.contains("proj2"));
	}

	@Test
	public void testToFileFormat() {
		String input = "(A) Test @context +project";
		Task task = TaskHelper.createTask(0, input);
		String fileFormat = TaskHelper.toFileFormat(task);
		assertEquals(input, fileFormat);
	}

	@Test
	public void testUpdateById() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1 +proj1"));
		tasks.add(TaskHelper.createTask(1, "(A) T2 +proj1 @context2"));
		tasks.add(TaskHelper.createTask(2, "T3 @context +proj2"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		Task task = TaskHelper.createTask(2, "updated");
		Task backup = TaskHelper.updateById(tasks, task);
		assertEquals(2, backup.id);
		assertEquals("T3 @context +proj2", backup.text);
		Task byId = TaskHelper.getById(tasks, 2);
		assertEquals("updated", byId.text);
	}

	@Test
	public void testCopyTask() {
		Task task = TaskHelper.createTask(1, "To copy");
		Task copy = TaskHelper.copy(task);
		assertEquals(task.id, copy.id);
		assertEquals(task.text, copy.text);
		assertEquals(task.prio, copy.prio);
	}

	@Test
	public void testCopyTaskTask() {
		Task src = TaskHelper.createTask(1, "To copy");
		Task dest = TaskHelper.createTask(1, "To copy to");
		TaskHelper.copy(src, dest);
		assertEquals(src.id, dest.id);
		assertEquals(src.text, dest.text);
		assertEquals(src.prio, dest.prio);
	}

	@Test
	public void testFind() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1 +proj1"));
		tasks.add(TaskHelper.createTask(1, "(A) T2 +proj1 @context2"));
		tasks.add(TaskHelper.createTask(2, "T3 @context +proj2"));
		Task src = TaskHelper.createTask(3, "(C) T4");
		tasks.add(src);
		Task task = TaskHelper.createTask(999, "(C) T4");
		Task found = TaskHelper.find(tasks, task);
		assertEquals(src.id, found.id);
		assertEquals(src.text, found.text);
		assertEquals(src.prio, found.prio);
	}

	@Test
	public void testIsDeleted() {
		//
		Task task = TaskHelper.createTask(3, "(C) T4");
		boolean deleted = TaskHelper.isDeleted(task);
		assertEquals(false, deleted);
		//
		task = TaskHelper.createTask(4, "");
		deleted = TaskHelper.isDeleted(task);
		assertEquals(true, deleted);
	}

	@Test
	public void testIsCompleted() {
		//
		Task task = TaskHelper.createTask(1, "(C) T4");
		boolean completed = TaskHelper.isCompleted(task);
		assertEquals(false, completed);
		//
		task = TaskHelper.createTask(2, "x 2010-09-11 T4");
		completed = TaskHelper.isCompleted(task);
		assertEquals(true, completed);
		//
		task = TaskHelper.createTask(3, "x T4");
		completed = TaskHelper.isCompleted(task);
		assertEquals(true, completed);
		//
		task = TaskHelper.createTask(4, " x T4"); //leading space is trimmed
		completed = TaskHelper.isCompleted(task);
		assertEquals(true, completed);
		//
		task = TaskHelper.createTask(5, "xx T4");
		completed = TaskHelper.isCompleted(task);
		assertEquals(false, completed);
		//
		task = TaskHelper.createTask(5, "x  T4");
		completed = TaskHelper.isCompleted(task);
		assertEquals(true, completed);
	}

	@Test
	public void testByIdComparator() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1 +proj1"));
		tasks.add(TaskHelper.createTask(1, "(A) T2 +proj1 @context2"));
		tasks.add(TaskHelper.createTask(2, "T3 @context +proj2"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		Collections.sort(tasks, TaskHelper.byId);
		//first
		assertEquals(0, tasks.get(0).id);
		//last
		assertEquals(3, tasks.get(tasks.size()-1).id);
	}

	@Test
	public void testByPrioComparator() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1 +proj1"));
		tasks.add(TaskHelper.createTask(1, "(A) T2 +proj1 @context2"));
		tasks.add(TaskHelper.createTask(2, "T3 @context +proj2"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		Collections.sort(tasks, TaskHelper.byPrio);
		//first
		assertEquals(1, tasks.get(0).id);
		//last
		assertEquals(2, tasks.get(tasks.size()-1).id);
	}

	@Test
	public void testByTextComparator() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) T1 +proj1"));
		tasks.add(TaskHelper.createTask(1, "(A) T2 +proj1 @context2"));
		tasks.add(TaskHelper.createTask(2, "T3 @context +proj2"));
		tasks.add(TaskHelper.createTask(3, "(C) T4"));
		Collections.sort(tasks, TaskHelper.byText);
		//first
		assertEquals(0, tasks.get(0).id);
		//last
		assertEquals(3, tasks.get(3).id);
	}

	@Test
	public void testByDueDateComparator() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(TaskHelper.createTask(0, "(B) TT1 +proj1"));
		tasks.add(TaskHelper.createTask(1, "(A) T2 +proj1 DUE:2010-01-02 @context2"));
		tasks.add(TaskHelper.createTask(2, "T3 @context +proj2"));
		tasks.add(TaskHelper.createTask(3, "(C) T4 DUE:2011-01-01"));
		tasks.add(TaskHelper.createTask(4, "(C) T4  DUE:2010-01-01"));
		tasks.add(TaskHelper.createTask(5, "T5"));
		Collections.sort(tasks, TaskHelper.byDueDate);
		//first
		assertEquals(4, tasks.get(0).id);
		assertEquals(4, tasks.get(0).id);
		//second
		assertEquals(1, tasks.get(1).id);
		//last
		assertEquals(0, tasks.get(5).id);
	}

}
