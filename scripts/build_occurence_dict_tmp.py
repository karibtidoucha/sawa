#import scripts.sawaFunctions
import pickle

def pickleOutDict(fileName,dic):
	pklOut = open(fileName, 'wb')
	pickle.dump(dic, pklOut)

with open('data/LM.lev') as f:
	examples = f.readlines()
with open('histograms/hist.RP.lev') as f:
	phrases = f.readlines()
	phrases = {x.split()[1]: [] for x in phrases}

for ind, example in enumerate(examples):
	example = example.split()
	if len(example) > 20:
		continue
	for i in range(len(example)):
		for j in range(i + 1, len(example) + 1):
			ngram = '_'.join(example[i:j])
			if ngram in phrases and len(phrases[ngram]) < 10:
				phrases[ngram].append(ind)
	print(ind, len(example))

del examples

pickleOutDict('models/ngram2sents.pkl', phrases)
