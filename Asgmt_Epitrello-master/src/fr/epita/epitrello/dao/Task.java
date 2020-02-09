package fr.epita.epitrello.dao;

public class Task {

	String listname, taskname, assignee, description;
	int id;
	Integer estTime, priority;
	boolean completed;
	
	public static int instanceCount = 0;

	public Task( String listname, String taskname, Integer estTime, Integer priority, String description) {
		super();
		this.listname = listname;
		this.taskname = taskname;
		this.description = description;
		this.estTime = estTime;
		this.priority = priority;
		this.id = instanceCount++;
		this.completed = false;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getListname() {
		return listname;
	}

	public void setListname(String listname) {
		this.listname = listname;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getEstTime() {
		return estTime;
	}

	public void setEstTime(Integer estTime) {
		this.estTime = estTime;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public boolean equals(Task b) {
		return this.getTaskname().equalsIgnoreCase(b.getTaskname());
	}
	
	public int getIndexOf(Task b) {
		return this.getId();
	}
	
	public void update(Task tempTask) {
		this.setAssignee(tempTask.getAssignee());
		this.setDescription(tempTask.getDescription());
		this.setEstTime(tempTask.getEstTime());
		this.setPriority(tempTask.getPriority());
		this.setTaskname(tempTask.getTaskname());
		this.setCompleted(tempTask.isCompleted());
	}
	
	

	@Override
	public String toString() {
		return "Task [listname=" + listname + ", taskname=" + taskname + ", assignee=" + assignee + ", description="
				+ description + ", estTime=" + estTime + ", priority=" + priority + ", completed=" + completed + "]";
	}

	public static void main(String[] args) {
		Task t = new Task("l1","t1",1,2,"desc1");
		Task nt = new Task("","t2",2,22,"desc2");
		System.out.println(t.toString());
		t.update(nt);
		System.out.println(t.toString());
	}

}
