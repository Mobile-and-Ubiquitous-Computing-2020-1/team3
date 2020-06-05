import sys, getopt
import cv2
import os
from keras import backend as K
import time

os.environ['KERAS_BACKEND'] = 'tensorflow'

from keras.models import load_model
from keras.preprocessing import image

#YOLOV2
#reference from https://github.com/experiencor/keras-yolo2
# https://github.com/experiencor/keras-yolo2/blob/master/LICENSE

MODEL_ROOT_PATH="./pretrain/"

#Load Model
model_hand = load_model(MODEL_ROOT_PATH+'yolov2_tiny-hand.h5')