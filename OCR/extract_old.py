import argparse
from nutrition_dict import *

def get_nutri_val(text):
    text_spl = text.split()
    num = 0
    for i in range(len(text_spl)):
        if text_spl[i][0].isdigit():
            num += 1
        elif len(text_spl[i])>=2:
            if (text_spl[i][0] == '(') & (text_spl[i][1].isdigit()):
                num += 1
            else:
                break
        else:
            break

    i = 0
    j = 0
    while (i < len(text)) & (j < num):
        if text[i:].startswith(text_spl[j]):
            print(" "+text_spl[j])
            i = i+len(text_spl[j])
            j = j+1
        else:
            i = i+1
    if j < num:
        print("why cannot find values?")
    return i


def get_val(text, num):
    i = 0
    j = 0
    text_spl = text.split()
    while (i < len(text)) & (j < num):
        if text[i:].startswith(text_spl[j]):
            print(" "+text_spl[j])
            i = i+len(text_spl[j])
            j = j+1
        else:
            i = i+1
    if j < num:
        print("could not found " + num + " value(s)")
    return i


def sanitize(text):
    i = 0
    j = 0
    new_text = ""
    while i < len(text):
        found = False
        for word in sanitize_dict:
            if text[i:].startswith(word):
                print("found " + word)
                found = True
                if i != 0:
                    new_text += text[j:i-1]
                i = i + len(word)
                j = i

        if found == False:
            i += 1
    new_text += text[j: i]
    print("sanitize: " + new_text)
    return new_text

def extract(text):
    i = 0
    while i < len(text):
        found = False
        for word in value_dict:
            if text[i:].startswith(word):
                print("found " + word)
                found = True
                i = i + len(word)
                num = value_dict[word]
                if num > 0:
                    i = i + get_val(text[i:], num)

        for word in nutri_dict:
            if text[i:].startswith(word):
                print("found " + word)
                found = True
                i = i + len(word)
                i = i + get_nutri_val(text[i:])
        if found == False:
            #print("cannot find " + text[i:])
            i = i + 1


#str = "Nutrition Facts Serving Size About 11 Pieces (30g) Serving Per Container About 45 Calories 160"

#san_str = sanitize(str)
#extract(san_str)

ap = argparse.ArgumentParser()
ap.add_argument("--file", help="file path")

args = vars(ap.parse_args())
file_path = args["file"]
valid_formats = ["txt"]
file_ext = file_path.split('.')[-1]
if file_ext in valid_formats:
    file_name = ('').join(file_path.split('.')[:-1]).split('/')[-1]
    with open(file_path, "rb") as f:
        data = f.read()

    sanitize_data = sanitize(data)
    extract(sanitize_data)
