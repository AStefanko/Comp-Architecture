import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class Cachesim2 {
	//String[][] memory;
		//String[][] cache;
		static ArrayList<ArrayList<Block2>> cache = new ArrayList<ArrayList<Block2>>();
		
		//these things are the things from command line
		static int asso;
		static int bSize;
		static String address00;
		
		String lOrS; //is this string load or store? 
		int acVal; //value of access in bytes
		static String toStore; //string to be stored 
		
		//Other things
		static int nSets;
		static AdData addr;
		static MainMemory memory;
		static Block2 bl;
		static int clock;
		static String oBits;
		


		public static void Cachesimz(String add, int cSize, String toStore) {
			addr= new AdData(add, bSize, asso, cSize);
			bl = new Block2(add, bSize, asso, cSize, toStore);
			oBits=bl.offBits;
			clock=0;
			
			
		}
		
		public static void nSets(int cSize) { //I make the number of sets
			int nBlocks = (cSize*1000)/bSize;
			int sets = nBlocks/asso;
			if(sets==0) {
				nSets=1;
			} else {
				nSets=sets;
			}
			
			System.out.println("nSets in nSets: " + nSets);
		}
		
		public static void cacheMaker(int sets, int cSize) { //I make the cache the right size
			for(int i=0; i<nSets; i++) {
				cache.add(new ArrayList<Block2>());
				for(int j=0; j<asso; j++) {
					Block2 toPut= new Block2("", bSize, asso, cSize, "");
					cache.get(i).add(toPut);
				}
			}
		}

		 
		
		public static String load(String address, int acVal, int cSize) {
			String[] bytes = new String[acVal];
			String fin="";
			int idx = addr.mIndex;
			for(int i=0; i<asso; i++) {
				if((cache.get(idx).get(i).tag).equals(addr.tagBits) && cache.get(idx).get(i).isValid()) {
					bytes = cache.get(idx).get(i).getDat(acVal, oBits);
					for(int j=0; j<bytes.length; j++) {
						fin+=bytes[j];
					}
					cache.get(idx).get(i).setClock(clock); //set the clock
					return "load" + addr + "hit" + fin;
				}
			}
			int memAdd = hexToDec(address);
			String[] stuffFromMem=memory.get(memAdd, acVal);
			String toPut="";
			for(int i=0; i<stuffFromMem.length; i++) {
				toPut+=stuffFromMem[i];
			}


			
			for(int a=0; a<asso; a++) {
				if(!cache.get(idx).get(a).isValid()) { //if not valid, there is room
					cache.get(idx).set(a, new Block2(address, bSize, asso, cSize, toPut));
					cache.get(idx).get(a).setClock(clock); //set the clock
					return "load " + address00 + " miss " + stuffFromMem.toString();
				}
			}
			ArrayList<Integer> clocks=new ArrayList<Integer>();
			for(int c=0; c<asso; c++) {
				clocks.add(cache.get(idx).get(c).getClock());
			}
			//this is where stuff gets evicted and replaced 
			int min = Collections.min(clocks);
			for(int d=0; d<asso; d++) {
				if(cache.get(idx).get(d).getClock()==min) {
					if(cache.get(idx).get(d).isDirty()) { //if the bit is dirty, load stuff into memory on a miss
						memory.toMem(memAdd, acVal, stuffFromMem);
					}
					cache.get(idx).set(d, new Block2(address, bSize, asso, cSize, toPut)); //update the stuff 
					cache.get(idx).get(d).setClock(clock); //set the clock
					return "load " + address00 + " miss " + stuffFromMem.toString();
				}
			}
			return fin;
		}
		
		public static String store(String address, int acVal, int cSize, String toStore) {
			int idx = addr.mIndex;
			int memAdd = hexToDec(address) - (hexToDec(address) % bSize);
			System.out.println("idx in store: " + idx);
			for(int i=0; i<asso; i++) {
				if((cache.get(idx).get(i).tag).equals(addr.tagBits) && cache.get(idx).get(i).isValid()) {
					cache.get(idx).set(i, new Block2(address, bSize, asso, cSize, toStore)); //if its in the cache just update what is stored in there
					cache.get(idx).get(i).setClock(clock);
					cache.get(idx).get(i).setDirty(); //now that it has been modified, set it to be dirty.
					return "store " + toStore + " hit"; 
				}
				else {
					continue;
				}
			}
			for(int i=0; i<asso; i++) {
				if(!cache.get(idx).get(i).isValid()) { //if something doesn't have a valid bit, then you store in and call it a day 
					
					cache.get(idx).set(i, new Block2(address, bSize, asso, cSize, toStore));
					
					String[] goesIntoMem = new String[toStore.length()];
					for(int k=0; k<toStore.length(); k++) {
						String temp="";
						temp+=toStore.charAt(k);
						goesIntoMem[k]=temp;
					}	
					cache.get(idx).get(i).setClock(clock); //set the clock
					cache.get(idx).get(i).setVal(); //set the valid to be 1
					System.out.println("store " + address+ " miss"); 
					return "store " + toStore + " miss"; 
				}
				//if the blocks are full and there is no room and something must be evicted
				ArrayList<Integer> clocks=new ArrayList<Integer>();
				for(int c=0; c<asso; c++) {
					clocks.add(cache.get(idx).get(c).getClock());
				}
				//this is where stuff gets evicted and replaced 
				String[] stufftoMem = new String[toStore.length()];
				int min = Collections.min(clocks);
				for(int d=0; d<asso; d++) {
					if(cache.get(idx).get(d).getClock()==min) {
						if(cache.get(idx).get(d).isDirty()) { //if the bit is dirty, load stuff into memory on a miss
							memory.toMem(memAdd, acVal, stufftoMem);
						}
						cache.get(idx).set(d, new Block2(address, bSize, asso, cSize, stufftoMem.toString())); //replace the blocks 
						cache.get(idx).get(d).setClock(clock);
						return "store " + address00 + " miss ";
					}
				}
			
			}
			return "";
			
			
		}

		
		public static void intoTheCache(int ad, String address, String[] data, int bSize, int cSize) {
			int idx = (ad/bSize) % nSets;
			for(int i=0; i<nSets; i++) {
				for(int j=0; j<asso; j++) {
					//I need to do something here with evictions and making it do the right thing 
					cache.get(idx).set(i, new Block2(address, bSize, asso, cSize, data.toString()));
				}
			}
		}
		public static void intoTheCache2(int idx, String address, String[] data, int bSize, int ass, int cSize) {
			for(int i=0; i<nSets; i++) {
				for(int j=0; j<ass; j++) {
					//I need to do something here with evictions and making it do the right thing 
					cache.get(idx).set(i, new Block2(address, bSize, ass, cSize, data.toString()));
				}
			}
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
		public static int toDecimal(String s) {
			int fin=0;
			for(int i=0; i<s.length(); i++){
				if(s.charAt(i)=='1') {
					fin+=Math.pow(2,  s.length()-1-i);
				}
			}
			return fin;
		}
		public static int hexToDec(String str) {
			String ops ="0123456789abcdef";
			int fin=0;
			for(int i=2; i<str.length(); i++) {
				char ch = str.charAt(i);
				int idx = ops.indexOf(ch);
				fin=16*fin+idx;
			}
			return fin;
		}

		
		//to fix: when you store you gotta show the address, not what you be storing 
		
		public static void main(String[] args) {
			
			int cacheSize=1;
			int asso=64;
			int bSize = 16;
			clock=0;
			memory = new MainMemory(Math.pow(2, 24)); //this makes the huge memory wowzas
			nSets(cacheSize);
			cacheMaker(nSets, cacheSize);
			
			String fileName="test2.txt";
			String line = null;
			String toStore="";
			try {
				FileReader fileR = new FileReader(fileName);
				BufferedReader buffR = new BufferedReader(fileR);
				while((line=buffR.readLine())!=null) {
					String[] vars = line.split(" ");
					String lOst=vars[0];
					String addresss=vars[1];
					address00=addresss;
					int byteAccess = Integer.parseInt(vars[2]);
					if(vars.length>3) {
						toStore=vars[3];
					} else {
						toStore="";
					}
					Cachesimz(addresss, cacheSize, toStore);
					if(lOst.equals("load")) {
						String finna = load(addresss, byteAccess, cacheSize);
					} else {
						String finna = store(addresss, byteAccess, cacheSize, toStore);
					}
					clock++;
					System.out.println(" ");
					System.out.println(" ");
					System.out.println(" ");
				}
				buffR.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("file is broken y'all");
			} catch(IOException e) {
				System.out.println("IOException brah");
			}

			
		}
}
