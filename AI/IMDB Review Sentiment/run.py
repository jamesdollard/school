import model

import numpy as np
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer

import re # regex

from nltk.stem.porter import PorterStemmer # natural language processing
from nltk.corpus import stopwords

def preprocessor(text):
    text = re.sub('<[^*]', '', text) # gets rid of html decorations
    emoticons = re.findall('(?::|;|=)(?:-)?(?:\)|\(|D|P)', text.lower()) # trying to figure out emoticons
    text = re.sub('[\W]+', ' ', text.lower()) + ' '.join(emoticons).replace('-', '')
    return text

porter = PorterStemmer()

stop = stopwords.words('english')

def tokenizer(text):
    return text.split()

def tokenizer_porter(text):
    return [porter.stem(word) for word in text.split()]

# corpus
docs = np.array([
    'The sun is shining',
    'The weather is sweet',
    'The sun is shining, the weather is sweet, and one and one is two'])

#bag of words representation
count = CountVectorizer()
bag = count.fit_transform(docs)

tfidf = TfidfTransformer(use_idf=True, norm='l2',smooth_idf=True)

vect = tfidf.fit_transform(bag)

print(vect)

