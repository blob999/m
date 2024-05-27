2.1 Write a program in asm to sort array elements using bubble sort.


#!/bin/bash

echo "Enter the elements of the array separated by spaces:"
read -a arr

echo "Array in original order:"
echo ${arr[*]}

# Performing Bubble sort
for ((i = 0; i < ${#arr[@]}; i++))
do
    for ((j = 0; j < ${#arr[@]}-i-1; j++))
    do
        if [ ${arr[j]} -gt ${arr[$((j+1))]} ]
        then
            # swap
            temp=${arr[j]}
            arr[$j]=${arr[$((j+1))]}
            arr[$((j+1))]=$temp
        fi
    done
done

echo "Array in sorted order:"
echo ${arr[*]}










2.2 Write a program to check whether a substring is present inside a string.


#!/bin/bash

str=""
substr=""

echo "Enter a string:"
read str

echo "Enter a substring:"
read substr

if [[ $str == *"$substr"* ]]; then
    echo "String contains substring."
else
    echo "String does not contain substring."
fi










2.3 Write a program to check whether string is palindrome or not.


#!/bin/bash

echo "Enter a String"
read input
reverse=""
len=${#input}

for (( i=$len-1; i>=0; i-- ))
do
    reverse="$reverse${input:$i:1}"
done

if [ "$input" == "$reverse" ]
then
    echo "$input is palindrome"
else
    echo "$input is not palindrome"
fi










3.1 Producer Consumer


import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ProducerConsumer {

    // Buffer size
    private static final int BUFFER_SIZE = 5;
    // Shared buffer using a queue
    private static Queue<Integer> buffer = new LinkedList<>();
    // Lock object for synchronization
    private static final Object lock = new Object();
    // Counter to track the number of items in the buffer
    private static int count = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input: Number of items to produce
        System.out.println("Enter the number of items to produce:");
        int itemCount = scanner.nextInt();

        // Creating and starting the producer thread
        Thread producerThread = new Thread(() -> {
            for (int i = 1; i <= itemCount; i++) {
                produce(i); // Produce item
            }
        });

        // Creating and starting the consumer thread
        Thread consumerThread = new Thread(() -> {
            for (int i = 1; i <= itemCount; i++) {
                consume(); // Consume item
            }
        });

        producerThread.start(); // Start the producer thread
        consumerThread.start(); // Start the consumer thread
    }

    // Method to produce items
    public static void produce(int item) {
        synchronized (lock) { // Synchronize on the lock object
            while (count == BUFFER_SIZE) { // Wait if the buffer is full
                try {
                    lock.wait(); // Wait for space to become available
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            buffer.add(item); // Add the item to the buffer
            count++; // Increment the count of items in the buffer
            System.out.println("Produced: " + item); // Print produced item

            lock.notifyAll(); // Notify all waiting threads
        }
    }

    // Method to consume items
    public static void consume() {
        synchronized (lock) { // Synchronize on the lock object
            while (count == 0) { // Wait if the buffer is empty
                try {
                    lock.wait(); // Wait for items to become available
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            int item = buffer.remove(); // Remove the item from the buffer
            count--; // Decrement the count of items in the buffer
            System.out.println("Consumed: " + item); // Print consumed item

            lock.notifyAll(); // Notify all waiting threads
        }
    }
}








3.2 Reader Writer


import java.util.Scanner;

public class ReaderWriter {

    private static int readerCount = 0;
    private static boolean isWriting = false;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of readers:");
        int readerCount = scanner.nextInt();

        System.out.println("Enter the number of writers:");
        int writerCount = scanner.nextInt();

        for (int i = 1; i <= readerCount; i++) {
            final int readerId = i;
            new Thread(() -> {
                try {
                    reader(readerId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        for (int i = 1; i <= writerCount; i++) {
            final int writerId = i;
            new Thread(() -> {
                try {
                    writer(writerId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }

    public static void reader(int readerId) throws InterruptedException {
        synchronized (lock) {
            while (isWriting) {
                lock.wait();
            }
            readerCount++;
        }

        System.out.println("Reader " + readerId + " is reading.");
        Thread.sleep(1000); // Simulate reading
        System.out.println("Reader " + readerId + " has finished reading.");

        synchronized (lock) {
            readerCount--;
            if (readerCount == 0) {
                lock.notifyAll();
            }
        }
    }

    public static void writer(int writerId) throws InterruptedException {
        synchronized (lock) {
            while (readerCount > 0 || isWriting) {
                lock.wait();
            }
            isWriting = true;
        }

        System.out.println("Writer " + writerId + " is writing.");
        Thread.sleep(1000); // Simulate writing
        System.out.println("Writer " + writerId + " has finished writing.");

        synchronized (lock) {
            isWriting = false;
            lock.notifyAll();
        }
    }
}






3.3 Dinning Philosopher


import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {

    // Lock array representing the forks
    private static Lock[] forks;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input: Number of philosophers
        System.out.println("Enter the number of philosophers:");
        int philosopherCount = scanner.nextInt();

        // Initialize forks (one more than the number of philosophers for simplicity)
        forks = new Lock[philosopherCount];
        for (int i = 0; i < philosopherCount; i++) {
            forks[i] = new ReentrantLock();
        }

        // Create and start philosopher threads
        for (int i = 0; i < philosopherCount; i++) {
            final int philosopherId = i;
            new Thread(() -> {
                try {
                    philosopher(philosopherId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }

    // Philosopher method
    public static void philosopher(int philosopherId) throws InterruptedException {
        while (true) {
            think(philosopherId); // Philosopher is thinking
            pickUpForks(philosopherId); // Philosopher picks up forks
            eat(philosopherId); // Philosopher is eating
            putDownForks(philosopherId); // Philosopher puts down forks
        }
    }

    // Simulate thinking
    private static void think(int philosopherId) throws InterruptedException {
        System.out.println("Philosopher " + philosopherId + " is thinking.");
        Thread.sleep((long) (Math.random() * 1000)); // Random thinking time
    }

    // Pick up forks
    private static void pickUpForks(int philosopherId) {
        int leftFork = philosopherId;
        int rightFork = (philosopherId + 1) % forks.length;

        // Ensure a consistent order of locking to prevent deadlock
        if (philosopherId % 2 == 0) {
            forks[leftFork].lock();
            forks[rightFork].lock();
        } else {
            forks[rightFork].lock();
            forks[leftFork].lock();
        }

        System.out.println("Philosopher " + philosopherId + " picked up forks.");
    }

    // Simulate eating
    private static void eat(int philosopherId) throws InterruptedException {
        System.out.println("Philosopher " + philosopherId + " is eating.");
        Thread.sleep((long) (Math.random() * 1000)); // Random eating time
    }

    // Put down forks
    private static void putDownForks(int philosopherId) {
        int leftFork = philosopherId;
        int rightFork = (philosopherId + 1) % forks.length;

        forks[leftFork].unlock();
        forks[rightFork].unlock();

        System.out.println("Philosopher " + philosopherId + " put down forks.");
    }
}










4.1 FCFS Process Scheduling 

(Java)

import java.util.Scanner;

public class FCFS_Scheduling {
    
    public static void findWaitingTime(int processes[], int n, int bt[], int wt[], int at[]) {
        int[] service_time = new int[n];
        service_time[0] = 0;
        wt[0] = 0;

        for (int i = 1; i < n; i++) {
            service_time[i] = service_time[i - 1] + bt[i - 1];
            wt[i] = service_time[i] - at[i];
            if (wt[i] < 0)
                wt[i] = 0;
        }
    }

    public static void findTurnAroundTime(int processes[], int n, int bt[], int wt[], int tat[]) {
        for (int i = 0; i < n; i++)
            tat[i] = bt[i] + wt[i];
    }

    public static void findAvgTime(int processes[], int n, int bt[], int at[]) {
        int[] wt = new int[n];
        int[] tat = new int[n];

        findWaitingTime(processes, n, bt, wt, at);
        findTurnAroundTime(processes, n, bt, wt, tat);

        System.out.println("\n Processes   Arrival time   Burst time   Waiting time   Turnaround time");
        int total_wt = 0, total_tat = 0;
        for (int i = 0; i < n; i++) {
            total_wt += wt[i];
            total_tat += tat[i];
            System.out.println("    " + processes[i] + "            " + at[i] + "              " + bt[i] + "                " + wt[i] + "                 " + tat[i]);
        }

        System.out.printf("Average waiting time = %.2f%n", (float) total_wt / n);
        System.out.printf("Average turnaround time = %.2f%n", (float) total_tat / n);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        int[] processes = new int[n];
        int[] burst_time = new int[n];
        int[] arrival_time = new int[n];

        System.out.println("Enter arrival times and burst times for each process:");
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time for process " + (i + 1) + ": ");
            arrival_time[i] = sc.nextInt();
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            burst_time[i] = sc.nextInt();
            processes[i] = i + 1;
        }

        findAvgTime(processes, n, burst_time, arrival_time);
        sc.close();
    }
}



Stdin

4
0
5
1
2
3
6
5
3





(C)


#include<stdio.h>

void findWaitingTime(int processes[], int n, int bt[], int wt[], int at[]) {
    int service_time[n];
    service_time[0] = 0;
    wt[0] = 0;

    for (int i = 1; i < n; i++) {
        service_time[i] = service_time[i - 1] + bt[i - 1];
        wt[i] = service_time[i] - at[i];
        if (wt[i] < 0)
            wt[i] = 0;
    }
}

void findTurnAroundTime(int processes[], int n, int bt[], int wt[], int tat[]) {
    for (int i = 0; i < n; i++)
        tat[i] = bt[i] + wt[i];
}

void findavgTime(int processes[], int n, int bt[], int at[]) {
    int wt[n], tat[n];

    findWaitingTime(processes, n, bt, wt, at);
    findTurnAroundTime(processes, n, bt, wt, tat);

    printf("Processes   Arrival time   Burst time   Waiting time   Turnaround time\n");
    int total_wt = 0, total_tat = 0;
    for (int i = 0; i < n; i++) {
        total_wt += wt[i];
        total_tat += tat[i];
        printf("\t%d ", processes[i]);
        printf("\t\t%d ", at[i]);
        printf("\t\t\t%d ", bt[i]);
        printf("\t\t\t\t%d", wt[i]);
        printf("\t\t\t\t\t%d\n", tat[i]);
    }

    printf("Average waiting time = %.2f", (float)total_wt / (float)n);
    printf("\nAverage turnaround time = %.2f", (float)total_tat / (float)n);
}

int main() {
    int n;
    printf("Enter the number of processes: ");
    scanf("%d", &n);

    int processes[n];
    int burst_time[n];
    int arrival_time[n];

    printf("Enter arrival times and burst times for each process:\n");
    for (int i = 0; i < n; i++) {
        printf("Enter arrival time for process %d: ", i + 1);
        scanf("%d", &arrival_time[i]);
        printf("Enter burst time for process %d: ", i + 1);
        scanf("%d", &burst_time[i]);
        processes[i] = i + 1;
    }

    findavgTime(processes, n, burst_time, arrival_time);
    return 0;
}









4.2 SJF


(Java)

import java.util.Scanner;

class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int waitingTime;
    int turnaroundTime;
    int startTime;
    int completionTime;

    Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }
}

public class SJF_Scheduling {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numProcesses = sc.nextInt();

        Process[] processes = new Process[numProcesses];
        boolean[] isCompleted = new boolean[numProcesses];
        int[] executionSequence = new int[numProcesses];
        int sequenceIndex = 0;

        System.out.println("\nEnter process details (PID, Arrival Time, Burst Time):");
        for (int i = 0; i < numProcesses; i++) {
            System.out.print("Process " + (i + 1) + ": ");
            int pid = sc.nextInt();
            int arrivalTime = sc.nextInt();
            int burstTime = sc.nextInt();
            processes[i] = new Process(pid, arrivalTime, burstTime);
            isCompleted[i] = false;
        }

        int currentTime = 0;
        int completedProcesses = 0;
        float avgWaitingTime = 0;
        float avgTurnaroundTime = 0;

        while (completedProcesses < numProcesses) {
            int minBurstTime = Integer.MAX_VALUE;
            int selectedProcess = -1;

            for (int i = 0; i < numProcesses; i++) {
                if (!isCompleted[i] && processes[i].arrivalTime <= currentTime && processes[i].burstTime < minBurstTime) {
                    minBurstTime = processes[i].burstTime;
                    selectedProcess = i;
                }
            }

            if (selectedProcess == -1) {
                currentTime++;
            } else {
                processes[selectedProcess].startTime = currentTime;
                processes[selectedProcess].completionTime = currentTime + processes[selectedProcess].burstTime;
                processes[selectedProcess].turnaroundTime = processes[selectedProcess].completionTime - processes[selectedProcess].arrivalTime;
                processes[selectedProcess].waitingTime = processes[selectedProcess].turnaroundTime - processes[selectedProcess].burstTime;

                avgWaitingTime += processes[selectedProcess].waitingTime;
                avgTurnaroundTime += processes[selectedProcess].turnaroundTime;

                isCompleted[selectedProcess] = true;
                executionSequence[sequenceIndex++] = processes[selectedProcess].pid;
                currentTime = processes[selectedProcess].completionTime;
                completedProcesses++;
            }
        }

        avgWaitingTime /= numProcesses;
        avgTurnaroundTime /= numProcesses;

        System.out.print("\nExecution Sequence: ");
        for (int i = 0; i < numProcesses; i++) {
            System.out.print("P" + executionSequence[i] + " ");
        }

        System.out.println("\n\nPID\tAT\tBT\tCT\tTAT\tWT");
        for (Process process : processes) {
            System.out.println(process.pid + "\t" + process.arrivalTime + "\t" + process.burstTime + "\t" +
                               process.completionTime + "\t" + process.turnaroundTime + "\t" + process.waitingTime);
        }

        System.out.printf("\nAverage Waiting Time: %.2f", avgWaitingTime);
        System.out.printf("\nAverage Turnaround Time: %.2f", avgTurnaroundTime);

        sc.close();
    }
}



Stdin
5
1 0 6
2 1 8
3 2 7
4 3 3
5 4 4




(C)


#include <stdio.h>
#include <stdlib.h>

struct Process {
    int pid;
    int arrival_time;
    int burst_time;
    int waiting_time;
    int turnaround_time;
    int start_time;
    int completion_time;
};

int main() {
    int num_processes;
    printf("Enter the number of processes: ");
    scanf("%d", &num_processes);

    float avg_waiting_time = 0;
    float avg_turnaround_time = 0;

    printf("\nEnter process details (PID, Arrival Time, Burst Time):\n");
    struct Process processes[num_processes];
    int is_completed[num_processes];
    int execution_sequence[num_processes];
    int sequence_index = 0;

    for (int i = 0; i < num_processes; i++) {
        printf("Process %d: ", i + 1);
        scanf("%d %d %d", &processes[i].pid, &processes[i].arrival_time, &processes[i].burst_time);
        is_completed[i] = 0;
    }

    int current_time = 0;
    int completed_processes = 0;

    while (completed_processes < num_processes) {
        int min_burst_time = __INT_MAX__;
        int selected_process = -1;

        for (int i = 0; i < num_processes; i++) {
            if (!is_completed[i] && processes[i].arrival_time <= current_time && processes[i].burst_time < min_burst_time) {
                min_burst_time = processes[i].burst_time;
                selected_process = i;
            }
        }

        if (selected_process == -1) {
            current_time++;
        } else {
            processes[selected_process].start_time = current_time;
            processes[selected_process].completion_time = current_time + processes[selected_process].burst_time;
            processes[selected_process].turnaround_time = processes[selected_process].completion_time - processes[selected_process].arrival_time;
            processes[selected_process].waiting_time = processes[selected_process].turnaround_time - processes[selected_process].burst_time;

            avg_waiting_time += processes[selected_process].waiting_time;
            avg_turnaround_time += processes[selected_process].turnaround_time;

            is_completed[selected_process] = 1;
            execution_sequence[sequence_index++] = processes[selected_process].pid;
            current_time = processes[selected_process].completion_time;
            completed_processes++;
        }
    }

    avg_waiting_time /= num_processes;
    avg_turnaround_time /= num_processes;

    printf("\nExecution Sequence: ");
    for (int i = 0; i < num_processes; i++) {
        printf("P%d ", execution_sequence[i]);
    }

    printf("\n\nPID\tAT\tBT\tCT\tTAT\tWT\n");
    for (int i = 0; i < num_processes; i++) {
        printf("%d\t%d\t%d\t%d\t%d\t%d\n", processes[i].pid, processes[i].arrival_time, processes[i].burst_time,
               processes[i].completion_time, processes[i].turnaround_time, processes[i].waiting_time);
    }

    printf("\nAverage Waiting Time: %.2f", avg_waiting_time);
    printf("\nAverage Turnaround Time: %.2f\n", avg_turnaround_time);

    return 0;
}








4.3 SJF Non preemptive


(C)

#include <stdio.h>

void sjf(int num) {
    int arrivalTime[num], burstTime[num], waitingTime[num], turnaroundTime[num], completionTime[num], remainingTime[num], current = 0, shortestJob = -1, totalBurstTime = 0;
    float sumWaitingTime = 0, sumTurnaroundTime = 0;
    int executionSequence[100], sequenceIndex = 0;  // Array to store the execution sequence and index

    for(int i = 0; i < num; i++) {
        printf("Enter the arrival time and burst time for process %d: ", i+1);
        scanf("%d %d", &arrivalTime[i], &burstTime[i]);
        remainingTime[i] = burstTime[i];
        totalBurstTime += burstTime[i];
    }

    // Check if the arrival time is less than or equal to the current time
    // If yes, execute the process
    // If during the execution of the process, another process of shorter burst time is ready, this process will interrupt the current process
    // Will go on until remainingTime[all] = 0

    while(totalBurstTime != 0) {
        shortestJob = -1;
        for(int i = 0; i < num; i++) {
            if(arrivalTime[i] <= current && (shortestJob == -1 || remainingTime[shortestJob] > remainingTime[i]) && remainingTime[i] != 0) {
                shortestJob = i;
            }
        }

        if(shortestJob == -1) {
            current++;
            continue;
        }

        totalBurstTime--;
        remainingTime[shortestJob]--;
        executionSequence[sequenceIndex++] = shortestJob + 1;  // Store the process number in the execution sequence array
        current++;

        if(remainingTime[shortestJob] == 0) {
            completionTime[shortestJob] = current;
            turnaroundTime[shortestJob] = completionTime[shortestJob] - arrivalTime[shortestJob];
            waitingTime[shortestJob] = turnaroundTime[shortestJob] - burstTime[shortestJob];
        }
    }

    printf("\nProcess | AT | BT | CT | TAT | WT |\n");
    for (int i = 0; i < num; i++) {
        printf("  P%d    | %2d | %2d | %2d | %2d | %2d  |\n", i + 1, arrivalTime[i], burstTime[i], completionTime[i], turnaroundTime[i], waitingTime[i]);
        sumWaitingTime  +=  waitingTime[i];
        sumTurnaroundTime += turnaroundTime[i];
    }

    printf("\nSequence of execution: ");
    for (int i = 0; i < sequenceIndex; i++) {
        printf("P%d ", executionSequence[i]);
    }
    printf("\n");

    printf("\nAvg WT  = %0.3f\n", sumWaitingTime / num);
    printf("Avg TAT = %0.3f\n", sumTurnaroundTime / num);
}

int main() {
    int num;
    printf("Enter the number of processes: ");
    scanf("%d", &num);

    sjf(num);
    return 0;
}









4.4 Round Robin


(Java)

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int completionTime;
    int waitingTime;
    int turnaroundTime;

    Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.completionTime = 0;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
    }
}

public class RoundRobin_Scheduling {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();

        Process[] processes = new Process[n];

        System.out.println("Enter arrival times and burst times for each process:");
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time for process " + (i + 1) + ": ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            int burstTime = scanner.nextInt();
            processes[i] = new Process(i + 1, arrivalTime, burstTime);
        }

        int timeQuantum = 2;
        int currTime = 0;
        int completedProcesses = 0;

        Queue<Process> readyQueue = new LinkedList<>();
        Queue<Process> arrivedQueue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            arrivedQueue.offer(processes[i]);
        }

        System.out.println("\nSequence of Execution:");

        while (completedProcesses < n) {
            while (!arrivedQueue.isEmpty()) {
                Process p = arrivedQueue.poll();
                if (p.arrivalTime <= currTime) {
                    readyQueue.offer(p);
                } else {
                    arrivedQueue.offer(p);
                    break;
                }
            }

            if (!readyQueue.isEmpty()) {
                Process current = readyQueue.poll();
                System.out.print("Executing Process " + current.pid + " from time " + currTime + " to ");
                int executionTime = Math.min(timeQuantum, current.remainingTime);
                currTime += executionTime;
                System.out.println(currTime);
                current.remainingTime -= executionTime;

                if (current.remainingTime > 0) {
                    readyQueue.offer(current);
                } else {
                    current.completionTime = currTime;
                    current.turnaroundTime = current.completionTime - current.arrivalTime;
                    current.waitingTime = current.turnaroundTime - current.burstTime;
                    completedProcesses++;
                }
            } else {
                currTime++;
            }
        }

        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            Process p = processes[i];
            System.out.println(p.pid + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" + p.completionTime + "\t" + p.turnaroundTime + "\t" + p.waitingTime);
        }

        scanner.close();
    }
}



Stdin

5
0 6
1 8
2 7
3 3
4 4





(C)


#include <stdio.h>
#include <stdbool.h>

typedef struct {
    int pid;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int completionTime;
    int waitingTime;
    int turnaroundTime;
} Process;

void enqueue(Process queue[], int *rear, Process p) {
    queue[++(*rear)] = p;
}

Process dequeue(Process queue[], int *front) {
    return queue[(*front)++];
}

bool isEmpty(int front, int rear) {
    return front > rear;
}

int main() {
    int n;
    printf("Enter the number of processes: ");
    scanf("%d", &n);

    Process processes[n];

    printf("Enter arrival times and burst times for each process:\n");
    for (int i = 0; i < n; i++) {
        printf("Enter arrival time for process %d: ", i + 1);
        scanf("%d", &processes[i].arrivalTime);
        printf("Enter burst time for process %d: ", i + 1);
        scanf("%d", &processes[i].burstTime);
        processes[i].pid = i + 1;
        processes[i].remainingTime = processes[i].burstTime;
        processes[i].completionTime = 0;
        processes[i].waitingTime = 0;
        processes[i].turnaroundTime = 0;
    }

    int timeQuantum = 2;
    int currTime = 0;
    int completedProcesses = 0;

    Process readyQueue[100];
    int front = 0, rear = -1;

    Process arrivedProcesses[100];
    for (int i = 0; i < n; i++) {
        arrivedProcesses[i] = processes[i];
    }
    int arrivedCount = n;

    printf("\nSequence of Execution:\n");

    while (completedProcesses < n) {
        for (int i = 0; i < arrivedCount; i++) {
            Process p = arrivedProcesses[i];
            if (p.arrivalTime <= currTime) {
                enqueue(readyQueue, &rear, p);
                for (int j = i; j < arrivedCount - 1; j++) {
                    arrivedProcesses[j] = arrivedProcesses[j + 1];
                }
                arrivedCount--;
                i--;
            }
        }

        if (!isEmpty(front, rear)) {
            Process current = dequeue(readyQueue, &front);
            printf("Executing Process %d from time %d to ", current.pid, currTime);
            int executionTime = (timeQuantum < current.remainingTime) ? timeQuantum : current.remainingTime;
            currTime += executionTime;
            printf("%d\n", currTime);
            current.remainingTime -= executionTime;

            for (int i = 0; i < arrivedCount; i++) {
                Process p = arrivedProcesses[i];
                if (p.arrivalTime <= currTime) {
                    enqueue(readyQueue, &rear, p);
                    for (int j = i; j < arrivedCount - 1; j++) {
                        arrivedProcesses[j] = arrivedProcesses[j + 1];
                    }
                    arrivedCount--;
                    i--;
                }
            }

            if (current.remainingTime > 0) {
                enqueue(readyQueue, &rear, current);
            } else {
                current.completionTime = currTime;
                current.turnaroundTime = current.completionTime - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
                for (int i = 0; i < n; i++) {
                    if (processes[i].pid == current.pid) {
                        processes[i] = current;
                        break;
                    }
                }
                completedProcesses++;
            }
        } else {
            currTime++;
        }
    }

    printf("\nPID\tAT\tBT\tCT\tTAT\tWT\n");
    for (int i = 0; i < n; i++) {
        Process p = processes[i];
        printf("%d\t%d\t%d\t%d\t%d\t%d\n", p.pid, p.arrivalTime, p.burstTime, p.completionTime, p.turnaroundTime, p.waitingTime);
    }

    return 0;
}









4.5 Priority Preemptive


(Java)

import java.util.Scanner;

public class Priority_Scheduling {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of processes : ");
        int p = scanner.nextInt();

        int[] at = new int[p];
        int[] bt = new int[p];
        int[] temp = new int[p];
        int[] tat = new int[p];
        int[] wt = new int[p];
        int[] visited = new int[p];
        int[] pp = new int[p];
        int small;
        int count = 0;
        int curTime = 0;
        int totalTime = 0;
        float t = 0;
        float w = 0;

        for (int i = 0; i < p; i++) {
            System.out.print("Enter Arrival Time of process " + i + " : ");
            at[i] = scanner.nextInt();
            System.out.print("Enter Burst Time of process " + i + " : ");
            bt[i] = scanner.nextInt();
            System.out.print("Enter Priority of process " + i + " : ");
            pp[i] = scanner.nextInt();
            System.out.println();
            totalTime += bt[i];
            temp[i] = bt[i];
            visited[i] = 0;
        }

        small = at[0];
        for (int i = 0; i < p; i++) {
            if (at[i] < small) {
                small = at[i];
            }
        }

        curTime = small;
        totalTime += small;

        while (curTime < totalTime) {
            for (int i = 0; i < p; i++) {
                if (visited[i] == 0 && at[i] <= curTime) {
                    small = i;
                    break;
                }
            }

            for (int i = 0; i < p; i++) {
                if (visited[i] == 0 && at[i] <= curTime && pp[i] < pp[small]) {
                    small = i;
                } else if (visited[i] == 0 && at[i] <= curTime && pp[i] == pp[small] && at[i] < at[small]) {
                    small = i;
                }
            }

            curTime++;
            bt[small]--;
            if (bt[small] == 0) {
                visited[small] = 1;
            }
        }

        System.out.println("\nProcess\tArrival Time\tBurst Time\tTurnaround Time\tWaiting Time");
        for (int i = 0; i < p; i++) {
            tat[i] = curTime - at[i];
            wt[i] = tat[i] - temp[i];
            t += tat[i];
            w += wt[i];
            System.out.println(i + "\t" + at[i] + "\t\t" + temp[i] + "\t\t" + tat[i] + "\t\t" + wt[i]);
        }

        System.out.printf("\nAvg. Turnaround Time : %.2f", t / p);
        System.out.printf("\nAvg. Waiting Time : %.2f", w / p);

        scanner.close();
    }
}




Stdin

5
0 6 3
1 8 1
2 7 4
3 3 2
4 4 5




(C)


#include <stdio.h>

void main()
{
    int p;
    printf("Enter number of processes : ");
    scanf("%d", &p);
    int at[p], bt[p], temp[p], tat[p], wt[p], visited[p], pp[p], small;
    int count = 0, curTime = 0, totalTime = 0;
    float t = 0, w = 0;
    for (int i = 0; i < p; i++)
    {
        printf("Enter Arrival Time of process %d : ", i);
        scanf("%d", &at[i]);
        printf("Enter Burst Time of process %d : ", i);
        scanf("%d", &bt[i]);
        printf("Enter Priority of process %d : ", i);
        scanf("%d", &pp[i]);
        printf("\n");
        totalTime += bt[i];
        temp[i] = bt[i];
        visited[i] = 0;
    }

    small = at[0];
    for (int i = 0; i < p; i++)
    {
        if (at[i] < small)
        {
            small = at[i];
        }
    }

    curTime = small;
    totalTime += small;

    while (curTime < totalTime)
    {
        for (int i = 0; i < p; i++)
        {
            if (visited[i] == 0 && at[i] <= curTime)
            {
                small = i;
                break;
            }
        }

        for (int i = 0; i < p; i++)
        {
            if (visited[i] == 0 && at[i] <= curTime && pp[i] < pp[small])
            {
                small = i;
            }
            else if (visited[i] == 0 && at[i] <= curTime && pp[i] == pp[small] && at[i] < at[small])
            {
                small = i;
            }
        }

        curTime++;
        bt[small]--;
        if (bt[small] == 0)
        {
            visited[small] = 1;
        }
    }

    printf("\nProcess\tArrival Time\tBurst Time\tTurnaround Time\tWaiting Time\n");
    for (int i = 0; i < p; i++)
    {
        tat[i] = curTime - at[i];
        wt[i] = tat[i] - temp[i];
        t += tat[i];
        w += wt[i];
        printf("%d\t%d\t\t%d\t\t%d\t\t%d\n", i, at[i], temp[i], tat[i], wt[i]);
    }

    printf("\nAvg. Turnaround Time : %.2f", t / p);
    printf("\nAvg. Waiting Time : %.2f", w / p);
}










4.6 Priority Non-Preemptive


(Java)

import java.util.Scanner;

class Process {
    int at;
    int ct;
    int bt;
    int pid;
    int priority;
    int tat;
    int wt;
}

public class PriorityScheduling {
    static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = scanner.nextInt();
        Process[] processes = new Process[n];

        System.out.println("Enter arrival times, burst times, and priorities for each process:");
        for (int i = 0; i < n; i++) {
            processes[i] = new Process();
            System.out.print("Enter arrival time for process " + (i + 1) + ": ");
            processes[i].at = scanner.nextInt();
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            processes[i].bt = scanner.nextInt();
            System.out.print("Enter priority for process " + (i + 1) + ": ");
            processes[i].priority = scanner.nextInt();
            processes[i].pid = i + 1;
            processes[i].ct = 0;
            processes[i].tat = 0;
            processes[i].wt = 0;
        }

        int currentTime = 0;
        int completedProcess = 0;

        System.out.println("\nExecution Sequence:");

        while (completedProcess < n) {
            Process highestPriority = null;
            for (int i = 0; i < n; i++) {
                if (processes[i].at <= currentTime && processes[i].ct == 0) {
                    if (highestPriority == null || processes[i].priority < highestPriority.priority) {
                        highestPriority = processes[i];
                    }
                }
            }

            if (highestPriority != null) {
                currentTime = max(currentTime, highestPriority.at);
                currentTime += highestPriority.bt;
                highestPriority.ct = currentTime;
                highestPriority.tat = highestPriority.ct - highestPriority.at;
                highestPriority.wt = highestPriority.tat - highestPriority.bt;
                System.out.println("Process " + highestPriority.pid + " completed at time " + highestPriority.ct);
                completedProcess++;
            } else {
                currentTime++;
            }
        }

        System.out.println("\nPID\tAT\tBT\tPriority\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println(processes[i].pid + "\t" + processes[i].at + "\t" + processes[i].bt + "\t" + processes[i].priority + "\t\t" + processes[i].ct + "\t" + processes[i].tat + "\t" + processes[i].wt);
        }

        scanner.close();
    }
}




Stdin

5
0 6 3
1 8 1
2 7 4
3 3 2
4 4 5




(C)


#include <stdio.h>

struct Process {
    int at;
    int ct;
    int bt;
    int pid;
    int priority;
    int tat;
    int wt;
};

int max(int a, int b) {
    return (a > b) ? a : b;
}

int main() {
    int n;
    printf("Enter number of processes: ");
    scanf("%d", &n);

    struct Process processes[n];

    printf("Enter arrival times, burst times, and priorities for each process:\n");
    for (int i = 0; i < n; i++) {
        printf("Enter arrival time for process %d: ", i + 1);
        scanf("%d", &processes[i].at);
        printf("Enter burst time for process %d: ", i + 1);
        scanf("%d", &processes[i].bt);
        printf("Enter priority for process %d: ", i + 1);
        scanf("%d", &processes[i].priority);
        processes[i].pid = i + 1;
        processes[i].ct = 0;
        processes[i].tat = 0;
        processes[i].wt = 0;
    }

    int currentTime = 0;
    int completedProcess = 0;

    printf("\nExecution Sequence:\n");

    while (completedProcess < n) {
        struct Process* highestPriority = NULL;
        for (int i = 0; i < n; i++) {
            if (processes[i].at <= currentTime && processes[i].ct == 0) {
                if (highestPriority == NULL || processes[i].priority < highestPriority->priority) {
                    highestPriority = &processes[i];
                }
            }
        }

        if (highestPriority != NULL) {
            currentTime = max(currentTime, highestPriority->at);
            currentTime += highestPriority->bt;
            highestPriority->ct = currentTime;
            highestPriority->tat = highestPriority->ct - highestPriority->at;
            highestPriority->wt = highestPriority->tat - highestPriority->bt;
            printf("Process %d completed at time %d\n", highestPriority->pid, highestPriority->ct);
            completedProcess++;
        } else {
            currentTime++;
        }
    }

    printf("\nPID\tAT\tBT\tPriority\tCT\tTAT\tWT\n");
    for (int i = 0; i < n; i++) {
        printf("%d\t%d\t%d\t%d\t\t%d\t%d\t%d\n", processes[i].pid, processes[i].at, processes[i].bt, processes[i].priority, processes[i].ct, processes[i].tat, processes[i].wt);
    }

    return 0;
}










5. Bankers Algorithm


import java.util.Scanner;

public class BankersAlgorithm {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        System.out.print("Enter the number of resources: ");
        int numResources = scanner.nextInt();

        int[][] max = new int[numProcesses][numResources];
        System.out.println("Enter the maximum need matrix:");
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                max[i][j] = scanner.nextInt();
            }
        }

        int[][] allocation = new int[numProcesses][numResources];
        System.out.println("Enter the allocation matrix:");
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                allocation[i][j] = scanner.nextInt();
            }
        }

        int[] available = new int[numResources];
        System.out.println("Enter the available resources:");
        for (int i = 0; i < numResources; i++) {
            available[i] = scanner.nextInt();
        }

        boolean[] finished = new boolean[numProcesses];
        int[][] need = new int[numProcesses][numResources];

        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }

        int[] work = new int[numResources];
        for (int i = 0; i < numResources; i++) {
            work[i] = available[i];
        }

        int[] safeSequence = new int[numProcesses];
        int count = 0;
        while (count < numProcesses) {
            boolean found = false;
            for (int i = 0; i < numProcesses; i++) {
                if (!finished[i]) {
                    int j;
                    for (j = 0; j < numResources; j++) {
                        if (need[i][j] > work[j]) {
                            break;
                        }
                    }
                    if (j == numResources) {
                        for (int k = 0; k < numResources; k++) {
                            work[k] += allocation[i][k];
                        }
                        safeSequence[count++] = i;
                        finished[i] = true;
                        found = true;
                    }
                }
            }
            if (!found) {
                break;
            }
        }

        if (count < numProcesses) {
            System.out.println("System is in an unsafe state. Deadlock may occur.");
        } else {
            System.out.println("System is in a safe state. Safe sequence:");
            for (int i = 0; i < numProcesses; i++) {
                System.out.print("P" + safeSequence[i]);
                if (i != numProcesses - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        }

        scanner.close();
    }
}



Stdin

3
3
7 5 3
3 2 2
9 0 2
0 1 0
2 0 0
3 0 2
3 3 2







6. Deadlock Detection


import java.util.Scanner;

public class DeadlockDetection {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        System.out.print("Enter the number of resources: ");
        int numResources = scanner.nextInt();

        int[][] max = new int[numProcesses][numResources];
        System.out.println("Enter the maximum need matrix:");
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                max[i][j] = scanner.nextInt();
            }
        }

        int[][] allocation = new int[numProcesses][numResources];
        System.out.println("Enter the allocation matrix:");
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                allocation[i][j] = scanner.nextInt();
            }
        }

        int[] available = new int[numResources];
        System.out.println("Enter the available resources:");
        for (int i = 0; i < numResources; i++) {
            available[i] = scanner.nextInt();
        }

        boolean[] finished = new boolean[numProcesses];
        int[] work = new int[numResources];
        int[][] need = new int[numProcesses][numResources];

        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }

        for (int i = 0; i < numResources; i++) {
            work[i] = available[i];
        }

        int count = 0;
        while (count < numProcesses) {
            boolean found = false;
            for (int i = 0; i < numProcesses; i++) {
                if (!finished[i]) {
                    int j;
                    for (j = 0; j < numResources; j++) {
                        if (need[i][j] > work[j]) {
                            break;
                        }
                    }
                    if (j == numResources) {
                        for (int k = 0; k < numResources; k++) {
                            work[k] += allocation[i][k];
                        }
                        finished[i] = true;
                        found = true;
                        count++;
                    }
                }
            }
            if (!found) {
                break;
            }
        }

        if (count < numProcesses) {
            System.out.println("Deadlock detected!");
        } else {
            System.out.println("System is in a safe state. No deadlock detected.");
        }

        scanner.close();
    }
}


Stdin

3
4
7 5 3 2
3 2 2 1
9 0 2 2
0 1 0 1
2 0 0 1
3 0 2 2
3 3 2 1








7.1 First Fit Memory Management 


import java.util.Scanner;

public class FirstFitMemoryAllocation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Taking inputs
        System.out.print("Enter the number of memory blocks: ");
        int numBlocks = scanner.nextInt();

        int[] blockSizes = new int[numBlocks];
        int[] remainingSizes = new int[numBlocks]; // Store remaining sizes of blocks
        System.out.println("Enter the size of each memory block:");
        for (int i = 0; i < numBlocks; i++) {
            blockSizes[i] = scanner.nextInt();
            remainingSizes[i] = blockSizes[i]; // Initially, remaining size is same as block size
        }

        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        int[] processSizes = new int[numProcesses];
        // Allocate processes using First Fit
        int[] allocation = new int[numProcesses];
        boolean[] allocated = new boolean[numBlocks];

        System.out.println("Enter the size of each process:");
        for (int i = 0; i < numProcesses; i++) {
            processSizes[i] = scanner.nextInt();

            // Find a block for allocation
            allocation[i] = -1; // Initialize allocation as -1 (not allocated)
            for (int j = 0; j < numBlocks; j++) {
                if (!allocated[j] && remainingSizes[j] >= processSizes[i]) {
                    allocation[i] = j + 1; // Block IDs start from 1
                    allocated[j] = true;
                    remainingSizes[j] -= processSizes[i]; // Update remaining size
                    break;
                }
            }
        }

        // Printing allocation results
        System.out.println("\nProcess\t\tSize\t\tBlock Allocated\t\tRemaining Block Size");
        for (int i = 0; i < numProcesses; i++) {
            System.out.println((i + 1) + "\t\t" + processSizes[i] + "\t\t" +
                    ((allocation[i] == -1) ? "Not Allocated" : allocation[i]) + "\t\t" +
                    ((allocation[i] == -1) ? "" : remainingSizes[allocation[i] - 1]));
        }

        scanner.close();
    }
}



Stdin

5
100
200
150
300
250
4
130
180
220
200








7.2 Next fit Memory Management 


import java.util.Scanner;

public class NextFitMemoryAllocation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Taking inputs
        System.out.print("Enter the number of memory blocks: ");
        int numBlocks = scanner.nextInt();

        int[] blockSizes = new int[numBlocks];
        int[] remainingSizes = new int[numBlocks]; // Store remaining sizes of blocks
        System.out.println("Enter the size of each memory block:");
        for (int i = 0; i < numBlocks; i++) {
            blockSizes[i] = scanner.nextInt();
            remainingSizes[i] = blockSizes[i]; // Initially, remaining size is same as block size
        }

        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        int[] processSizes = new int[numProcesses];
        // Prompt the user to enter the size of each process
        System.out.println("Enter the size of each process:");
        for (int i = 0; i < numProcesses; i++) {
            processSizes[i] = scanner.nextInt();
        }

        // Allocate processes using Next Fit
        int[] allocation = new int[numProcesses];
        boolean[] allocated = new boolean[numBlocks];
        int lastBlockUsed = 0;

        for (int i = 0; i < numProcesses; i++) {
            allocation[i] = -1; // Initialize allocation as -1 (not allocated)

            // Start searching for block allocation from the last used block
            for (int j = lastBlockUsed; j < numBlocks; j++) {
                if (!allocated[j] && blockSizes[j] >= processSizes[i]) {
                    allocation[i] = j;
                    allocated[j] = true;
                    remainingSizes[j] -= processSizes[i]; // Update remaining size
                    lastBlockUsed = j; // Update the index of the last block used
                    break;
                }
            }

            // If no block found from last used block, start searching from the beginning
            if (allocation[i] == -1) {
                for (int j = 0; j < lastBlockUsed; j++) {
                    if (!allocated[j] && blockSizes[j] >= processSizes[i]) {
                        allocation[i] = j;
                        allocated[j] = true;
                        remainingSizes[j] -= processSizes[i]; // Update remaining size
                        lastBlockUsed = j; // Update the index of the last block used
                        break;
                    }
                }
            }
        }

        // Printing allocation results
        System.out.println("\nProcess\t\tSize\t\tBlock Allocated\t\tRemaining Block Size");
        for (int i = 0; i < numProcesses; i++) {
            System.out.println((i + 1) + "\t\t" + processSizes[i] + "\t\t" +
                    ((allocation[i] == -1) ? "Not Allocated" : (allocation[i] + 1)) + "\t\t" +
                    ((allocation[i] == -1) ? "" : remainingSizes[allocation[i]]));
        }

        scanner.close();
    }
}



Stdin

5
100
200
150
300
250
4
130
180
220
200









7.3 Best Fit Memory Management


import java.util.Scanner;

public class BestFitMemoryAllocation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Taking inputs
        System.out.print("Enter the number of memory blocks: ");
        int numBlocks = scanner.nextInt();

        int[] blockSizes = new int[numBlocks];
        int[] remainingSizes = new int[numBlocks]; // Store remaining sizes of blocks
        System.out.println("Enter the size of each memory block:");
        for (int i = 0; i < numBlocks; i++) {
            blockSizes[i] = scanner.nextInt();
            remainingSizes[i] = blockSizes[i]; // Initially, remaining size is same as block size
        }

        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        int[] processSizes = new int[numProcesses];
        // Prompt the user to enter the size of each process
        System.out.println("Enter the size of each process:");
        for (int i = 0; i < numProcesses; i++) {
            processSizes[i] = scanner.nextInt();
        }

        // Allocate processes using Best Fit
        int[] allocation = new int[numProcesses];

        for (int i = 0; i < numProcesses; i++) {
            allocation[i] = -1; // Initialize allocation as -1 (not allocated)
            int bestFitIndex = -1;

            // Find the best fit block for allocation
            for (int j = 0; j < numBlocks; j++) {
                if (remainingSizes[j] >= processSizes[i]) {
                    if (bestFitIndex == -1 || remainingSizes[j] < remainingSizes[bestFitIndex]) {
                        bestFitIndex = j;
                    }
                }
            }

            // Allocate the process to the best fit block
            if (bestFitIndex != -1) {
                allocation[i] = bestFitIndex;
                remainingSizes[bestFitIndex] -= processSizes[i]; // Update remaining size
            }
        }

        // Printing allocation results
        System.out.println("\nProcess\t\tSize\t\tBlock Allocated\t\tRemaining Block Size");
        for (int i = 0; i < numProcesses; i++) {
            System.out.println((i + 1) + "\t\t" + processSizes[i] + "\t\t" +
                    ((allocation[i] == -1) ? "Not Allocated" : (allocation[i] + 1)) + "\t\t" +
                    ((allocation[i] == -1) ? "" : remainingSizes[allocation[i]]));
        }

        scanner.close();
    }
}



Stdin

5
100
200
150
300
250
4
130
180
220
200










7.4 Worst Fit Memory Management


import java.util.Scanner;

public class WorstFitMemoryAllocation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Taking inputs
        System.out.print("Enter the number of memory blocks: ");
        int numBlocks = scanner.nextInt();

        int[] blockSizes = new int[numBlocks];
        int[] remainingSizes = new int[numBlocks]; // Store remaining sizes of blocks
        System.out.println("Enter the size of each memory block:");
        for (int i = 0; i < numBlocks; i++) {
            blockSizes[i] = scanner.nextInt();
            remainingSizes[i] = blockSizes[i]; // Initially, remaining size is same as block size
        }

        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        int[] processSizes = new int[numProcesses];
        // Prompt the user to enter the size of each process
        System.out.println("Enter the size of each process:");
        for (int i = 0; i < numProcesses; i++) {
            processSizes[i] = scanner.nextInt();
        }

        // Allocate processes using Worst Fit
        int[] allocation = new int[numProcesses];

        for (int i = 0; i < numProcesses; i++) {
            allocation[i] = -1; // Initialize allocation as -1 (not allocated)
            int worstFitIndex = -1;

            // Find the worst fit block for allocation
            for (int j = 0; j < numBlocks; j++) {
                if (remainingSizes[j] >= processSizes[i]) {
                    if (worstFitIndex == -1 || remainingSizes[j] > remainingSizes[worstFitIndex]) {
                        worstFitIndex = j;
                    }
                }
            }

            // Allocate the process to the worst fit block
            if (worstFitIndex != -1) {
                allocation[i] = worstFitIndex;
                remainingSizes[worstFitIndex] -= processSizes[i]; // Update remaining size
            }
        }

        // Printing allocation results
        System.out.println("\nProcess\t\tSize\t\tBlock Allocated\t\tRemaining Block Size");
        for (int i = 0; i < numProcesses; i++) {
            System.out.println((i + 1) + "\t\t" + processSizes[i] + "\t\t" +
                    ((allocation[i] == -1) ? "Not Allocated" : (allocation[i] + 1)) + "\t\t" +
                    ((allocation[i] == -1) ? "" : remainingSizes[allocation[i]]));
        }

        scanner.close();
    }
}



Stdin


5
100
200
150
300
250
4
130
180
220
200








8.1 FIFO Page Replacement 


import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class FIFOPageReplacement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Taking inputs
        System.out.print("Enter the number of frames: ");
        int numberOfFrames = scanner.nextInt();

        System.out.print("Enter the length of the page request sequence: ");
        int sequenceLength = scanner.nextInt();

        int[] pageRequests = new int[sequenceLength];
        System.out.println("Enter the page request sequence:");
        for (int i = 0; i < sequenceLength; i++) {
            pageRequests[i] = scanner.nextInt();
        }

        // Simulating FIFO Page Replacement
        Queue<Integer> frameQueue = new LinkedList<>();
        int[] frames = new int[numberOfFrames];
        for (int i = 0; i < numberOfFrames; i++) {
            frames[i] = -1; // Initialize frames with -1 indicating empty frame
        }

        int pageFaults = 0;
        int pageHits = 0;
        int index = 0;

        for (int i = 0; i < sequenceLength; i++) {
            int currentPage = pageRequests[i];
            if (!frameQueue.contains(currentPage)) {
                // Page fault occurs
                pageFaults++;
                if (frameQueue.size() < numberOfFrames) {
                    // If there is space in frames, add the page to the queue and frame
                    frameQueue.add(currentPage);
                    frames[index] = currentPage;
                    index = (index + 1) % numberOfFrames;
                } else {
                    // Remove the oldest page and add the new page to the queue and frame
                    int oldestPage = frameQueue.poll();
                    for (int j = 0; j < numberOfFrames; j++) {
                        if (frames[j] == oldestPage) {
                            frames[j] = currentPage;
                            break;
                        }
                    }
                    frameQueue.add(currentPage);
                }
            } else {
                // Page hit occurs
                pageHits++;
            }

            // Printing the current state of frames
            System.out.print("Frames after request " + currentPage + ": ");
            for (int frame : frames) {
                if (frame == -1) {
                    System.out.print("empty ");
                } else {
                    System.out.print(frame + " ");
                }
            }
            System.out.println();
        }

        // Printing the total number of page faults and page hits
        System.out.println("Total number of page faults: " + pageFaults);
        System.out.println("Total number of page hits: " + pageHits);

        scanner.close();
    }
}


Stdin

3
10
7
0
1
2
0
3
0
4
2
3








8.2 Least Recently Used


import java.util.*;

public class LRUPageReplacement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Taking inputs
        System.out.print("Enter the number of frames: ");
        int numberOfFrames = scanner.nextInt();

        System.out.print("Enter the length of the page request sequence: ");
        int sequenceLength = scanner.nextInt();

        int[] pageRequests = new int[sequenceLength];
        System.out.println("Enter the page request sequence:");
        for (int i = 0; i < sequenceLength; i++) {
            pageRequests[i] = scanner.nextInt();
        }

        // Simulating LRU Page Replacement
        List<Integer> frames = new ArrayList<>();
        Map<Integer, Integer> pageMap = new HashMap<>(); // page to its most recent index

        int pageFaults = 0;
        int pageHits = 0;

        for (int i = 0; i < sequenceLength; i++) {
            int currentPage = pageRequests[i];
            
            if (frames.contains(currentPage)) {
                // Page hit occurs
                pageHits++;
            } else {
                // Page fault occurs
                pageFaults++;
                if (frames.size() < numberOfFrames) {
                    frames.add(currentPage);
                } else {
                    // Find the least recently used page
                    int lruPage = getLRUPage(pageMap, frames);
                    frames.set(frames.indexOf(lruPage), currentPage);
                }
            }
            // Update the most recent use of the current page
            pageMap.put(currentPage, i);

            // Printing the current state of frames
            System.out.print("Frames after request " + currentPage + ": ");
            for (int frame : frames) {
                System.out.print(frame + " ");
            }
            System.out.println();
        }

        // Printing the total number of page faults and page hits
        System.out.println("Total number of page faults: " + pageFaults);
        System.out.println("Total number of page hits: " + pageHits);

        scanner.close();
    }

    private static int getLRUPage(Map<Integer, Integer> pageMap, List<Integer> frames) {
        int lruPage = frames.get(0);
        int oldestIndex = pageMap.get(lruPage);

        for (int frame : frames) {
            if (pageMap.get(frame) < oldestIndex) {
                oldestIndex = pageMap.get(frame);
                lruPage = frame;
            }
        }
        return lruPage;
    }
}



Stdin

3
10
7
0
1
2
0
3
0
4
2
3











8.3 Optimal Page Replacement 


import java.util.*;

public class OptimalPageReplacement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Taking inputs
        System.out.print("Enter the number of frames: ");
        int numberOfFrames = scanner.nextInt();

        System.out.print("Enter the length of the page request sequence: ");
        int sequenceLength = scanner.nextInt();

        int[] pageRequests = new int[sequenceLength];
        System.out.println("Enter the page request sequence:");
        for (int i = 0; i < sequenceLength; i++) {
            pageRequests[i] = scanner.nextInt();
        }

        // Simulating Optimal Page Replacement
        List<Integer> frames = new ArrayList<>();
        int pageFaults = 0;
        int pageHits = 0;

        for (int i = 0; i < sequenceLength; i++) {
            int currentPage = pageRequests[i];

            if (frames.contains(currentPage)) {
                // Page hit occurs
                pageHits++;
            } else {
                // Page fault occurs
                pageFaults++;
                if (frames.size() < numberOfFrames) {
                    frames.add(currentPage);
                } else {
                    int pageToReplace = findPageToReplace(pageRequests, frames, i);
                    frames.set(frames.indexOf(pageToReplace), currentPage);
                }
            }

            // Printing the current state of frames
            System.out.print("Frames after request " + currentPage + ": ");
            for (int frame : frames) {
                System.out.print(frame + " ");
            }
            System.out.println();
        }

        // Printing the total number of page faults and page hits
        System.out.println("Total number of page faults: " + pageFaults);
        System.out.println("Total number of page hits: " + pageHits);

        scanner.close();
    }

    private static int findPageToReplace(int[] pageRequests, List<Integer> frames, int currentIndex) {
        int farthestIndex = -1;
        int pageToReplace = -1;
        
        for (int frame : frames) {
            int farthest = currentIndex;
            for (int i = currentIndex + 1; i < pageRequests.length; i++) {
                if (frame == pageRequests[i]) {
                    farthest = i;
                    break;
                }
            }
            if (farthest > farthestIndex) {
                farthestIndex = farthest;
                pageToReplace = frame;
            }
        }
        
        return pageToReplace;
    }
}




Stdin

3
10
7
0
1
2
0
3
0
4
2
3










9.1 FIFO Disk Management


import java.util.Scanner;

public class FIFODiskScheduling {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of disk requests: ");
        int numRequests = scanner.nextInt();

        int[] requests = new int[numRequests];
        System.out.println("Enter the disk requests (separated by space): ");
        for (int i = 0; i < numRequests; i++) {
            requests[i] = scanner.nextInt();
        }

        System.out.println("Enter the initial head position: ");
        int initialHeadPosition = scanner.nextInt();

        int totalSeekTime = calculateTotalSeekTime(requests, initialHeadPosition);
        System.out.println("Total seek time: " + totalSeekTime);
    }

    private static int calculateTotalSeekTime(int[] requests, int initialHeadPosition) {
        int totalSeekTime = 0;
        int currentPosition = initialHeadPosition;

        System.out.print("Order of servicing requests: ");
        for (int request : requests) {
            System.out.print(request + " ");
            totalSeekTime += Math.abs(currentPosition - request);
            currentPosition = request;
        }
        System.out.println(); // for new line

        return totalSeekTime;
    }
}


Stdin

8
98 183 37 122 14 124 65 67
53








9.2 Shortest Seek Time First (SSTF) Disk Management 


import java.util.Scanner;
import java.util.ArrayList;

public class SSTFDiskScheduling {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of disk requests: ");
        int numRequests = scanner.nextInt();

        int[] requests = new int[numRequests];
        System.out.println("Enter the disk requests (separated by space): ");
        for (int i = 0; i < numRequests; i++) {
            requests[i] = scanner.nextInt();
        }

        System.out.println("Enter the initial head position: ");
        int initialHeadPosition = scanner.nextInt();

        SSTFResult result = calculateSSTF(requests, initialHeadPosition);
        System.out.println("Order of servicing requests: " + result.order);
        System.out.println("Total seek time: " + result.totalSeekTime);
    }

    private static SSTFResult calculateSSTF(int[] requests, int initialHeadPosition) {
        boolean[] visited = new boolean[requests.length];
        int totalSeekTime = 0;
        int currentPosition = initialHeadPosition;
        ArrayList<Integer> order = new ArrayList<>();

        for (int i = 0; i < requests.length; i++) {
            int closestRequestIndex = -1;
            int minimumDistance = Integer.MAX_VALUE;

            for (int j = 0; j < requests.length; j++) {
                if (!visited[j]) {
                    int distance = Math.abs(currentPosition - requests[j]);
                    if (distance < minimumDistance) {
                        minimumDistance = distance;
                        closestRequestIndex = j;
                    }
                }
            }

            visited[closestRequestIndex] = true;
            totalSeekTime += minimumDistance;
            currentPosition = requests[closestRequestIndex];
            order.add(requests[closestRequestIndex]);
        }

        return new SSTFResult(order, totalSeekTime);
    }

    static class SSTFResult {
        ArrayList<Integer> order;
        int totalSeekTime;

        SSTFResult(ArrayList<Integer> order, int totalSeekTime) {
            this.order = order;
            this.totalSeekTime = totalSeekTime;
        }
    }
}



Stdin

8
98 183 37 122 14 124 65 67
53








9.3 SCAN Memory Management


import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SCANDiskScheduling {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of disk requests: ");
        int numRequests = scanner.nextInt();

        ArrayList<Integer> requests = new ArrayList<>();
        System.out.println("Enter the disk requests (separated by space): ");
        for (int i = 0; i < numRequests; i++) {
            requests.add(scanner.nextInt());
        }

        System.out.println("Enter the initial head position: ");
        int initialHeadPosition = scanner.nextInt();

        System.out.println("Enter the total number of cylinders: ");
        int totalCylinders = scanner.nextInt();

        System.out.println("Enter the direction (1 for high, 0 for low): ");
        int direction = scanner.nextInt();

        SCANResult result = calculateSCAN(requests, initialHeadPosition, totalCylinders, direction);
        System.out.println("Order of servicing requests: " + result.order);
        System.out.println("Total seek time: " + result.totalSeekTime);
    }

    private static SCANResult calculateSCAN(ArrayList<Integer> requests, int initialHeadPosition, int totalCylinders, int direction) {
        Collections.sort(requests);

        ArrayList<Integer> left = new ArrayList<>();
        ArrayList<Integer> right = new ArrayList<>();

        for (int request : requests) {
            if (request < initialHeadPosition) {
                left.add(request);
            } else {
                right.add(request);
            }
        }

        ArrayList<Integer> order = new ArrayList<>();
        int totalSeekTime = 0;
        int currentPosition = initialHeadPosition;

        if (direction == 1) {
            // Moving towards the high end
            order.addAll(right);
            order.add(totalCylinders - 1); // Go to the end
            order.addAll(left);
        } else {
            // Moving towards the low end
            Collections.reverse(left);
            order.addAll(left);
            order.add(0); // Go to the start
            order.addAll(right);
        }

        for (int request : order) {
            totalSeekTime += Math.abs(currentPosition - request);
            currentPosition = request;
        }

        return new SCANResult(order, totalSeekTime);
    }

    static class SCANResult {
        ArrayList<Integer> order;
        int totalSeekTime;

        SCANResult(ArrayList<Integer> order, int totalSeekTime) {
            this.order = order;
            this.totalSeekTime = totalSeekTime;
        }
    }
}



Stdin

10
98 183 37 122 14 124 65 67 53 85
53
200
1









9.4 CSCAN Memory Management


import java.util.Arrays;
import java.util.Scanner;

public class CSCANDiskScheduling {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Taking inputs
        System.out.print("Enter the total number of disk requests: ");
        int n = scanner.nextInt();
        
        int[] requests = new int[n];
        System.out.println("Enter the disk requests:");
        for (int i = 0; i < n; i++) {
            requests[i] = scanner.nextInt();
        }
        
        System.out.print("Enter the initial head position: ");
        int head = scanner.nextInt();
        
        System.out.print("Enter the disk size (total number of cylinders): ");
        int diskSize = scanner.nextInt();
        
        // Sorting the requests
        Arrays.sort(requests);
        
        // Finding the position to insert the initial head in the sorted array
        int index = 0;
        while (index < n && requests[index] < head) {
            index++;
        }
        
        // Order in which requests are serviced
        int[] seekOrder = new int[n];
        int seekCount = 0;
        int seekIndex = 0;
        
        // Servicing the requests to the right of the initial head position
        for (int i = index; i < n; i++) {
            seekOrder[seekIndex++] = requests[i];
            seekCount += Math.abs(head - requests[i]);
            head = requests[i];
        }
        
        // Moving the head to the end of the disk and wrapping around
        if (index != 0) {
            seekCount += Math.abs(head - (diskSize - 1));
            head = 0;
            seekCount += diskSize - 1;
        }
        
        // Servicing the remaining requests starting from the beginning
        for (int i = 0; i < index; i++) {
            seekOrder[seekIndex++] = requests[i];
            seekCount += Math.abs(head - requests[i]);
            head = requests[i];
        }
        
        // Printing the results
        System.out.println("Seek order:");
        for (int i = 0; i < n; i++) {
            System.out.print(seekOrder[i] + " ");
        }
        System.out.println();
        
        System.out.println("Total seek time: " + seekCount);
        
        scanner.close();
    }
}



Stdin

10
98 183 37 122 14 124 65 67 53 85
53
200











