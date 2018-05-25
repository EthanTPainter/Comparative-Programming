/*
 * @author: Ethan Painter 
 * Compiled: Java 1.8
 * Last Updated: 5/24/2018
 * Originally Created: 10/23/2017
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import javax.crypto.spec.IvParameterSpec;

public class SymmetricEnc {
	private SecretKeySpec secretKey;
	private Cipher cipher;
	private byte[] IV;
	private int NUM_OF_BYTES;
	private File output;
	private String setting;
	private MessageDigest md;
	private int mode = -1;
	
	public SymmetricEnc(String secret, int length, String algorithm, String IV,File output,String setting, int option)
			throws UnsupportedEncodingException, 
					      NoSuchAlgorithmException, 
					      NoSuchPaddingException {
 		//if the output == 1 then we are hashing 
 		//else we are encryting
		this.output = output;

		//meant to keep track of the setting
		this.setting = setting.toUpperCase();
		
		if(option == 1){
			//initialize the hashing funcitonality
			//using the openssl libs
			md = MessageDigest.getInstance(setting);
		}else{
			byte[] key = new byte[length];

			//grab theb bytes of the key and IV and assign them to their respective byte-arrays
			if(secret.length() < 16 || IV.length() < 16){
				echo("Key and IV must be 16 characters in hex format");
				System.exit(0);
			}	
			
			//turn the key and iv into bytes
			key = fixSecret(secret, secret.length(),0);
			this.IV = fixSecret(IV,IV.length(),1);
			
			this.NUM_OF_BYTES = 8;

			//create the secret key with the given algorithm which is always "des"
			this.secretKey = new SecretKeySpec(key, algorithm);//create a secretKey with the

			//if the setting is CBC then initialize the 
			//cipher with the ECB algorithm
			//as ECB + XOR = CBC	
			if(this.setting.equalsIgnoreCase("CBC")){
				this.cipher = Cipher.getInstance(algorithm);
			}else if("CFB OFB".contains(this.setting.toUpperCase())){
				//else we are using CFB or OFB which both need to be initialized with
				//the number of bits passing per round
				this.cipher = Cipher.getInstance(algorithm+"/"+this.setting+"64/NoPadding");
			}else{
				//else we are using ECB
				this.cipher = Cipher.getInstance(algorithm+"/"+this.setting+"/NoPadding");
			}	
		}

	}

	/*
	 * This is meant to turn the given Key/IV
	 * into a byte array for then to be used in the algorithm to encrypt/decrypt
	 * the data
	*/
	private byte[] fixSecret(String s, int length,int IV) throws UnsupportedEncodingException {
		byte[] fixedBytes = new byte[8];

		int k , i;
		for(i = 0, k = 0; i<length; i+=2){
			fixedBytes[k++] = H2B(s.substring(i,i+2),IV);	
		}

		return fixedBytes;	

	}

	/*
	 * This function is meant to return a byte
	 * given the string of two characters
	 * Then use the integer class to convert the given String
	 * as the Byte value
	 *
	 * Throws an error given string is not in hexadecimal format 
	 */
	private byte H2B (String text,int IV){
		byte bytee = 0x00;
		try{
			bytee = (byte)Integer.decode("0x"+text).byteValue();
		}catch(NumberFormatException e){
			String er = "Key";
			if(IV==1){
				er="IV";
			}
			echo("For input of " + er+ " you did not enter in hexadecimal format\nError:"+e.getMessage());
			System.exit(0);
		}

		return bytee;
	}

	/*
	 * This function is used to use openssl's hashing library
	 * It reads the plaintext and hashes it
	*/
	public void hashFile(File f)throws InvalidKeyException, IOException, 
					      IllegalBlockSizeException, BadPaddingException ,
					      InvalidAlgorithmParameterException{
		System.out.println("Hashing file: " + f.getName());
		//read in plain-text
		FileInputStream encryptedInput = new FileInputStream(f.getName());
		byte[] encText = new byte[encryptedInput.available()];
		encryptedInput.read(encText);
		encryptedInput.close();
		//assign plaintext to the pt array
		byte [] pt = encText;
		//start the timer
		long startTime = System.nanoTime();	
		md.update(pt);	
		pt = md.digest();
		//calcualte time taken to hash plaintext
		long estimatedTime = System.nanoTime() - startTime;
		//write time taken to speed file
		this.writeSpeedToFile(-1, estimatedTime,f.getName());
		writeToFile(f,pt);
	}

	public void encryptFile(File f)
			throws InvalidKeyException, IOException, 
					      IllegalBlockSizeException, BadPaddingException{	
		System.out.println("Encrypting file: " + f.getName());
		this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
		int count = 0;

		//read in plain-text
		FileInputStream encryptedInput = new FileInputStream(f.getName());
		byte[] encText = new byte[encryptedInput.available()];
		encryptedInput.read(encText);
		encryptedInput.close();


		//store plaintext in pt
		byte[] pt = encText;	
		
		//pad plaintext if needed
		pt = this.padd(pt);

		//ct is used to keep track of the final cipher text to be written to the file
		byte[] ct = new byte[pt.length];
		//meant to keep track of the previous block of cipher text
		byte[] prev = new byte[this.NUM_OF_BYTES];

		//start the timer for my implementation of CBC encryption
		long estimatedTime = 0;
		long startTime = System.nanoTime();

		//keep on encrypting the data until we are at the end of the plaintext
		while(count*this.NUM_OF_BYTES < pt.length){
			byte[] xored = new byte[this.NUM_OF_BYTES];
			
			for(int i = 0; i<this.NUM_OF_BYTES;i++){
				if(count == 0){
					//xored[i] = xor PT[i] with IV[i]
					xored[i] = (byte)((byte)pt[i] ^ (byte)this.IV[i]);
				}else{
					//xored[i] = xor PT[i] with prev[i]
					xored[i] = (byte)((byte)pt[i+(count * this.NUM_OF_BYTES)] ^ (byte)prev[i]); 
				}
			}

			//prev = encrypt (xored[])
			if((count+1)*this.NUM_OF_BYTES>=pt.length){

				prev = this.cipher.doFinal(xored);
				estimatedTime = System.nanoTime() - startTime;
			}else{
				prev = this.cipher.update(xored);
			}

			//ct[current-indx] to ct[next-indx] = prev[0] to prev[7]
			for(int i = count * this.NUM_OF_BYTES; i < ((count + 1) * this.NUM_OF_BYTES); i++){
				ct[i] = prev[i%this.NUM_OF_BYTES];
			}
			
			count++;
		}
		//write the speed of this encryption algorithm to the speed file
		this.writeSpeedToFile(1,estimatedTime,f.getName());

		//write the cipher text to output file
		writeToFile(f,ct);
	}

	/*
	 * This function is only called when 
	 * the user wants to encrypt the input file
	 * using CFB,OFB, or ECB
	 *
	 * Which then uses openssl's library
	 */
	public void notCBCEncryptFile(File f) throws InvalidKeyException, IOException, 
					      IllegalBlockSizeException, BadPaddingException ,
					      InvalidAlgorithmParameterException {
		System.out.println("Encrypting file: " + f.getName());

		//if the setting is ECB then we initialize the 
		//algorithm with the IV as the ECB mode does not
		//need an IV to encrypt the data
		//else we initialize the cipher algorithm with the given IV
		if(this.setting.equalsIgnoreCase("ECB")){
			this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
		}else{
			this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey,new IvParameterSpec(this.IV));
		}

		//read in plain-text
		FileInputStream encryptedInput = new FileInputStream(f.getName());
		byte[] encText = new byte[encryptedInput.available()];
		encryptedInput.read(encText);
		encryptedInput.close();
		//store plaintext in pt
		byte[] pt = encText;
		//start the timer to measure the time it takes the openssl algorithm
		//to encrypt the data
		long startTime = System.nanoTime();	
		pt = this.padd(pt);
		//encrypt the plaintext
		pt = this.cipher.doFinal(pt);
		//calculate the time taken to ecnrypt the data
		long estimatedTime = System.nanoTime() - startTime;
		//write the time taken to the speed file
		this.writeSpeedToFile(1,estimatedTime,f.getName());
		writeToFile(f,pt);
	}	

	//Echo string sent to console
	private void echo(String s){
		System.out.println(s);
	}

	/*
	 * This function is used to pad the plaintext in 
	 * case that it is not a mutlple of 8 as we are 
	 * encrypting by blocks of size 8 bits
	 *
	 * This function is only called with the use of the CBC implementation 
	 *
	 * This function pads the plaintext with bytes equal to the number
	 * of bytes padded
	 *
	 * So if the number of bytes needed to pad the plaintext 5 then 
	 * we pad the plaintext with the bytes 0x05 for the last 5 bytes
	 */	
	private byte[] padd(byte[] fileText){

		//calculate the number of bytes needed for padding
		int bytesForPadding = fileText.length % this.NUM_OF_BYTES;
		int count = 0;
		
		if(bytesForPadding>0){
			bytesForPadding = this.NUM_OF_BYTES - bytesForPadding;
			byte[] fileBytes = new byte[fileText.length + bytesForPadding];

			for(byte b : fileText){
				fileBytes[count++] = b;
			}
		
			int bytee = bytesForPadding;	

			byte b = 0x00;

			while(bytee-->0)
				b++;
			
			while(bytesForPadding-- >0 ){
				fileBytes[count++] = (byte) b;
			}
			
			count = 0;

			return Arrays.copyOf(fileBytes,fileBytes.length);
		}
		return fileText;
	}

	/*
	 * This function is only used to decrypt 
	 * files that were encrypted by my implementation of CBC.
	 *
	 * It reads the input file
	 * Calculates the size of the file
	 * loops
	 * 	decrypts file using openssl's ecb library implementation
	 * 	xors that with the IV or previous block of cipher text
	 * 	save it into the pt array
	 *
	 * write pt array to the output file
	 */
	public void decryptFile(File f)
			throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
		System.out.println("Decrypting file: " + f.getName());

		//initialize the ECB algorithm to decrypt mode
		this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
		//read the cipher text and assign it the encText array
		FileInputStream encryptedInput = new FileInputStream(f.getName());
		byte[] encText = new byte[encryptedInput.available()];
		encryptedInput.read(encText);
		encryptedInput.close();
		//assign the encrypted file's bytes to ct, to represent the cipher text
		byte[] ct = encText;	
		int sizeOfCT = ct.length;
		int count = 0;	
		byte[] outputOfDES = new byte[this.NUM_OF_BYTES];
		byte[] pt = new byte[sizeOfCT];
		byte[] xored = new byte[this.NUM_OF_BYTES];
		byte[] c_main = new byte[this.NUM_OF_BYTES];
		byte[] c_prev = new byte[this.NUM_OF_BYTES];
		long startTime = System.nanoTime();
		while(count*this.NUM_OF_BYTES < sizeOfCT){
			//read the current block from the cipher text to c_main so it can be decrypted
			for(int i = 0; i < this.NUM_OF_BYTES; i++){
				c_main[i] = ct[i + (count * this.NUM_OF_BYTES)];
			}
			outputOfDES  = this.cipher.update(c_main);	
			outputOfDES = this.cipher.update(c_main);
			//xor the block received from DES to get the actual plaintext block
			for(int i = 0; i<this.NUM_OF_BYTES;i++){
				if(count == 0){
					//xored[i] = xor PT[i] with IV[i]
					xored[i] = (byte)((byte)outputOfDES[i] ^ (byte)this.IV[i]);
				}else{
					//xored[i] = xor PT[i] with prev[i]	
					xored[i] = (byte)(((byte)outputOfDES[i] ^ (byte)c_prev[i])&(0xff)); 
				}
			}
			
			int c = 0;
			for(byte b : c_main){
				c_prev[c++] = b;
			}
			
			//copy the decrypted ciphertext block to the pt byte array
			for(int i = count * this.NUM_OF_BYTES; i < ((count + 1) * this.NUM_OF_BYTES); i ++){
				pt[i] = xored[i%this.NUM_OF_BYTES];
			
			}

			count++;
		}
		//finally check if the plaintext had any padding 
		pt = this.checkPadding(pt);
		//calculate the estimated time it took to decrypt the CBC encrypted file	
		long estimatedTime = System.nanoTime() - startTime;
		//write the speed to the file of speed tests ran
		this.writeSpeedToFile(0,estimatedTime,f.getName());
		//assign 0 to this.mode so when we write to the file
		//it prints that we are decrypting a file
		this.mode = 0;
		this.writeToFile(f,pt);
	}

	/*
	 * This function is used only to decrypt files that are 
	 * not encrypted with my implemented openssl CBC algorithm
	 * using the openssl library
	 */
	public void notCBCdecryptFile(File f)
			throws InvalidKeyException, IOException, IllegalBlockSizeException,
					      BadPaddingException,
					      InvalidAlgorithmParameterException {
		System.out.println("Decrypting file: " + f.getName());
		
		//if the setting is ECB then the DES algorithm does not need an IV
		//as the ECB algorithm does not use an IV
		//else then initialize the cipher with the given IV
		//and initialize it to decrypt mode
		if(this.setting.equalsIgnoreCase("ECB")){
			this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
		}else{
			this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey,new IvParameterSpec(this.IV));
		}
		//read the input file and save it to the array encText
		FileInputStream encryptedInput = new FileInputStream(f.getName());
		byte[] encText = new byte[encryptedInput.available()];
		encryptedInput.read(encText);
		encryptedInput.close();
		//assign the encrypted file's bytes to ct, to represent the cipher text
		byte[] ct = encText;
		//start the timer
		long startTime = System.nanoTime();
		//decrypt the blocks of the cipher text
		byte [] pt  = this.cipher.update(ct);
		//check if there was any padding
		pt = this.checkPadding(pt);
		//calculate the estimated time from start to finish
		long estimatedTime = System.nanoTime() - startTime;
		//write the speed to the file of speed tests ran
		this.writeSpeedToFile(0,estimatedTime,f.getName());
		//finally write the decrypted plaintext to the output file given
		writeToFile(f,pt);

	}

	/*
	 * This function checks if the given plaintext has been appended with padding.
	 */
	private byte[] checkPadding(byte[] pt){
		//get the last byte of the byte array
		byte lastByte = pt[pt.length-1];
		//meant to keep track if there is padding
		boolean thereIsPadding = true;
		//meant to keep track of the spaces of padding if any
		int spaces = lastByte;
		//if the spaces of the plaintext is allegedly >= 8 then there is not padding
		//as the block sizes are 8 bytes
		if(spaces >= 8){
			return pt;
		}
		//else check the if the last n (where n = spaces) blocks are equal to the lastByte
		//if so then there must be padding
		for(int i = (pt.length-spaces) ; i< pt.length;i++){
			if(pt[i] != lastByte){
				thereIsPadding=false;
				break;
			}
		}
		//if there is padding then assign the plaintext to the new subArray of bytes
		if(thereIsPadding){
			pt = Arrays.copyOfRange(pt,0,pt.length-spaces);
		}
		return pt;
	}

	/*
	 * This function writes the speed of the hashing/encrypting/decrypting of the
	 * file to a file SpeedsOfDES_and_Hash.txt to keep track of tests ran
	*/
	public void writeSpeedToFile(int m, long time, String fileName)throws IOException, IllegalBlockSizeException, BadPaddingException{ 
		String mode = "decrypt";
		if(m == 1){
			mode = "encrypt";
		}else if(m == -1){
			mode = "hash";
		}
		String timeToDocument = this.setting+"\nTime it took to "+mode+" file \""+ fileName + "\" : "+time;	
		FileOutputStream out = new FileOutputStream("SpeedsOfDES_and_Hash.txt",true);
		out.write(timeToDocument.getBytes());
		out.write("\n\n".getBytes());
		out.flush();
		out.close();
	}

	//Custom write to file to get file name, write, flush, and close
	public void writeToFile(File f,byte[] output) throws IOException, IllegalBlockSizeException, BadPaddingException {
		FileOutputStream out = new FileOutputStream(this.output.getName());
		out.write(output);
		out.flush();
		out.close();
	}

	//Main execution of the program
	public static void main(String[] args) {
		//initialize variables for later use
		File file = null;
		String IV = "";
		String Key = "";
		file = null;
		File output = null;
		String setting = ""; 
		int option = -1;
		if(args.length < 3 || args.length == 4 || args.length >= 6){ 
			System.out.println("If encrypting\nsyntax: > java SymmetricEnc [IV:put 00 if using ECB ecnryption] [Key] "+ 
					"[File to Encrypt/Decrypt] [File saving output to]" +
					"[Mode: CBC,ECB,CFB,OFB]");
			System.out.println("If hashing\nsyntax: > java SymmetricEnc"+ 
					"[File to hash] [File saving output to]" +"[Mode: MD5,SHA-224,SHA-256,SHA-384,SHA-512]");
			System.exit(1);
		}else if(args.length == 5){
			//else if there are 5 arguments from the command line then 
			//get the IV, Key, the output file and the input file
			IV = new String(args[0]);
			if(IV.equalsIgnoreCase("00")){
				IV = "0000000000000000";
			}
			Key = new String(args[1]);
			file = new File(args[2]);
			output = new File(args[3]);
			setting = args[4].toUpperCase();
			option = 0;
			//if the setting attempted to give was a not the ones implemented print error
			if(!"CBC OFB CFB ECB".contains(setting)){
				System.out.println("The only modes accepted for encrypting are: CBC, OFB, CFB, ECB");
				System.out.println("If encrypting\nsyntax: > java SymmetricEnc [IV:put 00 if using ECB ecnryption] [Key] "+ 
					"[File to Encrypt/Decrypt] [File saving output to]" +
					"[Mode: CBC,ECB,CFB,OFB]");
				System.out.println("If hashing\nsyntax: > java SymmetricEnc"+ 
					"[File to hash] [File saving output to]" +"[Mode: MD5,SHA-224,SHA-256,SHA-384,SHA-512]");
				System.exit(1);
			}
		}else{
			//else then we are hashing
			//grab input and out files and the setting wanted to hash the file
			option = 1;
			file = new File(args[0]);
			output = new File(args[1]);
			setting = args[2].toUpperCase(); 
		}
		//if the file does not exist exit prog
		if(!file.exists()){
			System.out.println("No file with the name:" + file.getName());
			System.exit(1);
		}

		SymmetricEnc ske;
		try {

			int choice = -2;
			String[] options = { "Encrypt", "Decrypt", "Exit" };
			
			//if we are hashing then change prompt to "Hash"
			if(option == 1){
				options[0] = "Hash";
				options[1] = "Unhash";
				ske = new SymmetricEnc(Key,16,"DES", IV,output,setting,option);
			}else{
				//else we are encrypting the input file
				ske = new SymmetricEnc(Key,16,"DES", IV,output,setting,option);
			}
			choice = JOptionPane.showOptionDialog(null, "Select an option", "Options", 0,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			switch (choice) {
			case 0:
				try {
					if("ECB CFB OFB".contains(setting.toUpperCase())){
						//if we are using the ecnryption algorithms that 
						//we are using the openssl libs for then 
						//call the funciton that does not do CBC
						ske.notCBCEncryptFile(file);
						System.out.println("Files encrypted successfully");
					}else if(option==1){
						//else if the option is hashing the call the 
						//hash function to hash the file
						ske.hashFile(file);
						System.out.println("Files hashed successfully");
					}else{
						//else we are using the CBC encryption 
						//algorithm thats not using the 
						//openssl cbc libs functionality
						ske.encryptFile(file);
						System.out.println("Files encrypted successfully");
					}
				} catch (InvalidKeyException | InvalidAlgorithmParameterException |
						IllegalBlockSizeException | BadPaddingException | 
						IOException e) {
					System.err.println("Couldn't encrypt " 
							+ file.getName() + ": " 
							+ e.getMessage());
				}
				break;
			case 1:
					try{ 
						if("ECB CFB OFB".contains(setting.toUpperCase())){
							//if we are decrypting a file use openssl libs 
							ske.notCBCdecryptFile(file);
							System.out.println("Files decrypted successfully");
						}else if(option == 1){
							//cannot unhash a hashed file
							// Hash(many vals) -->  map to --> 1 hash value 
							System.out.println("Cannot \"unhash\" hashed files.");
						}else{
							//else decrypt using implemented 
							//CBC form
							ske.decryptFile(file);
							System.out.println("Files decrypted successfully");
						}
					} catch (InvalidKeyException | InvalidAlgorithmParameterException |
						IllegalBlockSizeException | BadPaddingException | IOException e) {
						System.err.println("Couldn't decrypt " + file.getName() + ": " 
								+ e.getMessage());
					}
				break;
			default:
				break;
			}
		} catch (UnsupportedEncodingException ex) {
			System.err.println("Couldn't create key: " + ex.getMessage());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			System.err.println(e.getMessage());
		}
	}
}
