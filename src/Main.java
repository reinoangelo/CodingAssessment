import java.util.*;

// Class containing all necessary variables
class Task implements Comparable<Task> {

    String name;
    int duration; // in days
    List<String> dependencies = new ArrayList<>();
    Date startDate;
    Date endDate;

    // Constructor with dependencies
    public Task(String name, int duration, List<String> dependencies) {
        this.name = name;
        this.duration = duration;
        this.dependencies = dependencies;
    }

    // Constructor without dependencies
    public Task(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    // Override the compareTo method to define the sorting order based on the number of dependencies
    @Override
    public int compareTo(Task other) {
        return Integer.compare(this.dependencies.size(), other.dependencies.size());
    }
}

class ProjectScheduler {

    public void checkSchedule(Calendar calendar, List<Task> taskList) {

        // Sort tasks by number of dependencies (earlier tasks with fewer dependencies first)
        Collections.sort(taskList);

        // Initialize the start date for the first task
        Date currentDate = calendar.getTime();

        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);

            // Calculate the start date based on task dependencies
            Date latestEndDate = getLatestDependencyEndDate(taskList, task);

            // If there are no dependencies, the task starts at the current date
            if (latestEndDate != null)
                task.startDate = latestEndDate;
             else
                task.startDate = currentDate;

            // Calculate the end date based on the task's start date and duration
            calendar.setTime(task.startDate);

            // Add the task's duration to the start date
            calendar.add(Calendar.DAY_OF_MONTH, task.duration);
            task.endDate = calendar.getTime();

            // Display task details
            displayTask(task);
        }
    }

    // Method to get the latest end date of dependencies
    private Date getLatestDependencyEndDate(List<Task> taskList, Task task) {
        Date latestEndDate = null;

        // Loop that finds a dependency by name
        for (String dependencyName : task.dependencies) {
            Task dependency = findTaskByName(taskList, dependencyName);

            // Checks the dependency's end date then set it to latest end date
            if (dependency != null && dependency.endDate != null) {
                if (latestEndDate == null || dependency.endDate.after(latestEndDate)) {
                    latestEndDate = dependency.endDate;
                }
            }
        }
        return latestEndDate;
    }

    // Method to find a task by its name
    private Task findTaskByName(List<Task> taskList, String taskName) {
        for (Task task : taskList) {
            if (task.name.equals(taskName)) {
                return task;
            }
        }
        return null;
    }

    // Method to display task information
    private void displayTask(Task task) {
        System.out.println("Task ID: " + task.name);
        System.out.println("Start Date: " + task.startDate );
        System.out.println("End Date: " + task.endDate);
        System.out.println();
    }
}

public class Main {

    public static void main(String[] args) {

        // Create instances
        ProjectScheduler scheduler = new ProjectScheduler();
        List<Task> taskList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        // Create a bunch of tasks
        taskList.add(new Task("Task1", 3)); // Task1 has no dependencies
        taskList.add(new Task("Task2", 5, List.of("Task1"))); // Task2 depends on Task1
        taskList.add(new Task("Task3", 6, List.of("Task1"))); // Task3 depends on Task1
        taskList.add(new Task("Task4", 10, List.of("Task2", "Task3"))); // Task4 depends on Task2 and Task3

        // Set initial start date to January 1, 2024
        calendar.set(2024, Calendar.JANUARY, 1);

        // Perform the scheduler
        scheduler.checkSchedule(calendar, taskList);
    }
}