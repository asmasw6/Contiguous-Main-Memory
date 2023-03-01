
package memory;

/**
 *
 * @author Asma
 */
public class Partition {
    
    private int firstInd;
    private String  statusName = "hole"; //default procees name
    private int sizePartition ; //block[i]
    
    public Partition(int sizePartition) {
        this.firstInd = 0;
        this.sizePartition = sizePartition;    
    }

    public Partition(int firstInd, int sizePartition) {
        this.firstInd = firstInd;
        this.sizePartition = sizePartition;//there will be reamaining 
        
    }

    public Partition( int sizePartition,  String nameProcess ) {
        this.firstInd = -1;
        this.statusName = nameProcess;
        this.sizePartition = sizePartition;
    }

    public int getFirstInd() {
        return firstInd;
    }

    public String getStatusName() {
        return statusName;
    }
     
    public int getSizePartition() {
        return sizePartition;
    }
    

    public void setFirstInd(int firstInd) {
        this.firstInd = firstInd;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setSizePartition(int sizePartition) {
        this.sizePartition = sizePartition;
    }
    

   

    
    
    
    
    
}
