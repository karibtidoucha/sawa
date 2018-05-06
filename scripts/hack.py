import pickle

def pickleInDict(fileName):
	pklFile = open(fileName, 'rb')
	dic = pickle.load(pklFile)
	pklFile.close()
	return dic

dic = pickleInDict('temp.txt'):

for key in dic:
	print 'KEY: {}'.format(key)
	for val in dic[key]:
		print 'VAL: {}'.format(val)