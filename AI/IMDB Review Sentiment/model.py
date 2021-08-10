import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.pipeline import Pipeline
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import GridSearchCV

from run import preprocessor
from run import stop
#

df = pd.read_csv('movie_data.csv', encoding='etf-8')
df['review'] = df['review'].apply(preprocessor) # clean up data

n = 10000
X_train = df.loc[:n, 'review'].values
y_train = df.loc[:n, 'sentiment'].values
X_test = df.loc[40000:, 'review'].values
y_test = df.loc[:40000:, 'sentiment'].values

clf = LogisticRegression(random_state=0, solver='liblinear')
tfidf = TfidfVectorizer(strip_accents=None, lowercase=False, preprocessor=None)

# construct data pipeline
lr_tfidf = Pipeline([('vect', tfidf), ('clf', clf)])

param_grid = [{
    'vect__ngram_range': [(1,1)],
    'vect__stop_words': [ stop ],
    'clf__penalty': [ 'l2' ],
    'clf__c': [1.0, 10.0, 100.0]}]

#five-fold cross validation
gs_lr_tfidf = GridSearchCV(lr_tfidf, param_grid, scoring='accuracy', cv=5, verbose=2, n_jobs=2)