import sys
from joblib import load
import numpy as np


class Predictor:
    def __init__(self, models, class_matching):
        self.models = models
        self.class_matching = class_matching

    def predict(self, data):
        results = [model.predict([data])[0] for model in self.models]
        occurrences = np.array([[x, results.count(x)] for x in set(results)])
        return self.class_matching[occurrences[np.argmax(occurrences[:, 1])][0]]


def main():
    predictor = sys.argv[1]
    data = sys.argv[2]
    data_sep = sys.argv[3]
    data = np.array(data.split(data_sep)).astype(np.float)
    predictor = load(predictor)
    return predictor.predict(data)


if __name__ == "__main__":
    sys.stdout.write(main())
