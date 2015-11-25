
public class Block2 {
	String[] data;
	String address;
	String tag;
	int valid;
	
	int acVal;
	
	int offset;
	int assoc;
	
	int dirtyBit;
	int clock;
	

	String idxBits;
	String offBits;
	
	
	public Block2(String addres, int blSize, int asso, int cSize, String toStore) {
		int bSize=blSize;
		int lenIndex=0;
		int lenTag=0;
		address=addres;

		valid=0;
		bSize=blSize;
		int dataSize=0;
		if(toStore.length()>bSize) {
			dataSize=toStore.length();
		} else {
			dataSize=bSize;
		}
		data = new String[dataSize];
		for(int i=0; i<dataSize; i++) {
			data[i]="0";
		}
		tag="";
		
		//calculating index length
		int nBits=(blSize*asso);
		int toLog = (cSize/nBits)*1000;
		if(toLog==0) {
			lenIndex=0;
		} else {
			double toL= (double) toLog;
			double f1= Math.log(toL)/Math.log(2);
			lenIndex= (int) f1; 
		}
		

		
		//calculating offset
		double bS = (double) blSize;
		double first= Math.log(bS)/Math.log(2);
		offset= (int) first;
		
		lenTag=Tag(lenIndex, offset); //this only takes the characters, that's why it's divided into 4
		tag=tagMaker(lenIndex, offset, lenTag); //this tag is only made up of characters 

		
		
		if(!address.equals("")) {
			int hex2Dec=hexToDec(addres); //getting dec representation of hex
			//System.out.println("hex2Dec: " + hex2Dec);
			double l1 = Math.log(hex2Dec)/Math.log(2);
			int len = (int) l1; //calculating length of binary string
			String daBin=toBinary(hex2Dec, len); //getting binary representation
			if(daBin.isEmpty()) {
				daBin="000000000000000000000000";
			}

			int endIdx=lenTag;
			idxBits = daBin.substring(endIdx, endIdx+lenIndex); //bits of the index
			int endOff = (lenTag*4)+lenIndex-1;
			offBits=daBin.substring(endOff+1, daBin.length()); //bit of the offBits

		}
		
		
		if(!toStore.isEmpty()) {

			toStoreThing(toStore, toDecimal(offBits)); //this makes a string array of data for the block 		}
		}
	}
	
	public String tagMaker(int indLen, int off, int lenTag) {
		if(address.length()==0) {
			return "";
		}
		String finna="";
		for(int j=2; j<2+lenTag; j++) {
			finna+=address.charAt(j);
		}
		return finna;
	}
	
	public void toStoreThing(String toStore, int off) { //makes data what it should be. Array of strings from the toStore 
		int z=0;
		for(int i=off; i<toStore.length(); i++) {
			String temp="";
			temp+=toStore.charAt(z);

			data[i]=temp;
			z++;
		}
	}

	
	public int Tag(int idx, int offset) { //How big is the tag?
		return (24-idx-offset)/4;
	}
	
	public String[] getDat(int accessValue, String Obits) { //this pulls the info from data into a String[] chinga dinga 
		
		int cnt=0;
		int offset2 = toDecimal(Obits);

		String[] finna = new String[(offset2*2)+(accessValue*2)];
		int end = (offset2*2)+(accessValue*2);
		for(int i=(offset2*2); i<end; i++) {
			finna[cnt]=data[i];
			cnt++;
		}
		return finna;
	}
	
	public boolean isValid() {
		if(valid==1) {
			return true;
		}
		return false;
	}
	
	public void setVal() { //makes the valid bit 1
		valid=1;
	}
	
	public boolean isDirty() {
		if(dirtyBit==1) {
			return true;
		}
		return false;
	}
	
	public void setDirty() { //makes the valid bit 1
		dirtyBit=1;
	}
	
	
	public void setClock(int uclock) { //makes the valid bit 1
		clock=uclock;
	}
	public int getClock() { //makes the valid bit 1
		return clock;
	}

	
	public String toBinary(int n, int length) { //n is number, length is number of digits that will be in the binary
		String fin="";
		for(int i=0; i<length; i++) {
			if(n%2==1) {
				fin="1"+fin;
			}
			if(n%2==0) {
				fin="0"+fin;
			}
			n=n/2;
		}
		for(int j=0; j<(24-length); j++){
			fin="0"+fin; //thinking of this as sign extending it 
		}
		return fin;
	}
	public int toDecimal(String s) {
		int fin=0;
		for(int i=0; i<s.length(); i++){
			if(s.charAt(i)=='1') {
				fin+=Math.pow(2,  s.length()-1-i);
			}
		}
		return fin;
	}
	public int hexToDec(String str) {
		String ops ="0123456789abcdef";
		int fin=0;
		for(int i=2; i<str.length(); i++) {
			char ch = str.charAt(i);
			int idx = ops.indexOf(ch);
			fin=16*fin+idx;
		}
		return fin;
	}
	
}
