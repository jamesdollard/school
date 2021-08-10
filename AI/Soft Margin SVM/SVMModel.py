import numpy as np

class SVMModel:

    def __init__(self):
        self.random_state = 1
        self.c = 1000000
        self.learningRate = 10
        self.epochs = 2


    def fit(self, trainingSet, trainingLabels):
        self.trainingSet = trainingSet
        self.trainingLabels = trainingLabels
        self.nexamples = len(trainingSet)
        self.nfeatures = len(trainingSet[0])

        #find largest vector
        self.r = 0

        for i in range(self.nexamples):

            vectorLength = np.linalg.norm(trainingSet[i])
            if (self.r < vectorLength):
                self.r = vectorLength


        rgen = np.random.RandomState(self.random_state)
        self.w = rgen.normal(loc=0.0, scale = 0.1, size=(len(trainingSet)))
        self.b = rgen.normal(loc=0.0, scale = 0.1, size=1)

        solutionExists = self.setHyperplane()

        return solutionExists

    def setHyperplane(self):

        count = 0
        while count < self.epochs:
            for i in range(self.nexamples):

                calc = self.w[i] + np.multiply(np.divide(self.learningRate, np.dot(self.trainingSet[i], self.trainingSet[i])), self.derL(i))

                self.w[i] = np.minimum(self.c, np.maximum(0, calc))

            count += 1


    def derL(self, weight_index):
        sum = 0
        for i in range(self.nexamples):

            sum += np.multiply(np.multiply(self.w[i], self.trainingLabels[i]), np.dot(self.trainingSet[weight_index], self.trainingSet[i]))

        ret = np.multiply(self.trainingLabels[weight_index], sum)
        return 1 - ret


    def predictionAccuracy(self, testSet, testLabels):
        success = 0
        failure = 0
        for i in range(len(testSet)):
            sum = 0
            for j in range(self.nexamples):
                sum += np.multiply(np.multiply(self.trainingLabels[j], self.w[j]), np.dot(testSet[i], self.trainingSet[j])) + self.b
            if (testLabels[i] * sum < 0):
                failure += 1
            else:
                success += 1

        return success