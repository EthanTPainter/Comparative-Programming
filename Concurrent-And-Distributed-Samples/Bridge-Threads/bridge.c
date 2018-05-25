//Author: Ethan Painter
//epainte2
//G00915079
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

#define MAX_BR_CAP 5
#define CROSS_TIME 4
#define DIREC_PROB 0.7

/* Added additional definitions */
#define EAST 0
#define WEST 1

#define handle_err(s) do{perror(s); exit(EXIT_FAILURE);}while(0)

typedef struct _thread_argv
{
	int vid;
	int direc;
	int time_to_cross;
} thread_argv;

/**
 * Student may add necessary variables to the struct
 **/
typedef struct _bridge {
	int dept_idx;		//departure index
	int num_car;		//number of cars on bridge
	int curr_dir;		//current direction
	int dirWait[2];		//direction of waiting (East is [0] and West is [1])
	int safe;		//Whether the bridge is safe to cross
} bridge_t;

void bridge_init();
void bridge_destroy();
void dispatch(int n);
void *OneVehicle(void *argv);

void ArriveBridge(int vid, int direc);		//Default ARRIVE
void CrossBridge(int vid, int direc, int time_to_cross);	//Default CROSS
void ExitBridge(int vid, int direc);		//Default EXIT

pthread_t *threads = NULL;	/* Array to hold thread structs */
thread_argv *args = NULL;	/* Array to hold thread arguments */
int num_v = 30;			/* Total number of vehicles to be created */

bridge_t br;			/* Bridge struct shared by the vehicle threads*/

/* Additional variables */
pthread_mutex_t bridgeLock;	//Mutex or lock for bridge operations
pthread_cond_t dirArray[2];	//Direction Array - East is 0, West is 1

int eastWait[30];		//Array of waiting cars in east
int westWait[30];		//Array of waiting cars in west
int counterE = 0;		//Counter for East wait index
int counterW = 0;		//Counter for West wait index
int nextE = 0;			//Next East car waiting to get on bridge
int nextW = 0;			//Next West car waiting to get on bridge

int main(int argc, char *argv[])
{
	int sched_opt;
	int i;

	if(argc < 2)
	{
		printf("Usage: %s SCHED_OPT [SEED]\n", argv[0]);
		exit(EXIT_SUCCESS);
	}

	/* Process Arguments */
	sched_opt = atoi(argv[1]);
	if(argc == 3)
		srand((unsigned int)atoi(argv[2]));
	else
		srand((unsigned int)time(NULL));

	/* Allocate memory for thread structs and arguments */
	if((threads = (pthread_t *)malloc(sizeof(pthread_t) * num_v)) == NULL)
		handle_err("malloc() Failed for threads");
	if((args = (thread_argv *)malloc(sizeof(thread_argv) * num_v)) == NULL)
		handle_err("malloc() Failed for args");

	/* Init bridge struct */
	bridge_init();

	/* Create vehicle threads */
	switch(sched_opt)
	{
		case 1 : dispatch(5); break;
		case 2 : dispatch(10); break;
		case 3 : dispatch(30); break;
		default:
			fprintf(stderr, "Bad Schedule Option %d\n", sched_opt);
			exit(EXIT_FAILURE);
	}
	
	/* Join all the threads */
	for(i = 0; i < num_v; i++)
		pthread_join(threads[i], NULL);

	/* Clean up and exit */
	bridge_destroy();
	
	//printing & debugging
	printf("\nThis print statement is located at the end of main.\nUsed to show order in which vehicles arrvied.\nEast Array");
	for(int i = 0; i < counterE; i++){
		printf("\neastArray[%d] = %d", i, eastWait[i]); 	
	}
	
	printf("\n\nWest Aray");
	for(int i = 0; i < counterW; i++){
		printf("\nwestArray[%d] = %d", i, westWait[i]);
	}
	printf("\n");
	
	exit(EXIT_SUCCESS);
}

/**
 *	Create n vehicle threads for every 10 seconds until the total
 * 	number of vehicles reaches num_v
 *	Each thread handle is stored in the shared array - threads
 */
void dispatch(int n)
{
  int k, i;
  
	for(k = 0; k < num_v; k += n)
	{
		printf("Dispatching %d vehicles\n", n);

		for( i = k; i < k + n && i < num_v; i++)
		{
			/* The probability of direction 0 is DIREC_PROB */
			int direc = rand() % 1000 > DIREC_PROB * 1000 ? 0 : 1;

			args[i] = (thread_argv){i, direc, CROSS_TIME};
			if(pthread_create(threads + i, NULL, &OneVehicle, args + i) != 0)
				handle_err("pthread_create Failed");
		}
		
		printf("Sleep 10 seconds\n"); sleep(10);
	}
}

void *OneVehicle(void *argv)
{
	//Locks used within methods
	
	thread_argv *args = (thread_argv *)argv;
	
	/* VERSION 1.0 (Default)
 	 * Used in ArriveBridge, CrossBridge, and ExitBridge
 	 */
	
	ArriveBridge(args->vid, args->direc);
	CrossBridge(args->vid, args->direc, args->time_to_cross);
	ExitBridge(args->vid, args->direc);
	pthread_exit(0);
	
}

/**
 *	Students to complete the following functions
 */

void bridge_init()
{
	br.dept_idx = 0;	// Deparature Index
	br.curr_dir = 0;	// Current direction 
	br.num_car = 0;		// Number of cars on the bridge
	br.dirWait[0] = 0;	// Number of cars waiting in a specified direction (EAST)
	br.dirWait[1] = 0;	// number of cars waiting in a specified direction (WEST)
	br.safe = 0;

	//init of mutex and CV's
	pthread_mutex_init(&bridgeLock,NULL);

	pthread_cond_init(&dirArray[0],NULL);
	pthread_cond_init(&dirArray[1],NULL);
	
	return;
}

/* 
 * VERSION 1.0 (Default)
 * Vehicles enter bridge only when vheicles are currently traveling in the same direction
 * Vehicles travelling opposite to traffic on bridge must wait until waiting vehicles
 * in opposite direction are finished.
 */

/* After all cars are done traveling across, destroy bridge */
void bridge_destroy()
{
	return;
}

//Helper Function to check the bridge
//Return Values: 1 or 0
//If the function returns 1, bridge is safe to enter
//If the function returns 0, bridge is not safe (must wait in a FIFO)
int checkBridge(int vid, int direc)
{
	if((br.num_car == 0) && (direc == EAST) && (nextE < counterE) && (vid == eastWait[nextE])){
		//No Cars on bridge, Direction = EAST, vid = first waiting position in East array
		nextE++;		//increment nextE
		br.safe = 1;		//bridge is safe
		return 1;
	}
	else if((br.num_car == 0) && (direc == WEST) && (nextW < counterW) && (vid == westWait[nextW])){
		//No cars on bridge, Direction = WEST, next < size of count, vid = first waiting position in West array
		nextW++;
		br.safe = 1;
		return 1;
	}
	else if((br.num_car < MAX_BR_CAP) && (br.curr_dir == EAST) && (nextE < counterE) && (vid == eastWait[nextE])){
		//Number of cars on bridge less than limit and in same direction
		nextE++;
		br.safe = 1;		//bridge is safe
		return 1;
	}
	else if((br.num_car < MAX_BR_CAP) && (br.curr_dir == WEST) && (nextW < counterW) && (vid == westWait[nextW])){
		nextW++;
		br.safe = 1;
		return 1;
	}
	else{				//Must not be ok to enter/cross the bridge
		br.safe = 0;		//bridge is not safe
		return 0;
	}
}

void ArriveBridge(int vid, int direc)
{
	//Bridge is shared data, so lock bridge operations
	pthread_mutex_lock(&bridgeLock);
	
	//Add vid to array depending on direc
	if(direc == EAST){
		eastWait[counterE] = vid;
		counterE++;
	}
	else{
		westWait[counterW] = vid;
		counterW++;
	}

	//Check if safe
	if(checkBridge(vid, direc) == 0){		//Not safe to enter/cross the bridge (MUST WAIT)
		br.dirWait[direc]++;		//Increment array for waiting car/direction
		while(checkBridge(vid, direc) == 0){
			if(br.dirWait[EAST] > 0 && direc == EAST && vid != eastWait[nextE]){
				pthread_cond_signal(&dirArray[EAST]);
			}
			if(br.dirWait[WEST] > 0 && direc == WEST && vid != westWait[nextW]){
				pthread_cond_signal(&dirArray[WEST]);
			}
			//Send cond_wait signal based on which direction is current
			pthread_cond_wait(&dirArray[direc], &bridgeLock);
		}
		br.dirWait[direc]--;	//Decrement array for waiting car/direction
	}
	br.num_car++;			//Car proceeds to bridge
	br.curr_dir = direc;		//Sets direction to current direction
	
	if((br.dirWait[direc] > 0) && (br.num_car < MAX_BR_CAP)){
		pthread_cond_signal(&dirArray[direc]);
	}
	
	pthread_mutex_unlock(&bridgeLock);
	return;
}

void CrossBridge(int vid, int direc, int time_to_cross)
{
	pthread_mutex_lock(&bridgeLock);	//Lock because reference to bridge values

	fprintf(stderr, "vid=%d dir=%d starts crossing. Bridge num_car=%d curr_dir=%d\n", 
		vid, direc, br.num_car, br.curr_dir);
	
	pthread_mutex_unlock(&bridgeLock);	
	
	sleep(time_to_cross);
	return;
}

void ExitBridge(int vid, int direc)
{
	//Bridge is shared data, so lock bridge operations
	pthread_mutex_lock(&bridgeLock);
	
	br.num_car--;			//Car leaves the bridge
	br.dept_idx++;			//Increment Departure index

	//Print statement - Standard print
	fprintf(stderr,"vid=%d dir=%d exit with departure idx=%d\n",vid,direc,br.dept_idx);
	
	if(br.num_car > 0){
		pthread_cond_signal(&dirArray[direc]);
	}
	else{
		if(br.dirWait[1-direc] != 0){		//opposite direction
			pthread_cond_signal(&dirArray[1-direc]);
		}
		else{					//Go back to current direction
			pthread_cond_signal(&dirArray[direc]);
		}
	}
	
	//Debugging print statement
	//How many are left waiting in either direction?
	//fprintf(stderr,"Waiting East=%d, Waiting West=%d, direc=%d\n", br.dirWait[0], br.dirWait[1],direc);

	pthread_mutex_unlock(&bridgeLock);	
	return;
}

