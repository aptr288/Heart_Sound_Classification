import pandas as pd
import numpy as np
from sklearn import metrics
from sklearn import tree
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report, confusion_matrix
from imblearn.over_sampling import RandomOverSampler
from imblearn.over_sampling import SMOTE, ADASYN
from sklearn.feature_selection import RFE
from sklearn.utils import resample
from sklearn.utils import class_weight
from sklearn.ensemble import AdaBoostClassifier
from sklearn.tree import DecisionTreeClassifier
from sklearn.feature_selection import SelectKBest
from sklearn.feature_selection import chi2
from sklearn.feature_selection import SelectPercentile, f_classif, SelectFpr
from statsmodels.sandbox.stats.runs import mcnemar

Train = pd.read_csv('FinalTrain.csv', delimiter=';', header=None)
Test = pd.read_csv('Newtes.csv', delimiter=';')
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

#calculating weights based on imbalanced data
class_weight = class_weight.compute_class_weight('balanced', np.unique(ytrain), ytrain)
cw ={1: class_weight[0], 2: class_weight[1], 3: class_weight[2], 4: class_weight[3], 5: class_weight[4], 6 : class_weight[5],
  7: class_weight[6], 8:class_weight[7]}
#{1: 0.34868421, 2:1.325, 3: 1.89285714, 4: 0.94642857, 5: 1.47222222, 6 : 0.88333333,
# 7: 1.89285714, 8:2.20833333}


 
# Feature selection methods used to select different features to eliminate unwanted features 
X_Trainnew = SelectPercentile(f_classif, percentile=10).fit_transform(Xtrain, ytrain)
X_Testnew = SelectPercentile(f_classif, percentile=10).fit_transform(Xtest, ytest)

#Ensamble method Boosting is done on decision tree with best combination of parameters obtained from the grid search  
destree = AdaBoostClassifier(
    DecisionTreeClassifier(max_depth=2,class_weight=cw, min_impurity_split= 3, min_samples_leaf =1,min_samples_split = 3),
    n_estimators=600,learning_rate=1)

destree.fit(X_Trainnew, ytrain)
ydecpred = destree.predict(X_Testnew)


#Random Forest model 
rfc = RandomForestClassifier(n_estimators=100)
rfc.fit(X_Trainnew, ytrain)
rfc_pred = rfc.predict(X_Testnew)


print("Decision Tree Accuracy:%.3f%%" % (metrics.accuracy_score(ytest, ydecpred) * 100.0))
print(classification_report(ytest, ydecpred))
print("Random Forest Accuracy:%.3f%%" % (metrics.accuracy_score(ytest, rfc_pred) * 100.0))
print(classification_report(ytest, rfc_pred))
