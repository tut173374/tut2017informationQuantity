package s4.b173374; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/

public class InformationEstimator implements InformationEstimatorInterface{
    // Code to tet, *warning: This code condtains intentional problem*
    boolean targetReady = false;
    boolean spaceReady = false;
    byte [] myTarget; // data to compute its information quantity
    byte [] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency

    // IQ: information quantity for a count,  -log2(count/sizeof(space))
    double iq(int freq) {
	return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    public void setTarget(byte [] target) {
	if(target != null){
	    myTarget = target;
	    if(target.length>0)
		targetReady = true;
	}
    }
    public void setSpace(byte []space) {
	if(space != null){
	    myFrequencer = new Frequencer();
	    mySpace = space; myFrequencer.setSpace(space);
	    spaceReady = true;
	}
    }

    public double estimation(){
	if(targetReady == false) return (double) 0.0;
	if(spaceReady == false) return Double.MAX_VALUE;

	myFrequencer.setTarget(myTarget);

	double [] prefixEstimation = new double[myTarget.length+1];

	prefixEstimation[0] = (double) 0.0; //IE("") = 0.0; 

	for(int n=1;n<=myTarget.length;n++) {
	    double value = Double.MAX_VALUE;
	    for(int start=n-1;start>=0;start--) {
		int freq = myFrequencer.subByteFrequency(start, n);
		if(freq != 0) {
		    // update "value" if it is needed.
		    double value1 = prefixEstimation[start]+iq(freq);
		    if(value>value1) value = value1;
		} else {
		    break; 
		}
	    }
	    prefixEstimation[n]=value;
	}
	return prefixEstimation[myTarget.length];

    }

    public static void main(String[] args) {
	InformationEstimator myObject;
	double value;
	myObject = new InformationEstimator();
	myObject.setSpace("3210321001230123".getBytes());
	myObject.setTarget("0".getBytes());
	value = myObject.estimation();
	System.out.println(">0 "+value);
	myObject.setTarget("01".getBytes());
	value = myObject.estimation();
	System.out.println(">01 "+value);
	myObject.setTarget("0123".getBytes());
	value = myObject.estimation();
	System.out.println(">0123 "+value);
	myObject.setTarget("00".getBytes());
	value = myObject.estimation();
	System.out.println(">00 "+value);
    }
}
				  
			       

	
    
