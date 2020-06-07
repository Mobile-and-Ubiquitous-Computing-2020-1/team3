#!/bin/bash -e
IMG_DIR=${PWD}/../test_images
EXT_DIR=${PWD}/../off-nutrition-table-extractor/nutrition_extractor
OCR_DIR=${PWD}/../OCR
RES_DIR=${PWD}/../result

NT_MSG="No table!!"

pushd $EXT_DIR > /dev/null

for img in ${IMG_DIR}/image_*
do
    bname=$(basename $img)
    img_name=${bname%.*}
    echo -n "processing $img_name ... "
    res=$(python detection.py -i $img 2>/dev/null)
    echo $res
    if [[ "$res" == *"$NT_MSG"* ]]; then
        echo "Failed: no table"
        continue
    fi

    mv ${EXT_DIR}/data/result/cropped.jpg ${RES_DIR}/${img_name}_crop.jpg
done

success=0
for img in ${RES_DIR}/image_*
do
    pushd $OCR_DIR > /dev/null
    python3 ocr_wrap.py --image $img -e
    popd > /dev/null
    break
done

popd > /dev/null
