#include <stdio.h>
#include <stdlib.h>
#include <string.h>


struct staticNode {
	char* First;
	char* Last;
	char* StreetAd;
	char* City;
	char* Phone;
	struct staticNode *next;
}*head=NULL;;


//struct staticNode* head=NULL;
FILE *input_f;

//This function creates the nodes fron the input file
//It eliminates the trailing characters and the stuff in city that isn't the city
void addNode1(char* fn, char* ln, char* cty) {
	struct staticNode *prev, *temp, *curr;
	temp=(struct staticNode *)malloc(sizeof(struct staticNode));
	int i=0;
	for(i=0; i<strlen(cty); i++) {
		if (isdigit(cty[i])) {
			cty[i-4]='\0';
		}
	} 
	int j=0;
	for(j=0; j<strlen(fn); j++) {
		if ((fn[j])=='\n' || (fn[j])==' ') {
			fn[j]='\0';
		}
	}
	int k=0;
	for(k=0; k<strlen(ln); k++) {
		if ((ln[k])=='\n' || (ln[k])==' ') {
			ln[k]='\0';
		}
	}	
	temp->Last=(char*)malloc(60*sizeof(char));
	temp->First=(char*)malloc(60*sizeof(char));
	temp->City=(char*)malloc(60*sizeof(char));

	strcpy(temp->First, fn);	//strcpy(temp->First, fn);
	strcpy(temp->Last, ln);
	strcpy(temp->City, cty);
	temp->next=NULL;
	if (head==NULL) {
		//printf("Head is null\n");
		head=temp;
	//	printf("Head is now temp: %p\n", head);
		prev=temp;
	} else {
		curr=head;
		while(curr->next!=NULL) {
			curr=curr->next;
		}
	//	printf("Head is now in else: %p\n", head);
		curr->next=temp;
	//	printf("temp is now in else: %p\n", temp);
		temp->next=NULL;
	}
	//printf("%s  %s  %s\n", temp->First, temp->Last, temp->City);

}

//This function prints everything
void printNode() {
	struct staticNode *temp4;
	//printf("head in printnde is %p\n", head);

	//printf("temp in printnde is %p\n", temp4);
	//printf("Print node\n");

	for(temp4=head; temp4!=NULL; temp4=temp4->next) {
		//printf("%s  %s  %s\n", head->First, head->Last, head->City);
		printf("%s  %s  %s\n", temp4->First, temp4->Last, temp4->City);
	}
}

//This swaps nodes in the function
//used in the sort function
void swap(struct staticNode* one, struct staticNode* two) {
	//printf("%s\n", "swap");

	char* aTemp;
	char* bTemp;
	char* cTemp;

	aTemp=(char*)malloc(60*sizeof(char));
	bTemp=(char*)malloc(60*sizeof(char));
	cTemp=(char*)malloc(60*sizeof(char));

	aTemp=one->First;	//strcpy(temp->First, fn);
	bTemp=one->Last;
	cTemp=one->City;

	one->First=two->First;
	one->Last=two->Last;
	one->City=two->City;

	two->First=aTemp;
	two->Last=bTemp;
	two->City=cTemp;

}

//This function sorts things through one iteration
void sort() {
	//printf("%s\n", "sort");
	struct staticNode *curr, *prev; //gettin
	struct staticNode *temp2=(struct staticNode *)malloc(sizeof(struct staticNode));
	temp2=head;
	if(head==NULL) {
		head=temp2;
	}
	while(temp2->next != NULL) {
		if ((strcmp(temp2->City, temp2->next->City) > 0)) {
			swap(temp2, temp2->next);
		}
		temp2=temp2->next;
	}

}


//This is the function used for adding the extra entries
//adds that which is hard coded in to the main function
//Bug: It includes the entire city entry
//When I tried to use the same method as I used in addNode
//to cut the state and zip code, I kept getting Bus Error: 10
//I'm sorry
void staticAdd(char* fst, char* lst, char* st, char* cty, char* numb) {
	struct staticNode* prev1, *curr;

	struct staticNode* temp3=(struct staticNode *)malloc(sizeof(struct staticNode));
	

	temp3->First=fst;	
	temp3->Last=lst;
	temp3->City=cty;

	if (head==NULL) {
		head=temp3;
		prev1=temp3;
	} else {
		curr=head;
		while(curr->next !=NULL) {
			curr=curr->next;
		}
		curr->next=temp3;
		temp3->next=NULL;
	}

	//printf("%s  %s  %s\n", temp3->First, temp3->Last, temp3->City);

}

//Exactly what it sounds like. it deletes entries. 
void findAndDelete(char* newFirst, char* newLast) { //is this ok? 
	//For end of list
	//printf("%s\n", "delete");
	struct staticNode* currD, *prev;
	currD=(struct staticNode *)malloc(sizeof(struct staticNode));
	currD = head;

	while (currD->next!=NULL) {
		if ((strcmp(currD->First, newFirst) == 0) && strcmp(currD->Last, newLast) == 0) {
			if (currD==head) {
				head=head->next;
				free(currD);
				break;
			//prev=head;
			//else if(currD->next->next==NULL) {
			//	free(currD->next->next);
			//	currD->next=NULL;
			}
		}

		else if ((strcmp(currD->next->First, newFirst) == 0) && strcmp(currD->next->Last, newLast) == 0) {
			prev=currD->next->next;
			free(currD->next);
			currD->next=prev;
			break;
		}
			
		currD=currD->next;
	}
}

//This is where the magic happens
//it iterates through the file and once it has read six files it puts 
//entries into the addNode function
//Then it adds and removes things 
//Then I call the sort function a million times
//Then i print it
//Then I close the file
main() {
	char line[200];
	input_f = fopen("input.txt", "r");
	//printf("%d\n", input_f);
	

	 
	char FirstName[100];
	char LastName[100];
	char Street[100];
	char City[100];
	char Number[100];
	int counter = 1;

	int i=0;
	while(!feof(input_f)) { 
		if (counter==6) {
			fgets(line, 100, input_f); 
			counter=1;
		}
		if (counter==1) {
			fgets(FirstName, 100, input_f);
		}
		if (counter==2) {
			fgets(LastName, 100, input_f);	
		}
		if (counter==3) {
			fgets(Street, 100, input_f);
		}
		if (counter==4) {
			fgets(City, 100, input_f);
			//Does this work? strtok returns a pointer I think
			//So it should?
		}
		if (counter==5) {
			fgets(Number, 100, input_f);
			
		}
		counter++;

		if (counter==6) {
			addNode1(FirstName, LastName, City);
		}

	}
	//printf("head here is %p\n", head);

	staticAdd("Pengfei", "Zheng", "123 Maple Street", "Durham NC 27705", "(919) 660-5555");
	staticAdd("Alfredo", "Velasco", "426 Walnut Drive", "Durham NC 27708", "(919) 660-5056");
	staticAdd("Ben", "Lee", "789 Chestnut St", "Durham NC 27708", "(919) 660-5057");
	findAndDelete("Pengfei", "Zheng");
	findAndDelete("Alfredo", "Velasco");
	//printNode();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	sort();
	printNode();

	

	//freeing errythang

	fclose(input_f);
}