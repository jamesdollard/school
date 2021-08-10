from sklearn.datasets import load_digits
import numpy as np

import AdalineModel
import SVMModel
import NeighborModel


digits = load_digits()
#images = digits['images']
data = digits['data']
label = digits['target']

am = AdalineModel.AdalineModel()
svm = SVMModel.SVMModel()
nm = NeighborModel.NeighborModel(5)

# ten fold cross examination

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

for i in range(10):
    label = np.where(label == i, 1, -1)

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

    am_accuracy = 0
    svm_accuracy = 0
    nm_accuracy = 0

    for j in range(10):
        testBin = dataList[j]
        testLabels = labelList[j]
        trainBin = data.copy()
        trainLabels = label.copy()
        for k in range(len(testBin)):
            np.delete(trainBin, (j*180 + k))
            np.delete(trainLabels, (j*180 + k))

        svm.fit(trainBin, trainLabels)
        am.fit(trainBin, trainLabels)
        nm.fit(trainBin, trainLabels)

        am_accuracy += np.divide(am.predictionAccuracy(testBin, testLabels), len(testBin))
        svm_accuracy += np.divide(svm.predictionAccuracy(testBin, testLabels), len(testBin))
        nm_accuracy += np.divide(nm.predictionAccuracy(testBin, testLabels), len(testBin))

        print(svm_accuracy)


    print("AM: " + str(np.divide(am_accuracy, 10)))
    print("SVM: " + str(np.divide(svm_accuracy, 10)))
    print("NM: " + str(np.divide(nm_accuracy, 10)))

