import warnings
warnings.filterwarnings('ignore', category=FutureWarning)
import numpy as np
import pandas as pd
import tensorflow as tf
import cv2
import h5py

from sklearn.model_selection import train_test_split
from keras.utils import to_categorical
from keras.callbacks import ModelCheckpoint
from keras.preprocessing.image import ImageDataGenerator

from keras.models import Sequential, load_model, Model
from keras.layers import Input, BatchNormalization
from keras.layers import Dense, LSTM, GlobalAveragePooling1D, GlobalAveragePooling2D
from keras.layers import Activation, Flatten, Dropout, BatchNormalization
from keras.layers import Conv2D, MaxPooling2D, GlobalMaxPooling2D
from keras.callbacks import EarlyStopping, ReduceLROnPlateau
from keras.layers.advanced_activations import PReLU, LeakyReLU

from keras.applications.inception_v3 import InceptionV3, preprocess_input

import scipy
from scipy import misc


def cp_model(shape):
    model = Sequential()

    model.add(Conv2D(32, (5, 5), padding='same', input_shape=shape))#x_train2.shape[1:]
    model.add(LeakyReLU(alpha=0.02))

    model.add(MaxPooling2D(pool_size=(2, 2)))
    model.add(Dropout(0.2))

    model.add(Conv2D(196, (5, 5)))
    model.add(LeakyReLU(alpha=0.02))

    model.add(MaxPooling2D(pool_size=(2, 2)))
    model.add(Dropout(0.2))

    model.add(GlobalMaxPooling2D())

    model.add(Dense(512))
    model.add(LeakyReLU(alpha=0.02))
    model.add(Dropout(0.5))

    model.add(Dense(10))
    model.add(Activation('softmax'))

    model.compile(loss='categorical_crossentropy', optimizer='nadam', metrics=['accuracy'])

    return model


if __name__ == '__main__':

    data = pd.read_csv("/Users/hawkwood/Downloads/style-color-images/style/style.csv")
    data.tail()
    f = h5py.File('/Users/hawkwood/Downloads/style-color-images/StyleColorImages.h5', 'r')
    keys = list(f.keys())
    brands = np.array(f[keys[0]])
    images = np.array(f[keys[1]])
    products = np.array(f[keys[2]])
    images = images.astype('float32')/255
    cat_brands = to_categorical(brands, 7)
    cat_products = to_categorical(products, 10)
    x_train2, x_test2, y_train2, y_test2 = train_test_split(images, cat_products,
                                                            test_size=0.2,
                                                            random_state=1)

    n = int(len(x_test2) / 2)
    x_valid2, y_valid2 = x_test2[:n], y_test2[:n]
    x_test2, y_test2 = x_test2[n:], y_test2[n:]
    cp_checkpointer = ModelCheckpoint(filepath='cp_model.styles.hdf5',
                                      verbose=2, save_best_only=True)
    cp_lr_reduction = ReduceLROnPlateau(monitor='val_loss',
                                        patience=5, verbose=2, factor=0.5)
    cp_model = cp_model((150, 150, 3))
    # cp_history = cp_model.fit(x_train2, y_train2,
    #                           epochs=50, batch_size=64, verbose=2,
    #                           validation_data=(x_valid2, y_valid2),
    #                           callbacks=[cp_checkpointer, cp_lr_reduction])
    # Load the model with the best validation accuracy
    cp_model.load_weights('cp_model.styles.hdf5')
    # Calculate classification accuracy on the testing set
#    cp_score = cp_model.evaluate(x_test2, y_test2)

#    print("Scores: \n", (cp_score))
    print(cp_model.predict(images[0:1]))
    print(x_train2.shape[1:])
    print(np.shape(images[0:1]))

