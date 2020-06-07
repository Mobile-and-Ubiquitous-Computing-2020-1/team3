import argparse
import os
from ocr import *
from extract import *
import time

ap = argparse.ArgumentParser()
ap.add_argument("--image", required=True, help="Image Path")
ap.add_argument("--eval", '-e', action='store_true',help="Evaluation")
args = vars(ap.parse_args())
img_path = args["image"]

if not args["eval"]:
    ocrdict = ocr(img_path)
    resdict = extract(ocrdict)

    with open(img_path+'.txt','w',encoding='utf-8') as outtext:
        for key in resdict:
            outtext.write(key + ": ")
            outtext.write(repr(resdict[key])+"\n")
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
            resdict = extract(ocrdict)
            end2 = time.time()

            total_ocr += float(end1-start1)
            total_ext += float(end2-start2)

            print("OCR = %.5fs" % float(end1-start1))
            print("Ext = %.5fs" % float(end2-start2))
            count += 1

    print("Average OCR: %.5fs" % (total_ocr / float(count)))

    print("Average Ext: %.5fs" % (total_ext / float(count)))


