import numpy as np
import time

import sys, getopt
import cv2
import os
from keras import backend as K
from matplotlib import pyplot as plt
import PIL
from PIL import Image

os.environ['KERAS_BACKEND'] = 'tensorflow'

from keras.models import load_model
from keras.preprocessing import image

import sys
sys.path.append('/root/Edwin/hand-detection-YoloKeras')
from interpret_ouput_yolov2 import *
from show_results import *
#YOLOV2
#reference from https://github.com/experiencor/keras-yolo2
# https://github.com/experiencor/keras-yolo2/blob/master/LICENSE

PWD = os.getcwd()
MODEL_ROOT_PATH="./pretrain/"
IMG_PATH = os.path.join(PWD ,'hand/5.jpg')
IMG_NAME = '5.jpg'
DST_DIR = os.path.join(PWD ,'result')
RESULT_FILE = ''

#Load Model
def preprocess_img(img):
    img = img[...,::-1]  #BGR 2 RGB
    inputs = img.copy() / 255.0  
    img_camera = cv2.resize(inputs, (416,416))
    preprocessed_img = np.expand_dims(img_camera, axis=0)
    img_cv = cv2.cvtColor(img, cv2.COLOR_RGB2BGR)
    return preprocessed_img, img_cv

def get_preprocessed_img(img_path,img_name, dst_dir):
    global RESULT_FILE
    RESULT_FILE = os.path.join(dst_dir,img_name[:-4]+'_result.png')
    print(os.getcwd())
    print(RESULT_FILE)
    img=cv2.imread(img_path, cv2.IMREAD_COLOR)
    preporcessed_img, img_cv = preprocess_img(img)
    return preporcessed_img, img_cv

def main(argv):
    model_hand = load_model(MODEL_ROOT_PATH+'yolov2_tiny-hand.h5')
    preporcessed_img, img_cv = get_preprocessed_img(IMG_PATH,IMG_NAME,DST_DIR)

    start = time.time()
    out2 = model_hand.predict(preporcessed_img)[0]
    end = time.time()

    start = time.time()
    results = interpret_output_yolov2(out2, img_cv.shape[1], img_cv.shape[0])
    end = time.time()
    
    # argv에 따라 plot, save 유무 결정 하는것으로 업뎃 예정
    print(RESULT_FILE)
    img_cp=show_results(img_cv, results, img_cv.shape[1], img_cv.shape[0])
    plt.imshow(img_cp)
    print(RESULT_FILE)
    plt.imsave(RESULT_FILE,img_cp)
    return results

if __name__=='__main__':
	main(sys.argv[1:])