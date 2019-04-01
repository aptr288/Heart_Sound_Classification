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

Train = pd.read_csv('NewTr.csv', delimiter=';', header=None)
MajorityClassdata = Train.iloc[0:38]
MinorityClassdata = Train.iloc[39:106]
#Resampling data to  address selection bias
ResampledTrain1 =resample(MajorityClassdata , n_samples = 90, random_state = 1)
ResampledTrain2 =resample(MinorityClassdata, n_samples = 10, random_state = 1)

TrainUpSampled = pd.concat([ResampledTrain1, ResampledTrain2])
Test = pd.read_csv('NewVal.csv', delimiter=';')

trainM = TrainUpSampled.as_matrix()
testM = Test.as_matrix()
ytrain = trainM[:, 0]
Xtrain = trainM[:, 1:]

ytest = testM[:, 0]
Xtest = testM[:, 1:]

destree = tree.DecisionTreeClassifier()
destree.fit(Xtrain, ytrain)
ydecpred = destree.predict(Xtest)



rfc = RandomForestClassifier(n_estimators=100)
rfc.fit(Xtrain, ytrain)
rfc_pred = rfc.predict(Xtest)

print("Decision Tree Accuracy:%.3f%%" % (metrics.accuracy_score(ytest, ydecpred) * 100.0))
print(classification_report(ytest, ydecpred))
print("Random Forest Accuracy:%.3f%%" % (metrics.accuracy_score(ytest, rfc_pred) * 100.0))
print(classification_report(ytest, rfc_pred))
