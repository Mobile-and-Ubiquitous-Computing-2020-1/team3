import sys, getopt
import cv2
import os
from keras import backend as K
import time

os.environ['KERAS_BACKEND'] = 'tensorflow'

from keras.models import load_model
from keras.preprocessing import image

MODEL_ROOT_PATH="/root/Edwin/hand-detection-YoloKeras/pretrain/"
#YOLOV2
#reference from https://github.com/experiencor/keras-yolo2
# https://github.com/experiencor/keras-yolo2/blob/master/LICENSE

def load_hdmodel():
    #Load Model
    print('model path:',MODEL_ROOT_PATH)
    model_hand = load_model(MODEL_ROOT_PATH+'yolov2_tiny-hand.h5')
    return model_hand