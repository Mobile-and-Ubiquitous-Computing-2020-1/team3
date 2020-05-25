import argparse
import os
from ocr import *
from extract import *


ap = argparse.ArgumentParser()
ap.add_argument("--image", help="Image Path")

args = vars(ap.parse_args())
img_path = args["image"]

ocrdict = ocr(img_path)
extract(ocrdict)
