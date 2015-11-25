
public class AdData {
	
	
	private int mValid;
	int mIndex; //this is the actual index, so akin to the row
	int nSets;
	String idxBits;
	String offBits;
	String tagBits;
	String addre;
	
	
	

	
	//bSize is block size in bytes
	//associativity is an int 
	//cSize is cache size in KB
	
	public AdData(String hexS, int bSize, int asso, int cSize) {
		int lenIndex=0;
		int lenOffset=0;
		int lenTag=0;
		addre=hexS;
		//Calculating index length
		int nBits=(bSize*asso);
		int toLog = (cSize/nBits)*1000;
		if(toLog==0) {
			lenIndex=0;
		} else {
			double toL= (double) toLog;
			double f1= Math.log(toL)/Math.log(2);

			lenIndex= (int) f1;
		}
		

		
		//Calculating offset
		double bS = (double) bSize;
		double first= Math.log(bS)/Math.log(2);
		lenOffset= (int) first;
		
		//Creating tag and tagbits 
		lenTag=Tag(lenIndex, lenOffset);	
		tagBits=tagMaker(lenIndex, lenOffset, lenTag);
		

		
		int hex2Dec=hexToDec(hexS); //getting dec representation of hex
		System.out.println("hex2Dec: " + hex2Dec);
		double l1 = Math.log(hex2Dec)/Math.log(2);
		int len = (int) l1; //calculating length of binary string
		String daBin=toBinary(hex2Dec, len); //getting binary representation
		if(daBin.isEmpty()) {
			daBin="000000000000000000000000";
		}
		System.out.println("daBin: " + daBin);
		

		int endIdx=lenTag;
		
		
		idxBits = daBin.substring(endIdx, endIdx+lenIndex); //bits of the index
		int endOff = lenTag+lenIndex;
		offBits=daBin.substring(endOff, endOff+lenOffset); //bit of the offBits 
		mIndex= toDecimal(idxBits);
		mValid=0;
		

	}
	
	public String tagMaker(int indLen, int off, int lenTag) {

		if(addre.length()==0) {
			return "";
		}
		String finna="";
		for(int j=2; j<2+lenTag; j++) {
			finna+=addre.charAt(j);
		}
		return finna;
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
	
	//Calculating the necessary things here 
	public void nSets(int cSize, int bSize, int ass) { //I make the number of sets
		int nBlocks = (cSize*1000)/bSize;
		int sets = nBlocks/ass;
		nSets=sets;
		//System.out.println("nSets in nSets: " + nSets);
	}
	
	public int Offset(int bSize) {
		double bS = (double) bSize;
		double first= Math.log(bS)/Math.log(2);
		return (int) first;
	}
	public int Index(int bSize, int ass, int cSize) {
		int setSize=(bSize*ass);
		System.out.println("setSize in Index: " + setSize);
		int toLog = (cSize*1000)/setSize;
		double toL= (double) toLog;
		double f1= Math.log(toL)/Math.log(2);
		//System.out.println("f1 in Index: " + f1);
		return (int) f1;
	}
	public int Tag(int idx, int offset) {
		return (24-idx-offset)/4;
	}

}
