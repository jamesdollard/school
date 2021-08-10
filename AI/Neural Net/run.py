import os
import pandas as pd
import numpy as np
import NeuralNetwork
from sklearn.datasets import load_digits

# iris data

url = os.path.join('https://archive.ics.uci.edu', 'ml', 'machine-learning-databases', 'iris', 'iris.data')

data = pd.read_csv(url, header=None, encoding='utf-8')

dataBin1 = data.iloc[0:15, 0:5]
dataBin2 = data.iloc[15:30, 0:5]
dataBin3 = data.iloc[30:45, 0:5]
dataBin4 = data.iloc[45:60, 0:5]
dataBin5 = data.iloc[60:75, 0:5]
dataBin6 = data.iloc[75:90, 0:5]
dataBin7 = data.iloc[90:105, 0:5]
dataBin8 = data.iloc[105:120, 0:5]
dataBin9 = data.iloc[120:135, 0:5]
dataBin10 = data.iloc[135:150, 0:5]

dataList = [dataBin1, dataBin2, dataBin3, dataBin4, dataBin5, dataBin6,
            dataBin7, dataBin8, dataBin9, dataBin10]
data_matrix = data.values
labels = data.iloc[:, 4].values

vectorized_labels = []
for i in range(len(labels)):
    if labels[i] == 'Iris-setosa':
        vectorized_labels.append([1, 0, 0])
    elif labels[i] == 'Iris-versicolor':
        vectorized_labels.append([0, 1, 0])
    else:
        vectorized_labels.append([0, 0, 1])

labels = vectorized_labels

nn = NeuralNetwork.NeuralNetwork(4,3,20,50,0.5) # lr * .95

for v in range(10): # ten fold cross validation

    testBin = dataList[v]
    trainBin = data.copy()
    for i in range(15):
        trainBin = trainBin.drop(index=(v*15 + i))

    testData = testBin.iloc[:15, 0:4].values
    testLabels = testBin.iloc[:15, 4].values
    trainData = trainBin.iloc[:, 0:4].values
    trainLabels = trainBin.iloc[:, 4].values

    vectorized_labels = []
    for i in range(len(testLabels)):
        if testLabels[i] == 'Iris-setosa':
            vectorized_labels.append([1, 0, 0])
        elif testLabels[i] == 'Iris-versicolor':
            vectorized_labels.append([0, 1, 0])
        else:
            vectorized_labels.append([0, 0, 1])
    testLabels = vectorized_labels

    vectorized_labels = []
    for i in range(len(trainLabels)):
        if trainLabels[i] == 'Iris-setosa':
            vectorized_labels.append([1, 0, 0])
        elif trainLabels[i] == 'Iris-versicolor':
            vectorized_labels.append([0, 1, 0])
        else:
            vectorized_labels.append([0, 0, 1])
    trainLabels = vectorized_labels

    nn.fit(trainData, trainLabels)
    print("Iris Error: " + str(nn.calcTotalErr(testData, testLabels)))

# handwritten digits data

digits = load_digits()
data = digits['data']
label = digits['target']

vectorized_label = []
for i in range(len(label)):
    if label[i] == 0:
        vectorized_label.append([1, 0, 0, 0, 0, 0, 0, 0, 0, 0])
    elif label[i] == 1:
        vectorized_label.append([0, 1, 0, 0, 0, 0, 0, 0, 0, 0])
    elif label[i] == 2:
        vectorized_label.append([0, 0, 1, 0, 0, 0, 0, 0, 0, 0])
    elif label[i] == 3:
        vectorized_label.append([0, 0, 0, 1, 0, 0, 0, 0, 0, 0])
    elif label[i] == 4:
        vectorized_label.append([0, 0, 0, 0, 1, 0, 0, 0, 0, 0])
    elif label[i] == 5:
        vectorized_label.append([0, 0, 0, 0, 0, 1, 0, 0, 0, 0])
    elif label[i] == 6:
        vectorized_label.append([0, 0, 0, 0, 0, 0, 1, 0, 0, 0])
    elif label[i] == 7:
        vectorized_label.append([0, 0, 0, 0, 0, 0, 0, 1, 0, 0])
    elif label[i] == 8:
        vectorized_label.append([0, 0, 0, 0, 0, 0, 0, 0, 1, 0])
    else:
        vectorized_label.append([0, 0, 0, 0, 0, 0, 0, 0, 0, 1])
label = vectorized_label


nn = NeuralNetwork.NeuralNetwork(64, 10, 12, 20, 0.2)

dataBin1 = data[:180]
dataBin2 = data[180:360]
dataBin3 = data[360:540]
dataBin4 = data[540:720]
dataBin5 = data[720:900]
dataBin6 = data[900:1080]
dataBin7 = data[1080:1260]
dataBin8 = data[1260:1440]
dataBin9 = data[1440:1620]
dataBin10 = data[1620:1797]

dataList = [dataBin1, dataBin2, dataBin3, dataBin4, dataBin5, dataBin6, dataBin7, dataBin8, dataBin9, dataBin10]

labelBin1 = label[:180]
labelBin2 = label[180:360]
labelBin3 = label[360:540]
labelBin4 = label[540:720]
labelBin5 = label[720:900]
labelBin6 = label[900:1080]
labelBin7 = label[1080:1260]
labelBin8 = label[1260:1440]
labelBin9 = label[1440:1620]
labelBin10 = label[1620:1797]

labelList = [labelBin1, labelBin2, labelBin3, labelBin4, labelBin5, labelBin6, labelBin7, labelBin8, labelBin9,
             labelBin10]


for j in range(10):
    testBin = dataList[j]
    testLabels = labelList[j]
    trainBin = data.copy()
    trainLabels = label.copy()
    for k in range(len(testBin)):
        np.delete(trainBin, (j*180 + k))
        np.delete(trainLabels, (j*180 + k))

    nn.fit(trainBin, trainLabels)
    # nn.fit(testBin, testLabels)
    print("Digit Error: " + str(nn.calcTotalErr(testBin, testLabels)))