import numpy as np

class NeuralNetwork:

    def __init__(self, ninput, noutput, nhidden, epochs, lr):

        self.epochs = epochs
        self.lr = lr

        self.ninput = ninput
        self.noutput = noutput
        self.nhidden = nhidden

        self.training_data = None
        self.training_labels = None

        self.current_input = [None] * ninput
        self.current_hidden = [None] * nhidden
        self.current_output = [None] * noutput

        rgen = np.random.RandomState(1) # initialize weights
        first_layer_weights = rgen.normal(loc=0.0, scale=0.5, size=(ninput, nhidden))
        second_layer_weights = rgen.normal(loc=0.0, scale=0.5, size=(nhidden, noutput))
        self.weights = []
        self.weights.append(first_layer_weights)
        self.weights.append(second_layer_weights)

    def fit(self, training_data, training_label):
        for e in range(self.epochs):
            for i in range(len(training_data)):
                data = training_data[i]
                label = training_label[i]
                self.current_input = data

                for j in range(self.nhidden):
                    self.current_hidden[j] = self.calcHiddenNode(j)

                for j in range(self.noutput):
                    self.current_output[j] = self.calcOutput(j)

                for j in range(self.nhidden):
                    for k in range(self.noutput):
                        self.weights[1][j][k] -= np.multiply(self.lr, self.secondLayerGradient([j,k], label))

                for j in range(self.ninput):
                    for k in range(self.nhidden):
                        self.weights[0][j][k] -= np.multiply(self.lr, self.firstLayerGradient([j,k], label))

            self.lr = self.lr * 0.95





    def sigmoid(self, x):
        return 1 / (1 + np.exp(-x))

    def calcHiddenNode(self, node_index):
        sum = 0
        for i in range(self.ninput):
            inputNode = self.current_input[i]
            weight = self.weights[0][i][node_index]
            sum += np.multiply(inputNode, weight)

        return self.sigmoid(sum)

    def calcOutput(self, output_index):
        sum = 0
        for i in range(self.nhidden):
            hiddenNode = self.calcHiddenNode(i)
            weight = self.weights[1][i][output_index]
            sum += np.multiply(hiddenNode, weight)

        return self.sigmoid(sum)

    def calcErr(self, input, label):
        self.current_input = input
        sum = 0
        for i in range(self.noutput):
            output_node = self.calcOutput(i)
            sum += np.square(output_node - label[i])

        return sum

    def calcTotalErr(self, all_inputs, all_labels):
        sum = 0
        for i in range(len(all_inputs)):
            input = all_inputs[i]
            label = all_labels[i]
            sum += self.calcErr(input, label)

        return np.divide(sum, len(all_inputs))

    def firstLayerGradient(self, weight_index, labels):
        start = weight_index[0]
        end = weight_index[1]

        input_node = self.current_input[start]
        hidden_node = self.current_hidden[end]

        sum = 0
        for i in range(self.noutput):
            output = self.current_output[i]
            label = labels[i]
            weight = self.weights[1][end][i]
            sum += (output - label) * output * (1 - output) * weight * hidden_node * (1 - hidden_node) * input_node

        return 2 * sum

    def secondLayerGradient(self, weight_index, label):
        start = weight_index[0]
        end = weight_index[1]

        hidden_node = self.current_hidden[start]
        output_node = self.current_output[end]

        return 2 * (output_node - label[end]) * output_node * (1 - output_node) * hidden_node



