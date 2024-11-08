import java.util.*;

abstract class CommonUtils {
	protected int[][] result;
	protected char[] states;
	protected LinkedList<Integer> pagesReference;
	public int hits, faults;

	public CommonUtils() {
		hits = faults = 0;
		pagesReference = new LinkedList<>();
		int[] pr = {7,0,1,2,0,3,0,4,2,3,0,3,2,3};// page reference store in array
		int frames =4;// total no of frames for page allocation
		states=new char[pr.length];
		Arrays.fill(states, 'N');

		for (int i = 0; i < pr.length; i++) {
			pagesReference.add(pr[i]);// copying the array into the linked list
		}
		result = new int[frames][pr.length];
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result[x].length; y++) {
				result[x][y] = -1;
			}
		}
	}

	public abstract void performAlgorithm();

	public void display() {
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result[x].length; y++) {
				if(result[x][y]==-1)
				{
					System.out.print("  ");
				}
				else
				{
					System.out.print(result[x][y] + " ");
				}
			}
			System.out.println();
		}
		for(int i=0;i<states.length;i++)
		{
			System.out.print("--");
		}
		System.out.println();
		for(int i=0;i<states.length;i++)
		{
			System.out.print(states[i]+" ");
		}
		System.out.println("\n\nNo. of hits =" + hits);
		System.out.println("No. of faults =" + faults);
	}
}

class FIFO extends CommonUtils {
	Queue<Integer> numQueue;

	public FIFO() {
		super();
		numQueue = new LinkedList<>();
	}

	@Override
	public void performAlgorithm() {
		System.out.println("This is FIFO page replacement algorithm");
		int j = 0;
		while (!pagesReference.isEmpty()) {
			int num = pagesReference.removeFirst();
			boolean isFault = true;
			boolean isInserted = false;
			for (int x = 0; x < result.length; x++) {
				if (result[x][j] == -1) {
					isFault = true;
					faults++;
					result[x][j] = num;
					isInserted = true;
					states[j]='F';
					break;
				} else if (result[x][j] == num) {
					isFault = false;
					hits++;
					states[j]='H';
					break;
				}
			}
			if (isFault && isInserted) {
				numQueue.add(num);
			} else if (isFault) {
				faults++;
				states[j]='F';
				numQueue.add(num);
				int n = numQueue.remove();
				for (int x = 0; x < result.length; x++) {
					if (result[x][j] == n) {
						result[x][j] = num;
						break;
					}
				}
			}
			j++;
			if (j < result[0].length) {
				for (int x = 0; x < result.length; x++) {
					result[x][j] = result[x][j - 1];
				}
			}

		}

	}
}

class OPT extends CommonUtils {
	Queue<Integer> numQueue;

	public OPT() {
		super();
		numQueue = new LinkedList<>();
	}

	@Override
	public void performAlgorithm() {
		System.out.println("This is Optimal page replacement algorithm");
		int j=0;
		while(!pagesReference.isEmpty())
		{
			int num=pagesReference.removeFirst();
			boolean isFault=true;
			
			//check if page is already present in the memory
			for(int x=0;x<result.length;x++)
			{
				if(result[x][j]==num)
				{
					isFault=false;
					hits++;
					states[j]='H';
					break;
				}
			}
			if(isFault)
			{
				faults++;
				states[j]='F';
				//finding the empty frame
				boolean emptyFound=false;
				for(int x=0;x<result.length;x++)
				{
					if(result[x][j]==-1)
					{
						result[x][j]=num;
						emptyFound=true;
						break;
					}
				}
				
				//if no empty frame found find page to replace
				if(!emptyFound)
				{
					int farthest=-1;
					int replaceIndex=0;
				
					//for each frame
					for(int x=0;x<result.length;x++)
					{
						int currentPage=result[x][j];
						int nextOcurrence=Integer.MAX_VALUE;
						
						//finding next Occurence of the current page
						for(int future=0;future<pagesReference.size();future++)
						{
							if(pagesReference.get(future)==currentPage)
							{
								nextOcurrence=future;
								break;
							}
						}
						
						//update farthest page if this occurs later
						if(nextOcurrence>farthest)
						{
							farthest=nextOcurrence;
							replaceIndex=x;
						}
						
						//if page never occurs again ,replace it immediately
						if(nextOcurrence==Integer.MAX_VALUE)
						{
							replaceIndex=x;
							break;
						}
					}
					
					//replace the page now
					result[replaceIndex][j]=num;
				}
			}
			
			//copying current state to the next column
			j++;
			if(j<result[0].length)
			{
				for(int x=0;x<result.length;x++)
				{
					result[x][j]=result[x][j-1];
				}
			}
		}
		

	}
}
class LRU extends CommonUtils {
    ArrayList<Integer> pageOrder; // Tracks order of page access

    public LRU() {
        super();
        pageOrder = new ArrayList<>();
    }

    @Override
    public void performAlgorithm() {
        System.out.println("This is LRU page replacement algorithm");
        int j = 0;
        
        while (!pagesReference.isEmpty()) {
            int num = pagesReference.removeFirst();
            boolean isFault = true;
            
            // Check if page is already in memory
            for (int x = 0; x < result.length; x++) {
                if (result[x][j] == num) {
                    isFault = false;
                    hits++;
                    states[j] = 'H';
                    
                    // Update access order - remove and add to make it most recently used
                    pageOrder.remove(Integer.valueOf(num));
                    pageOrder.add(num);
                    break;
                }
            }
            
            if (isFault) {
                faults++;
                states[j] = 'F';
                
                // Try to find an empty frame first
                boolean emptyFound = false;
                for (int x = 0; x < result.length; x++) {
                    if (result[x][j] == -1) {
                        result[x][j] = num;
                        pageOrder.add(num);
                        emptyFound = true;
                        break;
                    }
                }
                
                // If no empty frame, replace least recently used page
                if (!emptyFound) {
                    int lruPage = pageOrder.get(0); // Get least recently used page
                    pageOrder.remove(0);            // Remove it from order list
                    pageOrder.add(num);            // Add new page as most recently used
                    
                    // Find and replace the LRU page in the frames
                    for (int x = 0; x < result.length; x++) {
                        if (result[x][j] == lruPage) {
                            result[x][j] = num;
                            break;
                        }
                    }
                }
            }
            
            // Copy current state to next column
            j++;
            if (j < result[0].length) {
                for (int x = 0; x < result.length; x++) {
                    result[x][j] = result[x][j - 1];
                }
            }
        }
    }
}

public class PageReplacement {

	public static void main(String[] args) {
		System.out.println("Page Replacement algorithm");
//		FIFO f = new FIFO();
//		f.performAlgorithm();
//		f.display();
//		OPT o=new OPT();
//		o.performAlgorithm();
//		o.display();
		LRU l=new LRU();
		l.performAlgorithm();
		l.display();
	}

}
