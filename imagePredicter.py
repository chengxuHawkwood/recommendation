import warnings
warnings.filterwarnings('ignore', category=FutureWarning)
from imageClassifier import  cp_model
import cv2
import numpy as np
if __name__ == '__main__':
    img = cv2.imread("/Users/hawkwood/Downloads/style-color-images/style/6_9_021.png")
    img = img.astype('float32')/255
    cp_model = cp_model((150, 150, 3))
    cp_model.load_weights('cp_model.styles.hdf5')
    print(cp_model.predict(np.array([img])))
