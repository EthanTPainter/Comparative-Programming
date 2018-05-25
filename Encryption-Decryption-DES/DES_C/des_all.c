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

//Added for time recording
//Used to implement with specific sizes of files
//To visualize how different file size impacts speed of encryption/decryption
void get_monotonic_time(struct timespec *ts){
      clock_gettime(CLOCK_MONOTONIC, ts);
}

//Get elapsed time or time passed during execution of DES type
double get_elapsed_time(struct timespec *before, struct timespec *after){
      double deltat_s  = after->tv_sec - before->tv_sec;
        double deltat_ns = after->tv_nsec - before->tv_nsec;
          return deltat_s + deltat_ns*1e-9;
}

/*isHex: A function to make sure a given string only contains hex values of  the correct length and convert string hex to hex array
 *
 * 0-9 (Ascii 48 - 57)
 * A-F (Ascii 65 - 70)
 * a-f (Ascii 97 - 102)
 *
 *Make sure Key and IV inputs are 16 hex char's long
 *Convert string hex into hex array using ascii offset
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
       else//when a 2 hex digits is found stored in output array
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

/* Main execution of program */
int main(int argc, char *argv[])
{
    //Incorrect Number of args
    if(argc != 5)
	{
		printf("Wrong number of arguements supplied.\nPlease try again with the format ./a.out iv key inputfile outputfile\n");
		exit(0);
	}	

	FILE *inFile, *outFile;
	inFile = fopen(argv[3], "r");
	outFile = fopen(argv[4], "w");

    //input file is not found (null)
	if(inFile == NULL)
	{
		printf("Invalid input file. \n");
		exit(0);
	}
	//output file is not found (null)
	if(outFile == NULL)
	{
		printf("Invalid output file. \n");
		exit(0);
	}

    //Mode of op (ENC or DEC) based on input and output files
     int len = strlen(argv[3]), len2 = strlen(argv[4]);
     int MODE = -1;
     if( argv[3][len-4] == '.' && argv[3][len-3] == 't' && argv[3][len-2] == 'x' && argv[3][len-1] == 't' && argv[4][len2-4] == '.' && argv[4][len2-3] == 'd' && argv[4][len2-2] == 'e' && argv[4][len2-1] == 's') { MODE = ENC; }
     
     if( argv[3][len-4] == '.' && argv[3][len-3] == 'd' && argv[3][len-2] == 'e' && argv[3][len-1] == 's' && argv[4][len2-4] == '.' && argv[4][len2-3] == 't' && argv[4][len2-2] == 'x' && argv[4][len2-1] == 't') { MODE = DEC; }

	//init clock
    struct timespec before, after;
    get_monotonic_time(&before); 

	//Make key by changing the string key into hex array
	int k;
	
	static unsigned char cbc_key[8];
    makeHex(argv[2],cbc_key);
	des_key_schedule key;
	
    if ((k = des_set_key_checked(&cbc_key,key)) != 0)
		printf("\nkey error\n");


    static unsigned char init[8]; 
    makeHex(argv[1], init);

    size_t blockSize;

    unsigned char inBuff[8];
    unsigned char outBuff[8]; 
    
    //Encrypt and Decrypt
    if(MODE == ENC || MODE == DEC)
    {
        while(blockSize = fread( inBuff, 1, 8, inFile))//read from infile to inbuff
        {
            //ENC/DEC inbuff and put result in outbuff
            
            DES_ecb_encrypt(&inBuff, &outBuff, &key, MODE);
            //DES_cfb_encrypt(inBuff, outBuff, 64, 8, &key, &init, MODE);   
            //DES_ofb_encrypt(inBuff, outBuff, 64, 8, &key, &init );

            fwrite(outBuff, 1, 8, outFile); //write outbuff to outfile
        }   
    }

    //Stop clock and print time
    get_monotonic_time(&after);
   
    printf("deltaT = %e s\n", get_elapsed_time(&before,&after));

    //Close files
	if(fclose(inFile) != 0 || fclose(outFile) != 0)
	{
		printf("Error! A file was not closed\n");
		exit(0);
	}
}
