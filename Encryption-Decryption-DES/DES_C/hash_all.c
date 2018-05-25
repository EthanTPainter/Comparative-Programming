/*
 * @author: Ethan Painter
 * Compiled w/: GCC
 * Last Updated: 5/24/2017
 * Originally Created: 10/23/2017
 */

#include <openssl/sha.h>
#include <openssl/md5.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <time.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/time.h>

void get_monotonic_time(struct timespec *ts){
      clock_gettime(CLOCK_MONOTONIC, ts);
}

double get_elapsed_time(struct timespec *before, struct timespec *after){
      double deltat_s  = after->tv_sec - before->tv_sec;
        double deltat_ns = after->tv_nsec - before->tv_nsec;
          return deltat_s + deltat_ns*1e-9;
}

int main(int argc, char *argv[])
{
    if(argc != 3)
	{
		printf("Wrong number of arguements supplied.\nPlease try again with the format ./a.out inputfile outputfile\n");
		exit(0);
	}	
    FILE *inFile, *outFile;
	inFile = fopen(argv[1], "r");
	outFile = fopen(argv[2], "w");

	if(inFile == NULL)
	{
		printf("Invalid input file. \n");
		exit(0);
	}
	if(outFile == NULL)
	{
		printf("Invalid output file. \n");
		exit(0);
    }
    //Start Clock
    struct timespec before, after;
    get_monotonic_time(&before); 


    int i;
	unsigned char rbuff[1];//make a buffer to read from file

    //Make buffer to store hash with size equal to DIGEST size

    //unsigned char wbuff[MD5_DIGEST_LENGTH];
    //unsigned char wbuff[SHA_DIGEST_LENGTH];
	//unsigned char wbuff[SHA224_DIGEST_LENGTH];
	//unsigned char wbuff[SHA256_DIGEST_LENGTH];
	//unsigned char wbuff[SHA384_DIGEST_LENGTH];
	unsigned char wbuff[SHA512_DIGEST_LENGTH];
    size_t block;
   
    //Create CTX
    //MD5_CTX c;
    //SHA_CTX c;
    //SHA256_CTX c;
    SHA512_CTX c;

    //Init Different has functions
    //MD5_Init(&c);
	//SHA1_Init(&c);
	//SHA224_Init(&c);
	//SHA256_Init(&c);
	//SHA384_Init(&c);
	SHA512_Init(&c);

    //Update hash functions
    while( block = fread(rbuff, 1, 1, inFile) )//Read from inFile
    {
        //Update C with rbuff
        
	    //MD5_Update(&c,rbuff,1);
        //SHA1_Update(&c,rbuff,1);
	    //SHA224_Update(&c,rbuff,1);
	    //SHA256_Update(&c,rbuff,1);
	    //SHA384_Update(&c,rbuff,1);
	    SHA512_Update(&c,rbuff,1);
    
    }
   
    //Finilize hash and store in wbuff
    //MD5_Final(wbuff,&c);
    //SHA1_Final(wbuff,&c);
    //SHA224_Final(wbuff,&c);
    //SHA256_Final(wbuff,&c);
    //SHA384_Final(wbuff,&c);
    SHA512_Final(wbuff,&c);

    fwrite(wbuff, 1, sizeof(wbuff), outFile);//Write wbuff to outfile

    get_monotonic_time(&after);
    printf("deltaT = %e s\n", get_elapsed_time(&before,&after));

	if(fclose(inFile) != 0 || fclose(outFile) != 0)
	{
		printf("Error! A file was not closed\n");
		exit(0);
    }
}
