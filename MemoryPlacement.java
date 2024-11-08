import java.util.Arrays;

public class MemoryPlacement {

	public static void main(String[] args) {
		System.out.println("Hello this is the memory placement program");
		int[] blocks={5,10,20};
		int[] processes={10,20,30};
		int[] blockAllocationToProcess=new int[processes.length];
		Arrays.fill(blockAllocationToProcess, -1);
		//firstFit(processes,blockAllocationToProcess,blocks);
		//bestFit(processes,blockAllocationToProcess,blocks);
		//worstFit(processes,blockAllocationToProcess,blocks);
		nextFit(processes,blockAllocationToProcess,blocks);
		display(processes,blockAllocationToProcess,blocks);
	
	}
	
	
	public static void display(int[] processes, int[] blockAllocationToProcess, int[] blocks) {
	    System.out.println("No.\tProcess \tBlock no. Allocated\tBlock size");
	    for (int i = 0; i < blockAllocationToProcess.length; i++) {
	        if (blockAllocationToProcess[i] == -1) {
	            System.out.println((i + 1) + "\t" + processes[i] + "\t\t\tNot Allocated\tNull");
	        } else {
	            System.out.println((i + 1) + "\t" + processes[i] + "\t\t\t" + (blockAllocationToProcess[i] + 1) 
	                               + "\t\t" + blocks[blockAllocationToProcess[i]]);
	        }
	    }
	}


		
		public static void firstFit(int[] processes,int[] blockAllocationToProcess,int blocks[])
		{
			//create a copy of blocks to track the remaining memory
			int remainingmemory[]=blocks.clone();
			
			//processing each request one by one
			for(int i=0;i<processes.length;i++)
			{
				
				//try to find a block for current process
				for(int j=0;j<blocks.length;j++)
				{
					if(remainingmemory[j]>=processes[i])
					{
						blockAllocationToProcess[i]=j;
						remainingmemory[j]-=processes[i];
						break;
					}
				}
			}
		}
		
		
		public static void bestFit(int[] processes,int[] blockAllocationToProcess,int blocks[])
		{
			//create a copy of blocks to track the remaining memory
			int remainingmemory[]=blocks.clone();
			int i,j;
			//processing each request one by one
			for(i=0;i<processes.length;i++)
			{
				int lesswaste=Integer.MAX_VALUE;
				int selectedBlock=-1;
				//try to find a block for current process
				for(j=0;j<blocks.length;j++)
				{
					if(remainingmemory[j]>=processes[i])
					{
						if(remainingmemory[j]-processes[i]<lesswaste)
						{
							lesswaste=remainingmemory[j]-processes[i];
							selectedBlock=j;
						}
					}
				}
				if(selectedBlock!=-1)
				{
					blockAllocationToProcess[i]=selectedBlock;
					remainingmemory[selectedBlock]-=processes[i];
				}
			}
			}
		
		public static void worstFit(int[] processes,int[] blockAllocationToProcess,int blocks[])
		{
			//create a copy of blocks to track the remaining memory
			int remainingmemory[]=blocks.clone();
			int i,j;
			//processing each request one by one
			for(i=0;i<processes.length;i++)
			{
				int morewaste=Integer.MIN_VALUE;
				int selectedBlock=-1;
				//try to find a block for current process
				for(j=0;j<blocks.length;j++)
				{
					if(remainingmemory[j]>=processes[i])
					{
						if(remainingmemory[j]-processes[i]>morewaste)
						{
							morewaste=remainingmemory[j]-processes[i];
							selectedBlock=j;
						}
					}
				}
				
					if(selectedBlock!=-1)
					{
						blockAllocationToProcess[i]=selectedBlock;
						remainingmemory[selectedBlock]-=processes[i];
					}
			}
		}
		
		public static void nextFit(int[] processes,int[] blockAllocationToProcess,int blocks[])
		{
			//create a copy of blocks to track the remaining memory
			int tempBlocks[]=blocks.clone();
			int lastIndex=0;
			for(int i=0;i<processes.length;i++)
			{
				int count=0;
				int j=lastIndex;
				
				while(count<blocks.length)
				{
					if(tempBlocks[j]>=processes[i])
					{
						blockAllocationToProcess[i]=j;
						tempBlocks[j]-=processes[i];
						lastIndex=(j+1)%blocks.length;
						break;
					}
					j=(j+1)%blocks.length;
					count++;
				}
			}
			
		}
}
		

		

