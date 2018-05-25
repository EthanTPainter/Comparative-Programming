/*
 * @author: Ethan Painter
 * Compiled w/: GCC
 * Last Updated: 5/24/2018
 * Originally Created: 10/23/2017
 */

#include <openssl/des.h>
#include <time.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>
#include <stdio.h>

#define ENC 1
#define DEC 0

void get_monotonic_time(struct timespec *ts){
      clock_gettime(CLOCK_MONOTONIC, ts);
}

double get_elapsed_time(struct timespec *before, struct timespec *after){
      double deltat_s  = after->tv_sec - before->tv_sec;
        double deltat_ns = after->tv_nsec - before->tv_nsec;
          return deltat_s + deltat_ns*1e-9;
}

/*isHex: A function to make sure a given string only contains hex values of  the correct length and convert string hex to hex array
 *
 *Class: 0-9 (Ascii 48 - 57)
 *Class: A-F (Ascii 65 - 70)
 *Class: a-f (Ascii 97 - 102)
 *
 *Make sure Key and IV inputs are 16 hex char's long
 *Convert string hex into hex array using ascii offset.
 */
void makeHex(char *input, char *output)
{
   int i = 0, c = (int)input[i];
   unsigned char hexChar = 0;

   while(c != 0)//loop until null terminator
   {
       if(c > 47 && c < 58) hexChar += c - 48;
       else if(c > 64 && c < 71) hexChar += c - 55;
       else if(c > 96 && c < 103) hexChar += c - 87;
       else //non valid char 
       {
            printf("Error: Invalid Key IV. Only use hex to represent the Key and IV.\n");
	        exit(0);
       }

       if(i%2 == 0) hexChar <<= 4;//shift first hex char to add second 
       else// when a group of 2 hex digits is found store in output array
       {
           output[(int)i/2] = hexChar;//output array is half the size of input 
           hexChar = 0;
       }
       c = (int)input[++i];
   }
   if(i > 16)//Make sure Key and IV are 8 bytes long
   {
		printf("Error: Key and IV should both be 16 hex digits.\n");
		exit(0);
   }

}
int main(int argc, char *argv[])
{
    if(argc != 5)
	{
		printf("Wrong number of arguements supplied.\nPlease try again with the format ./a.out iv key inputfile outputfile\n");
		exit(0);
	}	
	FILE *inFile, *outFile;
	inFile = fopen(argv[3], "r");
	outFile = fopen(argv[4], "w");

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
     int len = strlen(argv[3]), len2 = strlen(argv[4]);
     int MODE = -1;
     if( argv[3][len-4] == '.' && argv[3][len-3] == 't' && argv[3][len-2] == 'x' && argv[3][len-1] == 't' && argv[4][len2-4] == '.' && argv[4][len2-3] == 'd' && argv[4][len2-2] == 'e' && argv[4][len2-1] == 's') { MODE = ENC; }
     
     if( argv[3][len-4] == '.' && argv[3][len-3] == 'd' && argv[3][len-2] == 'e' && argv[3][len-1] == 's' && argv[4][len2-4] == '.' && argv[4][len2-3] == 't' && argv[4][len2-2] == 'x' && argv[4][len2-1] == 't') { MODE = DEC; }

    struct timespec before, after;
    get_monotonic_time(&before); 
	
	int k;
	
	static unsigned char cbc_key[8];
    makeHex(argv[2],cbc_key);
	des_key_schedule key;
    
	
    if ((k = des_set_key_checked(&cbc_key,key)) != 0)
		printf("\nkey error\n");



    long readBlock[2], block[2], initBlock[2];//blocks for chaining cbc using ecb

    static unsigned char init[8];
    makeHex(argv[1], init);
    memcpy(initBlock, init, 8);

    size_t blockSize;

    unsigned char readBlockB[8];
    if(MODE == ENC)
    {
        //read from input file
        while(blockSize = fread(readBlockB, 1, 8, inFile))
        {
            //if block is short fill it with spaces
            if(blockSize != 8)
            {
                while(blockSize < 8)
                {
                    readBlockB[blockSize++] = 32;
                }
            }
            
            memcpy(readBlock, readBlockB, 8);//copyString into block

            //XOR current data with either previous data or IV if no previous
            block[0] = readBlock[0] ^ initBlock[0];
            block[1] = readBlock[1] ^ initBlock[1];
        
            des_encrypt1(block,key,ENC);//encrypt block
        
            fwrite(block, 1, 8, outFile);//write block to output file

            //store previous block for next inpurt XOR
            initBlock[0] = block[0];
            initBlock[1] = block[1];

        }   


    }
    else if(MODE == DEC)
    {
        long cipherBlock[2];
        while(blockSize = fread(readBlock, 1, 8, inFile))//read input file
        {
            //store cipher text for next block 
            cipherBlock[0] = readBlock[0];
            cipherBlock[1] = readBlock[1];

            des_encrypt1(readBlock,key,DEC);//Decrypt cipher text 
        
            //XOR decrypted cipher text  with previous cypher text or IV
            block[0] = readBlock[0] ^ initBlock[0];
            block[1] = readBlock[1] ^ initBlock[1];
            fwrite(block, 1, 8, outFile);


            //Store cipher text block for XOR
            initBlock[0] = cipherBlock[0];
            initBlock[1] = cipherBlock[1];

        }   


    }
    get_monotonic_time(&after);
   
    printf("deltaT = %e s\n", get_elapsed_time(&before,&after));
	if(fclose(inFile) != 0 || fclose(outFile) != 0)
	{
		printf("Error! A file was not closed\n");
		exit(0);
	}
}


