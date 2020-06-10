import argparse
import os
from ocr import *
from extract import *
import time
import cv2
import numpy

def ocr_server(img_path):
    count = 0
    while True:
        ocrdict = ocr(img_path)
        success, resdict = extract(ocrdict)
        if success:
            print("Succeeded nutrition extraction!")
            return success, resdict
        else:
            print("Failed nutrition extration!")
            count += 1

            if count < 4:
                image = cv2.imread(img_path, cv2.IMREAD_COLOR)
                rotate_image = np.rot90(image, 1)
                img_path = 'rotate'+str(count)+'.jpg'
                cv2.imwrite(img_path, rotate_image)
            else:
                return success, resdict




