
package memory;

import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Asma & Kawthar 
 */
public class Memory {

   
    public static int memorySize ;   
    static ArrayList <Partition> partition = new ArrayList<>();
    static int numPartionMem = 5+ (int)(Math.random() * ((5) +1));
    
   
    public static void main(String[] args) {
    
        //read memory size from the user
        Scanner input = new Scanner(System.in);
        System.out.print("./allocator ");
        memorySize = input.nextInt();
        //chooce any partitin need ===!//RANDOMALY//!====
        //in the program we choose random num partition 5<= num <= 10
        int [] blo =divideBlock(memorySize, numPartionMem);
        int firstInd =0;
        //make partition
        for(int i=0 ; i<4 ; i++){
           partition.add(i, new Partition(firstInd , blo[i]));
           firstInd += partition.get(i).getSizePartition();
         }
            
        //display menue
        menue();
        while(true){
        
        //raed command from user & split them
        Scanner in = new Scanner(System.in);
        System.out.print("allocator>");
        String command = in.nextLine();
        String [] Comm = command.split(" ");
        
        if(Comm[0].equalsIgnoreCase("RQ")){//-----Request-----
                int indFit = -1 ;
                String processName =  Comm[1];
                int ByteSize = Integer.parseInt(Comm[2]);
                String Strategy = Comm[3];
                //make object 
                Partition newPrt = new Partition(ByteSize, processName);
                //analysing fit
                if(Strategy.equalsIgnoreCase("B")){//if Best Fit
                    indFit = insertByBestFit( newPrt);
                }else if(Strategy.equalsIgnoreCase("W")){//if Worst Fit
                     indFit = insertByWorstFit(newPrt);
                }else if(Strategy.equalsIgnoreCase("F")){//if First Fit
                    indFit =  insertByFirstFit(newPrt);            
                 }  
                
                if(indFit !=-1){
                    //---Allocation---
                    //Allocate
                    System.out.println("Succeded allocation");
                    //get the aporprate Fit object, take size, firstAdd
                    Partition tempPart = partition.get(indFit);
                    int remainSize = tempPart.getSizePartition();
                    int firstAdd = tempPart.getFirstInd();
                    //set First address
                    newPrt.setFirstInd(firstAdd);
                    partition.remove(indFit);
                    partition.add(indFit, newPrt);
                    remainSize -=  newPrt.getSizePartition();
                    if(remainSize!=0){
                        int newFirstAdd = firstAdd + newPrt.getSizePartition();                        
                        //new partition as hole
                        partition.add( indFit+1 ,new Partition(newFirstAdd, remainSize));                 
                }
                }else{
                    System.out.println("Failed Allocation");
                }
                
        
        }else if(Comm[0].equalsIgnoreCase("RL")){//-----Release-------
            
             ReleasePro(Comm[1]);
            }else if(Comm[0].equalsIgnoreCase("C")){//-----Compaction-----
                compaction();
                
            }else if(Comm[0].equalsIgnoreCase("STATE")){//-----State-----
                reportState();
                 
            }else if(Comm[0].equalsIgnoreCase("EXIT")){//-----Exit-----
                System.exit(0); 
            }
            
        
        }
        
    }
    
    
  
    public static void menue(){
        System.out.print("**------------------------------------------**\n");
        System.out.print("a. Request for a contiguous block of memory. --RQ--\n" +
                             "b. Release of a contiguous block of memory. --RL--\n" +
                             "c. Compact unused holes of memory into one single block. --C--\n" +
                             "d. Report the regions of free and allocated memory. --STATE--\n" +
                             "e. Exite from program. --EXIT--\n" );
        System.out.print("**------------------------------------------**\n");
     
    }
    
    public static int [] divideBlock(int size, int partition){
        Random random = new Random();            
        int[] container = new int[partition];

        while (size > 0)
        {
            container[random.nextInt(partition)]++;
            size--;
        }  
        return container;
    }
    
    
    
    public static int insertByFirstFit(Partition obj){
        int indFit = -1;
        //loop to find apporporate BLOCK to fit process
        for (int i = 0; i < partition.size(); i++) {
                if(obj.getSizePartition()<= partition.get(i).getSizePartition() 
                        && partition.get(i).getStatusName().equals("hole")){
                     
                        indFit = i;}
        }
                
        return indFit;
     
    }
    
    public static int insertByBestFit(Partition obj){
        int bestFistInd = -1;
       
        for (int i = 0; i < partition.size(); i++) {
            //find best index that is menimum fit block and greater than process size. 
             bestFistInd = -1;
            for (int j = 0; j < partition.size() ; j++) {
               if (obj.getSizePartition() <=partition.get(j).getSizePartition() && partition.get(j).getStatusName().equals("hole"))
                {
                    if(bestFistInd == -1)
                        bestFistInd = j;
                    else if(  partition.get(j).getSizePartition() < partition.get(bestFistInd).getSizePartition())
                        bestFistInd =j;
                 }
             }//end inside loop

        }//End outside loop
        
        return bestFistInd; 
  }
    
    
    public static int insertByWorstFit(Partition obj){
         int worstFistInd = -1;
       
        for (int i = 0; i < partition.size(); i++) {
            //find best index that is maximum fit block 
            worstFistInd = -1;
            for (int j = 0; j < partition.size() ; j++) {
                if (obj.getSizePartition() <partition.get(j).getSizePartition()&& partition.get(j).getStatusName().equals("hole"))
                {
                    if(worstFistInd == -1)
                        worstFistInd = j;
                    
                    else if(  partition.get(j).getSizePartition() > partition.get(worstFistInd).getSizePartition())
                        worstFistInd =j;
                 }
             }//end inside loop
 }
         return worstFistInd;
         
    
    }
    
    public static void compaction() {
        /* The functions which compacts holes. */
        int sumHoles=0 ;
        int tempAdd = 0;
       
         ArrayList <Partition> temp = new ArrayList<>(); //make temperory array list
        //sum all holes
        for (int i = 0; i < partition.size(); i++) {
            if(partition.get(i).getStatusName().equals("hole")){//check if hole or not 
                sumHoles += partition.get(i).getSizePartition();//sum the size of hole 
            }else{//if its process add it to temperory array list
                temp.add(partition.get(i));
            }
    }
        
            //remove all content in partition arrayList
            partition.removeAll(partition);
            
            //return object from temperory to partition arrayList
            for (int i = 0; i < temp.size(); i++) {
                partition.add(temp.get(i));
        }
         // arrange the adresses of partition after deletion
         
         for (int i = 0; i < partition.size(); i++) {
             if( !(tempAdd == partition.get(i).getSizePartition()) ){
                 partition.get(i).setFirstInd(tempAdd);
             }
             tempAdd = partition.get(i).getFirstInd() + partition.get(i).getSizePartition();
         }
         //finaly set new partition as big hole in the end
         partition.add(new Partition(tempAdd, sumHoles));
                   
    }
    
    public static void ReleasePro(String name){
        boolean notFound = true;
           //find the process need to release and change name to hole 
           for (int i = 0; i < partition.size(); i++) {
                    if(partition.get(i).getStatusName().equalsIgnoreCase(name)){
                        partition.get(i).setStatusName("hole");
                        notFound=false;}
           }
           if(notFound){
               System.out.println("The cannot be release, isn't found.");
           }
           
           //compact adjacjent holes
           for (int i = 0; i < partition.size()-1 ; ++i) {
               if(partition.get(i).getStatusName().equals("hole") &&
                   partition.get(i+1).getStatusName().equals("hole") ){
                   //get sum of tow adjacen parttion holes
                   int sum = partition.get(i).getSizePartition()+partition.get(i+1).getSizePartition();
                   //set as size for the first adjacent object
                   partition.get(i).setSizePartition(sum);
                   //remove the second adjacent object
                   partition.remove(i+1);
                   i--;
              }
          }
 }
        
        

    public static void reportState() {
       for (int i = 0; i < partition.size(); i++) {
             int FirstAdd = partition.get(i).getFirstInd();
             int LastAdd =  FirstAdd + partition.get(i).getSizePartition()-1;
             String ProcessName = partition.get(i).getStatusName();
             if(ProcessName.equals("hole"))
                 System.out.println("Addresses[" + FirstAdd + ":" + LastAdd + "] " + " Unused");
             else
                System.out.println("Addresses[" + FirstAdd + ":" + LastAdd + "] " + " Process " + ProcessName);
      
             
         }
        
    }
   
}


