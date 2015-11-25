
public class MainMemory {
String[] mMem;
	
	public MainMemory(double adSize) {
		int adS= (int) adSize;
		mMem=new String[adS];
		for(int i=0; i<mMem.length; i++) {
			mMem[i]="0";
		}
	}
	public void toMem(int begin, int acVal, String[] stuff) {
		for(int i=0; i<acVal; i++) {
			mMem[begin]=stuff[begin];
			begin++;
		}
	}
	public String[] get(int add, int acVal) {
		String[] finna = new String[acVal*2];
		System.arraycopy(mMem, add, finna, 0, acVal*2);
		return finna;
	}

}
