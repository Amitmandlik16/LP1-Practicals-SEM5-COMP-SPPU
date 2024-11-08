import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

class Process
{
	int id,arrivalTime,burstTime,remainingTime,waitingTime,turnarroundTime,priority,completionTime;

	public Process(int id, int arrivalTime, int burstTime,int priority) {
		super();
		this.id = id;
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;
		this.remainingTime=burstTime;
		this.waitingTime=0;
		this.turnarroundTime=0;
		this.priority=priority;
		this.completionTime=0;
	}

	@Override
	public String toString() {
		return "Process id=" + id + ", arrivalTime=" + arrivalTime + ", burstTime=" + burstTime + ", remainingTime="
				+ remainingTime + ", waitingTime=" + waitingTime + ", turnarroundTime=" + turnarroundTime
				+ ", priority=" + priority + ", completionTime=" + completionTime ;
	}
	
	

}

public class Scheduling {

	public static void main(String[] args) {
		System.out.println("Hello ");
		int n=4;
		int ids[]= {1,2,3,4};
		int arrivalTimes[]= {0,1,2,3};
		int burstTimes[]= {5,4,2,1}; 
		int priorites[]= {0,0,0,0}; 
		int quantumTime=2;
		
		Process Processes[]=new Process[n];
		for(int i=0;i<n;i++)
		{
			Processes[i]=new Process(ids[i],arrivalTimes[i],burstTimes[i],priorites[i]);
		}
		System.out.println("Process Input data stored sucessfully.");
		//display(Processes);
		
		//FCFS(Processes.clone());
		//SJFpreemptive(Processes.clone());
		//Prioritynonpreemptive(Processes.clone());
		RoundRobin(Processes.clone(),quantumTime);
	}
		
		public static void display(Process Processes[])
		{
			float totalWaitingTime=0,totalTurnArroundTime=0,averageWaitingTime=0,averageTurnArroundTime=0;
			//displaying the process
			for(int i=0;i<Processes.length;i++)
			{
				System.out.println(Processes[i]);
				totalWaitingTime+=Processes[i].waitingTime;
				totalTurnArroundTime+=Processes[i].turnarroundTime;
			}
			averageWaitingTime=totalWaitingTime/Processes.length;
			averageTurnArroundTime=totalTurnArroundTime/Processes.length;
			System.out.println("Average waiting Time ="+averageWaitingTime);
			System.out.println("Average TurnArround Time ="+averageTurnArroundTime);
		}
		
		public static void FCFS(Process Processes[])
		{
			System.out.println("\nThis is FCFS(First Come First Serve) Scheduling");
			int currentTime=0;
			Arrays.sort(Processes, Comparator.comparingInt(p->p.arrivalTime));
			for(Process p:Processes)
			{
				if(currentTime<p.arrivalTime)
				{
					currentTime=p.arrivalTime;
				}
				p.waitingTime=currentTime-p.arrivalTime;
				p.completionTime=currentTime+p.burstTime;
				currentTime=p.completionTime;
				p.turnarroundTime=p.completionTime-p.arrivalTime;
			}
			display(Processes);
		}
		
		public static void SJFpreemptive(Process Processes[])
		{
			System.out.println("\nThis is SJF(Shortest Job First) preemptive Scheduling");
			int currentTime=0,completed=0;
			int quantumTime=1;
			Process[] ProcessQueue=Arrays.copyOf(Processes, Processes.length);
			while(completed<Processes.length)
			{
				Process Shortest=null;
				int shortestRemainingTime=Integer.MAX_VALUE;
				for(Process p:ProcessQueue)
				{
					if(p.arrivalTime<=currentTime && p.remainingTime>0 && p.remainingTime<shortestRemainingTime)
					{
						Shortest=p;
						shortestRemainingTime=p.remainingTime;
					}
				}
				
				if(Shortest==null)
				{
					currentTime++;
					continue;
				}
				
				if(Shortest.remainingTime>=quantumTime)
				{
					Shortest.remainingTime-=quantumTime;
					currentTime+=quantumTime;
				}
				else
				{
					currentTime+=Shortest.remainingTime;
					Shortest.remainingTime=0;
				}
				
				if(Shortest.remainingTime==0)
				{
					completed++;
					Shortest.completionTime=currentTime;
					Shortest.turnarroundTime=Shortest.completionTime-Shortest.arrivalTime;
					Shortest.waitingTime=Shortest.turnarroundTime-Shortest.burstTime;
				}
			}
			display(ProcessQueue);
		}
		
		public static void Prioritynonpreemptive(Process Processes[])
		{
			System.out.println("\nThis is Priority Non preemptive Scheduling");
			int currentTime=0;
			int completed=0;
			
			while(completed!=Processes.length)
			{
				Process selectedProcess=null;
				int highestPriority=Integer.MAX_VALUE;
				
				//finding process with highest priority among the arrived process upto the current time
				for(Process p:Processes)
				{
					if(p.arrivalTime<=currentTime && p.remainingTime>0)
					{
						if(p.priority<highestPriority)
						{
							highestPriority=p.priority;
							selectedProcess=p;
						}
						else if(p.priority==highestPriority &&p.arrivalTime<selectedProcess.arrivalTime)
						{
							highestPriority=p.priority;
							selectedProcess=p;
						}
					}
				}
				if(selectedProcess==null)
				{
					currentTime++;
				}
				else
				{
					selectedProcess.remainingTime=0;
					selectedProcess.waitingTime=currentTime-selectedProcess.arrivalTime;
					selectedProcess.completionTime=currentTime+selectedProcess.burstTime;
					currentTime=selectedProcess.completionTime;
					selectedProcess.turnarroundTime=selectedProcess.completionTime-selectedProcess.arrivalTime;
					completed++;
				}
				
			}
			display(Processes);
		}
		
		public static void RoundRobin(Process Processes[],int quantumTime)
		{
			System.out.println("\nThis is Round Robin Scheduling");
			int currentTime=0;
			int completed=0;
			Queue<Process> queue=new LinkedList<>();
			Process[] processesQueue=Arrays.copyOf(Processes,Processes.length);
			Arrays.sort(processesQueue,Comparator.comparingInt(p->p.arrivalTime));
			display(processesQueue);
			
			int i=0;
			while(completed<processesQueue.length)
			{
				
				//adding newly arrived processes to the queue
				while(i<processesQueue.length && processesQueue[i].arrivalTime<=currentTime)
				{
					queue.add(processesQueue[i]);
					i++;
				}
				
				if(queue.isEmpty())
				{
					currentTime++;
					continue;
				}
				
				Process current=queue.poll();//takes the value at the head/start of the queue
				int	execTime=Math.min(quantumTime, current.remainingTime);
				current.remainingTime-=execTime;
				currentTime+=execTime;
				
				if(current.remainingTime==0)
				{
					completed++;
					current.completionTime=currentTime;
					current.turnarroundTime=current.completionTime-current.arrivalTime;
					current.waitingTime=current.turnarroundTime-current.burstTime;
				}
				else
				{
					while(i<processesQueue.length && processesQueue[i].arrivalTime<=currentTime)
					{
						queue.add(processesQueue[i]);
						i++;
					}
					queue.add(current);
				}
			}
			display(processesQueue);
		
		}

}
