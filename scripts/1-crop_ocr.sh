#!/bin/bash -e
IMG_DIR=${PWD}/../test_images
EXT_DIR=${PWD}/../off-nutrition-table-extractor/nutrition_extractor
OCR_DIR=${PWD}/../OCR
RES_DIR=${PWD}/../result

NT_MSG="No table!!"

pushd $EXT_DIR

for img in ${IMG_DIR}/image_*
do
    bname=$(basename $img)
    img_name=${bname%.*}
    echo "processing $img_name"
    res=$(python detection.py -i $img -c 2>/dev/null)
    #echo $res
    if [[ "$res" == *"$NT_MSG"* ]]; then
        echo "no table"
        continue
    fi

    pushd $OCR_DIR
    python3 ocr.py --image ${EXT_DIR}/data/result/cropped.jpg
    popd
    mv ${EXT_DIR}/data/result/cropped.jpg ${RES_DIR}/${img_name}_crop.jpg
    mv ${EXT_DIR}/data/result/cropped.jpg.txt ${RES_DIR}/${img_name}_res.txt
done

popd
