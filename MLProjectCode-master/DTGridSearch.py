import pandas as pd
import numpy as np
from sklearn import metrics
from sklearn import tree
from sklearn import grid_search
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report, confusion_matrix
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import roc_auc_score



Train = pd.read_csv('NewTr.csv', delimiter=';', header=None)
Test = pd.read_csv('NewVal.csv', delimiter=';')
trainM = Train.as_matrix()
testM = Test.as_matrix()
ytrain = trainM[:, 0]
Xtrain = trainM[:, 1:]
ytest = testM[:, 0]
Xtest = testM[:, 1:]

#Grid search with param grid having different set of parameters 
param_grid = {'max_depth': np.arange(3, 10), 'min_impurity_split':np.arange(1, 5), 'min_samples_leaf':np.arange(1, 5), 'min_samples_split':np.arange(3, 10)  }

tree = grid_search.GridSearchCV(DecisionTreeClassifier(), param_grid)

tree.fit(Xtrain, ytrain)
tree_preds = tree.predict_proba(Xtest)[:, 1]



print("Best accuracy possible and best parameters to achieve them ")

print (tree.best_score_, tree.best_params_)



