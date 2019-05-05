from keras import backend as K
from keras.models import Sequential, load_model, Model
from imageClassifier import  cp_model
import numpy as np
import h5py
f = h5py.File('/Users/hawkwood/Downloads/style-color-images/StyleColorImages.h5', 'r')
keys = list(f.keys())
print(keys)
brands = np.array(f[keys[0]])
images = np.array(f[keys[1]])
images = images.astype('float32')/255
cp_model = cp_model((150, 150, 3))
cp_model.load_weights('cp_model.styles.hdf5')
layer_name = 'dense_2'
mid_model = Model(input=cp_model.input,
                   output=cp_model.get_layer(layer_name).output)
mid_output = mid_model.predict(images)

