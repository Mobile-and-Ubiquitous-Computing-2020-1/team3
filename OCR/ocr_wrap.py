import argparse
import os
from ocr import *
from extract import *


ap = argparse.ArgumentParser()
ap.add_argument("--image", help="Image Path")

args = vars(ap.parse_args())
img_path = args["image"]

ocrdict = ocr(img_path)
resdict = extract(ocrdict)
#print(resdict)
with open(img_path+'.txt','w',encoding='utf-8') as outtext:
    for key in resdict:
        outtext.write(key + ": ")
        outtext.write(repr(resdict[key])+"\n")
