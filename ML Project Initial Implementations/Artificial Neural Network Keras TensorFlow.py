import pandas as pd
import numpy as np
import keras
from keras import metrics
from keras.models import Sequential
from keras.layers import *
from keras.layers import Activation, Dense

RUN_NAME = "run 2 with 5 nodes in layer 1"
Train = pd.read_csv('NewTr.csv', delimiter=';')
Test = pd.read_csv('Newval.csv', delimiter=';')
trainM = Train.as_matrix()
testM = Test.as_matrix()
ytrain = trainM[:, 0]
Xtrain = trainM[:, 1:]
ytest = testM[:, 0]
Xtest = testM[:, 1:]



# Define the model
#With sequential model api we can start with empty model and add layers to it in sequence
model = Sequential()
#Adition of layers here dense represents full connected nodes
model.add(Dense(50, input_dim=3276, activation='relu', name='layer_1'))
model.add(Dense(100, activation='relu', name='layer_3'))
model.add(Dense(100, activation='relu', name='layer_4'))
model.add(Dense(100, activation='relu', name='layer_5'))
model.add(Dense(1, activation='linear', name='output_layer'))
#model.add(Activation('softmax'))
model.compile(loss='mean_squared_error', optimizer='adam',metrics=['accuracy'])

# Create a TensorBoard logger
logger = keras.callbacks.TensorBoard(
    log_dir='logs/{}'.format(RUN_NAME),
    write_graph=True,
    histogram_freq=0

)

# Train the model with number of epochs
model.fit(
    Xtrain,
    ytrain,
    epochs=100,
    shuffle=True,
    verbose=2,
    callbacks=[logger]
)

# Load the separate test data set

test_error_rate = model.evaluate(Xtest, ytest, verbose=0)

print("The mean squared error (MSE) for the test data set is: {}".format(test_error_rate))


# Make a prediction with the neural network
prediction = model.predict(Xtest)
prediction = np.floor(prediction)

#acc = metrics.binary_accuracy(ytest,prediction)

print("The disease to be classified are- ${}".format(prediction))
