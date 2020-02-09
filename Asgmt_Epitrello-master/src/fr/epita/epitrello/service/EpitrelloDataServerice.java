package fr.epita.epitrello.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import fr.epita.epitrello.dao.Task;
import fr.epita.epitrello.database.DBActivity;

public class EpitrelloDataServerice implements DataService {

	List<Task> taskItemList; // List of All Tasks
	List<String> userList; // List of all users
	List<String> trelloLists; // Holds Mapping of List to Task Name

	public EpitrelloDataServerice() {
		taskItemList = new ArrayList<>();
		userList = new ArrayList<>();
		trelloLists = new ArrayList<>();
	}

	public String addUser(String username) {

		if (userList.contains(username))
			return DataService.USER_EXISTS;

		userList.add(username);
		
		DBActivity DB = new DBActivity();
		DB.DBInsert(username);

		return DataService.SUCC;
	}

	public String addList(String listName) {

		if (trelloLists.contains(listName))
			return DataService.LIST_EXISTS;

		trelloLists.add(listName);

		return DataService.SUCC;
	}

	public String addTask(String listname, String taskname, Integer estTime, Integer priority, String description) {
		Task newTask = new Task(listname,taskname, estTime, priority, description);

		if (taskItemList.contains(newTask))
			return DataService.TASK_EXISTS;
		else
			taskItemList.add(newTask);

		return DataService.SUCC;
	}

	public String editTask(String taskname, int estimatedTime, int priority, String description) {

		boolean hasBeenUpdated = false;
		Task updateForTask = new Task("tempList", taskname, estimatedTime, priority, description);
		
		Task taskToUpdate = getTaskFromList(updateForTask);
		if (taskToUpdate != null) {
			taskToUpdate.update(updateForTask);
			hasBeenUpdated = true;
		}

		return (hasBeenUpdated) ? DataService.SUCC : DataService.TASK_DOESNT_EXIST;
	}

	public String assignTask(String taskName, String assignee) {
		
		boolean isUserValid = isUserValid(assignee);
		Task taskToUpdate = getTaskFromList(taskName);
		
		String errMsg = null;
		
		if(isUserValid && taskToUpdate != null) {
			taskToUpdate.setAssignee(assignee);
		} else {
			errMsg = (isUserValid) ? "User is not valid" : DataService.TASK_DOESNT_EXIST;
		}
		
		return (errMsg != null) ? errMsg : DataService.SUCC;
	}


	public String printTask(String taskName) {
		Task taskToPrint = getTaskFromList(taskName);
		return (taskToPrint != null)? taskToPrint.toString() : DataService.TASK_DOESNT_EXIST;
	}

	public String completeTask(String taskName) {
		Task taskToComplete = getTaskFromList(taskName);
		taskToComplete.setCompleted(true);
		return (taskToComplete != null)? DataService.SUCC : DataService.TASK_DOESNT_EXIST;
	}

	public String printUsersByPerformance() {//Descending
		Map<String,Integer> userPerf = new TreeMap<String, Integer>(Collections.reverseOrder());
		
		for(Task item : taskItemList){
			String assignee = item.getAssignee();
			if(assignee != null && userPerf.containsKey(assignee)){
				int userCurrentPerf = userPerf.get(assignee);
				userPerf.put(assignee, userCurrentPerf + item.getEstTime());
			} 
		}

		return userPerf.toString();
	}

	public String printUsersByWorkload() {//Ascending
		Map<String,Integer> userPerf = new TreeMap<>();

		for(Task item : taskItemList){
			String assignee = item.getAssignee();
			if(assignee != null && userPerf.containsKey(assignee)){
				int userCurrentPerf = userPerf.get(assignee);
				userPerf.put(assignee, userCurrentPerf + item.getEstTime());
			} 
		}

		return userPerf.toString();
	}

	public String printUnassignedTasksByPriority() {
		Comparator<Task> pComp = Comparator.comparing(Task::getPriority);
		Collections.sort(taskItemList,pComp);
		return taskItemList.toString();
	}

	public String deleteTask(String taskName) {
		String resp;

		Task taskToDelete = getTaskFromList(taskName);
		if(taskToDelete!=null){
			taskItemList.remove(taskToDelete);
			resp = DataService.SUCC;
		} else {
			resp = DataService.TASK_DOESNT_EXIST;
		}
		
		return resp;
	}

	public String printAllUnfinishedTasksByPriority() {
		
		Comparator<Task> pComp = Comparator.comparing(Task::getPriority);
		List<Task> unFinishedTasks = taskItemList.stream().filter(arg -> !arg.isCompleted()).collect(Collectors.toList());
		Collections.sort(unFinishedTasks,pComp);
		return unFinishedTasks.toString();
	}

	public String moveTask(String task, String targetList) {
		
		boolean hasBeenMoved = false;
		
		Task taskToUpdate = getTaskFromList(task);
		
		if (taskToUpdate != null  && trelloLists.contains(targetList)) {
			taskToUpdate.setListname(targetList);
			hasBeenMoved = true;
		}

		return (hasBeenMoved) ? DataService.SUCC : DataService.TASK_DOESNT_EXIST;
	}

	public String printList(String listName) 
	{		
		Task listToPrint = getList(listName);
		return (listToPrint != null)? listToPrint.toString() : DataService.LIST_DOESNT_EXIST;
	}


	public String printAllLists() 
	{
		String list="List = ";
		
		for (String listItem : trelloLists) 
		{
			list =list+listItem +", ";
		}
		return (list != null)? list : DataService.LIST_DOESNT_EXIST;
	}

	public String printUserTasks(String string) 
	{
		String taskAssgn= "Task Assignes to "+string+" is ";

		for (Task taskItem : taskItemList) {
			if (taskItem.getAssignee()!=null && taskItem.getAssignee().equalsIgnoreCase(string)) 
			{
				taskAssgn = taskAssgn + taskItem.getTaskname();
			}
		}
		return taskAssgn;
	}

	// If return is null, the task does not exist
	private Task getTaskFromList(Task tempTask) {

		Task retTask = null;

		for (Task taskItem : taskItemList) {
			if (taskItem.equals(tempTask)) {
				retTask = taskItem;
			}
		}

		return retTask;
	}

	// If return is null, the task does not exist
	private Task getTaskFromList(String taskName) {

		Task retTask = null;

		for (Task taskItem : taskItemList) {
			if (taskItem.getTaskname().equalsIgnoreCase(taskName)) {
				retTask = taskItem;
			}
		}

		return retTask;
	}
	
	private Task getList(String listName) 
	{
		Task retTask = null;

		for (Task taskItem : taskItemList) {
			if (taskItem.getListname().equalsIgnoreCase(listName)) {
				retTask = taskItem;
			}
		}

		return retTask;
	}

	private boolean isUserValid(String assignee) {
		for (String user : userList) {
            if (user.equalsIgnoreCase(assignee))
            	return true;
        }
		return false;
	}

}
