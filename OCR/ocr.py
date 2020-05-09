import json
import base64
import requests
import argparse
import os

ap = argparse.ArgumentParser()
ap.add_argument("--image", help="Image Path")

args = vars(ap.parse_args())
img_path = args["image"]

valid_formats = ["jpg", "jpeg", "png", "JPG", "JPEG", "PNG"]

file_ext = img_path.split('.')[-1]

if(file_ext in valid_formats):

    file_name = ('').join(img_path.split('.')[:-1]).split('/')[-1]

    with open(img_path, "rb") as f:
        img = base64.b64encode(f.read())

    # NCP OCR API Gateway URL
    URL = "https://38bf9a5f9c2a407fbd5036e66a12cde6.apigw.ntruss.com/custom/v1/1755/e615e362b1e89d1821da5df6d37b0ece23079314626183a3aa1c8707e5505adc/general"
        
    # Secret Key
    KEY = "R1NuTXBaYXJsY25Kd3VabGlsUGJFWWtrdFp1ZFhDY0w="
        
    headers = {
        "Content-Type": "application/json",
        "X-OCR-SECRET": KEY
    }
        
    data = {
        "version": "V2",
        "requestId": "sample_id", # API Request UUID
        "timestamp": 0, # API Request Timestamp
        "images": [
            {
                "name": img_path,
                "format": file_ext,
                "data": img.decode('utf-8')
            }
        ]
    }

    data = json.dumps(data)
    response = requests.post(URL, data=data, headers=headers)
    res = json.loads(response.text)
    with open(img_path+'.json', 'w', encoding='utf-8') as outfile:
        json.dump(res, outfile, indent=4, ensure_ascii=False)
    
    textlist=[]

    for item in res['images'][0]['fields']:
        textlist.append(item['inferText'])
    
    with open(img_path+'.txt', 'w', encoding='utf-8') as outtext:
        outtext.write(' '.join(map(str, textlist)))