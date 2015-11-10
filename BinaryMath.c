#include <stdio.h>
#include <stdlib.h>
#include <limits.h>


//this works
//takes the two's complement
int twoComp(int a) {
	int b = ~a;
	int bitmask=1;
	//bool yes = b & bitmask;
	while (b & bitmask) {
		b = b ^ bitmask;
		bitmask <<= 1;
	}
	b=b ^ bitmask;
	return b;
}

//Takes the sum of the things 
int Subtract(int x, int y) {

	//Take two's complement
	int y2 = twoComp(y);

	
	int toCarry;

	while (y2 != 0) {
		toCarry = x & y2;
		x=x^y2;
		y2=toCarry << 1;
	}
	//printf("%d\n", x);
	return x;
}


//tests and gets the thing to run 
void main() {
	int i=0;
	int diff=0;
	int random1=0;
	int random2=0;
	int subtracted=0;
	for(i=0; i<1000; i++) {
		int random1 = rand() % (INT_MAX/2 + 1);
		int random2 = rand() % (INT_MAX/2 + 1);
		//printf("%d\n", random1);
		//printf("%d\n", random2);
		int subtracted = Subtract(random1, random2);
		//printf("Subtracted using function: %d\n", subtracted);
		int diff = random1 - random2;
		//printf("Real Difference: %d\n", diff);
		if (subtracted == diff) {
			printf("WOO! It worked! \n");
			
		} else {
			printf("You done fucked up. \n");
			break;
		}
		//return subtracted;
	}
	printf("I AM DONE AND I AM WORKED! \n");
	//return subtracted;

}