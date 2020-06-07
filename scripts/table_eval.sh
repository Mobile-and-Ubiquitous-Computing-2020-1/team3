#!/bin/bash -e
IMG_DIR=${PWD}/../test_images
EXT_DIR=${PWD}/../off-nutrition-table-extractor/nutrition_extractor
OCR_DIR=${PWD}/../OCR
RES_DIR=${PWD}/../result

NT_MSG="No table!!"

pushd $EXT_DIR > /dev/null

for img in ${IMG_DIR}/image*
do
    success=0
    bname=$(basename $img)
    img_name=${bname%.*}
    res=$(python detection.py -i $img -e 2>/dev/null)
    echo $res
    if [[ "$res" == *"$NT_MSG"* ]]; then
        echo "Failed: no table"
        break
    fi
    break

done

popd > /dev/null
