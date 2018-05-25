Files included with Descriptions:

	des_ecb.c : 
		DES cbc using DES ecb. 
		
		-Method called makeHex converts string input into a hex array, which is used for the Key and IV. 
		It does this by using the ascii values of each char to convert it to hex. This method also has 
		error checking to make sure the string Key and IV only contain hex and also that they are the 
		appropriate length.

		-Main takes in 5 arguments in the form:

        ./a.out iv key inputfile outputfile

		Process in Main:
		Open all the files, 
		Check if user wants to encrypt/decrypt 
		Start the clock 
		Make the key and IV perform the encrypt/decrypt,
		End the timer
		Close the files. 

		-Repeated in other files(tempsha1.c and tempdes2.c)

		-In this file the encryption is done though cascading XOR with the use of an IV/ecb block.

		-Decryption is done through cascading XOR of the decrypted block with IV/previous cipher text.

	des_all.c : 
		-Similar to the des.c file but made using openssl functions.

	hash_all.c : 
		-Implementation of MD5 & SHA1 - SHA512 using openssl functions

		-I get command line arguments and open an input/output file like the other programs but this time I implement 
		the hash functions using the init->update->final with the appropriate CTX and DIGEST lengths.
