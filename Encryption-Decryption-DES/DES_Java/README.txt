SymmetricEnc.java 

Syntax to compile code
	- javac SymmetricEnc.java

Syntax to run code (Encryption/Decryption Mode):
	- java SymmetricEnc [IV:put 00 if using ECB ecnryption] [Key] [File to Encrypt/Decrypt] [File saving output to] [Mode: CBC,ECB,CFB,OFB]

Syntax to run code (Hashing Mode):
	- java SymmetricEnc [File to hash] [File saving output to] [Mode: MD5,SHA-224,SHA-256,SHA-384,SHA-512]
