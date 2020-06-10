import argparse
import os
from ocr import *
from extract import *
import time
import sys
import cv2
import numpy as np

ap = argparse.ArgumentParser()
ap.add_argument("--image", required=True, help="Image Path")
ap.add_argument("--eval", '-e', action='store_true',help="Evaluation")
args = vars(ap.parse_args())
img_path = args["image"]

if not args["eval"]:

    count = 0
    while True:
        print(img_path)
        ocrdict = ocr(img_path)
        success, resdict = extract(ocrdict)
        if success:
            print("Succeeded nutrition extraction!")
            with open(img_path+'.txt','w',encoding='utf-8') as outtext:
                for key in resdict:
                    outtext.write(key + ": ")
                    outtext.write(repr(resdict[key])+"\n")
            sys.exit()
        else:
            print("Failed nutrition extration!")
            count += 1

            if count < 4:
                image = cv2.imread(img_path, cv2.IMREAD_COLOR)
                print(image.shape)
                h, w, c = image.shape
                rotate_image = np.rot90(image, 1)
                #rotM = cv2.getRotationMatrix2D((w/2, h/2), 90, 1)
                #rotate_image = cv2.warpAffine(image, rotM,(h, w))
                img_path = 'rotate'+str(count)+'.jpg'
                cv2.imwrite(img_path, rotate_image)
            else:
                sys.exit()






else:
    img_dir = os.path.dirname(img_path)
    total_ocr = 0
    total_ext = 0
    count = 0
    for img in os.listdir(img_dir):
        if img.endswith(".jpg") or img.endswith(".jpeg"):
            start1 = time.time()
            ocrdict = ocr(img_path)
            end1 = time.time()

            start2 = time.time()
            success, resdict = extract(ocrdict)
            end2 = time.time()
            if not success:
                continue
            total_ocr += float(end1-start1)
            total_ext += float(end2-start2)

            print("OCR = %.5fs" % float(end1-start1))
            print("Ext = %.5fs" % float(end2-start2))
            count += 1

    print("Average OCR: %.5fs" % (total_ocr / float(count)))

    print("Average Ext: %.5fs" % (total_ext / float(count)))


