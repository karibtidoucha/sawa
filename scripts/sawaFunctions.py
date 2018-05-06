# -*- coding: utf-8 -*-
from __future__ import division
import pickle
import sys
import chunk
import os
import subprocess
import gensim
import random

import sys

from docopt import docopt

import camel_tools as camelt
from camel_tools.utils import CharMapper
from camel_tools.transliterate import Transliterator


__version__ = camelt.__version__



dictionaries_loaded = False

def pickleInDict(fileName):
	pklFile = open(fileName, 'rb')
	dic = pickle.load(pklFile)
	pklFile.close()
	return dic

def pickleOutDict(fileName,dic):
	pklOut = open(fileName, 'wb')
	pickle.dump(dic, pklOut)

def getDifficulty(nGram,ngram2difficulty,morph2freq):
	nGram = nGram.replace(' ', '_')
	try:
		return ngram2difficulty[nGram]
	except:
		minDiff = 1
		for n in range(len(nGram)-1,0,-1): # length of ngram
			for i in range(0,len(nGram)): # starting point of ngram
				if i + n < len(nGram):
					potMorf = nGram[i:i+n]

					# look for a substring as a word
					if potMorf in morph2freq:
						diff = 1/morph2freq[potMorf]
						if diff < minDiff:
							minDiff = diff
					# look for a suffix
					if i > 0 and '+'+potMorf in morph2freq:
						diff = 1/morph2freq['+'+potMorf]
						if diff < minDiff:
							minDiff = diff
						# look for a medial morpheme
						if i + n < len(nGram) -1 and '+'+potMorf+'+' in morph2freq:
							diff = 1/morph2freq['+'+potMorf+'+']
							if diff < minDiff:
								minDiff = diff
					# look for a prefix
					elif i + n < len(nGram) -1 and potMorf+'+' in morph2freq:
						diff = 1/morph2freq[potMorf+'+']
						if diff < minDiff:
							minDiff = diff

			if minDiff != 1:
				return minDiff

		return minDiff

def getStem(nGram,ngram2difficulty,morph2freq):
	nGram = nGram.replace(' ', '_')
	if len(nGram.split()) > 1:
		return None
	else:
		try:
			return word2stem[nGram]
		except:
			minDiff = 1
			stem = None
			for n in range(len(nGram)-1,0,-1): # length of ngram
				for i in range(0,len(nGram)): # starting point of ngram
					if i + n < len(nGram):
						potMorf = nGram[i:i+n]

						# look for a substring as a word
						if potMorf in morph2freq:
							diff = 1/morph2freq[potMorf]
							if diff < minDiff:
								minDiff = diff
								stem = potMorf
						# look for a suffix
						if i > 0 and '+'+potMorf in morph2freq:
							diff = 1/morph2freq['+'+potMorf]
							if diff < minDiff:
								minDiff = diff
								stem = potMorf
							# look for a medial morpheme
							if i + n < len(nGram) -1 and '+'+potMorf+'+' in morph2freq:
								diff = 1/morph2freq['+'+potMorf+'+']
								if diff < minDiff:
									minDiff = diff
									stem = potMorf
						# look for a prefix
						elif i + n < len(nGram) -1 and potMorf+'+' in morph2freq:
							diff = 1/morph2freq[potMorf+'+']
							if diff < minDiff:
								minDiff = diff
								stem = potMorf

				if minDiff != 1:
					return stem

			return stem

def convertToSafeBW(s):

	scheme = 'ar2safebw'
	ignore_markers = None
	strip_markers = None

	mapper = CharMapper.builtin_mapper(scheme)
	trans = Transliterator(mapper, '@@IGNORE@@')
	out = trans.transliterate(s, strip_markers, ignore_markers)

	# out = subprocess.check_output('echo "'+s+'" | camel_transliterate -s ar2safebw',shell=True).decode('utf-8')
	out = out.replace('Y','y').replace('M','A').replace('L','A').replace('O','A').replace('I','A')
	return out

def convertToUTF8(s):

	scheme = 'safebw2ar'
	ignore_markers = None
	strip_markers = None

	mapper = CharMapper.builtin_mapper(scheme)
	trans = Transliterator(mapper, '@@IGNORE@@')
	outp = trans.transliterate(s, strip_markers, ignore_markers)

	# outp = subprocess.check_output('echo "'+s+'" | camel_transliterate -s safebw2ar',shell=True).decode('utf-8')

	return outp

def chunkAndGetDiffs(str,ngram2difficulty,morph2freq):
	strBW = convertToSafeBW(str)
	difficulties = []
	chunks = chunk.chunk(strBW,ngram2difficulty)
	chunkedString = []
	for ngram in chunks:
		difficulties.append(getDifficulty(ngram,ngram2difficulty,morph2freq))
		chunkedString.append(convertToUTF8(ngram).replace('_',' '))
	return chunkedString, difficulties

def getDefinition(s,definitions):
	try:
		return definitions[s]
	except:
		return 'No definitions'

def getNNs(s,w2v,word2stem):
	stem = getStem(s,ngram2difficulty,morph2freq)
	mostSimilarWords = []
	try:
		ms = w2v.similar_by_word(s,50)
		for x in ms:
			NN = x[0].replace('_',' ')
			if len(NN.split()) > 1:
				mostSimilarWords.append(convertToUTF8(NN))
			else:
				NNstem = getStem(NN,ngram2difficulty,morph2freq)
				if NNstem != stem or NNstem == None:
					mostSimilarWords.append(convertToUTF8(NN))
			if len(mostSimilarWords) == 3:
				break
		return mostSimilarWords
	except:
		return ['SORRY, NO KNOWN SIMILAR WORDS']

def getContextSentence(ngram2sents,corpus,ngram):
	try:
		return convertToUTF8(corpus[random.choice(ngram2sents[ngram])])
	except:
		return 'No known examples'

def popUP(s,definitions,w2v,word2stem,ngram2sents,corpus):
	# global definitions,w2v,word2stem,ngram2sents,corpus
	safeBWs = convertToSafeBW(s)
	definition = getDefinition(s,definitions)
	
	# NNs = getNNs(safeBWs,w2v,word2stem)
	NNs = ['No similar words']

	conSent = getContextSentence(ngram2sents,corpus,safeBWs)
	return [[definition],NNs,[conSent]]

def feedback(word,definition,definitions):
	if word not in definitions:
		definitions[word] = []
	definitions[word].append(definition)

	return definitions

if not dictionaries_loaded:
	dictionaries_loaded = True
	morph2freq = pickleInDict('models/morph2freq.pkl')
	ngram2difficulty = pickleInDict('models/ngram2difficulty.pkl')
	word2stem = pickleInDict('models/word2stem.pkl')
	definitions = pickleInDict('models/definitions.pkl')
	# w2v = gensim.models.KeyedVectors.load_word2vec_format('models/lev.bin', fvocab=None, binary=True, encoding='utf8', unicode_errors='strict', limit=None)
	corpus = (open('data/LM.lev').read().splitlines())
	ngram2sents = pickleInDict('models/ngrams2sents.pkl')

if __name__ == '__main__':

	### SENDER
	# if 'sender' == sys.argv[1]:
	# print zip(*chunkAndGetDiffs(raw_input(),ngram2difficulty,morph2freq))

	### RECIEVER
	# if 'reciever' == sys.argv[1]:
	print popUP(raw_input(),definitions,'w2v',word2stem,ngram2sents,corpus)

	### FEEDBACK
	# if 'feedback' == sys.argv[1]:
	# 	print feedback(word,definition,definitions)
	# 	pickleOutDict(definitions)


