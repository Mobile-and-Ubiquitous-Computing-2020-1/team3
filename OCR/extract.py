import argparse
from nutrition_dict import *
import copy
low_offset = 20

def preprocess(ocrlist):
    for item in ocrlist:
        text = item[0]
        if text[0].isalpha():
            i = 0
            while i<len(text):
                if text[i].isdigit():
                    break
                i += 1
            if i < len(text):
                high = (item[1][0]['y']+item[1][1]['y'])/2
                low = (item[1][2]['y']+item[1][3]['y'])/2
                midhigh = (item[1][0]['x']+item[1][1]['x'])/2
                midlow = (item[1][2]['x']+item[1][3]['x'])/2

                newitem = copy.deepcopy(item)
                item[0] = text[:i]
                item[1][1]['x'] = midhigh
                item[1][1]['y'] = high
                item[1][2]['x'] = midlow
                item[1][2]['y'] = low

                newitem[0] = text[i:]
                newitem[1][0]['x'] = midhigh
                newitem[1][0]['y'] = high
                newitem[1][3]['x'] = midlow
                newitem[1][3]['y'] = low
                ocrlist.append(newitem)

def find(text, valdict):
    for word in valdict:
        if text == word:
            return valdict[text]
    return 0

def find_nutri(extlist, item):
    text = item[0]
    res = find(text, nutri_dict)
    low = (item[1][2]['y']+item[1][3]['y'])/2
    left = (item[1][0]['x']+item[1][3]['x'])/2
    right = (item[1][1]['x']+item[1][2]['x'])/2

    if res == 'Fat' or res == 'fat' or res == 'Calories':
        if find_sameline(extlist, item):
            return False
    if res != 0:
        extlist.append([[res, left, right, low]])
        return True
    return False

def update_offset(extlist):
    min_ldif = float('inf')
    for idx in range(len(extlist)-1):
        ldif = extlist[idx+1][0][3] - extlist[idx][0][3]
        if ldif < min_ldif:
            min_ldif = ldif
    low_offset = ldif*0.4

def find_sameline(extlist, item):
    text = item[0]
    low = (item[1][2]['y']+item[1][3]['y'])/2
    left = (item[1][0]['x']+item[1][3]['x'])/2
    right = (item[1][1]['x']+item[1][2]['x'])/2
    added=False
    for wordlist in extlist:
        if abs(wordlist[0][3]- low) < low_offset:
            for idx in range(len(wordlist)):
                if (idx == len(wordlist)-1) & (wordlist[idx][2]<=left):
                    wordlist.append([text, left, right, low])
                    added=True
                    break
                elif wordlist[idx][1] >= left:
                    if wordlist[idx][1] >= right:
                        wordlist.insert(idx, [text, left, right, low])
                        added=True
                        break
                    else:
                        #print("overlapping word: " + text)
                        break
    return added


def extract_nutri(resultdict, vlist):
    idx = 0
    isname = True
    incl = False
    nutriname = ""
    values = []

    #print(vlist)
    while (idx < len(vlist)):
        val = vlist[idx][0]
        if isname:
            if val[0].isalpha():
                if val == 'Incl.' or val == 'Includes':
                    incl = True
                elif val != 'Total': # skip Total
                    nutriname += val
                idx += 1
            else:
                isname = False
        else:
            if (val[0].isdigit() or val[0] == '<') and not val.isdigit():
                for unit in unit_list:
                    if val.endswith(unit):
                        values.append(val)
                        break
                idx += 1
            elif val.isdigit():
                values.append(val)
                idx += 1
            elif val[0].isalpha():
                isname = True
                if not incl:
                    resultdict[nutriname] = values
                    values = []
                    nutriname = ""
                incl = False
            else:
                idx += 1
            if idx == len(vlist):
                resultdict[nutriname] = values

def postprocess(resultdict):

    removekey = []
    for val in resultdict:
        found = False
        for nutrival in nutri_dict:
            if val.find(nutri_dict[nutrival])>=0:
                found = True
                break
        if not found:
            removekey.append(val)
    for key in removekey:
        #print("delete " +repr(key))

        del resultdict[key]


def extract(ocrlist):
    extlist = []
    secondlist = []
    resultdict = {}
    preprocess(ocrlist)
    for item in ocrlist:
        if find_nutri(extlist, item):
            ocrlist.remove(item)

    update_offset(extlist)

    while len(ocrlist)>0:
        item = ocrlist.pop()
        find_sameline(extlist, item)
    for vlist in extlist:
        #for values in vlist:
        #    print(values[0])
        #print(vlist)
        extract_nutri(resultdict, vlist)
    postprocess(resultdict)
    return resultdict



