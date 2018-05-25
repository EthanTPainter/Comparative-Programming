# @author: Ethan Painter
# Compiled: python (could use pycrypto)
# Last Updated: 5/24/2018
# Originally Created: 10/23/2017

from Crypto.Cipher import DES
import sys
import string
import time

#checks if the files have the correct extensions
def checkEncOrDec():
	if(sys.argv[2][-4:] == ".txt" and sys.argv[3][-4:] == ".des"):
		print "Encrypting ", (sys.argv[2])
		return 1
	elif(sys.argv[2][-4:] == ".des" and sys.argv[3][-4:] == ".txt"):
		print "Decrypting ", (sys.argv[2])
		return 0
	else:
		return -1

#convert 16 byte string to 8 byte hex string
def build8Byte(val):
	string = ""
	temp = "0x"
	for i in range(0, len(val), 2):
		temp += val[i:i+2]
		string += chr(int(temp, 16))
		temp = "0x"
	return string

if(len(sys.argv[1]) != 16):
	print "Key is not 64 bits\n"
        sys.exit()

#check that every char in string is a valid hex value
if((all(c in string.hexdigits for c in sys.argv[1])) == False):
        print "Key is not all hexadecimal values"
	sys.exit()	

fin = open(sys.argv[2], "r", 0)
fout = open(sys.argv[3], "w", 0)

encryptOrDecrypt = checkEncOrDec()

if(encryptOrDecrypt == -1):
	print "incorrect file types"
	sys.exit()

key = build8Byte(sys.argv[1])

des = DES.new(key, DES.MODE_ECB)	

start = time.time()

#read 64 bit blocks and encrypt or decrypt in ECB then write to file out
for readBlock in iter(lambda: fin.read(8), ""):
	if(len(readBlock) < 8):
		readBlock += ' ' * (8 - len(readBlock))

	if(encryptOrDecrypt == 0):
		writeBlock = des.decrypt(readBlock)

	if(encryptOrDecrypt == 1):
		writeBlock = des.encrypt(readBlock)

	fout.write(writeBlock)

end = time.time()

print "Time taken", end - start, "seconds"
