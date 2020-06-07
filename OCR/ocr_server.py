import argparse
import os
from ocr import *
from extract import *
import time

def ocr_server(img_path):
    ocrdict = ocr(img_path)
    resdict = extract(ocrdict)
    return resdict

    with open(img_path+'.txt','w',encoding='utf-8') as outtext:
        for key in resdict:
            outtext.write(key + ": ")
            outtext.write(repr(resdict[key])+"\n")



